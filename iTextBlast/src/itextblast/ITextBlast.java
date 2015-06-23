/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package itextblast;

import java.io.FileOutputStream;
import java.io.IOException;

// import part1.chapter03.MovieTemplates;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;

/**
 *
 * @author leow
 */
public class ITextBlast {

    /**
     * Format of the resulting PDF files.
     */
    public static final String RESULT
            = "./results/soalan-%d.pdf";

    /**
     * Main method.
     *
     * @param args no arguments needed
     * @throws DocumentException
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {

        // use one of the previous examples to create a PDF
        // new MovieTemplates().createPdf(MovieTemplates.RESULT);
        // Create a reader; from current existing file
        // Next time pass it from args ..
        PdfReader reader = new PdfReader("./source/imokman.pdf");
        // We'll create as many new PDFs as there are pages
        Document document;
        PdfCopy copy;
        // loop over all the pages in the original PDF
        int n = reader.getNumberOfPages();
        for (int i = 0; i < n;) {
            // step 1
            document = new Document();
            // step 2
            copy = new PdfCopy(document,
                    new FileOutputStream(String.format(RESULT, ++i)));
            // step 3
            document.open();
            // step 4
            copy.addPage(copy.getImportedPage(reader, i));
            // step 5
            document.close();
        }
        reader.close();
    }

    //in each pdf folder splitted pdf files into.pdf soalan-x.pdf soalan-x-pdf
    // <Original_FileName>/intro.pdf
    // <original_filename>/soalan-x.pdf
    /*
     * This class is part of the book "iText in Action - 2nd Edition"
     * written by Bruno Lowagie (ISBN: 9781935182610)
     * For more info, go to: http://itextpdf.com/examples/
     * This example only works with the AGPL version of iText.
     */
        public static void splitByPage(String[] args) throws IOException, DocumentException {

        // use one of the previous examples to create a PDF
        // new MovieTemplates().createPdf(MovieTemplates.RESULT);
        // Create a reader; from current existing file
        // Next time pass it from args ..
        PdfReader reader = new PdfReader("./source/imokman.pdf");
        // We'll create as many new PDFs as there are pages
        Document document;
        PdfCopy copy;
        // loop over all the pages in the original PDF
        int n = reader.getNumberOfPages();
        for (int i = 0; i < n;) {
            // step 1
            document = new Document();
            // step 2
            copy = new PdfCopy(document,
                    new FileOutputStream(String.format(RESULT, ++i)));
            // step 3
            document.open();
            // step 4
            copy.addPage(copy.getImportedPage(reader, i));
            // step 5
            document.close();
        }
        reader.close();
    }

}
