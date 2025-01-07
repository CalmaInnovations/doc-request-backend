package calma.com.pe.Calma.Controller;

import calma.com.pe.Calma.Dto.DatosDTO;
import calma.com.pe.Calma.Services.serviceImpl.BuscarService;
import calma.com.pe.Calma.Services.serviceImpl.LectorExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {

    private final BuscarService buscarService;
    private final LectorExcel lectorExcel;

    @Autowired
    public ExcelController(BuscarService buscarService, LectorExcel lectorExcel) {
        this.buscarService = buscarService;
        this.lectorExcel = lectorExcel;
    }

    @GetMapping("/buscar")
    public Map<String, String> buscarRegistro(@RequestParam String correo) {
        String excelPath = "C:\\Prueba\\CARTA DE ACEPTACIÓN.xlsx";
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
