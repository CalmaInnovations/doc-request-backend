package com.calma.DocManagerServer.services;

import com.calma.DocManagerServer.model.PracticanteVoluntario;
import jakarta.mail.MessagingException;
import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.util.Map;

public interface DocumentoService {
    String generarPdf(Map<String, Object> datos, String outputPath) throws IOException;

    default String processThymeleafTemplate(String templateName, Map<String, Object> params) {
        return null;
    }

    void enviarCarta(String email, String id) throws MessagingException;

    public InputStreamResource obtenerRecursoPdfTemporal(String pdfPath) throws IOException;

    PracticanteVoluntario save(PracticanteVoluntario practicante);
}

