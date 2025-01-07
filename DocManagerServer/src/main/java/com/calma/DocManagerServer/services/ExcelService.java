package com.calma.DocManagerServer.services;

import java.io.IOException;
import java.util.Map;

public interface ExcelService {
    Map<String, Object> buscarDatosEnExcel(String dni) throws IOException;
}
