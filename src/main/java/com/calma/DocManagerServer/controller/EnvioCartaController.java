package com.calma.DocManagerServer.controller;

import com.calma.DocManagerServer.dto.request.EmailRequest;
import com.calma.DocManagerServer.services.ExcelService;
import com.calma.DocManagerServer.services.DocumentoService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor

public class EnvioCartaController {

    @Autowired
    private DocumentoService documentoService;
    private JavaMailSender mailSender;
    private final ExcelService excelService;

    /* Descargar PDF
    * Modificar el controlador downloadPdf para que reciba la ruta del contenido de la solicitud y la plantilla de PDF
    * Se guarda el contenido mientras que el pdf se expone para la descarga a los destinatarios
    * En el controlador no debe haber lógica de negocio
    *
    * */
    @GetMapping("/downloadPdf")
    public ResponseEntity<InputStreamResource> downloadPdf(@RequestParam("path") String pdfPath) throws IOException {
        File file = new File(pdfPath);
        if (!file.exists()) {
            throw new IOException("El archivo no existe en la ruta especificada: " + pdfPath);
        }
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(file.length())
                .body(resource);
    }

    @PostMapping("/enviarEmail")
    public ResponseEntity<String> enviarEmail(@RequestBody EmailRequest emailRequest) throws MessagingException, IOException {
        documentoService.enviarCarta(emailRequest.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Correo enviado correctamente a " + emailRequest.getEmail());
    }


}
