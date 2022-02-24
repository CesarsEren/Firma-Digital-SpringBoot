package com.alo.digital.firmadigital.util;

import com.alo.digital.firmadigital.jobs.JobsLectorInicial;
import com.alo.digital.firmadigital.process.FirmarDocumento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.File;

@Component
public class UtilMove {

    private static Logger logger = LoggerFactory.getLogger(UtilMove.class);

    public File MoverDeCarpetaINaPROCESS(File xml) throws Exception {
        logger.info("Copiando XML {} A process ", xml.getName());
        File xmlProcess = new File(JobsLectorInicial.directoryPROCESS + "\\" + xml.getName());
        logger.info("Copiando XML {} ", xmlProcess.getAbsolutePath());
        FileCopyUtils.copy(xml, xmlProcess);
        logger.info("Eliminando XML {} ", xml.getAbsolutePath());
        xml.delete();
        return xmlProcess;
    }

    public File MoverDeCarpetaPROCESSaOUT(File xml) throws Exception {
        logger.info("COPIANDO XML {} A OUT ", xml.getName());
        File xmlProcess = new File(JobsLectorInicial.directoryOUT + "\\" + xml.getName());
        logger.info("Copiando XML {} ", xmlProcess.getAbsolutePath());
        FileCopyUtils.copy(xml, xmlProcess);
        logger.info("Eliminando XML {} ", xml.getAbsolutePath());
        xml.delete();
        return xmlProcess;
    }

    public void MoverDeCarpetaPROCESSaNoProcesado(File xml) {
        try {
            logger.info("COPIANDO XML {} A OUT ", xml.getName());
            File xmlProcess = new File(JobsLectorInicial.directoryNOPROCESS + "\\" + xml.getName());
            logger.info("Copiando XML {} ", xmlProcess.getAbsolutePath());
            FileCopyUtils.copy(xml, xmlProcess);
            logger.info("Eliminando XML {} ", xml.getAbsolutePath());
            xml.delete();
        } catch (Exception exception) {
            logger.error("{}", exception);
        }
    }

}
