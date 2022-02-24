package com.alo.digital.firmadigital.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantesJKS {

    public static String keystoreType;
    public static String keystoreFile;
    public static String keystorePass;
    public static String privateKeyAlias;
    public static String privateKeyPass;
    public static String certificateAlias;
    public static String ruc;
    public static String razonSocial;

    @Value("${app.ruc}")
    public void setRuc(String ruc) {
        ConstantesJKS.ruc = ruc;
    }

    @Value("${app.razonSocial}")
    public void setRazonSocial(String razonSocial) {
        ConstantesJKS.razonSocial = razonSocial;
    }

    @Value("${app.keystoreType}")
    public void setKeystoreType(String keystoreType) {
        ConstantesJKS.keystoreType = keystoreType;
    }

    @Value("${app.keystoreFile}")
    public void setKeystoreFile(String keystoreFile) {
        ConstantesJKS.keystoreFile = keystoreFile;
    }

    @Value("${app.keystorePass}")
    public void setKeystorePass(String keystorePass) {
        ConstantesJKS.keystorePass = keystorePass;
    }

    @Value("${app.privateKeyAlias}")
    public void setPrivateKeyAlias(String privateKeyAlias) {
        ConstantesJKS.privateKeyAlias = privateKeyAlias;
    }

    @Value("${app.privateKeyPass}")
    public void setPrivateKeyPass(String privateKeyPass) {
        ConstantesJKS.privateKeyPass = privateKeyPass;
    }

    @Value("${app.certificateAlias}")
    public void setCertificateAlias(String certificateAlias) {
        ConstantesJKS.certificateAlias = certificateAlias;
    }
}
