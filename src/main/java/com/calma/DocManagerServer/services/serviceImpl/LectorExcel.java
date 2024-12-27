package com.calma.DocManagerServer.services.serviceImpl;


import com.calma.DocManagerServer.dto.DatosDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class LectorExcel {
    public List<DatosDTO> leerExcel(String filePath) {
        List<DatosDTO> datosList = new ArrayList<>();

        try (FileInputStream fileInputStream = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                DatosDTO datosDTO = new DatosDTO();

                datosDTO.setNombresApellidos(row.getCell(0).getStringCellValue());
                datosDTO.setCorreoElectronico(row.getCell(1).getStringCellValue());
                datosDTO.setDni(row.getCell(2).getStringCellValue());
                datosDTO.setCelular(row.getCell(3).getStringCellValue());
                datosDTO.setUniversidadOInstituto(row.getCell(4).getStringCellValue());
                datosDTO.setCodigoEstudiante(row.getCell(5).getStringCellValue());
                datosDTO.setCarrera(row.getCell(6).getStringCellValue());
                datosDTO.setTipoPracticas(row.getCell(7).getStringCellValue());
                datosDTO.setArea(row.getCell(8).getStringCellValue());
                datosDTO.setLiderArea(row.getCell(9).getStringCellValue());
                datosDTO.setPuesto(row.getCell(10).getStringCellValue());
                datosDTO.setFechaIngreso(row.getCell(11).getLocalDateTimeCellValue().toLocalDate());
                datosDTO.setFechaSalida(row.getCell(12).getLocalDateTimeCellValue().toLocalDate());

                datosList.add(datosDTO);
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al leer el archivo Excel: " + e.getMessage());
        }

        return datosList;
    }
}