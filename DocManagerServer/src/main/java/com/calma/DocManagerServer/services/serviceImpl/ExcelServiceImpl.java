package com.calma.DocManagerServer.services.serviceImpl;

import com.calma.DocManagerServer.services.ExcelService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class ExcelServiceImpl implements ExcelService {
    @Override
    public Map<String, Object> buscarDatosEnExcel(String dni) throws IOException {
        Map<String, Object> datos = new HashMap<>();

        try (FileInputStream fis = new FileInputStream("ruta_del_archivo.xlsx");
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                Cell cellDni = row.getCell(0);
                if (cellDni != null && cellDni.getStringCellValue().equals(dni)) {
                    datos.put("nombre", row.getCell(1).getStringCellValue());
                    datos.put("correo", row.getCell(2).getStringCellValue());
                    datos.put("celular", row.getCell(3).getStringCellValue());
                    datos.put("codigo", row.getCell(4).getStringCellValue());
                    datos.put("carrera", row.getCell(5).getStringCellValue());
                    datos.put("tipo_practica", row.getCell(6).getStringCellValue());
                    datos.put("area", row.getCell(7).getStringCellValue());
                    datos.put("lider_area", row.getCell(8).getStringCellValue());
                    datos.put("puesto", row.getCell(9).getStringCellValue());
                    datos.put("fecha_inicio", row.getCell(10).getDateCellValue().toString());
                    datos.put("fecha_salida", row.getCell(11).getDateCellValue().toString());
                    break;
                }
            }
        }
        return datos;
    }
}