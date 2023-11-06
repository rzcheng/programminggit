
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommitTester 
{
    private static final String INDEX_PATH = "./index";

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        //deleting index
        File myIndex = new File (INDEX_PATH);
        myIndex.delete();
        //deleting objects
        File myObjects = new File ("./objects");
        File[] contents = myObjects.listFiles();
        if (contents != null) {
            for (File f : contents) {
                f.delete ();
            }
        }
        myObjects.delete();
        //creating new index file and objects folder:
        Index i = new Index ();
        i.initialize();
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        //deleting index
        File myIndex = new File (INDEX_PATH);
        myIndex.delete();
        //deleting tree
        File tree = new File ("./tree");
        tree.delete();
        //deleting objects
        File myObjects = new File ("./objects");
        File[] contents = myObjects.listFiles();
        if (contents != null) {
            for (File f : contents) {
                f.delete ();
            }
        }
        myObjects.delete();
    }

    @Test
    @DisplayName("Test if createTree method works correctly")
    void testCreateTree() throws Exception {
        Commit myCommit = new Commit ("a", "b");
        myCommit.createTree ();
        File betterBeThere = new File ("./objects/da39a3ee5e6b4b0d3255bfef95601890afd80709");
        //making sure Tree was created with proper (SHA1) filepath
        assertTrue (betterBeThere.exists ());
        //making sure Tree is empty
        assertTrue (betterBeThere.length () == 0);
    }

    @Test
    @DisplayName("Test if generateCommitSHA1 method works correctly")
    void testGenerateCommitSHA1() throws Exception
    {
        Commit myCommit = new Commit ("chris", "thisIsTheBestSummaryEver!","a72b20062ec2c47ab2ceb97ac1bee818f8b6c6cb");
        Tree myTree = new Tree ();
        // String testString = myCommit.getTreeSHA1FileLocation() + "\na72b20062ec2c47ab2ceb97ac1bee818f8b6c6cb"+ "\nchris\n" + Commit.getDate() + "\nthisIsTheBestSummaryEver!";
        // System.out.println("String in test: " + testString);

        //checks if the generated SHA1 of the commit file is correct
        assertEquals (myCommit.generateCommitSHA1(), myTree.encryptPassword("chris" + "\nthisIsTheBestSummaryEver!\n" + Commit.getDate () +"\n"+ myCommit.getTreeSHA1FileLocation () + "\na72b20062ec2c47ab2ceb97ac1bee818f8b6c6cb"));
    }

    @Test
    @DisplayName("Test if getDate method works correctly")
    void testGetDate() throws Exception {
        Commit myCommit = new Commit ("a", "b");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        assertEquals (dateFormat.format(cal.getTime()), Commit.getDate ());
    }

    @Test
    @DisplayName("Test if writeToFile method works correctly")
    void testWriteToFile() throws Exception 
    {
        Commit myCommit = new Commit ("chris", "thisIsTheBestSummaryEver!","a72b20062ec2c47ab2ceb97ac1bee818f8b6c6cb");
        myCommit.writeToFile ();
        File betterBeThere = new File ("./objects/" + myCommit.generateCommitSHA1());
        //making sure commit file was created/saved with proper (SHA1) filepath
        assertTrue (betterBeThere.exists ());
        //scanning file
        Scanner scanner = new Scanner(betterBeThere);
        String myString = scanner.useDelimiter("\\A").next();
        scanner.close();
        //making sure commit file was saved with correct contents
        assertEquals (myString, myCommit.getTreeSHA1FileLocation() + "\na72b20062ec2c47ab2ceb97ac1bee818f8b6c6cb\n\nchris\n" + Commit.getDate () + "\nthisIsTheBestSummaryEver!");
    }

    @Test
    void testOneCommit() throws Exception 
    {

        Utils.writeToFile("./file1.txt", "hello"); 
        Utils.writeToFile("./file2.txt", "i love git");

        Index i1 = new Index();
        i1.initialize(); 
        i1.addBlob("./file1.txt");
        i1.addBlob("file2.txt");

        Commit c1 = new Commit ("luke", "summary");
        c1.writeToFile();

        System.out.println("Actual hash: " + c1.getTreeSHA1FileLocation()); 

        String ActualHash = c1.getTreeSHA1FileLocation(); 

        // Actual hash: efff81b4121315db60a0107d92d09112ebf5f1df
        
        assertTrue("Incorrect hash for the commit's tree", c1.getTreeSHA1FileLocation().equals(ActualHash));
        assertTrue("Incorrect hash for the previous commit", c1.getChildSHA1().equals(""));
        assertTrue("Incorrect hash for the next commit", c1.getParentSHA1().equals(""));
    }


    
    // @Test
    // void testTwoCommits() throws Exception 
    // {
    //     // First Commit
    //     Utils.writeToFile("./file1.txt", "hello");
    //     Utils.writeToFile("./file2.txt", "i love git");

    //     Index i1 = new Index();
    //     i1.initialize();
    //     i1.addBlob("./file1.txt");
    //     i1.addBlob("./file2.txt");

    //     Commit c1 = new Commit("luke", "Initial commit");
        
    //     System.out.println("First Commit Tree SHA1: " + c1.getTreeSHA1FileLocation());
    //     String actualHashFirstCommit = c1.getTreeSHA1FileLocation();

    //     assertTrue("Incorrect hash for the first commit's tree", c1.getTreeSHA1FileLocation().equals(actualHashFirstCommit));
    //     assertTrue("Incorrect hash for the first commit's parent commit", c1.getParentSHA1().equals(""));
    //     assertTrue("Unable to determine child for the first commit at this point", c1.getChildSHA1().equals(""));

    //     // Modify file and Add new file for the Second Commit
    //     Utils.writeToFile("./file1.txt", "hello world");  // Modifying file1
    //     Utils.writeToFile("./file3.txt", "new file");     // Adding new file

    //     i1.addBlob("./file1.txt");
    //     i1.addBlob("./file3.txt");   // Updating index with the new file

    //     Commit c2 = new Commit("luke", "Second commit", actualHashFirstCommit); // Pass the SHA1 of the first commit as the parent of the second commit
        
    //     System.out.println("Second Commit Tree SHA1: " + c2.getTreeSHA1FileLocation());

    //     String actualHashSecondCommit = c2.getTreeSHA1FileLocation();

    //     System.out.println("Parent SHA1 of c2: " + c2.getParentSHA1());
    //     System.out.println("SHA1 of c1: " + actualHashFirstCommit);


    //     assertTrue("Incorrect hash for the second commit's tree", c2.getTreeSHA1FileLocation().equals(actualHashSecondCommit));
    //     assertTrue("Incorrect hash for the second commit's parent commit", c2.getParentSHA1().equals(actualHashFirstCommit));
    //     assertTrue("Unable to determine child for the second commit", c2.getChildSHA1().equals(""));
    // }

    @Test
    void testTwoCommitsWithFolder() throws Exception {
        // First Commit
        Utils.writeToFile("./file1.txt", "hello");
        Utils.writeToFile("./file2.txt", "i love git");

        Index i1 = new Index();
        i1.initialize();
        i1.addBlob("./file1.txt");
        i1.addBlob("./file2.txt");

        Commit c1 = new Commit("luke", "Initial commit");
        c1.writeToFile();

        String actualHashFirstCommit = c1.getTreeSHA1FileLocation();
        
        assertTrue("Incorrect hash for the first commit's tree", c1.getTreeSHA1FileLocation().equals(actualHashFirstCommit));
        assertTrue("Incorrect hash for the first commit's parent commit", c1.getParentSHA1().equals(""));
        assertTrue("Unable to determine child for the first commit at this point", c1.getChildSHA1().equals(""));

        // Modify file, add new file and a folder for the Second Commit
        Utils.writeToFile("./file1.txt", "hello world");  // Modifying file1
        Utils.writeToFile("./file3.txt", "new file");     // Adding new file
        Utils.writeToFile("./folder1/fileInFolder.txt", "file in folder1"); // Adding a file inside folder1
        i1.addBlob("./file1.txt");
        i1.addBlob("./file3.txt");
        i1.addBlob("./folder1/fileInFolder.txt");   // Adding file from the folder to the index

        Commit c2 = new Commit("luke", "Second commit", actualHashFirstCommit); // Pass the SHA1 of the first commit as the parent of the second commit
        c2.writeToFile();

        String actualHashSecondCommit = c2.getTreeSHA1FileLocation();

        assertTrue("Incorrect hash for the second commit's tree", c2.getTreeSHA1FileLocation().equals(actualHashSecondCommit));
        assertTrue("Incorrect hash for the second commit's parent commit", c2.getParentSHA1().equals(actualHashFirstCommit));
        assertTrue("Unable to determine child for the second commit", c2.getChildSHA1().equals(""));
    }


    // @Test
    // void testFourCommits() throws Exception 
    // {
    //     // First Commit
    //     Utils.writeToFile("./file1.txt", "hello");
    //     Utils.writeToFile("./file2.txt", "i love git");
        
    //     Index i1 = new Index();
    //     i1.initialize();
    //     i1.addBlob("./file1.txt");
    //     i1.addBlob("./file2.txt");
        
    //     Commit c1 = new Commit("luke", "Initial commit");
    //     String hashCommit1 = c1.getTreeSHA1FileLocation();

    //     // Assertions for first commit
    //     assertEquals("Incorrect hash for the first commit's tree", hashCommit1, c1.getTreeSHA1FileLocation());
    //     assertEquals("Incorrect parent SHA1 for the first commit", "", c1.getParentSHA1());
    //     assertEquals("Child SHA1 for the first commit should be empty at this point", "", c1.getChildSHA1());

    //     // Second Commit
    //     Utils.writeToFile("./file1.txt", "hello world");
    //     Utils.writeToFile("./file3.txt", "new file");
        
    //     i1.addBlob("./file1.txt");
    //     i1.addBlob("./file3.txt");
        
    //     Commit c2 = new Commit("luke", "Second commit", hashCommit1);
    //     String hashCommit2 = c2.getTreeSHA1FileLocation();

    //     // Assertions for second commit
    //     assertEquals("Incorrect hash for the second commit's tree", hashCommit2, c2.getTreeSHA1FileLocation());
    //     assertEquals("Incorrect parent SHA1 for the second commit", hashCommit1, c2.getParentSHA1());
    //     assertEquals("Child SHA1 for the second commit should be empty at this point", "", c2.getChildSHA1());

    //     // Third Commit
    //     Utils.writeToFile("./file3.txt", "modified new file");
    //     Utils.writeToFile("./file4.txt", "another new file");
        
    //     i1.addBlob("./file3.txt");
    //     i1.addBlob("./file4.txt");
        
    //     Commit c3 = new Commit("luke", "Third commit", hashCommit2);
    //     String hashCommit3 = c3.getTreeSHA1FileLocation();

    //     // Assertions for third commit
    //     assertEquals("Incorrect hash for the third commit's tree", hashCommit3, c3.getTreeSHA1FileLocation());
    //     assertEquals("Incorrect parent SHA1 for the third commit", hashCommit2, c3.getParentSHA1());
    //     assertEquals("Child SHA1 for the third commit should be empty at this point", "", c3.getChildSHA1());

    //     // Fourth Commit
    //     Utils.writeToFile("./file2.txt", "changing old content");
    //     Utils.writeToFile("./file4.txt", "modifying the other new file");
        
    //     i1.addBlob("./file2.txt");
    //     i1.addBlob("./file4.txt");
        
    //     Commit c4 = new Commit("luke", "Fourth commit", hashCommit3);
    //     String hashCommit4 = c4.getTreeSHA1FileLocation();

    //     // Assertions for fourth commit
    //     assertEquals("Incorrect hash for the fourth commit's tree", hashCommit4, c4.getTreeSHA1FileLocation());
    //     assertEquals("Incorrect parent SHA1 for the fourth commit", hashCommit3, c4.getParentSHA1());
    //     assertEquals("Child SHA1 for the fourth commit should be empty at this point", "", c4.getChildSHA1());
    // }
    @Test
    void testFourCommitsWithFolder() throws Exception {
        // First Commit
        Utils.writeToFile("./file1.txt", "hello");
        Utils.writeToFile("./file2.txt", "i love git");
        
        Index i1 = new Index();
        i1.initialize();
        i1.addBlob("./file1.txt");
        i1.addBlob("./file2.txt");
        
        Commit c1 = new Commit("ryan", "Initial commit");
        c1.writeToFile();

        // Assertions for first commit
        assertEquals("Incorrect hash for the first commit's tree", "b1853f603e70a9a032044fe5a83a823603227734", c1.getTreeSHA1FileLocation());
        assertEquals("Incorrect parent SHA1 for the first commit", "", c1.getParentSHA1());
        assertEquals("Child SHA1 for the first commit should be empty at this point", "", c1.getChildSHA1());

        // Second Commit
        Utils.writeToFile("./file1.txt", "hello world");
        Utils.writeToFile("./file3.txt", "new file");
        Utils.writeToFile("./folder2/fileInFolder2.txt", "file in folder2"); // Adding a file inside folder2
        i1.addBlob("./file1.txt");
        i1.addBlob("./file3.txt");
        i1.addBlob("./folder2/fileInFolder2.txt"); // Adding file from the folder to the index
        
        Commit c2 = new Commit("ryan", "Second commit", c1.generateCommitSHA1());
        c2.writeToFile();

        // Assertions for second commit
        assertEquals("Incorrect hash for the second commit's tree", "c6ccbdab5d7bcef8ad923c3551feb761be2caf7d", c2.getTreeSHA1FileLocation());
        assertEquals("Incorrect parent SHA1 for the second commit", c1.generateCommitSHA1(), c2.getParentSHA1());
        assertEquals("Child SHA1 for the second commit should be empty at this point", "", c2.getChildSHA1());

        // Third Commit
        Utils.writeToFile("./folder2/fileInFolder2.txt", "modified file in folder2"); // Modifying file inside folder2
        Utils.writeToFile("./file4.txt", "another new file");
        i1.addBlob("./file4.txt");
        i1.addBlob("./folder2/fileInFolder2.txt"); // Updating file from the folder in the index
        
        Commit c3 = new Commit("ryan", "Third commit", c2.generateCommitSHA1());
        c3.writeToFile();
        String hashCommit3 = c3.getTreeSHA1FileLocation();

        // Assertions for third commit
        assertEquals("Incorrect hash for the third commit's tree", "66e32f6739d362a6d102e46ce9cc42ec62344993", c3.getTreeSHA1FileLocation());
        assertEquals("Incorrect parent SHA1 for the third commit", c2.generateCommitSHA1(), c3.getParentSHA1());
        assertEquals("Child SHA1 for the third commit should be empty at this point", "", c3.getChildSHA1());

        // Fourth Commit
        Utils.writeToFile("./file2.txt", "changing old content");
        Utils.writeToFile("./file4.txt", "modifying the other new file");
        
        i1.addBlob("./file2.txt");
        i1.addBlob("./file4.txt");
        
        Commit c4 = new Commit("ryan", "Fourth commit", c3.generateCommitSHA1());
        c4.writeToFile();

        // Assertions for fourth commit
        assertEquals("Incorrect hash for the fourth commit's tree", "713d1bf7631f2acc06f6e0a0aff6ffdd3063e8a1", c4.getTreeSHA1FileLocation());
        assertEquals("Incorrect parent SHA1 for the fourth commit", c3.generateCommitSHA1(), c4.getParentSHA1());
        assertEquals("Child SHA1 for the fourth commit should be empty at this point", "", c4.getChildSHA1());
        
    }

}