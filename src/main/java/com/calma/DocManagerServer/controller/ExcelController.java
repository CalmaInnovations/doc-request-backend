package com.calma.DocManagerServer.controller;
import com.calma.DocManagerServer.dto.DatosDTO;
import com.calma.DocManagerServer.services.serviceImpl.BuscarService;
import com.calma.DocManagerServer.services.serviceImpl.LectorExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/excel")
@CrossOrigin(origins = "http://localhost:5173")

public class ExcelController {

    private final BuscarService buscarService;
    private final LectorExcel lectorExcel;
    private static final String UPLOAD_DIR = "src/main/resources/files/Prueba1.xlsx";
    @Autowired
    public ExcelController(BuscarService buscarService, LectorExcel lectorExcel) {
        this.buscarService = buscarService;
        this.lectorExcel = lectorExcel;
    }

    @GetMapping("/buscar")
    public Map<String, String> buscarRegistro(@RequestParam String correo) {
        String excelPath = UPLOAD_DIR;
        try {
            Map<String, String> resultado = buscarService.buscarRegistro(excelPath, correo);
            if (resultado != null) {
                return resultado;
            } else {
                throw new RuntimeException("Registro no encontrado.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al procesar el archivo: " + e.getMessage());
        }
    }

    @GetMapping("/leer")
    public ResponseEntity<?> leerExcel(@RequestParam String filePath) {
        try {
            List<DatosDTO> datos = lectorExcel.leerExcel(filePath);

            if (datos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No se encontraron datos en el archivo");
            }

            return ResponseEntity.ok(datos);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar el archivo: " + e.getMessage());
        }
    }


}