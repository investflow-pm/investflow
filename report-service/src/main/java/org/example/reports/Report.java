package org.example.reports;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.example.tinkoff.instruments.InfoInstruments;
import ru.tinkoff.piapi.core.InvestApi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Report {
    private static String filename;

    private static String sheetname;
    private static Workbook wb = new HSSFWorkbook();
    private static Sheet sheet;
    private static String[] headers = new String[7];
    public static void createSheet() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filename)){
            wb. write(fos);
            System.out.println(sheetname + " report rady");
        }
    }
    public Report setTitle(String title1) {
        Row row = sheet.createRow(2);
        Cell cell = row.createCell(2);
        return this;
    }

    public Report setSheetname(String sheetname) {
        Report.sheetname = sheetname;
        sheet= wb.createSheet(sheetname);
        return this;
    }

    public Report setFilename(String filename) {
        Report.filename = filename;
        return this;
    }

    public Report setHeaders(String[] headers) {
        this.headers = headers;
        header(headers);
        return this;
    }
    private void header(String[] headers) {
        Row row = sheet.createRow(1);
        int i = 0;
        for (String s: headers) {
            Cell cell = row.createCell(i++);
            cell.setCellValue(s);
        }
    }
    public Report setDataFigi(ArrayList<String> positions, InvestApi api) {
        int i = 2;
        InfoInstruments infoInstruments = new InfoInstruments(api);
        for (String s: positions) {
            int j = 0;
            Row row = sheet.createRow(i++);
            Cell cellFigi = row.createCell(j++);
            Cell cellName = row.createCell(j++);
            Cell cellTicker = row.createCell(j++);
            Cell cellCurency = row.createCell(j++);
            Cell cellLot = row.createCell(j++);
            Cell cellPrice = row.createCell(j++);
            cellFigi.setCellValue(s);
            cellName.setCellValue(infoInstruments.getName(s));
            cellTicker.setCellValue(infoInstruments.getTicker(s));
            cellCurency.setCellValue(infoInstruments.getCurency(s));
            cellLot.setCellValue(infoInstruments.getLot(s));
            cellPrice.setCellValue(infoInstruments.getPrice(s));
        }
        return this;
    }
}
