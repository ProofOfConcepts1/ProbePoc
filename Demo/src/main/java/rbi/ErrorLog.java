package rbi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ErrorLog {
	
	ErrorLog(String YN, String Exception){
		File file = new File("./ExcelReader/FEMAViolation.xlsx");
		FileInputStream inStream = null;
		try {
			inStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		
		Workbook dataBook = null;
		try {
			dataBook = WorkbookFactory.create(inStream);
		} catch (EncryptedDocumentException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		Sheet dataSheet = dataBook.getSheetAt(0);
		
		int noOfRows = dataSheet.getLastRowNum();
		
			Row r = dataSheet.createRow(noOfRows + 1);	
			
			List<String> errorData = new ArrayList<String>();
			
			errorData.add(ErrorLog.currentDate());
			errorData.add(YN);
			errorData.add(Exception);			
			
			for (int col = 0; col < errorData.size(); col++) {
				Cell cell = r.createCell(col);				
				cell.setCellValue(errorData.get(col));				
			}			
		FileOutputStream outstream = null;
		try {
			outstream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		try {
			dataBook.write(outstream);
		} catch (IOException e) {
			
			e.printStackTrace();
		}	
		
		try {
			dataBook.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		try {
			outstream.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
    public static String currentDate() {

//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm.ss");  
//        LocalDate currentDate = LocalDate.now();   
        String date = new SimpleDateFormat("dd.MM.yyy, HH.mm").format(new java.util.Date());
        return date;
    }


}

