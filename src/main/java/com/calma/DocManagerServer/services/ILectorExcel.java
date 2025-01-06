package com.calma.DocManagerServer.services;

import com.calma.DocManagerServer.dto.DatosDTO;

import java.util.List;

public interface ILectorExcel {
    List<DatosDTO> leerExcel(String filePath);

}
