import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TreeTest {
     @BeforeAll
    static void setUpBeforeClass() throws Exception {
        File testFile = new File("file1");
        testFile.createNewFile();
        PrintWriter pw = new PrintWriter(new FileWriter("file1"));
        
        pw.print("derpderpderp");
        pw.close();

        //initializes an index file + adds blobs to it?
        Index ind = new Index();
        ind.initialize();
        ind.addBlob("file1");
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
    @DisplayName("[8] Test if initialize and objects are created correctly")
    void testInitialize() throws Exception {
        Tree test = new Tree();
        test.initialize();

        // check if the file exists
        File file = new File("tree");
        Path path = Paths.get("objects");

        assertTrue(file.exists());
        assertTrue(Files.exists(path));
    }

    @Test
    void testAddTree() throws IOException {
        Tree test = new Tree();
        String input = "blob : 732d12f7e4f2e629e2954acbb720c32c0be985d1 : file1";
        test.addTree(input);

        //check if the above string is in test file
        File file = new File("tree");
        Path path = Paths.get("objects");

        //TEST CONTENTS: test if the "blob : sha1 : fileName" is in there
        //ACTUALLY just test if all contents are the same? how?? IDK!!!!!!

        String contents = "";
        BufferedReader br = new BufferedReader(new FileReader(file));
        while(br.ready()) {
            contents += "" + br.readLine();
        }
        br.close();
        
        //File contents of Tree contain input
        assertTrue(contents.contains(input));
    }

    @Test
    void testAddTreeIfAlreadyTree() throws IOException {
        
    }

    @Test
    void testDeleteTree() {
        
    }

    @Test
    void testDeleteTreeIfNoTree() {
        
    }
}
