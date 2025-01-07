package com.calma.DocManagerServer.services;

import java.io.IOException;
import java.util.Map;

public interface IBuscarService {
    Map<String, String> buscarRegistro(String excelPath, String correoCriterio) throws IOException;

}
