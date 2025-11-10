package co.edu.uniquindio.service.utils.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Clase utilitaria para convertir HTML en bytes PDF.
 */
public class HtmlToPdfConverter {

    public static byte[] convertHtmlToPdf(String html) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            XMLWorkerHelper.getInstance().parseXHtml(
                    writer, document,
                    new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)),
                    StandardCharsets.UTF_8
            );

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error converting HTML to PDF: " + e.getMessage());
        }
    }
}