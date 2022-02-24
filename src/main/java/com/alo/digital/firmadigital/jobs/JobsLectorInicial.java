package com.alo.digital.firmadigital.jobs;

import com.alo.digital.firmadigital.process.FirmarDocumento;
import com.alo.digital.firmadigital.util.TaskUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class JobsLectorInicial {

    private static Logger logger = LoggerFactory.getLogger(JobsLectorInicial.class);

    public static String directoryIN;
    public static String directoryPROCESS;
    public static String directoryOUT;
    public static String directoryNOPROCESS;


    @Autowired
    FirmarDocumento firmarDocumento;

    @Scheduled(fixedDelayString = "${app.time.delay.sg}")
    public void JobParaLeerCarpeta() {
        try {
            logger.info("INICIA PROCESO ");
            logger.info("REVISANDO CARPETA ; {} ", directoryIN);
            File fileIn = new File(directoryIN);
            File[] xmls = fileIn.listFiles();
            if (xmls.length == 0) {
                logger.info("\"{}\" está vacío---", directoryIN);
            } else {
                File xmlCurrent = xmls[0];

                logger.info("CREANDO NUEVO HILO PARA PROCESAR DOCUMENTO ");
                TaskUtil.exec(() -> firmarDocumento.process(xmlCurrent));
            }

        } catch (RuntimeException e) {
            logger.error("No se pudo firmar en el tiempo establecido. {}={}", e.getClass().getSimpleName(), e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error en el firmado. {}={}", e.getClass().getSimpleName(), e.getMessage(), e);
        }
    }


    @Value("${app.directory.xml.noprocess}")
    public void setDirectoryNOPROCESS(String directoryNOPROCESS) {
        JobsLectorInicial.directoryNOPROCESS = directoryNOPROCESS;
    }

    @Value("${app.directory.xml.process}")
    public void setDirectoryPROCESS(String directoryPROCESS) {
        JobsLectorInicial.directoryPROCESS = directoryPROCESS;
    }

    @Value("${app.directory.xml.in}")
    public void setDirectoryIN(String directoryIN) {
        JobsLectorInicial.directoryIN = directoryIN;
    }

    @Value("${app.directory.xml.out}")
    public void setDirectoryOUT(String directoryOUT) {
        JobsLectorInicial.directoryOUT = directoryOUT;
    }
}
