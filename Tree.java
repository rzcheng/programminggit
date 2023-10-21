import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;

public class Tree {
    //might need a hashmap
    private ArrayList<String> blobList;
    private ArrayList<String> treeList;
    String encryption = "";

    public Tree () {
        blobList = new ArrayList<String>();
        treeList = new ArrayList<String>();
    }
    

    public void saveToObjects() throws IOException {
    }

    public String getEncryption() {
        return encryption;
    }

    public boolean addToTree(String input) throws IOException {
       if(!entryExists(input)) {
            //PrintWriter pw = new PrintWriter(new FileWriter(tree));
            String type = "" + input.substring(0,4);
            if(type.equals("blob")) {
                blobList.add(input);
            }
            else if(type.equals("tree")) {
                treeList.add(input);
            }
            
            return true;
        }
        return false;


    }

    //checks if input line appears in our tree
    private boolean entryExists(String inputLine) throws IOException {

        return blobList.contains(inputLine) || treeList.contains(inputLine);
    }

    // private void printList(File fileName) throws IOException {
    //     boolean b = fileName.isFile();

    //     PrintWriter pw = new PrintWriter(new FileWriter(fileName));

    //     pw.print(Utils.arrayListToFileFormat(blobList));
    //     pw.print(Utils.arrayListToFileFormat(treeList));

    //     pw.close();
    // }

    //honestly not sure what this is meant to do - ryan
    /*
    public boolean deleteTree(String input) throws Exception {
        File inputFile = new File("tree");
        String lineToRemove = "";
        
        if(!entryExists2(input)) {
            return false;
        }

        lineToRemove = findLine(input,tree);
        System.out.println("remove: " + lineToRemove);//test: correct

        String type = lineToRemove.substring(0,4);
        if(type.equals("blob")) {
            blobList.remove(lineToRemove);
        }
        else if(type.equals("tree")) {
            treeList.remove(lineToRemove);
        }

        printList(tempFile);

        boolean successful = tempFile.renameTo(inputFile);
        return successful;
    }
    */

    public void addDirectory (String directory) throws Exception
    {
        File directoryFile = new File (directory);
        if (!directoryFile.isDirectory()) {
            throw new Exception (directory + " is an invalid directory path.");
        }

        for (File file : directoryFile.listFiles()) 
        {
            if (file.isFile())
            {
                String filePath = directory + "/" + file.getName();
                Blob blob = new Blob (filePath); //optional

                addToTree("blob : " + blob.getEncryption() + " : " + filePath);
            }
            else if (file.isDirectory())
            { 
                Tree subTree = new Tree();
                String tempPath = file.getPath();
                subTree.addDirectory(tempPath);
                subTree.saveToObjects();

                addToTree("tree : " + subTree.getEncryption() + " : " + tempPath);
            }
        }
    }

}