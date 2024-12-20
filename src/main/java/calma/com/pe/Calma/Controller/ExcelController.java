package calma.com.pe.Calma.Controller;

import calma.com.pe.Calma.Services.BuscarService;
import calma.com.pe.Calma.Services.ProcesarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {

    @Autowired
    private BuscarService buscarService;

    @GetMapping("/buscar")
    public Map<String, String> buscarRegistro(@RequestParam String dni) {
        String excelPath = "ruta/del/archivo/solicitantes.xlsx";
        try {
            Map<String, String> resultado = buscarService.buscarRegistro(excelPath, dni);
            if (resultado != null) {
                return resultado;
            } else {
                throw new RuntimeException("Registro no encontrado.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al procesar el archivo: " + e.getMessage());
        }
    }

}
