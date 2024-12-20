package com.calma.DocManagerServer.services;

import com.calma.DocManagerServer.dto.DatosDTO;
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