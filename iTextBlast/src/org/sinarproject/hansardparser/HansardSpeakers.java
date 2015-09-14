/*
 * All related to Speakers, Speech Block, Speaking Identifiers, Current Speaker
 *  Spillover-Speaker
 */
package org.sinarproject.hansardparser;

import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import java.io.IOException;
import static java.lang.System.out;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author leow
 */
public class HansardSpeakers {

    private static String last_identified_speaker;
    public static final String RESULT_SPEAKERS = "./results/%s/h-speakers-%s.pdf";
    public static final String RESULT_SPEAKERS_UNSURE = "./results/%s/h-speakers-%s-unsure.pdf";
    public static final String RESULT_TRANSCRIPT = "./results/%s/h-transcript-%s.pdf";

    public static void identifySpeakersinTopic(Map<Integer, Integer> myHalamanStartEnd, Map<Integer, List<String>> myHalamanHash) throws IOException {
        // NOTE: Assumes: first page is index; and there is offset .. does it apply across the spectrum??
        // Should probably put a safe guard to test actual start/end ...
        // Look out for the DR pattern and its page number ..
        // TODO: Above ..
        // Prepare the data structures to be returned .. (see Utils.java)
        Map<String, String> myHansardComplete = new HashMap<>();
        // HansardComplete['Topic Title']['Speakers'] --> {  [name:'Speaker1', name:'Speaker2']}
        // HansardComplete['Topic Title']['Log'] --> SpeechBlock1 --> SpeechBlock2 --> Timestamp .. --> ..

        for (Integer current_page : myHalamanStartEnd.keySet()) {
            // Get the cleaned up topicbypagenumber .. which is the KEY to the Map 
            String topicbyPageNumber;
            topicbyPageNumber = Utils.getTopicbyPageNumber(current_page, myHalamanHash);
            // Above is the main key: TOPIC
            // TODO: Below to initialize the 4 subkeyspace: Speakers, Log, Speakers_Maybe, Log_Maybe
            // Start iterating through all content ..
            int start_page = current_page + 1;
            int end_page = myHalamanStartEnd.get(current_page) + 1;
            out.println("For current block with title: " + topicbyPageNumber + " start page is " + start_page + " and end page is " + end_page);
            for (int i = start_page; i <= end_page; i++) {
                // PdfDictionary pageDict = reader.getPageN(i);
                // use location based strategy
                out.println("Page " + i);
                out.println("===========");
                String content = PdfTextExtractor.getTextFromPage(HansardParser.my_reader, i);
                // out.println(content);
                // Identify people ..
                preparePage(content);
                // ... and what they say??
                // How to regexp detect paragraph ..
            }
            // Like in HansardCopy.java
            if (!((end_page == 1)
                    || (end_page >= HansardParser.my_reader.getNumberOfPages()))) {
                // Put the Maybe here ..
                out.println("Page " + (end_page + 1));
                out.println("===========");
                String content = PdfTextExtractor.getTextFromPage(HansardParser.my_reader, (end_page + 1));
                // out.println(content);
                // Identify people ..
                preparePage(content);
                // The maybes .. attach to the MyStars as game interface or PyBossa: Partial; yes? no? yes --> clean and identify
                // HansardComplete['Topic Title']['Speakers_Maybe'] --> Attach Speakers in possible; to be cleaned manually
                // HansardComplete['Topic Title']['Log_Maybe'] --> Attach Log; can clean up similarly
                // HansardComplete['Topic Title']['PDF_Maybe'] --> File name for the Maybes ..    
                // Returns parsedPage => Map<String, List<String>>
                // Sorted List? Scan? If no exist; only append?
                // parsedPage['speakers'] Map<String, Map<String,String>>
                // HashMap or TreeMap?
                // Map<String, Map<String, String>> mymap = new TreeMap<>();
            }
            // Rescan needed? no need; just give CMS to tag their speech ..
            // DEBUG: For demo; break out after first cycle as per below:
            // break;

            // What to do with the HaansardComplete Map??
            // DUMP it!!
        }

        out.println("Final ERR Count: " + HansardParser.my_error_count);
    }

    /*
     *
     * Returns: Map<String,String> overAllSpeakers
     */
    private static void preparePage(String content) {
        // Prepare page for analysis
        // Remove the DR line
        // Remove timeline marker??
        // Extract out the sections; if exist at least one : in it
        Pattern pattern_speaker_exist;
        pattern_speaker_exist = Pattern.compile("\\:");
        Pattern pattern_speaker_alt_exist;
        pattern_speaker_alt_exist = Pattern.compile("\\[.+?\\]");
        // Identify all the players and append the special >>IMOKMAN** tag to name
        // else if match the alternative patterns is OK too ...
        if (pattern_speaker_exist.matcher(content).find()
                || pattern_speaker_alt_exist.matcher(content).find()) {
            out.println("Found at least one speaker :) ... replacing >>> ");
            // put here since need to use it first to identify speakers in page ..
            Pattern pattern_mark_speakers;
            pattern_mark_speakers = Pattern.compile("(.+?\\:)");
            Matcher found_speakers = pattern_mark_speakers.matcher(content);
            // Below for debugging purpose; should be attached to overall instead??
            out.println("SPEAKERS:");
            while (found_speakers.find()) {
                out.println(found_speakers.group(1));
            }
            out.println("<<<<<<<<<<<<<>>>>>>>>>>>>");
            // Do ALT first ... otherwise will have double ..
            Pattern pattern_mark_alt_speakers;
            // Pattern is [ ]+?(.+?\[.+?\])\s+?
            pattern_mark_alt_speakers = Pattern.compile("([ ]+.+\\[.+\\]\\s+?)");
            // /(.+?\:)/g replace with >>IMOKMAN**$1
            // Should check for the special exception of not being person Tuan Speaker ..
            Matcher matched_alt_speakers = pattern_mark_alt_speakers.matcher(content);
            // replace the specific case ..
            String marked_content = matched_alt_speakers.replaceAll(">>IMOKMAN**$1");
            // Start the main replacement .. the general one
            // /(.+?\:)/g replace with >>IMOKMAN**$1
            // Should check for the special exception of not being person Tuan Speaker ..
            Matcher matched_speakers = pattern_mark_speakers.matcher(marked_content);
            String final_marked_content;
            final_marked_content = matched_speakers.replaceAll(">>IMOKMAN**$1");
            // For debugging
            // out.println(final_marked_content);
            prepareSpeechBlock(final_marked_content);

        } // else process and atatch to previous speaker
        else {
            // Skip for now
            out.println("Found no speaker :( ... skipping ..");
            // Next time attch to the last known speaker ..
        }

    }

    private static void prepareSpeechBlock(String final_marked_content) {

        // replace all newline with space /\n+/g
        Pattern pattern_newlines;
        pattern_newlines = Pattern.compile("\\n+");
        final_marked_content = pattern_newlines.matcher(final_marked_content).replaceAll(" ");
        // Look for the speaker pattern --> /(\s+.+?|IMOKMAN\*\*)(.+?)(>>|$)/g;
        //  and have each section recognized .. $2 is the SpeechBlock
        Pattern pattern_marked_speakers;
        pattern_marked_speakers = Pattern.compile("(\\s+.+?|IMOKMAN\\*\\*)(.+?)(>>|$)");

        Matcher matched_marked_speakers = pattern_marked_speakers.matcher(final_marked_content);
        while (matched_marked_speakers.find()) {
            String matched_speech_block = matched_marked_speakers.group(2);
            // Check both patterns to extract out speaker ..
            Pattern pattern_mark_speakers;
            pattern_mark_speakers = Pattern.compile("(.+?)\\:");
            Matcher found_speakers = pattern_mark_speakers.matcher(matched_speech_block);
            Pattern pattern_mark_alt_speakers;
            // Pattern is [ ]+?(.+?\[.+?\])\s+?
            pattern_mark_alt_speakers = Pattern.compile("([ ]+.+\\[.+\\]\\s+?)");
            Matcher matched_alt_speakers = pattern_mark_alt_speakers.matcher(matched_speech_block);

            // Below for SPEECH_BLOCK
            String final_message = "";
            String final_speaker = "";
            if (found_speakers.find()) {
                final_speaker = found_speakers.group(1);
                final_message = found_speakers.replaceAll("");
            } else if (matched_alt_speakers.find()) {
                final_speaker = matched_alt_speakers.group(1);
                final_message = matched_alt_speakers.replaceAll("");
            } else {
                final_speaker = "ERR";
                final_message = matched_speech_block;
                HansardParser.my_error_count++;
            }
            // DEBUG: Below for debugging purposes ..
            /*
             out.println("SPEECH_BLOCK");
             out.println("Speaker " + final_speaker + " says ---> " + final_message);
             out.println("=============================");
             */
            // Split out speaker from what was said; look for the : pattern
            // Maybe even detect time marker??
            // Special case; from previous page; append the previous guy ..
            // Mark the last guy to move on to the next page ..

        }
    }

}
