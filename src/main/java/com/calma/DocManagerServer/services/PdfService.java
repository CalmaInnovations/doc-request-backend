package com.calma.DocManagerServer.services;

import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.Map;

public interface PdfService {
    void generarPdf(Map<String, Object> datos, String outputPath) throws IOException;

    public default String processThymeleafTemplate(String templateName, Map<String, Object> params) {
        return null;
    }

    void enviarCarta(String email, String downloadUrl) throws MessagingException;
}

