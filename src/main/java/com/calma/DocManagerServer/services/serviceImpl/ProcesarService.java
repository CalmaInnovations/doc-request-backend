package com.calma.DocManagerServer.services.serviceImpl;


import com.calma.DocManagerServer.dto.DatosDTO;
import com.calma.DocManagerServer.services.IProcesarService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcesarService implements IProcesarService {

    private final LectorExcel lectorExcel;

    public ProcesarService(LectorExcel lectorExcel) {
        this.lectorExcel = lectorExcel;
    }

    public List<DatosDTO> procesarArchivo(String filePath) {
        return lectorExcel.leerExcel(filePath);
    }
}