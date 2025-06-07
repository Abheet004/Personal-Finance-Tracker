package com.abheet.finance.exporter;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.abheet.finance.entity.Transaction;

public class ExcelExporter {
    private List<Transaction> txns;
    private XSSFWorkbook workbook;
    private Sheet sheet;

    public ExcelExporter(List<Transaction> txns) {
        this.txns = txns;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Transactions");
    }

    private void writeHeaderRow() {
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        row.createCell(0).setCellValue("Date");
        row.createCell(1).setCellValue("Type");
        row.createCell(2).setCellValue("Category");
        row.createCell(3).setCellValue("Amount");

        for (int i = 0; i < 4; i++) {
            row.getCell(i).setCellStyle(style);
        }
    }

    private void writeDataRows() {
        int rowCount = 1;

        for (Transaction t : txns) {
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue(t.getDate().toString());
            row.createCell(1).setCellValue(t.getType());
            row.createCell(2).setCellValue(t.getCategory());
            row.createCell(3).setCellValue(t.getAmount());
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderRow();
        writeDataRows();

        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        workbook.close();
        out.close();
    }
}
