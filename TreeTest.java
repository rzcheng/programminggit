import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
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
        test.addTree("blob : 732d12f7e4f2e629e2954acbb720c32c0be985d1 : file1");

        // check if the above string is in test
        File file = new File("tree");
        Path path = Paths.get("objects");

        assertTrue(file.exists());
        assertTrue(Files.exists(path));
    }

    @Test
    void testDeleteTree() {
        
    }
}
