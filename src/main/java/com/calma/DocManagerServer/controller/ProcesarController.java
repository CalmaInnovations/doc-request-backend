package com.calma.DocManagerServer.controller;


import com.calma.DocManagerServer.dto.DatosDTO;
import com.calma.DocManagerServer.services.serviceImpl.ProcesarService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/procesar")
public class ProcesarController {
    private final ProcesarService procesarService;

    @Value("${ruta.archivo.excel}")
    private String rutaExcel;

    public ProcesarController(ProcesarService procesarService) {
        this.procesarService = procesarService;
    }

    @GetMapping("/datos")
    public ResponseEntity<List<DatosDTO>> obtenerDatosDesdeExcel() {
        List<DatosDTO> datos = procesarService.procesarArchivo(rutaExcel);
        return ResponseEntity.ok(datos);
    }
}
