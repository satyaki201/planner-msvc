package com.finance.planner.mapper;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ExcelToJSON {

    public Map<String,Map<String, Map<String, Double>>> convertSheetToJSON(Sheet sheet) {
        Map<String, Map<String, Double>> jsonData = new HashMap<>();

        Row headerRow = sheet.getRow(0);
        Map<String, Integer> columnIndexMap = new HashMap<>();

        // Map column headers (from index 1 to 9) to their positions
        for (int columnIndex = 2; columnIndex < 10; columnIndex++) {
            Cell cell = headerRow.getCell(columnIndex);
            if (cell != null && !Objects.equals(cell.getStringCellValue(), "")) {
                columnIndexMap.put(cell.getStringCellValue(), columnIndex);
            }
        }

        // Read up to 50 rows of data
        for (int rowIndex = 2; rowIndex <= 50; rowIndex++) {
            Row currentRow = sheet.getRow(rowIndex);
            if (currentRow != null) {
                String rowKey = currentRow.getCell(0).getStringCellValue();
                Map<String, Double> rowData = new HashMap<>();

                for (var entry : columnIndexMap.entrySet()) {
                    String columnName = entry.getKey();
                    int columnPosition = entry.getValue();
                    rowData.put(columnName, currentRow.getCell(columnPosition).getNumericCellValue());
                }

                jsonData.put(rowKey, rowData);
            }
        }


        Map<String,Map<String, Map<String, Double>>> finalJson=new HashMap<>();
        finalJson.put(sheet.getSheetName(),jsonData);

        return finalJson;
    }
}
