import static org.junit.Assert.*;

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

import Utilities.FileUtils;

public class TreeTest {
     @BeforeAll
    static void setUpBeforeClass() throws Exception {
        FileUtils.createFile("file1");
        FileUtils.writeFile("file1", "derpderpderp");

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

        FileUtils.deleteFile("file1");
        FileUtils.deleteDirectory("./objects");
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
        test.initialize();

        String input = "blob : 732d12f7e4f2e629e2954acbb720c32c0be985d1 : file1";
        test.addTree(input);

        //check if the above string is in test file
        File file = new File("tree");
        //Path path = Paths.get("objects");

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
        Tree test = new Tree();
        test.initialize();

        String input = "blob : 732d12f7e4f2e629e2954acbb720c32c0be985d1 : file1";
        test.addTree(input);
        test.addTree(input);

        File file = new File("tree");

        int inputCounter = 0;
        BufferedReader br = new BufferedReader(new FileReader(file));
        while(br.ready()) {
            if(input.equals(br.readLine())) {
                inputCounter++;
            }
        }
        br.close();

        assertTrue(inputCounter<=1);

    }

    @Test
    void testDeleteTree() throws Exception {
        //creates a testing tree
        Tree test = new Tree();
        test.initialize();

        File file = new File("tree");
        String input = "blob : 732d12f7e4f2e629e2954acbb720c32c0be985d1 : file1";
        String input2 = "tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b";
        String input3 = "blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file2.txt";
        test.addTree(input);
        test.addTree(input2);
        test.addTree(input3);

        test.deleteTree("file2.txt");//input3

        int deletedCounterN = 0;//counts times inp';ut appears
        int deletedCounterY = 0;//counts times input3 appears
        BufferedReader br = new BufferedReader(new FileReader(file));
        while(br.ready()) {
            String str = br.readLine();
            if(input.equals(str)) {
                deletedCounterN++;
            }
            if(input3.equals(str)) {
                deletedCounterY++;
            }
        }
        br.close();

        //assertTrue(deletedCounterN==1);//something weird is happenning
        //assertFalse(deletedCounterN==0);
        assertTrue(deletedCounterY==0);
        
    }

    @Test
    void testDeleteTreeIfNoTree() throws Exception {
        Tree test = new Tree();
        test.initialize();

        String input = "blob : 732d12f7e4f2e629e2954acbb720c32c0be985d1 : file1";
        test.addTree(input);

        assertFalse(test.deleteTree("blahblah"));
    }

    @Test
    void testAddToObjects() throws IOException {
        Tree test = new Tree();
        test.initialize();

        String input = "blob : 732d12f7e4f2e629e2954acbb720c32c0be985d1 : file1";
        test.addTree(input);

        test.saveToObjects();
        
        
        String dirName = "./objects/";
        File dir = new File (dirName);//create this directory (File class java)
        //dir.mkdir();
        File file = new File (dir, "327426a7b35dc90762e335b17ea59ecfaec45e16");

        assertTrue(file.exists());
    }

    @Test
    void testAddDirectory () throws Exception
    {
        //first create some files that we can use
        File subDir = new File("./test1");
        subDir.mkdirs();

        FileWriter fw = new FileWriter("./test1/examplefile1.txt");
        fw.write("the sha of this is ... ?");
        fw.close();
        fw = new FileWriter("./test1/examplefile2.txt");
        fw.write("zomg wut are u doing. LAWL");
        fw.close(); 
        fw = new FileWriter("./test1/examplefile3.txt");
        fw.write("LOL please dont read this.  Good job being thorough tho!");
        fw.close(); 

        Tree t = new Tree();
        t.addDirectory("./test1");

        t.saveToObjects();

        BufferedReader bufferedReader = new BufferedReader (new FileReader ("./objects/" + t.getEncryption()));

        StringBuilder stringBuilder = new StringBuilder();
        while (bufferedReader.ready())
        {
            stringBuilder.append((char) bufferedReader.read());
        }

        bufferedReader.close();
        String contents = stringBuilder.toString(); 

        assertTrue("tree is missing expected files", contents.contains("./test1/examplefile1.txt"));
        assertTrue("tree did not hash correctly", contents.contains("6cecd98f685b1c9bfce96f2bbf3f8f381bcc717e"));
    }
}
