package com.calma.DocManagerServer.services;

import com.calma.DocManagerServer.model.PracticanteVoluntario;
import jakarta.mail.MessagingException;
import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.util.Map;

public interface DocumentoService {
    String generarPdf(Map<String, Object> datos, String templateName, String outputPath) throws IOException;

    default String processThymeleafTemplate(String templateName, Map<String, Object> params) {
        return null;
    }

    void enviarCarta(String email, String id) throws MessagingException;

    InputStreamResource obtenerRecursoPdfTemporal(String pdfPath) throws IOException;

    Object save(PracticanteVoluntario practicante);
}

