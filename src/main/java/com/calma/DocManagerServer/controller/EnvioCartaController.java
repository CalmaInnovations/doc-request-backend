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
    private DocumentoService pdfService;
    private JavaMailSender mailSender;
    private final ExcelService excelService;


    /* Descargar PDF */
    @GetMapping("/downloadPdf")
    public ResponseEntity<InputStreamResource> downloadPdf(@RequestParam("path") String pdfPath) throws IOException {
        // Validar que el archivo existe
        File file = new File(pdfPath);
        if (!file.exists()) {
            throw new IOException("El archivo no existe en la ruta especificada: " + pdfPath);
        }

        // Preparar el archivo para la descarga
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(file.length())
                .body(resource);
    }

    public void buscarEnExcel(){

    }

    /*  Recibir data  */
    public Map<String, Object> recibirDatos(Map<String, Object> datos){

        datos.get("nombre");
        datos.get("dni");
        datos.get("correo");
        datos.get("dni");
        datos.get("celular");
        datos.get("codigo");
        datos.get("carrera");
        datos.get("tipo_practica");
        datos.get("area");
        datos.get("lider_area");
        datos.get("puesto");
        datos.get("fecha_inicio");
        datos.get("fecha_salida");


        buscarEnExcel();

        return datos;
    }
    /* Convertir html a pdf */
    public ResponseEntity<String> mostrarCartaAceptacion(@RequestParam Map<String, Object> params) throws IOException  {
        Map<String, Object> datos = new HashMap<>();
        datos.put("fundacion", params.getOrDefault("fundacion", "FUNDACIÓN CALMA"));
        datos.put("ruc", params.getOrDefault("ruc", "20600792220"));
        datos.put("nombre", params.getOrDefault("nombre", "Sebastian Adauco Beteta Espinoza"));
        datos.put("dni", params.getOrDefault("dni", "75253829"));
        datos.put("area", params.getOrDefault("area", "Automatización"));
        datos.put("practicas", params.getOrDefault("practicas", "Prácticas Pre Profesionales"));
        datos.put("carrera", params.getOrDefault("carrera", "Ingeniería de Software"));
        datos.put("inicio", params.getOrDefault("inicio", "03 de octubre de 2024"));
        datos.put("fin", params.getOrDefault("fin", "03 de abril de 2025"));
        datos.put("firma", params.getOrDefault("firma", "Cindy Shirley Martinez Jimenez"));
        datos.put("lugar_fecha", params.getOrDefault("lugar_fecha", "San Isidro, 10 de octubre de 2024"));

        if (datos.isEmpty()) {
            return ResponseEntity.badRequest().body("DNI no encontrado en el archivo Excel.");
        }

        File tempPDF = File.createTempFile("cartaAceptacion", ".pdf");
        String pdfPath = tempPDF.getAbsolutePath();
        pdfService.generarPdf(datos, pdfPath);
        String baseUrl = "http://localhost:8080/api/downloadPdf";
        String downloadUrl = baseUrl + "?path=" + pdfPath;
        String response = "Enlace de descarga: " + downloadUrl;

        return ResponseEntity.ok(response);
    }
    /* Enviar carta por email */
    @PostMapping("/enviarEmail")
    public ResponseEntity<String> enviarEmailConCarta(@RequestBody EmailRequest emailRequest) throws MessagingException, IOException {

        Map<String, Object> params = new HashMap<>();

        ResponseEntity<String> cartaResponse = mostrarCartaAceptacion(params);

        String downloadUrl = cartaResponse.getBody().replace("Enlace de descarga: ", "");

        pdfService.enviarCarta(emailRequest.getEmail(), downloadUrl);

        return ResponseEntity.status(HttpStatus.CREATED).body("Correo enviado con éxito a " + emailRequest.getEmail());
    }

}
