import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;

public class Commit {
    private Tree myTree;
    private String treeSHA1FileLocation = "";
    private String parentSHA1 = "";
    private String childSHA1 = "";
    private String author = "";
    private String summary = "";
    //private long timestamp

    public Commit(String author, String summary) throws Exception {
        this.myTree = new Tree();
        treeSHA1FileLocation = createTree();
        this.author = author;
        this.summary = summary;
        //this.timestamp = System.currentTimeMillis();
    }

    public Commit(String author, String summary, String parentSHA1) throws Exception {
        this.myTree = new Tree();
        treeSHA1FileLocation = createTree();
        this.author = author;
        this.summary = summary;
        this.parentSHA1 = parentSHA1; // setting the parent commit SHA1
        
    }

    public void writeToFile () throws Exception
    {
        FileWriter writer = new FileWriter("./objects/" + generateCommitSHA1(),false);
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

        if (!parentSHA1.equals(""))
        {
            Utils.writeFileLine("./objects/" + parentSHA1, 3, generateCommitSHA1());
        }
    }

 
    public String generateCommitSHA1() 
    {
        if (myTree == null) 
        {
            return "";  // or throw an exception depending on your design
        }
        String parentSHA1ToBeUsed = parentSHA1;
        if (parentSHA1ToBeUsed == null) {
            parentSHA1ToBeUsed = "";
        }
        System.out.println("parentSHA1 during hash generation: " + parentSHA1ToBeUsed);

        
        //return myTree.encryptPassword(treeSHA1FileLocation + "\n" + parentSHA1ToBeUsed + "\n" + author + "\n" + preciseTimestamp + "\n" + summary);
       // return myTree.encryptPassword(treeSHA1FileLocation + "\n" + parentSHA1ToBeUsed + "\n" + author + "\n" + getDate() + "\n" + summary);
        return myTree.encryptPassword(author + "\n" + summary + "\n" + getDate() + "\n" + treeSHA1FileLocation + "\n" + parentSHA1ToBeUsed);

    }
    

    public static String getDate ()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        return(dateFormat.format(cal.getTime()));
    }
    // Old method
    public String createTree () throws Exception
    {
        ArrayList<String> list = new ArrayList<String>();

        BufferedReader br = new BufferedReader(new FileReader("./index"));

        while (br.ready()) 
        {
            list.add(br.readLine());
        }

        br.close();
        ArrayList<String> indexContents = list;

        Tree currentIndexTree = new Tree();

        for (String s : indexContents)
        {
            String type = s.substring(0, s.indexOf(" "));
            if (type.equals("blob")) {
                currentIndexTree.addToTree(s);
            }
            else {
                if (s == null || s.isEmpty()) {
                    return "";
                }
                String[] words = s.trim().split("\\s+");
                String lastWord = words[words.length - 1];
                currentIndexTree.addDirectory(lastWord);
                
            }
        }

        if (!Utils.readFromFile("./index").equals(""))
        {
            System.out.println();
        }

        if (!parentSHA1.isEmpty()) {
            File f = new File("./objects/" + childSHA1);
            if (childSHA1.isEmpty() || !f.exists()) throw new Exception("commit doesn't exist");

            ArrayList<String> arrList = new ArrayList<String>();

        BufferedReader br2 = new BufferedReader(new FileReader("./objects/" + childSHA1));

        while (br2.ready()) 
        {
            arrList.add(br2.readLine());
        }

        br2.close(); 
 
        String result = arrList.get(0);
            currentIndexTree.addToTree("tree : " + arrList);
        }
        // To link the childSHA1 of this commit to treeSHA1 of the previous commit:
        //this.childSHA1 = parentSHA1;
        //Optional<String> previousTreeSHA1 = getTreeSHA1OfPreviousCommit();


        // Now assign it to childSHA1
        // if (previousTreeSHA1 == null || previousTreeSHA1.isEmpty()) {
        //     this.childSHA1 = null; // or some default value
        // } else {
        //     this.childSHA1 = previousTreeSHA1;
        // }

        currentIndexTree.saveToObjects(); 
        treeSHA1FileLocation = currentIndexTree.getEncryption(); 
        return currentIndexTree.getEncryption();

    }

    // public String createTree() throws Exception {
    //     ArrayList<String> list = new ArrayList<String>();
    //     BufferedReader br = new BufferedReader(new FileReader("/Users/ryancheng/p/Honors Topics/programminggit//index.txt"));
    //     StringBuilder treeContent = new StringBuilder();
    
    //     while (br.ready()) {
    //         String line = br.readLine();
    //         list.add(line);
    //         treeContent.append(line).append("\n");
    //     }
    //     br.close();
    //     ArrayList<String> indexContents = list;
    //     Tree currentIndexTree = new Tree();
    
    //     for (String s : indexContents) {
    //         String type = s.substring(0, s.indexOf(" "));
    //         if (type.equals("blob")) {
    //             currentIndexTree.addToTree(s);
    //         } else {
    //             if (s == null || s.isEmpty()) {
    //                 return "";
    //             }
    //             String[] words = s.trim().split("\\s+");
    //             String lastWord = words[words.length - 1];
    //             currentIndexTree.addDirectory(lastWord);
    //         }
    //     }
        
    //     // return a hash of the tree content to ensure uniqueness
    //     return myTree.encryptPassword(treeContent.toString());
    // }
    

    // public String getTreeFromCommit(String curCommit) throws Exception {
    //     File f = new File("/Users/ryancheng/p/Honors Topics/programminggit/objects/" + curCommit);
    //     if (curCommit.isEmpty() || !f.exists()) throw new Exception("commit doesn't exist");

    //     //BufferedReader br2 = new BufferedReader(new FileReader("/Users/ryancheng/p/Honors Topics/programminggit/objects/" + childSHA1));
    //     //BufferedReader br2 = new BufferedReader(new FileReader("/Users/ryancheng/p/Honors Topics/programminggit/objects/" + curCommit));
    //     BufferedReader br2 = new BufferedReader(new FileReader(f));

    //     ArrayList<String> arrList = new ArrayList<String>();
    //     while (br2.ready()) 
    //     {
    //         arrList.add(br2.readLine());
    //     }

    //     br2.close(); 
 
    //     return arrList.get(0);
    // }

    public String getTreeFromCommit(String curCommit) throws Exception {
        if (curCommit == null || curCommit.isEmpty()) {
            curCommit = "";
        }
        // File f = new File("/Users/ryancheng/p/Honors Topics/programminggit/objects/" + curCommit);
        // if (!f.exists()) 
        // {
        //     return "";
        // }
        File f = new File("./objects/" + curCommit);
        if (!f.exists()) {
            System.out.println("Attempting to access: " + f.getAbsolutePath());
            return "";
        }
        BufferedReader br2 = new BufferedReader(new FileReader(f));
        ArrayList<String> arrList = new ArrayList<String>();
        while (br2.ready()) {
            arrList.add(br2.readLine());
        }
        br2.close();
        
        if (arrList.isEmpty()) {
            throw new Exception("File is empty");
        }
        return arrList.get(0);
        
        }
   
    public Tree getMyTree() 
    {
        return this.myTree;
    }


    public void setMyTree(Tree myTree) 
    {
        this.myTree = myTree;
    }
    public void setTreeSHA1FileLocation(String treeSHA1FileLocation) 
    {
        this.treeSHA1FileLocation = treeSHA1FileLocation;
    }
    public String getTreeSHA1FileLocation ()
    {
        return treeSHA1FileLocation;
    }

    public String getParentSHA1() {
        return this.parentSHA1;
    }

    public void setParentSHA1(String parentSHA1) {
        this.parentSHA1 = parentSHA1;
    }

    public String getChildSHA1() {
        return this.childSHA1;
    }

    public void setChildSHA1(String c1Hash) {
        this.childSHA1 = c1Hash;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
    
}
