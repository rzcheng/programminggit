import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Commit {
    Tree myTree;
    String treeSHA1FileLocation;
    String parentSHA1;
    String childSHA1;
    String author;
    String summary;

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

    public void writeFile ()
    {

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
