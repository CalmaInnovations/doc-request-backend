package com.calma.DocManagerServer.services;

import com.calma.DocManagerServer.dto.DatosDTO;

import java.util.List;

public interface IProcesarService {
    List<DatosDTO> procesarArchivo(String filePath);
}
