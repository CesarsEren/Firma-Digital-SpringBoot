package com.alo.digital.firmadigital.process;

import com.alo.digital.firmadigital.jobs.JobsLectorInicial;
import com.alo.digital.firmadigital.util.ConstantesJKS;
import com.alo.digital.firmadigital.util.GeneralFunctions;
import com.alo.digital.firmadigital.util.UtilMove;
import org.apache.xml.security.Init;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import org.w3c.dom.Document;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


@Component
public class FirmarDocumento {

    private static Logger logger = LoggerFactory.getLogger(FirmarDocumento.class);

    @Autowired
    UtilMove utilMove;

    public void process(File xml) {
        try {
            File processXML = utilMove.MoverDeCarpetaINaPROCESS(xml);
            FirmarDocumento(processXML);
            utilMove.MoverDeCarpetaPROCESSaOUT(processXML);

        } catch (Exception e) {
            logger.error("Error Procesando el documento {} , {} , {}", e.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    public void FirmarDocumento(File documentoaFirmar) throws IOException, ParserConfigurationException, XMLSecurityException, SAXException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, TransformerException {
        Init.init();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        builder = factory.newDocumentBuilder();

        factory.setNamespaceAware(true);

        Document doc1 = builder.parse(documentoaFirmar);

        Constants.setSignatureSpecNSprefix("ds");

        KeyStore ks = KeyStore.getInstance(ConstantesJKS.keystoreType);
        ks.load(new FileInputStream(ConstantesJKS.keystoreFile), ConstantesJKS.keystorePass.toCharArray());

        PrivateKey privateKey = (PrivateKey) ks.getKey(ConstantesJKS.privateKeyAlias, ConstantesJKS.privateKeyPass.toCharArray());

        //File signatureFile = documentoaFirmar;
        String baseURI = documentoaFirmar.toString();
        XMLSignature xmlSignature = new XMLSignature(doc1, baseURI, XMLSignature.ALGO_ID_SIGNATURE_RSA);
        Node prevExtensionContent = doc1.getElementsByTagName("ext:ExtensionContent").item(0);
        Node Signature = doc1.getElementsByTagName("ds:Signature").item(0);
        prevExtensionContent.replaceChild(xmlSignature.getElement(), Signature);
        xmlSignature.setId("Sign" + ConstantesJKS.ruc);
        Transforms transforms = new Transforms(doc1);
        transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
        xmlSignature.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);

        X509Certificate cert = (X509Certificate) ks.getCertificate(ConstantesJKS.certificateAlias);
        xmlSignature.addKeyInfo(cert);
        xmlSignature.addKeyInfo(cert.getPublicKey());
        xmlSignature.sign(privateKey);

        OutputStream os = new FileOutputStream(JobsLectorInicial.directoryOUT + "\\" + documentoaFirmar.getName());
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans = tf.newTransformer();
        trans.transform(new DOMSource(doc1), new StreamResult(os));
        os.close();

        GeneralFunctions.crearZip(JobsLectorInicial.directoryOUT, new File(JobsLectorInicial.directoryOUT + "\\" + documentoaFirmar.getName()));
    }

}
