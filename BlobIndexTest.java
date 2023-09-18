
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BlobIndexTest {
     @BeforeAll
    static void setUpBeforeClass() throws Exception {
        //creating test file
        File testFile = new File("file1");
        testFile.createNewFile();
        PrintWriter pw = new PrintWriter(new FileWriter("file1"));
        
        pw.print("derpderpderp");
        pw.close();

        //creating the objects directory
        /*File objects = new File("./objects");
        if (!objects.exists())
            objects.mkdirs();*/
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        /*
         * Utils.deleteFile("junit_example_file_data.txt");
         * Utils.deleteFile("index");
         * Utils.deleteDirectory("objects");
         */
    }

    @Test
    void testCreateBlob() throws Exception {
        //setUpBeforeClass();
        File objects = new File("./objects");
        if (!objects.exists())
            objects.mkdirs();

        Blob testBlob = new Blob("file1");//????
        File file = new File("732d12f7e4f2e629e2954acbb720c32c0be985d1");
        Path path = Paths.get("objects");

        assertTrue(file.exists());
        assertTrue(Files.exists(path));

        //STILL NEED TO TEST CONTENTS MATCH
        String contents = "";
        BufferedReader br = new BufferedReader(new FileReader("732d12f7e4f2e629e2954acbb720c32c0be985d1"));
        while(br.ready()) {
            contents += "" + br.readLine();
        }
        br.close();
        
        //File contents of Tree contain input
        assertTrue(contents.contains("derpderpderp"));//the sha1 new file created by blob does not contain original contents
    }

    @Test
    void testGetEncryption() throws Exception {
       File objects = new File("./objects");
        if (!objects.exists())
            objects.mkdirs();

        Blob testBlob = new Blob("file1");//????
        String encryption = testBlob.getEncryption();

        assertTrue(encryption.equals("732d12f7e4f2e629e2954acbb720c32c0be985d1"));
    }

    @Test
    @DisplayName("[8] Test if initialize and objects are created correctly")
    void testInit() throws Exception {
        Index test = new Index();
        test.initialize();

        // check if the file exists
        File file = new File("index");
        Path path = Paths.get("objects");

        assertTrue(file.exists());
        assertTrue(Files.exists(path));
    }


    @Test
    void testAddBlob() throws Exception {
        Index test = new Index();
        test.initialize();

        test.addBlob("file1");
        boolean entryExists = false;
        BufferedReader br = new BufferedReader(new FileReader("index"));
        String str = "";
        while(br.ready()) {
            str = "" + br.readLine();
            if(str.equals("file1 : 732d12f7e4f2e629e2954acbb720c32c0be985d1")) {
                entryExists = true;
            }
        }
        br.close();

        //test if index file is updated
        assertTrue(entryExists);

        //test if blob created
        File file = new File("732d12f7e4f2e629e2954acbb720c32c0be985d1");

        assertTrue(file.exists());

    }

    @Test
    void testDeleteBlob() throws Exception {
        Index test = new Index();
        test.initialize();

        test.addBlob("file1");
        test.deleteBlob("file1");
        boolean entryExists = false;
        BufferedReader br = new BufferedReader(new FileReader("index"));
        String str = "";
        while(br.ready()) {
            str = "" + br.readLine();
            if(str.equals("file1 : 732d12f7e4f2e629e2954acbb720c32c0be985d1")) {
                entryExists = true;
            }
        }
        br.close();

        //test if index file is updated
        assertFalse(entryExists);


    }



}
