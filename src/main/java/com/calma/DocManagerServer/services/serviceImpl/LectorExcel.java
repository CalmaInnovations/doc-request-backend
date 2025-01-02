package com.calma.DocManagerServer.services.serviceImpl;


import com.calma.DocManagerServer.dto.DatosDTO;
import com.calma.DocManagerServer.services.ILectorExcel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LectorExcel implements ILectorExcel {
    @Override
    public List<DatosDTO> leerExcel(String filePath) {
        List<DatosDTO> datosList = new ArrayList<>();

        try (FileInputStream fileInputStream = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                if (row == null) continue;

                try {
                    DatosDTO datosDTO = new DatosDTO();

                    datosDTO.setNombresApellidos(getCellValueAsString(row.getCell(0), "Nombres y Apellidos"));
                    datosDTO.setCorreoElectronico(getCellValueAsString(row.getCell(1), "Correo Electrónico"));
                    datosDTO.setDni(getCellValueAsString(row.getCell(2), "DNI"));
                    datosDTO.setCelular(getCellValueAsString(row.getCell(3), "Celular"));
                    datosDTO.setUniversidadOInstituto(getCellValueAsString(row.getCell(4), "Universidad o Instituto"));
                    datosDTO.setCodigoEstudiante(getCellValueAsString(row.getCell(5), "Código de Estudiante"));
                    datosDTO.setCarrera(getCellValueAsString(row.getCell(6), "Carrera"));
                    datosDTO.setTipoPracticas(getCellValueAsString(row.getCell(7), "Tipo de Prácticas"));
                    datosDTO.setArea(getCellValueAsString(row.getCell(8), "Área"));
                    datosDTO.setLiderArea(getCellValueAsString(row.getCell(9), "Líder de Área"));
                    datosDTO.setPuesto(getCellValueAsString(row.getCell(10), "Puesto"));
                    datosDTO.setFechaIngreso(getCellValueAsDate(row.getCell(11), "Fecha de Ingreso"));
                    datosDTO.setFechaSalida(getCellValueAsDate(row.getCell(12), "Fecha de Salida"));

                    datosList.add(datosDTO);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Error en la fila " + (i + 1) + ": " + e.getMessage());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al leer el archivo Excel: " + e.getMessage());
        }

        return datosList;
    }

    private String getCellValueAsString(Cell cell, String fieldName) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            throw new IllegalArgumentException(fieldName + " esta vacío");
        }
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue().trim();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((long) cell.getNumericCellValue());
        } else {
            throw new IllegalArgumentException(fieldName + " este tipo de dato no es valido");
        }
    }

    private LocalDate getCellValueAsDate(Cell cell, String fieldName) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC || !DateUtil.isCellDateFormatted(cell)) {
            throw new IllegalArgumentException(fieldName + " la fecha no es valida");
        }
        return cell.getLocalDateTimeCellValue().toLocalDate();
    }
}