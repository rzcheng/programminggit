
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
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        //deleting index
        File myIndex = new File ("./index");
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
        File myIndex = new File ("./index");
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
    }

    @Test
    @DisplayName("Test if createTree method works correctly")
    void testCreateTree() throws IOException {
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
    void testGenerateCommitSHA1() throws IOException {
        Commit myCommit = new Commit ("a72b20062ec2c47ab2ceb97ac1bee818f8b6c6cb", "chris", "thisIsTheBestSummaryEver!");
        Tree myTree = new Tree ();
        //checks if the generated SHA1 of the commit file is correct
        assertEquals (myCommit.generateCommitSHA1 (), myTree.encryptPassword(myCommit.getTreeSHA1FileLocation () + "\na72b20062ec2c47ab2ceb97ac1bee818f8b6c6cb\nchris\n" + myCommit.getDate () + "\nthisIsTheBestSummaryEver!"));
    }

    @Test
    @DisplayName("Test if getDate method works correctly")
    void testGetDate() throws IOException {
        Commit myCommit = new Commit ("a", "b");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        assertEquals (dateFormat.format(cal.getTime()), myCommit.getDate ());
    }

    @Test
    @DisplayName("Test if writeToFile method works correctly")
    void testWriteToFile() throws IOException {
        Commit myCommit = new Commit ("a72b20062ec2c47ab2ceb97ac1bee818f8b6c6cb", "chris", "thisIsTheBestSummaryEver!");
        myCommit.writeToFile ();
        File betterBeThere = new File ("./objects/" + myCommit.generateCommitSHA1());
        //making sure commit file was created/saved with proper (SHA1) filepath
        assertTrue (betterBeThere.exists ());
        //scanning file
        Scanner scanner = new Scanner(betterBeThere);
        String myString = scanner.useDelimiter("\\A").next();
        scanner.close();
        //making sure commit file was saved with correct contents
        assertEquals (myString, myCommit.getTreeSHA1FileLocation() + "\na72b20062ec2c47ab2ceb97ac1bee818f8b6c6cb\n\nchris\n" + myCommit.getDate () + "\nthisIsTheBestSummaryEver!");
    }
}