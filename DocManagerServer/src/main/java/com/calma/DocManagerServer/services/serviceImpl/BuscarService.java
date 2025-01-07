package com.calma.DocManagerServer.services.serviceImpl;

import com.calma.DocManagerServer.services.IBuscarService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class BuscarService implements IBuscarService {
    @Override
    public Map<String, String> buscarRegistro(String excelPath, String correoCriterio) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(excelPath));
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);

        String[] columnas = {
                "nombresApellidos", "correoElectronico", "dni", "celular",
                "universidadOInstituto", "codigoEstudiante", "carrera", "tipoPracticas",
                "area", "liderArea", "puesto", "fechaIngreso", "fechaSalida"
        };

        for (Row row : sheet) {
            Cell correoCell = row.getCell(1);
            if (correoCell != null && correoCell.getCellType() == CellType.STRING) {
                if (correoCell.getStringCellValue().equalsIgnoreCase(correoCriterio)) {
                    Map<String, String> registro = new HashMap<>();
                    for (int i = 0; i < columnas.length; i++) {
                        Cell cell = row.getCell(i);
                        if (cell != null) {
                            registro.put(columnas[i], getCellValueAsString(cell));
                        } else {
                            registro.put(columnas[i], "");
                        }
                    }
                    workbook.close();
                    return registro;
                }
            }
        }

        workbook.close();
        return null;
    }

    private String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate().toString();
                } else {
                    return String.valueOf((int) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
}