package com.calma.DocManagerServer.controller;

import com.calma.DocManagerServer.dto.request.EmailRequest;
import com.calma.DocManagerServer.exception.DatosNoCoincidenException;
import com.calma.DocManagerServer.model.PracticanteVoluntario;
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
@CrossOrigin(origins = "https://docmanager-client.onrender.com/")

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
    public ResponseEntity<InputStreamResource> downloadPdf(@RequestParam("path") String pdfPath) {
        try {
            InputStreamResource resource = documentoService.obtenerRecursoPdfTemporal(pdfPath);

            File file = new File(pdfPath);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + file.getName())
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(file.length())
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
                    .body(null);
        }
    }

    @PostMapping("/enviarCarta")
    public ResponseEntity<?> create(@RequestBody PracticanteVoluntario practicante) {
        try {
            PracticanteVoluntario savedPracticante = (PracticanteVoluntario) documentoService.save(practicante);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPracticante);
        } catch (DatosNoCoincidenException ex) {
            return ResponseEntity.badRequest().body(ex.getErrores());
        } catch (Exception ex) {
            // Manejo de cualquier otra excepción general
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocurrió un error inesperado: " + ex.getMessage());
        }
    }



}
