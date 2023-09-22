import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Commit {
    private Tree myTree;
    private String treeSHA1FileLocation;
    private String parentSHA1;
    private String childSHA1;
    private String author;
    private String summary;

    public Commit (String author, String summary) throws IOException
    {
        treeSHA1FileLocation = createTree ();
        this.author = author;
        this.summary = summary;
    }

    public Commit (String parentSHA1, String author, String summary) throws IOException
    {
        treeSHA1FileLocation = createTree ();
        this.parentSHA1 = parentSHA1;
        this.author = author;
        this.summary = summary;
    }

    public void writeToFile () throws IOException
    {
        FileWriter writer = new FileWriter("./objects/" + generateCommitSHA1 (),false);
        PrintWriter out = new PrintWriter(writer);
        String parentSHA1ToBeUsed = parentSHA1;
        String childSHA1ToBeUsed = childSHA1;
        if (parentSHA1ToBeUsed == null)
        {
            parentSHA1ToBeUsed = "";
        }
        if (childSHA1ToBeUsed == null)
        {
            childSHA1ToBeUsed = "";
        }
        out.print (treeSHA1FileLocation + "\n" + parentSHA1ToBeUsed + "\n" + childSHA1ToBeUsed + "\n" + author + "\n" + getDate () + "\n" + summary);
        writer.close ();
        out.close ();
    }

    public String generateCommitSHA1 ()
    {
        String parentSHA1ToBeUsed = parentSHA1;
        if (parentSHA1ToBeUsed == null)
        {
            parentSHA1ToBeUsed = "";
        }
        return myTree.encryptPassword (treeSHA1FileLocation + "\n" + parentSHA1ToBeUsed + "\n" + author + "\n" + getDate () + "\n" + summary);
    }

    public String getDate ()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        return(dateFormat.format(cal.getTime()));
    }

    public String createTree () throws IOException
    {
        File OGTree = new File ("tree");
        OGTree.createNewFile ();
        myTree = new Tree ();
        myTree.initialize ();
        myTree.saveToObjects ();
        return (myTree.encryptPassword (""));
    }
}
