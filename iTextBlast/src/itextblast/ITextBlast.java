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
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import java.io.FileNotFoundException;
import static java.lang.System.out;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.sinarproject.hansardparser.HansardParser;

/**
 *
 * @author leow
 */
public class ITextBlast {

    /**
     * Format of the resulting PDF files.
     */
    public static final String RESULT = "./results/%s/soalan-%s.pdf";
    public static final String SOURCE = "./source/%s.pdf";
    public static String working_dir = "";
    private static PdfReader my_reader;
    private static String qa_filename;
    private static String myaction;
    private static String mymeta;

    /**
     * Main method.
     *
     * @param args no arguments needed
     */
    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                ITextBlast.working_dir = args[0];
                ITextBlast.qa_filename = args[1];
                if (args.length > 2) {
                    ITextBlast.myaction = args[2];
                } else {
                    ITextBlast.myaction = "default";
                }
                if (args.length > 3) {
                    ITextBlast.mymeta = args[3];
                } else {
                    ITextBlast.mymeta = "";
                }
            } else {
                // Extract filemame from CLI
                // otherwise use below as default ..
                ITextBlast.qa_filename = "imokman";
            }
            out.println("PROCESSING " + ITextBlast.qa_filename + " in " + ITextBlast.working_dir);
            // TODO: as preparation; make sure the inout file actually exists first!!
            // TODO: as preparation; create the resulting output folder?? if does not exist already
            // TODO: Should ne more flexible than requiring the exact correct order; 
            //  but leave that as an exercise for the future
            if ("default".equals(ITextBlast.myaction)) {
                out.println("Default behavor ..");
                // Default behavior ..
                ITextBlast.processQAFile(ITextBlast.qa_filename);
            } else if ("--parser=hansard".equals(ITextBlast.myaction)) {
                // Pass in the filename of the PDF beig processed ..
                // TODO: should refactor and rename variable
                HansardParser.processHansardFile(ITextBlast.qa_filename, ITextBlast.mymeta);
            } else {
                // Don;t know what to do; note it and go away .. possibly throw error?
                out.println("I don't know what to do.  Arg is " + ITextBlast.myaction);
            }
        } catch (IOException | DocumentException ex) {
            Logger.getLogger(ITextBlast.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void processQAFile(String qa_filename) throws IOException, DocumentException {

        // use one of the previous examples to create a PDF
        // new MovieTemplates().createPdf(MovieTemplates.RESULT);
        // Create a reader; from current existing file
        // Next time pass it from args ..
        PdfReader reader = new PdfReader(String.format(ITextBlast.working_dir + SOURCE, qa_filename));
        ITextBlast.my_reader = reader;
        // We'll create as many new PDFs as there are pages
        // Document document;
        // PdfCopy copy;
        // loop over all the pages in the original PDF
        int n = reader.getNumberOfPages();
        // For test of extraction and regexp; use first 5 pages ..
        // n = 15;
        // Text Extraction Strategy here ...
        // LocationTextExtractionStrategy strategy = new LocationTextExtractionStrategy();
        // SimpleTextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        // Both ^ does not work well; weird behavior ... no need so clever ..
        // START SMART Start Number ********
        Pattern smart_start_pattern;
        smart_start_pattern = Pattern.compile(".*?SOALAN.*?N.*?O.*?(\\d+)\\b+.*", Pattern.CASE_INSENSITIVE);
        // Extract cover page number as smartly as possible??
        String cover_page_content = PdfTextExtractor.getTextFromPage(reader, 1);
        Matcher smart_start_matcher = smart_start_pattern.matcher(cover_page_content);
        String smart_start_question_number = null;
        if (smart_start_matcher.find()) {
            // Extract the question number based on backreference
            smart_start_question_number = smart_start_matcher.group(1);
            // How will it look when using a different strategy?
            out.println("Matched " + smart_start_matcher.group(0) + " and SMART Start Number: " + smart_start_question_number);
        }
        // END SMART Start Number ********
        Pattern liberal_found_question_pattern_uno;
        liberal_found_question_pattern_uno = Pattern.compile(".*N.*O.*SOALAN.*", Pattern.CASE_INSENSITIVE);
        Pattern liberal_found_question_pattern_dos = Pattern.compile(".*SOALAN.*N.*O.*", Pattern.CASE_INSENSITIVE);
        Pattern pattern_uno;
        // pattern = Pattern.compile("^.*NO.*SOALAN.*?(\\d+).*$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        // pattern = Pattern.compile(".*SOALAN.*?(\\d+).*", Pattern.CASE_INSENSITIVE);
        pattern_uno = Pattern.compile(".*N.*O.*SOALAN.*?(\\d+)\\b+.*", Pattern.CASE_INSENSITIVE);
        Pattern pattern_dos = Pattern.compile(".*SOALAN.*N.*O.*?(\\d+)\\b+.*", Pattern.CASE_INSENSITIVE);
        // OPTION 2 is to try with the next available number between word boundaries .. but may then need non-greedy ..
        // Init start and end page
        int start_page = 1;
        int end_page = 1;
        String question_number = "0-intro";
        for (int i = 1; i < n; i++) {
            // init found_question_number
            String found_question_number = null;
            boolean found_match = false;
            // PdfDictionary page = reader.getPageN(i);
            // use location based strategy
            out.println("Page " + i);
            out.println("===========");
            // out.println(PdfTextExtractor.getTextFromPage(reader, i, strategy));
            String content = PdfTextExtractor.getTextFromPage(reader, i);
            // DEBUG: Uncomment below ..
            // out.println(content);
            Matcher liberal_uno_matcher = liberal_found_question_pattern_uno.matcher(content);
            if (liberal_uno_matcher.find()) {
                out.println("Matched UNO!");
                found_match = true;
                Matcher matcher = pattern_uno.matcher(content);
                // Loop to find the digit; it is possible it is not found an dleft as null ..
                while (matcher.find()) {
                    // Extract the question number based on backreference
                    found_question_number = matcher.group(1);
                    // How will it look when using a different strategy?
                    out.println("Matched " + matcher.group(0) + " and Question Number: " + found_question_number);
                }
            } else if (liberal_found_question_pattern_dos.matcher(content).find()) {
                if ("0-intro".equals(question_number)) {
                    out.println("SMART!!!");
                } else {
                    found_match = true;
                    out.println("Matched DOS!");
                    Matcher matcher = pattern_dos.matcher(content);
                    // Loop to find the digit; it is possible it is not found an dleft as null ..
                    while (matcher.find()) {
                        // Extract the question number based on backreference
                        found_question_number = matcher.group(1);
                        // How will it look when using a different strategy?
                        out.println("Matched " + matcher.group(0) + " and Question Number: " + found_question_number);
                    }

                }
            }
            // If matched; take out the last start, end 
            if (found_match) {
                // copy page over and write it down ..
                end_page = i - 1;
                if (end_page < 1) {
                    end_page = 1;
                }
                if (null == found_question_number) {
                    if ("0-intro".equals(question_number)) {
                        // After intro; if got problem; try the smart start
                        found_question_number = smart_start_question_number;
                        out.println("First question could not determine number; using Q No. => " + found_question_number);
                        // Print out content to debug
                        out.println("*****DEBUG Content*******");
                        out.println(content);
                    } else {
                        // otherwise; use current question and just append Unix timestamp ..
                        found_question_number = question_number + "_" + (System.currentTimeMillis() / 1000L);
                        out.println("Unexpectedly could not determine number; using Q No. => " + found_question_number);
                        // Print out content to debug
                        out.println("*****DEBUG Content*******");
                        out.println(content);
                    }
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
                new FileOutputStream(String.format(ITextBlast.working_dir + RESULT, ITextBlast.qa_filename, question_number)));
        document.open();
        for (int i = start_page; i <= end_page; i++) {
            copy.addPage(copy.getImportedPage(ITextBlast.my_reader, i));
        }
        document.close();
    }

}
