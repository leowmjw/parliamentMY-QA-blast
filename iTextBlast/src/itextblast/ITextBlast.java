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
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import java.io.FileNotFoundException;
import static java.lang.System.out;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author leow
 */
public class ITextBlast {

    /**
     * Format of the resulting PDF files.
     */
    public static final String RESULT
            = "./results/soalan-%s.pdf";
    private static PdfReader my_reader;

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
        ITextBlast.my_reader = reader;
        // We'll create as many new PDFs as there are pages
        // Document document;
        // PdfCopy copy;
        // loop over all the pages in the original PDF
        int n = reader.getNumberOfPages();
        // For test of extraction and regexp; use first 5 pages ..
        n = 15;
        // Text Extraction Strategy here ...
        // LocationTextExtractionStrategy strategy = new LocationTextExtractionStrategy();
        // SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        // Both ^ does not work well; weird behavior ... no need so clever ..
        Pattern pattern;
        // pattern = Pattern.compile("^.*NO.*SOALAN.*?(\\d+).*$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        // pattern = Pattern.compile(".*SOALAN.*?(\\d+).*", Pattern.CASE_INSENSITIVE);
        pattern = Pattern.compile(".*NO.*SOALAN.*?(\\d+)\\b+.*", Pattern.CASE_INSENSITIVE);
        // OPTION 2 is to try with the next available number between word boundaries .. but may then need non-greedy ..
        // Init start and end page
        int start_page = 1;
        int end_page = 1;
        String question_number = "0-intro";
        for (int i = 1; i < n; i++) {
            // init found_question_number
            String found_question_number = null;
            // PdfDictionary page = reader.getPageN(i);
            // use location based strategy
            out.println("Page " + i);
            out.println("===========");
            // out.println(PdfTextExtractor.getTextFromPage(reader, i, strategy));
            String content = PdfTextExtractor.getTextFromPage(reader, i);
            // DEBUG: Uncomment below ..
            // out.println(content);
            Matcher matcher = pattern.matcher(content);
            boolean found_match = false;
            while (matcher.find()) {
                found_match = true;
                // Extract the question number based on backreference
                found_question_number = matcher.group(1);
                // How will it look when using a different strategy?
                out.println("Matched " + matcher.group(0) + " and Question Number: " + found_question_number);
            }
            // If matched; take out the last start, end 
            if (found_match) {
                // copy page over and write it down ..
                end_page = i - 1;
                if (end_page < 1) {
                    end_page = 1;
                }
                // Write based on previous confirmed question_number
                ITextBlast.copySelectedQuestionPage(start_page, end_page, question_number);
                // re-set to current page
                start_page = i;
                end_page = i;
                question_number = found_question_number;
            }
            // out.println(PdfTextExtractor.getTextFromPage(reader, i));
            // Pattern RegExp:  #^.*NO.*SOALAN.*(\d)+$#im
            out.println();
            out.println();
            // use helper file to dump out        
            // Look out for pattern  "NO. SOALAN"
            // Once see pattern or reach end; snip off copy from start to end
            // reset start/end
            // else increase the end
        }
        // If end of the loop there are still straglers; mark with the special question_number = 999
        if (start_page <= end_page) {
            // Should always happen actually ..
            ITextBlast.copySelectedQuestionPage(start_page, end_page, question_number);
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

    private static void copySelectedQuestionPage(int start_page, int end_page, String question_number)
            throws FileNotFoundException, DocumentException, IOException {

        Document document;
        PdfCopy copy;
        document = new Document();
        copy = new PdfCopy(document,
                new FileOutputStream(String.format(RESULT, question_number)));
        document.open();
        for (int i = start_page; i <= end_page; i++) {
            copy.addPage(copy.getImportedPage(ITextBlast.my_reader, i));
        }
        document.close();
    }

}
