package calma.com.pe.Calma.Services;

import calma.com.pe.Calma.Dto.DatosDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcesarService {

    private final LectorExcel lectorExcel;

    public ProcesarService(LectorExcel lectorExcel) {
        this.lectorExcel = lectorExcel;
    }

    public List<DatosDTO> procesarArchivo(String filePath) {
        return lectorExcel.leerExcel(filePath);
    }
}

