import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IndexTest {
     @BeforeAll
    static void setUpBeforeClass() throws Exception {
        File testFile = new File("file1");
        testFile.createNewFile();
        PrintWriter pw = new PrintWriter(new FileWriter("file1"));
        
        pw.print("derpderpderpderp");
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
    void testIndex() throws Exception {
        Index test = new Index();
        test.initialize();

        // check if the file exists
        File file = new File("index");
        Path path = Paths.get("objects");

        assertTrue(file.exists());
        assertTrue(Files.exists(path));
    }


    @Test
    void testAddBlob() {

    }

    @Test
    void testDeleteBlob() {

    }

    @Test
    void testInitialize() {

    }

    @Test
    void testWriteToIndex() {

    }
}
