/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sinarproject.hansardparser;

import java.util.List;
import java.util.Map;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author leow
 */
public class UtilsNGTest {
    
    public UtilsNGTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * Test of getTopicbyPageNumber method, of class Utils.
     */
    @Test
    public void testGetTopicbyPageNumber() {
        System.out.println("getTopicbyPageNumber");
        int current_page = 0;
        Map<Integer, List<String>> myHalamanHash = null;
        String expResult = "";
        String result = Utils.getTopicbyPageNumber(current_page, myHalamanHash);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTopicStats method, of class Utils.
     */
    @Test
    public void testGetTopicStats() {
        System.out.println("getTopicStats");
        String Topic = "";
        Utils.getTopicStats(Topic);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMergedSpeakerList method, of class Utils.
     */
    @Test
    public void testGetMergedSpeakerList() {
        System.out.println("getMergedSpeakerList");
        List expResult = null;
        List result = Utils.getMergedSpeakerList();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMergedSpeakerListbyTopic method, of class Utils.
     */
    @Test
    public void testGetMergedSpeakerListbyTopic() {
        System.out.println("getMergedSpeakerListbyTopic");
        String topic = "";
        List expResult = null;
        List result = Utils.getMergedSpeakerListbyTopic(topic);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of mergeSpeakerList method, of class Utils.
     */
    @Test
    public void testMergeSpeakerList() {
        System.out.println("mergeSpeakerList");
        List<String> speaker_list = null;
        Utils.mergeSpeakerList(speaker_list);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of mergeSpeakerListbyTopic method, of class Utils.
     */
    @Test
    public void testMergeSpeakerListbyTopic() {
        System.out.println("mergeSpeakerListbyTopic");
        List<String> speaker_list = null;
        String topic = "";
        Utils.mergeSpeakerListbyTopic(speaker_list, topic);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of mergeLogbyTopic method, of class Utils.
     */
    @Test
    public void testMergeLogbyTopic() {
        System.out.println("mergeLogbyTopic");
        String speaker_name = "";
        String speech_block = "";
        String topic = "";
        Utils.mergeLogbyTopic(speaker_name, speech_block, topic);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hasContentfromPreviousTopic method, of class Utils.
     */
    @Test
    public void testHasContentfromPreviousTopic() {
        System.out.println("hasContentfromPreviousTopic");
        String current_topic_title = "";
        boolean expResult = false;
        boolean result = Utils.hasContentfromPreviousTopic(current_topic_title);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createParentFoldersIfMissing method, of class Utils.
     */
    @Test
    public void testCreateParentFoldersIfMissing() {
        System.out.println("createParentFoldersIfMissing");
        String full_folder_path = "";
        boolean expResult = false;
        boolean result = Utils.createParentFoldersIfMissing(full_folder_path);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of writeMergedSpeakers method, of class Utils.
     */
    @Test
    public void testWriteMergedSpeakers() {
        System.out.println("writeMergedSpeakers");
        Map<String, String> speakers_map = null;
        String result_file_path = "";
        Utils.writeMergedSpeakers(speakers_map, result_file_path);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of writeMergedSpeechTranscripts method, of class Utils.
     */
    @Test
    public void testWriteMergedSpeechTranscripts() {
        System.out.println("writeMergedSpeechTranscripts");
        List<Map<String, String>> speech_transcript_logs = null;
        String result_file_path = "";
        Utils.writeMergedSpeechTranscripts(speech_transcript_logs, result_file_path);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of prepareContentForSpeakerIdentification method, of class Utils.
     */
    @Test
    public void testPrepareContentForSpeakerIdentification() {
        System.out.println("prepareContentForSpeakerIdentification");
        String raw_content = "";
        String expResult = "";
        String result = Utils.prepareContentForSpeakerIdentification(raw_content);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of cleanSpeakersName method, of class Utils.
     */
    @Test
    public void testCleanSpeakersName() {
        System.out.println("cleanSpeakersName");
        String raw_speakers_name = "";
        String expResult = "";
        String result = Utils.cleanSpeakersName(raw_speakers_name);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
