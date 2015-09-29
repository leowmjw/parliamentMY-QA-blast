/*
 * Put the misc Utils that can be used here ...
 */
package org.sinarproject.hansardparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.boon.Boon;
import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;

/**
 *
 * @author leow
 */
public class Utils {

    // Patterns for the various regexp used
    private static final String more_than_one_space_regexp = "\\s+";
    private static final Pattern pattern_more_than_one_space = Pattern.compile(more_than_one_space_regexp);
    // above try the private static final pattern ..
    private static final String disallow_number_dot_speakers_regexp = "[^\\w\\s]";
    private static final Pattern pattern_illegal_speaker = Pattern.compile(disallow_number_dot_speakers_regexp);
    private static final String not_word_number_topic_regexp = "[^\\w\\d\\s]";
    private static final Pattern pattern_illegal_topic = Pattern.compile(not_word_number_topic_regexp);
    private static final String document_page_header = "^DR\\.\\d+\\.\\d+\\.\\d+.*\\d+";
    private static final Pattern pattern_document_page_header = Pattern.compile(document_page_header);
    private static final String imokman_timestamp = "■(.*)";
    private static final Pattern pattern_imokman_timestamp = Pattern.compile(imokman_timestamp);
    private static final String actions_in_hall = "(\\[(dewan riuh)\\]|\\[(tepuk)\\]"
            + "|\\[(ketawa)\\]|\\[(bangun)\\]|\\[(menyampuk)\\])";
    //      + "|\\[(bercakap.*?)\\]|\\[(menunjuk.*?)\\]|\\[(masa.*?)\\]|\\[(usul.*?)\\]|\\[(bentara.*?)\\])";
    private static final Pattern pattern_actions_in_hall = Pattern.compile(actions_in_hall, Pattern.CASE_INSENSITIVE);
    // Below are the data structures for maintainign the final mapping for use outside ..
    // HansardComplete['Speakers'] --> {  [name:'Speaker1', name:'Speaker2']}
    private static List<String> all_speakers_who_talked;
    // HansardComplete['Topic Title']['Speakers'] --> {  [name:'Speaker1', name:'Speaker2']}
    private static Map<String, List<String>> speakers_per_topic;
    // HansardComplete['Topic Title']['Log'] --> SpeechBlock1 --> SpeechBlock2 --> Timestamp .. --> ..
    private static Map<String, List<String>> speakers_log_per_topic;

    public static String getTopicbyPageNumber(int current_page, Map<Integer, List<String>> myHalamanHash) {
        List<String> myTopicList = myHalamanHash.get(current_page);
        String sanitized_encoded_topic = null;
        for (String myTopic : myTopicList) {
            // For now we assume the last it list; pop??
            sanitized_encoded_topic = Utils.cleanTopicTitle(myTopic);
        }

        return sanitized_encoded_topic;

    }

    public static void getTopicStats(String Topic) {
        // Make sure topic is standardized ...
        // Count number of speakers
        // Utils.speakers_per_topic.size();
    }

    public static List<String> getMergedSpeakerList() {
        return null;

    }

    public static List<String> getMergedSpeakerListbyTopic(String topic) {
        // clean first to standard to make sure we can pull the right index ..
        return null;

    }

    public static void mergeSpeakerList(List<String> speaker_list) {

    }

    public static void mergeSpeakerListbyTopic(List<String> speaker_list, String topic) {
        // clean first to standard to make sure we can put in the right index ..

    }

    public static void mergeLogbyTopic(String speaker_name, String speech_block, String topic) {
        // clean first to standard to make sure we can put in the right index ..

    }

    public static boolean hasContentfromPreviousTopic(String current_topic_title) {
        // Ignore the DR pattern; and see after that ..
        // Flatten content; before trying a match ..
        // by default assumes NO content from Previous Topic ..
        return false;
    }

    public static void writeMergedSpeakers(Map<String, String> speakers_map,
            String result_file_path) {

        /*
        List<Map<String, String>> l;
        l = new ArrayList();
        l.add(speakers_map);
        */
        // Add to list to ease making JSONlines ..
        ObjectMapper object_mapper;
        object_mapper = JsonFactory.create();
        // DEBUG: Output as JSON
        // out.println("==========  SPEAKERS inJSON  ===========");
        // out.println(object_mapper.toJson(speakers_map));
        try {
            //             object_mapper.writeValue(new FileOutputStream(result_file_path), l);
            object_mapper.writeValue(new FileOutputStream(result_file_path), speakers_map);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // NOTE: There is probably a Generics refactoring possible here ..
    public static void writeMergedSpeechTranscripts(
            List<Map<String, String>> speech_transcript_logs,
            String result_file_path) {
        // Good reference to boon v0.3.x; somehow v0.4 looks totally different ..
        // http://tutorials.jenkov.com/java-json/boon-objectmapper.html#date-formats-in-JSON
        // JSON lines?
        // DEBUG: Raw structure out; before being JSON-ize
        // out.println("writeMergedSpeechTranscripts ======0000000======000000=====000000=======");
        // Boon.puts(speech_transcript_logs);

        ObjectMapper object_mapper;
        object_mapper = JsonFactory.create();
        // DEBUG: Output as JSON
        // out.println("Boon toJSON ************>>>>>>>>");
        // out.println(object_mapper.toJson(speech_transcript_logs));
        try {
            object_mapper.writeValue(new FileOutputStream(result_file_path), speech_transcript_logs);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String prepareContentForSpeakerIdentification(String raw_content) {
        return renameTimestampWithMetaSpeaker(
                cleanActionsInHall(
                        cleanContentHeader(raw_content)
                )
        );
    }

    // clean up of speakers??
    public static String cleanSpeakersName(String raw_speakers_name) {
        // remove chars not allowed; after clearing the edge case of IMOKMAN Canary Trigger
        //  which means the regexp for detecting Speaker from the Marked Modification
        //  does not work when the Speaker appears in the very first line
        //  UGLY, yes! :P
        // Case Example Snippet below:
        // CANARY: >IMOKMAN**Tuan Yang di-Pertua
        // ****** ALERT ******** IMOKMAN Canary Triggered!!! ****** ALERT ********
        // >>IMOKMAN**Tuan Yang di-Pertua:  Yang Berhormat Menteri, Yang Berhormat Menteri.
        //  >>IMOKMAN**Tuan Manivannan a/l Gowindasamy [Kapar]:  Who is provoking?  
        //  >>IMOKMAN**Dato' Haji Tajuddin bin Abdul Rahman:  Who are you?  
        //  >>IMOKMAN**Tuan Sim Chee Keong [Bukit Mertajam]:  Who is provo
        //  ...
        // Look out for ==> IMOKMANTUAN_YANG_DIPERTUA
        // DEBUG: Canary detection
        /*
         if (raw_speakers_name.contains("IMOKMAN")) {
         out.println("CANARY: " + raw_speakers_name + " and cleanedup is "
         + raw_speakers_name.replaceAll("\\>IMOKMAN\\*\\*", ""));
         }
         */
        Matcher matched_illegal_speaker = pattern_illegal_speaker.matcher(
                raw_speakers_name.replaceAll("\\>IMOKMAN\\*\\*", "")
        );
        // apply a trim
        // remove extra spaec ebecome one
        // Make things all UPPERCASE so it is standardized!
        Matcher matched_extra_space = pattern_more_than_one_space.matcher(
                matched_illegal_speaker.replaceAll("").trim().toUpperCase()
        );
        // For speakers replace with space ...
        return matched_extra_space.replaceAll("_");

    }

    private static String renameTimestampWithMetaSpeaker(String raw_content) {
        Matcher match_imokman_timestamp = pattern_imokman_timestamp.matcher(raw_content);
        if (match_imokman_timestamp.find()) {
            // return match_imokman_timestamp.replaceAll("PARLIAMENT @[Clock]@" + match_imokman_timestamp.group(1));
            return match_imokman_timestamp.replaceAll("");
        }
        return raw_content;
    }

    private static String cleanActionsInHall(String raw_content) {
        Matcher matched_actions_in_hall;
        matched_actions_in_hall = pattern_actions_in_hall.matcher(raw_content);
        if (matched_actions_in_hall.find()) {
            // DEBUG:
            // out.println("Replace action with @" + matched_actions_in_hall.group(1) + "@");
            return matched_actions_in_hall.replaceAll("@" + matched_actions_in_hall.group(1) + "@");
            // return matched_actions_in_hall.replaceAll("");
        }
        return raw_content;
    }

    // Pattern match only the first line??
    // Match the DR pattern .
    private static String cleanContentHeader(String raw_content) {

        Matcher matched_document_page_header;
        matched_document_page_header = pattern_document_page_header.matcher(raw_content);
        // Remove full the pattern: /^DR\.\d+\.\d+\.\d+.*\d+/g 
        // Example: DR.18.6.2015 7 
        if (matched_document_page_header.find()) {
            return matched_document_page_header.replaceAll("");
        }
        return raw_content;
    }

    // pattern matching of speaker
    // apttern matching of alt speaker 
    // Match timestamp ..
    // clean up of title
    private static String cleanTopicTitle(String raw_topic_title) {
        // remove chars not allowed; leave as a space
        Matcher matched_illegal_topic = pattern_illegal_topic.matcher(raw_topic_title);
        // apply a trim
        Matcher matched_extra_space = pattern_more_than_one_space.matcher(
                matched_illegal_topic.replaceAll(" ").trim()
        );
        // For topic leave multiple spaces as a single space to be consistent with 
        //  Parliament Q&A Blaster .. POW!
        return matched_extra_space.replaceAll(" ");

    }

    // IDEA: Post-processing
    // [KEY] - @Replace_With, @Unmodified_Name, @Post, @Area
    //      look for keywords like Menteri; if yes; extract from to match [], basic scan
    //      Write in standard <@Unmodified_Name> (@Post) <@Area>
    //      @Unmodified_Name should come from the Map<KEY,Unmodifeid_Name> insead of Boolean
    // Keeping track of overall speakers stats
    // Scenarios:
    // a) Who spoke in this session? Semi-correct attendance for those who appeared ..
    // b) How many speech blocks this session?  Does not seem too useful; and not 
    //      very accurate
    // HansardComplete['Speakers'] --> {  [name:'Speaker1', name:'Speaker2']}
    // Keeping track of speech per issue
    // Scenarios:
    // a) Who spoke for this topic?  Possible has interest or is in a commitee for it ..
    // b) How many speechblocks per speaker in this topic??
    // c) Sentiment analysis
    // d) How many BN/Opposition talk; ration?  Might not be too useful in terms 
    //      of raw speech block tho ..
    // HansardComplete['Topic Title']['Speakers'] --> {  [name:'Speaker1', name:'Speaker2']}
    // HansardComplete['Topic Title']['Log'] --> SpeechBlock1 --> SpeechBlock2 --> Timestamp .. --> ..
    // The maybes .. attach to the MyStars as game interface or PyBossa: Partial; yes? no? yes --> clean and identify
    // HansardComplete['Topic Title']['Speakers']['Maybe'] --> Attach Speakers in possible; to be cleaned manually
    // HansardComplete['Topic Title']['Log']['Maybe'] --> Attach Log; can clean up similarly
    // HansardComplete['Topic Title']['PDF']['Maybe'] --> File name for the Maybes ..    
    // HansardComplete['Topic Title']['Speaker1'] --> "{ [talked:12, asked:1, objected:5 ] }"
    // HansardComplete['Topic Title']['Speaker2'] --> "{ [talked:12, asked:1, objected:5 ] }"
    // HansardComplete['Topic Title']['Incumbent'] --> "{ [talked:19, asked:10, objected:9 ] }"
    // HansardComplete['Topic Title']['Opposition'] --> "{ [talked:120, asked:111, objected:50 ] }"
}
