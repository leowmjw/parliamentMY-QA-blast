/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sinarproject.hansardparser;

import static java.lang.System.out;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import org.boon.Boon;
import static org.boon.Boon.toJson;
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
public class HansardSpeakersNGTest {

    // Above here are example of raw; unprepped; for use in observeSpeakers etc..
    private static final String multiple_speakers = "  \n"
            + " \n"
            + "memastikan mereka mencapai tahap CPT C1 ataupun  Cambridge Placement Test C1 dan C2 \n"
            + "agar mereka boleh mengajar bahasa Inggeris dengan baik.   \n"
            + "Tidak kurang juga kita mempunyai 360 native speakers ataupun saya panggil orang putih, \n"
            + "orang yang bahasa penuntut jati bahasa Inggeris yang mengajar, mendidik, menjadi mentor 5,000 \n"
            + "guru-guru kita.  100 guru ETA, English Teaching Assistant yang sedang berada di sekolah-sekolah \n"
            + "kita di samping program-program Teach for Malaysia dan sebagainya yang sedang dilaksanakan \n"
            + "untuk memastikan mula daripada sekolah lagi ianya bermula.  Untuk kemasukan universiti, kita \n"
            + "ada MUET untuk memastikan mereka mencapai tahap bahasa Inggeris yang diperlukan.   \n"
            + "Program 2+2 ini adalah satu program yang pernah dilaksanakan di sebuah universiti di \n"
            + "Canada sebenarnya.  Bukan perkara baru.  Dalam kita mengkaji kebolehpasaran graduan-graduan \n"
            + "kita, mereka tidak mampu untuk masuk pasaran dengan baik kerana mereka tidak mempunyai \n"
            + "kemahiran komunikasi katakanlah antaranya juga bahasa Inggeris, tidak mempunyai kemahiran \n"
            + "sosial yang mencukupi.  Dengan adanya Program 2+2 ini, mereka didedahkan dengan apa yang \n"
            + "dikehendaki oleh pasaran.   \n"
            + "Bukan hanya Program 2+2 kita mempunyai Program Integrated CGPA di mana mereka \n"
            + "bukan hanya diukur mengikut kemahiran akademik mereka semata-mata, tetapi juga mengikut \n"
            + "kemahiran komunikasi mereka, kemahiran sosial mereka dan juga nilai-nilai kepimpinan yang ada \n"
            + "pada mereka.  Saya jangka dengan adanya program-program yang akan dilaksanakan oleh \n"
            + "kementerian ini, tahap kebolehpasaran akan meningkat. \n"
            + "Tuan Yang di-Pertua:  Yang Berhormat Kepong sudah berdiri saya panggil, lama saya \n"
            + "tidak dengar pendapat Yang Berhormat Kepong.  Sila. \n"
            + "Dr. Tan Seng Giaw [Kepong]:  Tuan Yang di-Pertua, soalan tambahan.  Apakah \n"
            + "kekangan-kekangan yang wujud dengan pelan yang akan dilaksanakan ini dan setakat mana pihak \n"
            + "pentadbir di semua universiti dapat menyambut dan tidak akan mensabotaj. \n"
            + "Dato’ Seri Haji Idris Jusoh:  Terima kasih Yang Berhormat Kepong.  Memang dalam apa \n"
            + "juga pelaksanaan kita daripada mula sebab itu saya katakan tadi kita telah berjumpa dengan \n"
            + "pemegang taaruf, kita berjumpa dengan rakyat keseluruhannya dan memang kita ada engagement \n"
            + "dengan izin, dengan pensyarah-pensyarah, pentadbir-pentadbir universiti untuk melaksanakan \n"
            + "pelan ini.  Sehingga kini saya tidak nampak banyak masalah di dalam pelaksanaan dan sokongan \n"
            + "yang kita dapati daripada pentadbir-pentadbir universiti untuk melaksanakan pelan ini dengan \n"
            + "sebaik-baiknya. \n"
            + " \n"
            + "2.  Tuan Sim Tze Tzin [Bayan Baru] minta Perdana Menteri menyatakan apakah rasional \n"
            + "harga siling rumah PR1MA dinaikkan kepada RM450,000. Berapa peratusan rumah PR1MA dalam \n"
            + "kategori tersebut. \n"
            + " \n"
            + "Menteri di Jabatan Perdana Menteri [Dato’ Seri Shahidan bin Kassim]:  Tuan Yang di-\n"
            + "Pertua, untuk makluman Ahli Yang Berhormat, PR1MA tidak pernah memutuskan untuk \n"
            + "menaikkan lingkungan harga bagi rumah-rumahnya.  Rumah mampu milik yang disediakan \n"
            + "PR1MA, masih kekal dalam lingkungan harga yang telah ditetapkan iaitu di antara RM100,000 ";
// In preparePage; when no Speakers; no transformation done ..
    private static final String no_speaker = "  \n"
            + " \n"
            + "dengan izin daripada Pelan Pembangunan Pendidikan (PPPM) sektor sekolah yang telah \n"
            + "dilancarkan pada tahun 2013.  PPPMPT ini telah dibangunkan secara inklusif dan komprehensif \n"
            + "dengan mengambil kira pandangan pelbagai pihak yang berkepentingan daripada 10,500 orang \n"
            + "termasuk 100 pemegang ‘Taro’, 30 pakar daripada tempatan dan juga antarabangsa. \n"
            + " Penggubalan pelan pendidikan ini juga berpandukan laporan kajian yang telah \n"
            + "dilaksanakan oleh agensi antarabangsa seperti Bank Dunia, UNESCO dan OECD.  Sebagai \n"
            + "langkah-langkah dalam perancangan untuk melaksanakan dan menjayakan PPPMPT ialah seperti \n"
            + "berikut.  Antaranya ialah untuk mempraktikkan lonjakan pertama pelan tersebut untuk melahirkan \n"
            + "graduan yang holistik, berciri keusahawanan dan seimbang.  Pelan telah memperkenalkan \n"
            + "Program Akademik Sarjana Muda berbentuk 2+2 iaitu yang melibatkan pembelajaran selama 2 \n"
            + "tahun di kampus dan juga 2 tahun luar kampus ataupun pembelajaran industri.  Ini akan \n"
            + "melengkapkan lagi pengalaman dan keyakinan pelajar semasa mereka bekerja.     \n"
            + " Keduanya, melaksanakan sistem gred purata nilai gred kumulatif yang bersepadu ataupun \n"
            + "integrated CGPA yang mana bukan hanya pencapaian akademik semata yang diukur tetapi juga \n"
            + "mengukur sumbangan sosial, kemahiran berkomunikasi, kemahiran sosial pelajar-pelajar tersebut.   \n"
            + " Ketiganya mewujudkan 4 laluan kerjaya kepada ahli akademik, yang mana ahli akademik \n"
            + "boleh memberi tumpuan sama ada mereka ini mengajar, keduanya ingin menyelidik, ketiga \n"
            + "menjadi pemimpin di peringkat universiti ataupun kita akan jemput pensyarah-pensyarah \n"
            + "profesional dari luar.   \n"
            + "Untuk melaksanakan laluan keempat pensyarah profesional dari luar, kita \n"
            + "memperkenalkan program CEO @ Faculty, dengan izin ataupun CEO The Faculty bagi \n"
            + "perkongsian tokoh-tokoh korporat terkenal contohnya Tan Sri Azman Mokhtar, Khazanah, Tan Sri \n"
            + "Tony Fernandez AirAsia, Ketua Eksekutif Shell, Ketua Eksekutif Samsung, Hyundai dan semalam \n"
            + "saya ada terima surat  daripada Dr. Richard Parker iaitu Director of Research & Technology of \n"
            + "Rolls-Royce yang telah bersetuju menyertai program ini. \n"
            + "Selain itu juga terdapat beberapa langkah lain yang turut dilaksanakan seperti memberi \n"
            + "autonomi kepada majoriti universiti awam dalam beberapa bidang termasuk tadbir urus, proses \n"
            + "pemilihan, sumber manusia dan penawaran kursus, melaksanakan massive online courses, \n"
            + "melaksanakan pembelajaran sepanjang hayat dan juga melahirkan graduan TVET yang berkualiti. \n"
            + " \n"
            + "Untuk soalan Yang Berhormat Tenom, baru-baru ini Dr. Irina Bokova Ketua Pengarah \n"
            + "Pertubuhan Pendidikan Saintifik dan Kebudayaan Bangsa-Bangsa Bersatu ataupun UNESCO, \n"
            + "mengatakan bahawa pelan pendidikan ini mampu menjadi penanda aras kepada transformasi \n"
            + "pendidikan rantau Asia dan boleh menjadi contoh kepada negara-negara lain.  Pengiktirafan yang \n"
            + "diberikan oleh UNESCO ini membuktikan hala tuju pendidikan negara berada di landasan yang \n"
            + "betul daripada peringkat prasekolah hingga ke institusi pendidikan tinggi.   \n"
            + "Saya yakin dengan usaha-usaha yang telah dan sedang dilaksanakan untuk menjayakan \n"
            + "transformasi pendidikan melalui pelan-pelan pembangunan pendidikan, prestasi pendidikan negara \n"
            + "akan terus meningkat ke tahap yang lebih baik ataupun dengan izin growing upwards dan ";
    // Prepped; those labeled with >>IMOKMAN** for use by prepareSpeechBlock
    // Below is specifically for issue #3; where there is speech block but before first formal Speaker
    private static final String missing_speaker_at_start_prepped = "  \n"
            + " \n"
            + "sampai hari ini tidak dapat.  Ini fasal ada Yang Berhormat panggil mereka dahulu rakan mereka \n"
            + "jugalah, topekongnya @[Ketawa]@ ada di Pulau Pinang itu hendak kena menyembah dia pula. \n"
            + ">>IMOKMAN**Tuan Sim Chee Keong [Bukit Mertajam]:  Siapa, siapa itu?  Kenapa cakap macam ini?  \n"
            + "Siapa itu? \n"
            + ">>IMOKMAN**Dato' Haji Tajuddin bin Abdul Rahman:  Siapa cakap hari itu, Yang Berhormat mana itu \n"
            + "di sebelah sana juga, he’s your friend. \n"
            + ">>IMOKMAN**Tuan Manivannan a/l Gowindasamy [Kapar]:  Mana boleh cakap your friend, your friend \n"
            + "ini Menteri? \n"
            + ">>IMOKMAN**Tuan Sim Chee Keong [Bukit Mertajam]:  Apa yang dia cakap itu, siapa yang cakap itu? \n"
            + ">>IMOKMAN**Dato' Haji Tajuddin bin Abdul Rahman:  Apa fasal tidak boleh? \n"
            + ">>IMOKMAN**Tuan Manivannan a/l Gowindasamy [Kapar]:  I mean spesifik lah siapa. \n"
            + ">>IMOKMAN**Tuan Sim Chee Keong [Bukit Mertajam]: Menteri jawab macam tidak ada standard. \n"
            + ">>IMOKMAN**Dato' Haji Tajuddin bin Abdul Rahman:  Yang Berhormat Mansor, Yang Berhormat \n"
            + "Mansor daripada mana?  Did you remember? \n"
            + ">>IMOKMAN**Tuan Manivannan a/l Gowindasamy [Kapar]: You are saying something, you should \n"
            + "remember.  You are saying something you should remember. \n"
            + ">>IMOKMAN**Dato' Haji Tajuddin bin Abdul Rahman:  Yes, I’m trying to remember dia punya \n"
            + "kawasan.  Yang Berhormat Mansor ya, Timbalan Menteri Besar. \n"
            + ">>IMOKMAN**Tuan Sim Chee Keong [Bukit Mertajam]:  Langsung tidak ada class cakap macam itu. \n"
            + ">>IMOKMAN**Tuan Yang di-Pertua:  Terima kasih, terima kasih. \n"
            + ">>IMOKMAN**Tuan Manivannan a/l Gowindasamy [Kapar]: You are just wasting time. \n"
            + ">>IMOKMAN**Dato' Haji Tajuddin bin Abdul Rahman:  Why? Wasting time you don’t want to listen, you \n"
            + "get out.  @[Ketawa]@ \n"
            + ">>IMOKMAN**Tuan Manivannan a/l Gowindasamy [Kapar]:  Why do you talk like that? \n"
            + ">>IMOKMAN**Tuan Yang di-Pertua:  Terima kasih, terima kasih. \n"
            + ">>IMOKMAN**Dato' Haji Tajuddin bin Abdul Rahman:  @[Ketawa]@ @[Ketawa]@ \n"
            + ">>IMOKMAN**Tuan Manivannan a/l Gowindasamy [Kapar]: You have to answer properly. Get out, get \n"
            + "out.  Sebenarnya you lah yang get out. \n"
            + ">>IMOKMAN**Dato' Haji Tajuddin bin Abdul Rahman:  You pergi minum airlah dekat luar. \n"
            + ">>IMOKMAN**Tuan Sim Chee Keong [Bukit Mertajam]:  Menteri jawab dengan standard sikitlah. \n"
            + ">>IMOKMAN**Tuan Yang di-Pertua:  Ahli-ahli Yang Berhormat... \n"
            + ">>IMOKMAN**Dato' Haji Tajuddin bin Abdul Rahman: You, todi pun you boleh minum. \n"
            + ">>IMOKMAN**Tuan Yang di-Pertua:  Ahli-ahli Yang Berhormat... \n"
            + ">>IMOKMAN**Tuan Manivannan a/l Gowindasamy [Kapar]:  Apa todi? \n"
            + ">>IMOKMAN**Dato' Haji Tajuddin bin Abdul Rahman: You’re asking for trouble...Why you don’t want to \n"
            + "quietly.. \n"
            + ">>IMOKMAN**Tuan Manivannan a/l Gowindasamy [Kapar]: You answer properly, you learn to answer \n"
            + "properly. \n"
            + ">>IMOKMAN**Dato' Haji Tajuddin bin Abdul Rahman:  Hei you try to provoke me. ";
    // Test for multiple speakers and also with the Speaker right in front 
    private static final String multiple_speakers_prepped = "  \n"
            + " \n"
            + ">>IMOKMAN**Tuan Yang di-Pertua:  Yang Berhormat Menteri, Yang Berhormat Menteri. \n"
            + ">>IMOKMAN**Tuan Manivannan a/l Gowindasamy [Kapar]:  Who is provoking? \n"
            + ">>IMOKMAN**Dato' Haji Tajuddin bin Abdul Rahman:  Who are you? \n"
            + ">>IMOKMAN**Tuan Sim Chee Keong [Bukit Mertajam]:  Who is provoking? \n"
            + ">>IMOKMAN**Tuan Manivannan a/l Gowindasamy [Kapar]:   Who the hell are you? \n"
            + ">>IMOKMAN**Tuan Yang di-Pertua:  Yang Berhormat Menteri, Yang Berhormat Menteri.. \n"
            + ">>IMOKMAN**Dato' Haji Tajuddin bin Abdul Rahman:  @[Ketawa]@ No, I was answering very slowly, try to \n"
            + "recall.  Tuan Yang di-Pertua, dengan izin I... \n"
            + ">>IMOKMAN**Tuan Yang di-Pertua: Ahli-ahli Yang Berhormat, jam 11.32 ini bermakna sesi pertanyaan-\n"
            + "pertanyaan bagi jawapan lisan berakhir. Sila Setiausaha. \n"
            + ">>IMOKMAN**Dato' Haji Tajuddin bin Abdul Rahman:  Okey, terima kasih. \n"
            + " \n"
            + "[Masa untuk Pertanyaan-pertanyaan bagi Jawab Lisan tamat] \n"
            + " \n"
            + "  \n"
            + "USUL \n"
            + " \n"
            + "WAKTU MESYUARAT DAN URUSAN \n"
            + "DIBEBASKAN DARIPADA PERATURAN MESYUARAT \n"
            + " \n"
            + "11.32 pg. \n"
            + ">>IMOKMAN**Menteri di Jabatan Perdana Menteri [Dato’ Seri Shahidan bin Kassim]: Tuan yang di-\n"
            + ">>IMOKMAN**pertua, saya mohon mencadangkan:  \n"
            + "“Bahawa mengikut Peraturan Mesyuarat 12(1), Mesyuarat pada hari ini \n"
            + "tidak akan ditangguhkan sehingga jam 6.30 petang dan sehingga selesai ucapan \n"
            + "ucapan-ucapan penangguhan dan selepas itu Majlis Mesyuarat akan ditangguhkan \n"
            + "sehingga suatu tarikh yang tidak ditetapkan.” \n"
            + " \n"
            + "Timbalan Menteri Perdagangan Dalam Negeri, Koperasi dan Kepenggunaan [Dato’ \n"
            + ">>IMOKMAN**Seri Ahmad Bashah bin Md. Hanipah]: Tuan Yang di-Pertua, saya mohon menyokong. \n"
            + ">>IMOKMAN**Tuan Yang di-Pertua: Terima kasih, Yang Berhormat.  Ahli-ahli Yang Berhormat, \n"
            + "sekarang saya kemukakan masalah kepada Majlis untuk diputuskan. Masalahnya ialah usul \n"
            + "seperti yang dikemukakan tadi hendaklah disetujukan. \n"
            + "[Usul dikemukakan bagi diputuskan; dan disetujukan] \n"
            + " \n"
            + "11.33 pg. \n"
            + ">>IMOKMAN** Tuan Charles Anthony [Klang]:  Tuan Yang di-Pertua, saya minta Tuan Yang di-Pertua \n"
            + "untuk ulang kaji usul yang telah saya kemukakan sebab wabak denggi sehingga hari ini telah \n"
            + "membunuh lebih kurang 144 orang dalam bulan ini. Seramai 144 people have died and 15,000 \n"
            + "infection.  Ini merupakan satu isu nasional, kecemasan nasional.  So, jawapan yang diberikan oleh ";

    public HansardSpeakersNGTest() {
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
     * Test of identifySpeakersinTopic method, of class HansardSpeakers.
     */
    // Ignore for now; in a future issue
    @Test(enabled = false)
    public void testIdentifySpeakersinTopic() throws Exception {
        System.out.println("identifySpeakersinTopic");
        Map<Integer, Integer> myHalamanStartEnd = null;
        Map<Integer, List<String>> myHalamanHash = null;
        HansardSpeakers.identifySpeakersinTopic(myHalamanStartEnd, myHalamanHash);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    // Test of observeSpeakers method, of class HansardSpeakers
    @Test
    public void testObserveSpeakers() throws Exception {
        Method reflected_observeSpeakers = null;
        out.println("observeSpeakers");
        Method[] m = HansardSpeakers.class.getDeclaredMethods();
        for (Method one_method : m) {
            if ("observeSpeakers".equals(one_method.getName())) {
                out.println("Testing Method " + one_method.getName());
                // See: http://stackoverflow.com/questions/34571/how-to-test-a-class-that-has-private-methods-fields-or-inner-classes
                one_method.setAccessible(true);
                reflected_observeSpeakers = one_method;
            }
        }
        // Setup the last speaker via Reflection
        Field f = HansardSpeakers.class.getDeclaredField("last_identified_speaker");
        f.setAccessible(true);
        f.set(null, "IMOKMAN");
        if (reflected_observeSpeakers != null) {
            // SCENARIO #1: No Speakers!!
            Map<String, String> speakers_map = (Map<String, String>) reflected_observeSpeakers.invoke(null, no_speaker);
            // Now the actual test and compare
            assertEquals(speakers_map.size(), 0);
            // Debug below
            out.println("TEST RESULT for SCENARIO #1 ===>");
            // out.println(toJson(speakers_map));
            Boon.puts(speakers_map);
            out.println("======*****==========******");
            // SCENARIO #2: Multiple speakers
            Map<String, String> multi_speakers_map = (Map<String, String>) reflected_observeSpeakers.invoke(null, multiple_speakers);
            assertEquals(multi_speakers_map.size(), 4);
            // Debug below ..
            out.println("TEST RESULT for SCENARIO #2 ===>");
            // out.println(toJson(multi_speakers_map));
            Boon.puts(multi_speakers_map);
            out.println("======*****==========******");
        }
    }

    // Test of preparePage method, of class HansardSpeakers
    @Test(enabled = false)
    public void testPreparePage() throws Exception {
        Method reflected_preparePage = null;
        out.println("preparePage");
        Method[] m = HansardSpeakers.class.getDeclaredMethods();
        for (Method one_method : m) {
            if ("preparePage".equals(one_method.getName())) {
                out.println("Testing Method " + one_method.getName());
                // See: http://stackoverflow.com/questions/34571/how-to-test-a-class-that-has-private-methods-fields-or-inner-classes
                one_method.setAccessible(true);
                reflected_preparePage = one_method;
            }
        }
        // Setup the last speaker via Reflection
        Field f = HansardSpeakers.class.getDeclaredField("last_identified_speaker");
        f.setAccessible(true);
        f.set(null, "IMOKMAN");
        if (reflected_preparePage != null) {
            List<Map<String, String>> speakers_transcript = (List<Map<String, String>>) reflected_preparePage.invoke(null, no_speaker);
            out.println("TEST RESULT for preparePage ===>");
            out.println(toJson(speakers_transcript));
        }
        // Finish boilerpalte above; good candidate for refactoring
        fail("The test case is a prototype.");
    }

    // Test of prepareSpeechBlock method, of class HansardSpeakers
    // For fix of issue #3
    @Test(enabled = false)
    public void testPrepareSpeechBlock() throws Exception {
        Method reflected_prepareSpeechBlock = null;
        out.println("prepareSpeechBlock");
        Method[] m = HansardSpeakers.class.getDeclaredMethods();
        for (Method one_method : m) {
            if ("prepareSpeechBlock".equals(one_method.getName())) {
                out.println("Testing Method " + one_method.getName());
                // See: http://stackoverflow.com/questions/34571/how-to-test-a-class-that-has-private-methods-fields-or-inner-classes
                one_method.setAccessible(true);
                reflected_prepareSpeechBlock = one_method;
            }
        }
        // Setup the last speaker via Reflection
        Field f = HansardSpeakers.class.getDeclaredField("last_identified_speaker");
        f.setAccessible(true);
        f.set(null, "IMOKMAN");
        if (reflected_prepareSpeechBlock != null) {
            // SCENARIO #x: Fix issue #3; speech block before first recognized speaker
            reflected_prepareSpeechBlock.invoke(null, "bob");
            // SCENARIO #1: Page of speech without any speakers
            List<Map<String, String>> result_map = (List<Map<String, String>>) reflected_prepareSpeechBlock.invoke(null, no_speaker);
            out.println("=== POST PROCESS ==>");
            // puts(result_map);
            // out.println(Boon.toJson(result_map));
            for (Map<String, String> single_map : result_map) {
                out.println(toJson(single_map));
            }
        }
        fail("The test case is a prototype!!");
    }
}
