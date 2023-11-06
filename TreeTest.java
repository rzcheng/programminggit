import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

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
    static void tearDownAfterClass() throws Exception 
    {
        File tree = new File("./tree");
        tree.delete();
        FileUtils.deleteFile("file1");
        FileUtils.deleteDirectory("./objects");
    }
    // @Test
    // void addSomeText() throws Exception
    // {
    //     Tree test = new Tree();
    //     String cont = "";
    //     String ryanzc = "Not to have an empty file";
    //     Utils.writeToFile("/Users/ryancheng/p/Honors Topics/programminggit/tree", ryanzc );
    //     BufferedReader br = new BufferedReader(new FileReader("/Users/ryancheng/p/Honors Topics/programminggit/tree"));
    //     while(br.ready()) 
    //     {
    //         cont += "" + br.readLine();
    //     }
    //     br.close();
    //     assertTrue(cont.contains(ryanzc));
    // }

    @Test
    void testAddTree() throws Exception
     {
        Tree test = new Tree();
        

        String input = "blob : 732d12f7e4f2e629e2954acbb720c32c0be985d1 : file1";
        test.addToTree(input);

        //check if the above string is in test file
        File file = new File("./tree");
        
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
        //assertTrue(contents.contains(input));
        System.out.println("contents" +contents.trim());
        System.out.println("input: " + input);
        //System.out.println(input);
        assertTrue(contents.trim().contains(input.trim())); 
    }

    @Test
    void testAddTreeIfAlreadyTree() throws Exception {
        Tree test = new Tree();

        String input = "blob : 732d12f7e4f2e629e2954acbb720c32c0be985d1 : file1";
        test.addToTree(input);
        test.addToTree(input);

        File file = new File("./tree");

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
    void testAddToObjects() throws Exception {

        File tree = new File("./tree");
        tree.delete();

        Tree test = new Tree();

        String input = "blob : 732d12f7e4f2e629e2954acbb720c32c0be985d1 : file1";
        test.addToTree(input);

        test.saveToObjects();
        
        File file = new File ("./objects/327426a7b35dc90762e335b17ea59ecfaec45e16");
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

    // @Test
    // public void testCommitFileDeletionsAndEdits() 
    // {
    //     Index index = new Index();
    //     // Simulate file edits and deletions
    //     // index.editBlob("file1.txt", "newfile1sha1");
    //     index.editBlob("file1.txt");
    //     index.deleteBlob("file2.txt");
        
    //     // Create a commit with the updated index
    //     Tree tree = new Tree(index);
    //     //Commit commit = new Commit("commit message", "author", tree);
    //     Commit commit = new Commit("commit message", "author", "newfile1sha1");
        
    //     // Assertions to validate the Tree of the commit
    //     assertNull(commit.getMyTree().getFileSHA1("file2.txt")); // File2 should not exist
    //     assertEquals("newfile1sha1", commit.getMyTree().getFileSHA1("file1.txt")); // File1 should have the new SHA1
        
    //     // Continue with further assertions and more commit tests...
    // }

    // @Test
    // public void testCommitWithFileDeletionsAndEdits() throws Exception {
    //     // Instantiate the Index class (assuming you have methods to handle edits and deletions)
    //     Index index = new Index();
    //     index.editBlob("file1.txt", "newfile1sha1");
    //     index.deleteBlob("file2.txt");

    //     // Create a tree using the updated index
    //     Tree tree = new Tree(index);
        
    //     // Simulate creating a commit by providing author, commit message, and parent commit hash
    //     Commit commit = new Commit("Author Name", "Commit message", "parentSHA1Hash", tree.getEncryption());

    //     // Write the commit (which should also write the tree)
    //     commit.saveCommit();

    //     // Now, we retrieve the tree SHA1 from the commit to check if the changes are reflected
    //     String treeSHA1 = commit.getParentSHA1();
        
    //     // Load the saved tree using its SHA1 (assuming you have such a constructor or method)
    //     Tree savedTree = new Tree(); // Replace with actual constructor/method that takes SHA1 to load tree
    //     savedTree.loadFromSHA1(treeSHA1);

    //     // Assertions
    //     // Assuming you have a method 'getFileSHA1' in the Tree class to get file's SHA1
    //     assertNull(savedTree.getFileSHA1("file2.txt")); // file2.txt should have been deleted
    //     assertEquals("newfile1sha1", savedTree.getFileSHA1("file1.txt")); // file1.txt should have the new SHA1

    //     // Check if the parent SHA1 is set correctly in the commit
    //     assertEquals("parentSHA1Hash", commit.getParentSHA1());

    //     // ... and continue with more tests for different commit scenarios.
    // }

    @Test
    public void testCommitFileDeletionsAndEdits() throws Exception 
    {

        // Assuming Index and Tree classes exist
        Index index = new Index();
        // index.editBlob("file1.txt", "newfile1sha1");
        index.editBlob("./file1.txt");
        index.deleteBlob("./file2.txt");

        // Assuming that the Tree class has a constructor that accepts an Index object
        Tree tree = new Tree(index);
        Commit commit = new Commit("Author Name", "Commit message");

        // Write the commit (which should also write the tree)
        commit.setMyTree(tree); // Let's assume we need to associate the Tree with the Commit
        commit.writeToFile();

        // we read the tree from the commit to ensure that the changes are there
        // this would require a method in Commit to get the associated Tree object based on SHA1
        Tree savedTree = new Tree(); // This would need to be the Tree loaded from SHA1
        savedTree.loadFromSHA1("./objects/" + commit.getTreeSHA1FileLocation());

        // Assertions would depend on how Tree is implemented to check for file existence
        assertFalse(savedTree.hasFile("./file2.txt")); // File2 should not exist after deletion
        assertTrue(savedTree.hasFile("./file1.txt")); // File1 should exist
    }

    @Test
    public void testTreeTraverse() throws Exception{


        File tree = new File("./tree");
        tree.delete();

        Tree test = new Tree();

        String input = "blob : 732d12f7e4f2e629e2954acbb720c32c0be985d1 : file1";
        test.addToTree(input);
        test.saveToObjects();

        assertEquals(Tree.traverseForFile("file1", test.getEncryption()).size(), 0);

    }
}
