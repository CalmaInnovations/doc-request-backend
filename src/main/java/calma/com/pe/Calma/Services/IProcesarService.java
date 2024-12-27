package calma.com.pe.Calma.Services;

import calma.com.pe.Calma.Dto.DatosDTO;

import java.util.List;

public interface IProcesarService {
    List<DatosDTO> procesarArchivo(String filePath);

}
