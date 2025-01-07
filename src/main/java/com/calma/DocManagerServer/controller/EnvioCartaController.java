package com.calma.DocManagerServer.controller;

import com.calma.DocManagerServer.exception.DatosNoCoincidenException;
import com.calma.DocManagerServer.model.PracticanteVoluntario;
import com.calma.DocManagerServer.services.DocumentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;


@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://docmanager-client.onrender.com/")

public class EnvioCartaController {

    private final DocumentoService documentoService;

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
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al descargar el archivo", e);
        }
    }

    @PostMapping("/enviarCarta")
    public ResponseEntity<Object> create(@RequestBody PracticanteVoluntario practicante) {
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
