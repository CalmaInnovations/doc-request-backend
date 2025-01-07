package com.calma.DocManagerServer.services.serviceImpl;
import com.calma.DocManagerServer.services.IBuscarService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class BuscarService implements IBuscarService {

    @Override
    public Map<String, String> buscarRegistro(String excelPath, String correoCriterio) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(excelPath);
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            Sheet sheet = workbook.getSheet("Cartas de aceptación");
            if (sheet == null) {
                throw new IllegalArgumentException("La hoja 'Cartas de aceptación' no existe en el archivo.");
            }

            Row encabezado = sheet.getRow(0);
            if (encabezado == null) {
                throw new IllegalArgumentException("No se encontró una fila de encabezado en la hoja.");
            }

            int inicioColumna = buscarIndiceColumna(encabezado, "NOMBRES Y APELLIDOS");
            int finColumna = buscarIndiceColumna(encabezado, "SALIDA");

            if (inicioColumna == -1 || finColumna == -1) {
                throw new IllegalArgumentException("No se encontraron las columnas 'NOMBRES Y APELLIDOS' o 'SALIDA' en el encabezado.");
            }

            int correoIndex = buscarIndiceColumna(encabezado, "CORREO ELECTRÓNICO");
            if (correoIndex == -1) {
                throw new IllegalArgumentException("No se encontró la columna 'CORREO ELECTRÓNICO' en el encabezado.");
            }

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                Cell correoCell = row.getCell(correoIndex);
                if (correoCell != null && correoCell.getCellType() == CellType.STRING &&
                        correoCell.getStringCellValue().trim().equalsIgnoreCase(correoCriterio)) {

                    Map<String, String> registro = new LinkedHashMap<>();
                    for (int i = inicioColumna; i <= finColumna; i++) {
                        String columna = getCellValueAsString(encabezado.getCell(i));
                        String valor = getCellValueAsString(row.getCell(i));
                        registro.put(columna, valor);
                    }

                    return registro;
                }
            }

            return Collections.emptyMap();
        }
    }

    private int buscarIndiceColumna(Row encabezado, String nombreColumna) {
        for (Cell cell : encabezado) {
            String valor = getCellValueAsString(cell).trim();
            if (nombreColumna.equalsIgnoreCase(valor)) {
                return cell.getColumnIndex();
            }
        }
        return -1;
    }


    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";

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