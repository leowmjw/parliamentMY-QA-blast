/*
 * All related to Speakers, Speech Block, Speaking Identifiers, Current Speaker
 *  Spillover-Speaker
 */
package org.sinarproject.hansardparser;

import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import java.io.IOException;
import static java.lang.System.out;
import java.util.ArrayList;
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
    public static final String RESULT_TRANSCRIPT_UNSURE = "./results/%s/h-transcript-%s-unsure.pdf";

    public static void identifySpeakersinTopic(Map<Integer, Integer> myHalamanStartEnd, Map<Integer, List<String>> myHalamanHash) throws IOException {
        Map<String, String> hansard_complete_speakers;
        hansard_complete_speakers = new TreeMap<>();
        Map<String, String> hansard_unsure_speakers;
        hansard_unsure_speakers = new TreeMap<>();
        List<Map<String, String>> hansard_complete_logs;
        hansard_complete_logs = new ArrayList<>();
        List<Map<String, String>> hansard_unsure_logs;
        hansard_unsure_logs = new ArrayList<>();

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
                // Clean the content out of DR headers before next stage in the pipelines ..
                String content = Utils.cleanActionsInHall(
                        Utils.cleanContentHeader(
                                PdfTextExtractor.getTextFromPage(HansardParser.my_reader, i)
                        )
                );
                // out.println(content);
                // Identify people ..
                hansard_complete_speakers.putAll(observeSpeakers(content));
                Utils.writeMergedSpeakers(hansard_complete_speakers, "");
                // Identify speech block and order them out ..
                //  put under the growing array for this topic
                hansard_complete_logs.addAll(preparePage(content));
                Utils.writeMergedSpeechTranscripts(hansard_complete_logs, "");
                // extract and write out into JSON log as per Topic
                // ... and what they say??
                // How to regexp detect paragraph ..
            }
            // Like in HansardCopy.java
            if (!((end_page == 1)
                    || (end_page >= HansardParser.my_reader.getNumberOfPages()))) {
                // Put the Maybe here ..
                out.println("Page " + (end_page + 1));
                out.println("===========");
                String content = Utils.cleanActionsInHall(
                        Utils.cleanContentHeader(
                                PdfTextExtractor.getTextFromPage(
                                        HansardParser.my_reader, (end_page + 1)
                                )
                        )
                );
                // out.println(content);
                // Identify people ..
                hansard_unsure_speakers.putAll(observeSpeakers(content));
                Utils.writeMergedSpeakers(hansard_unsure_speakers, "");
                // Identify speech block and order them out ..
                //  put under the growing array for this topic
                hansard_unsure_logs.addAll(preparePage(content));
                Utils.writeMergedSpeechTranscripts(hansard_unsure_logs, "");
                // extract and write out into JSON log as per Topic
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
            // What to do with the HaansardComplete Map??
            // DUMP it out to file per topic!!
            // DEBUG: For demo; break out after first cycle as per below:
            break;

        }

        out.println("Final ERR Count: " + HansardParser.my_error_count);
    }

    /*
     * Use pattern to extract out unique sorted list of Speakers
     * FUTURE: Pull data from PopIt to identify?
     * Returns: Sorted List of speakers 
     */
    private static Map<String, String> observeSpeakers(String content) {
        // Inits ..
        Map<String, String> speaker_map = new TreeMap<>();
        // Pattern below
        Pattern pattern_mark_speakers;
        pattern_mark_speakers = Pattern.compile("(.+?\\:)");
        Matcher found_speakers = pattern_mark_speakers.matcher(content);
        // DEBUG:
        // out.println("SPEAKERS:");
        while (found_speakers.find()) {
            String normalized_speaker_name = Utils.cleanSpeakersName(found_speakers.group(1));
            // Just put it .. is fine .. only unique KV
            speaker_map.put(normalized_speaker_name, found_speakers.group(1).trim());
            // DEBUG: Below to show conversion to the normalized form
            // out.println("Normalized:" + found_speakers.group(1) + " ==> " + normalized_speaker_name);
        }
        // DEBUG: 
        // out.println("<<<<<<<<<<<<<>>>>>>>>>>>>");
        return speaker_map;
    }

    /*
     * Input: content
     * Returns: Map<String, List<Map<String, String>>> overAllSpeakersLogs
     */
    private static List<Map<String, String>> preparePage(String content) {
        // Prepare page for analysis
        // Remove the DR line
        // Remove timeline marker??
        // Extract out the sections; if exist at least one : in it
        Pattern pattern_speaker_exist;
        pattern_speaker_exist = Pattern.compile("\\:");
        Pattern pattern_speaker_alt_exist;
        pattern_speaker_alt_exist = Pattern.compile("\\[.+?\\]");
        // TODO: Future idea; make the timestamp pattern be recognized as a speaker
        //      hardcode in a Speaker name of IMOKMAN_Clock:  10.45 am ..
        List<Map<String, String>> speakers_transcript_map;
        // Identify all the players and append the special >>IMOKMAN** tag to name
        // else if match the alternative patterns is OK too ...
        if (pattern_speaker_exist.matcher(content).find()
                || pattern_speaker_alt_exist.matcher(content).find()) {
            out.println("Found at least one speaker :) ... replacing >>> ");
            // put here since need to use it first to identify speakers in page ..
            Pattern pattern_mark_speakers;
            pattern_mark_speakers = Pattern.compile("(.+?\\:)");
            // DEBUG: Speakers found; no uniq, that will be handled later in prepareSpeechBlock
            /*
             Matcher found_speakers = pattern_mark_speakers.matcher(content);
             out.println("SPEAKERS:");
             while (found_speakers.find()) {
             out.println(found_speakers.group(1));
             }
             out.println("<<<<<<<<<<<<<>>>>>>>>>>>>");
             */
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
            // Get the speech blocks as a timeline out ..
            speakers_transcript_map = prepareSpeechBlock(final_marked_content);

        } // else process and atatch to previous speaker
        else {
            // Skip for now
            out.println("Found no speaker; assign to previous identified speaker ==> " + HansardSpeakers.last_identified_speaker);
            speakers_transcript_map = new ArrayList<>();
            Map<String, String> m;
            m = new HashMap<>();
            // Next time attch to the last known speaker ..
            m.put(HansardSpeakers.last_identified_speaker, content);
            // Put back all the needed dta ..
            speakers_transcript_map.add(m);
        }
        // Return the Map; even though it may be empty!!
        return speakers_transcript_map;
    }

    // 
    // Pass In: Final marked up content with the special IMOKMAN keyword
    // Returns: Map of SpeechBlock; hash [Normalized_Speaker][] --> 
    //
    private static List<Map<String, String>> prepareSpeechBlock(String final_marked_content) {

        // replace all newline with space /\n+/g
        Pattern pattern_newlines;
        pattern_newlines = Pattern.compile("\\n+");
        final_marked_content = pattern_newlines.matcher(final_marked_content).replaceAll(" ");
        // Look for the speaker pattern --> /(\s+.+?|IMOKMAN\*\*)(.+?)(>>|$)/g;
        //  and have each section recognized .. $2 is the SpeechBlock
        Pattern pattern_marked_speakers;
        pattern_marked_speakers = Pattern.compile("(\\s+.+?|IMOKMAN\\*\\*)(.+?)(>>|$)");

        Matcher matched_marked_speakers = pattern_marked_speakers.matcher(final_marked_content);
        // =========================
        // Initialize Ordered List for speech transcript; each row with 
        //  one speaker and what the speaker said mapping
        List<Map<String, String>> speaker_transcript = new ArrayList<>();
        // Structure Looks like below: 
        // List[] --> [speaker]=> [content_transcript]

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
            // Initialize data to be added every loop through the while ..
            String final_message;
            final_message = "";
            String final_speaker;
            final_speaker = "";
            if (found_speakers.find()) {
                final_speaker = Utils.cleanSpeakersName(found_speakers.group(1));
                final_message = found_speakers.replaceAll("");
                Map<String, String> m;
                m = new TreeMap();
                m.put(final_speaker, final_message);
                speaker_transcript.add(m);
                // Mark the last guy to move on to the next page ..
                HansardSpeakers.last_identified_speaker = final_speaker;
            } else if (matched_alt_speakers.find()) {
                final_speaker = Utils.cleanSpeakersName(matched_alt_speakers.group(1));
                final_message = matched_alt_speakers.replaceAll("");
                Map<String, String> m;
                m = new TreeMap();
                m.put(final_speaker, final_message);
                speaker_transcript.add(m);
                // Mark the last guy to move on to the next page ..
                HansardSpeakers.last_identified_speaker = final_speaker;
            } else {
                // final_speaker = "ERR";
                final_speaker = HansardSpeakers.last_identified_speaker;
                final_message = matched_speech_block;
                HansardParser.my_error_count++;
                // DEBUG: Below for debugging purposes ..

                out.println("ERROR_SPEECH_BLOCK");
                out.println("Speaker " + final_speaker + " says ---> " + final_message);
                out.println("=============================");
                out.println(final_marked_content);

            }
        }
        // return Map that was initialized earlier .. even if it is empty
        return speaker_transcript;
    }

}
