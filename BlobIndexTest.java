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
        File objects = new File("./objects");
        if (!objects.exists())
            objects.mkdirs();
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
        setUpBeforeClass();

        Blob testBlob = new Blob("file1");//????
        File file = new File("index");
        Path path = Paths.get("objects");

        assertTrue(file.exists());
        assertTrue(Files.exists(path));
    }

}
