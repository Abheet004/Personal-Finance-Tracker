package com.abheet.finance.exporter;

import com.abheet.finance.entity.Transaction;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import jakarta.servlet.http.HttpServletResponse;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

public class PDFExporter {
    private List<Transaction> txns;

    public PDFExporter(List<Transaction> txns) {
        this.txns = txns;
    }

    public void export(HttpServletResponse response) throws IOException {
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, response.getOutputStream());

            document.open();
            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLACK);
            Paragraph title = new Paragraph("Transaction Report", fontTitle);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" ")); // blank line

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            writeTableHeader(table);
            writeTableData(table);

            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setPadding(5);
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        cell.setPhrase(new Phrase("Date", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Type", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Category", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Amount", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        for (Transaction t : txns) {
            table.addCell(t.getDate().toString());
            table.addCell(t.getType());
            table.addCell(t.getCategory());
            table.addCell(String.valueOf(t.getAmount()));
        }
    }
}
