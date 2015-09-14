/*
 * Main HansardParser package ..
 */
package org.sinarproject.hansardparser;

// Java Standard libs ...
import com.itextpdf.text.DocumentException;
import java.io.IOException;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
// iTextPDF libs ..
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import itextblast.ITextBlast;
import java.io.FileNotFoundException;

/**
 *
 * @author leow
 */
public class HansardParser {

    /**
     * The resulting PDF file.
     */
    public static String SOURCE
            // = "example/DR-PARLIMEN/DR-18062015.PDF";
            = "example/DR-PARLIMEN/2015/DR-17062015.PDF";
    // = "example/DR-PARLIMEN/2015/DR-18062015.PDF";  
    // = "example/DR-PARLIMEN/2015/DR-24062013.pdf";
    // PdfReader for multiple uses?
    // The two items below should NOT be static; danger to race /override conditions likely .. :P
    static PdfReader my_reader;
    static String hansard_filename;
    static int my_error_count;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        out.println("Sinar Project's Hansard Parser ..");
        PdfReader reader = null;
        try {
            reader = new PdfReader(HansardParser.SOURCE);
        } catch (IOException ex) {
            Logger.getLogger(HansardParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Assign it for later reuse ..
        HansardParser.my_reader = reader;
        HansardParser.processHansardStructure("all");
    }

    public static void processHansardFile(String hansard_filename, String mymeta) {
        if (mymeta == null) {
            out.println("Inside the function processHansardFile!!");
            mymeta = "split"; // Avialble metas: "all", "split", "speakers"
        } else {
            out.println("Inside the function processHansardFile with meta of " + mymeta);
        }
        out.println("PROCESSING " + hansard_filename + " in " + ITextBlast.working_dir);
        // Fill up the reader link
        PdfReader reader = null;
        try {
            reader = new PdfReader(String.format(ITextBlast.working_dir + ITextBlast.SOURCE, hansard_filename));
        } catch (IOException ex) {
            Logger.getLogger(HansardParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Assign it for later reuse ..
        HansardParser.my_reader = reader;
        HansardParser.hansard_filename = hansard_filename;
        HansardParser.processHansardStructure(mymeta);
    }

    private static void processHansardStructure(String mymeta) {
        // Below gets the Topics associated with each page ..
        Map<Integer, List<String>> myHalamanHash;
        myHalamanHash = HansardParser.getHalaman(HansardParser.my_reader);
        // Below gets the start page and end page mappings
        Map<Integer, Integer> myHalamanStartEnd;
        myHalamanStartEnd = HansardParser.splitHalamanbyTopic(myHalamanHash);

        if ("all".equals(mymeta) || "split".equals(mymeta)) {
            // ACTION: Split and copy (in "all" and "split")
            try {
                // Below copies out the files and split them ..
                HansardCopy.copyHalamanbyTopic(myHalamanStartEnd, myHalamanHash);
            } catch (FileNotFoundException | DocumentException ex) {
                Logger.getLogger(HansardParser.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(HansardParser.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                // Copy the front page (index) for further use as needed
                HansardCopy.copyHalamanFrontPage();
            } catch (DocumentException | IOException ex) {
                Logger.getLogger(HansardParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if ("all".equals(mymeta) || "speakers".equals(mymeta)) {
            try {
                // ACTION: Identify speakers (in "all" and "speakers")
                // Identify the playas
                HansardSpeakers.identifySpeakersinTopic(myHalamanStartEnd, myHalamanHash);
            } catch (IOException ex) {
                Logger.getLogger(HansardParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static Map<Integer, Integer> splitHalamanbyTopic(Map<Integer, List<String>> myHalamanHash) {
        Map<Integer, Integer> Halamans = new TreeMap<>();
        // Halamans[0] = topic, start_page, end_page (1,1)
        // Halamans[1] = topic, start_page, end_page (1,26)
        // Halamans[2] = topic, start_page, end_page (27,60)
        // Halamans[3] = topic, start_page, end_page (61

        // Actual Start page (Start page + 1)
        // Actual End page (Page Number - 1)
        out.println("Sorted Pages and Title");
        out.println("=======================");
        // Send back ordered HashMap of Start page and mapped End page
        int current_start_page = 0;
        int current_end_page = 0;
        int previous_page = 0;
        // Current index - 1, fill in the end_page based on start_page - 1; if start_page != 1, else fill 1
        for (Integer myStart_Page : myHalamanHash.keySet()) {
            current_start_page = myStart_Page;
            if (previous_page == 0) {
                // do nothing
            } else {
                if (current_start_page == 1) {
                    // special case: remain as 1
                    current_end_page = 1;
                } else {
                    // check if the current_start_page has partial content from previous topic; if yes ..
                    // it will be start_page - 1
                    current_end_page = current_start_page - 1;
                }
                // Attach to previous page index the value
                out.println("Calculated block starting at page " + previous_page + " end at page " + current_end_page);
                Halamans.put(previous_page, current_end_page);
            }
            // Upddate previous_page_index to the current start_page
            previous_page = current_start_page;
            // DEBUG: Below for debugging purposes only ..
            /*
             List<String> myTopicList = myHalamanHash.get(myStart_Page);
             for (String myTopic : myTopicList) {
             out.println("Page: " + myStart_Page + " Topic: " + myTopic);
             }
             */

        }
        // At the end; we need to know that the previous page; the endpage is size of the whole doc - 1
        current_end_page = HansardParser.my_reader.getNumberOfPages() - 1;
        out.println("FINALLY: Block starting at page " + previous_page + " end at page " + current_end_page);
        Halamans.put(previous_page, current_end_page);

        return Halamans;
    }

    private static Map<Integer, List<String>> getHalaman(PdfReader myreader) {
        try {
            // OPTIONE #1:
            // RegExp for Halaman is .. /(.*)\(Halaman.*(\d+)\)/ig
            // Pattern pattern_halaman = Pattern.compile("(.*)\\(Halaman.*(\\d+)\\)", Pattern.CASE_INSENSITIVE);
            // OPTION #2:
            // Better pattern is --> http://www.regexr.com/3bkfc
            // /(.*?\n?.*?)\(Halaman.*?(\d+)\).*?\n/igm
            Pattern pattern_halaman = Pattern.compile("(.*?\\n?.*?)\\(Halaman.*?(\\d+)\\).*?\\n", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
            // Title name is $1
            // Halaman # is $2
            // Extract out and log every matched items ??
            String content = PdfTextExtractor.getTextFromPage(myreader, 1);
            // Use TreeMap to have native sort in keys
            Map<Integer, List<String>> myHalaman = null;
            myHalaman = new TreeMap<>();

            Matcher halaman_matcher = pattern_halaman.matcher(content);
            while (halaman_matcher.find()) {
                // Replace all until : pattern ..??
                Pattern pattern_header = Pattern.compile(".*\\:", Pattern.CASE_INSENSITIVE);
                // Replace anything NOT valid Title -->  [^\(\)\-\.\w\d]           
                Pattern pattern_valid_title = Pattern.compile("[^\\(\\)\\-\\. \\w\\d]", Pattern.CASE_INSENSITIVE);
                // Replace multiple spaces into one space
                Pattern pattern_single_spacing = Pattern.compile("[ ]+");
                String halaman_title
                        = pattern_single_spacing.matcher(
                                pattern_valid_title.matcher(
                                        pattern_header.matcher(
                                                halaman_matcher.group(1)
                                        ).replaceAll("")
                                ).replaceAll("")
                        ).replaceAll(" ").trim();
                out.println("Title is: " + halaman_title);
                String halaman_page_number_str = halaman_matcher.group(2).trim();
                out.println("Start Page is: " + halaman_page_number_str);
                // This is the page number ..
                Integer halaman_page_number = new Integer(halaman_page_number_str);
                // Get the list from the current key; which is page number ...
                List<String> l;
                l = myHalaman.get(halaman_page_number);
                if (l == null) {
                    myHalaman.put(halaman_page_number, l = new ArrayList<>());
                }
                l.add(halaman_title);
                // myHalaman.put(halaman_title, halaman_page_number_str);
                // Actually do the copy here??
                // Sort by page to rearrange the split ..
                // [1:a]   ====>  [1:a]
                // [1:b]   ====>  [1:b]
                // [27:c]  ====>  [23:e]
                // [61:d]  ====>  [27:c]
                // [23:e]  ====>  [23:e]
            }

            return myHalaman;
        } catch (IOException ex) {
            Logger.getLogger(HansardParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }
}
