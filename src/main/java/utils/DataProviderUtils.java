package utils;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataProviderUtils {
    
    @DataProvider(name = "loginData")
    public static Object[][] getLoginDataFromJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String filePath = System.getProperty("user.dir") + "/src/test/resources/testdata/loginData.json";
            JsonNode rootNode = mapper.readTree(new File(filePath));
            JsonNode testDataArray = rootNode.get("loginTestData");
            
            List<Object[]> testData = new ArrayList<>();
            for (JsonNode testCase : testDataArray) {
                String email = testCase.get("email").asText();
                String password = testCase.get("password").asText();
                boolean expectedResult = testCase.get("expectedResult").asBoolean();
                testData.add(new Object[]{email, password, expectedResult});
            }
            
            return testData.toArray(new Object[0][]);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read JSON test data", e);
        }
    }
    
    @DataProvider(name = "excelData")
    public static Object[][] getDataFromExcel(String sheetName) {
        try {
            String filePath = System.getProperty("user.dir") + "/src/test/resources/testdata/testData.xlsx";
            FileInputStream fis = new FileInputStream(filePath);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet(sheetName);
            
            int rowCount = sheet.getLastRowNum();
            int colCount = sheet.getRow(0).getLastCellNum();
            
            Object[][] data = new Object[rowCount][colCount];
            
            for (int i = 1; i <= rowCount; i++) {
                Row row = sheet.getRow(i);
                for (int j = 0; j < colCount; j++) {
                    Cell cell = row.getCell(j);
                    data[i-1][j] = getCellValue(cell);
                }
            }
            
            workbook.close();
            fis.close();
            return data;
        } catch (Exception e) {
            throw new RuntimeException("Failed to read Excel test data", e);
        }
    }
    
    private static Object getCellValue(Cell cell) {
        if (cell == null) return "";
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return cell.getNumericCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            default:
                return "";
        }
    }
}