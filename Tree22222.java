import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Tree22222 {
    private File tree;
    //might need a hashmap
    private HashMap<String,List<String>> map;

    public Tree22222 () {
        map = new HashMap<String,List<String>>();
        ArrayList<String> blobList = new ArrayList<String>();
        map.put("blob",blobList);
        ArrayList<String> treeList = new ArrayList<String>();
        map.put("tree",treeList);
    }

    public void initialize() throws IOException {
        String dirName = "./objects/";
        File dir = new File (dirName);
        dir.mkdir();
        tree = new File (dir, "tree");
        //tree = new File ("tree");

        if(!tree.exists()) {
            tree.createNewFile();
        }
    }

    
    /*public void initialize() throws IOException {
        String dirName = "./objects/";
        File dir = new File (dirName);
        dir.mkdir();
        tree = new File (dir, "tree");
        rename(tree);
    }

    private void rename(File fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String str = "";

        while(br.ready()) {
            str += br.readLine();
        }

        br.close();

        //converting to sha1

        String sha1 = encryptPassword(str);
        //System.out.println("TEST BYTE: " + str);
        
        //System.out.println("SHA : " + sha1);

        //printing to objects folder
        String dirName = "./objects/";
        File dir = new File (dirName);//create this directory (File class java)
        //dir.mkdir();
        tree = new File (dir, sha1);

        PrintWriter pw = new PrintWriter(tree);

        pw.print(str);

        pw.close();
    }

    private String encryptPassword(String password)
    {
        String sha1 = "";
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return sha1;
    }

    private String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }*/
    




    public boolean addTree(String input) throws IOException {
       if(!entryExists(input, tree)) { //is the input just the whole string? or do u need to create something?
            /*if(checkContents(tree).equals("")){
                PrintWriter pw = new PrintWriter(new FileWriter(tree, true));
                pw.append(input);//need some way to not have this extra \n
                pw.close();
                return true;
            }
            else {
                PrintWriter pw = new PrintWriter(new FileWriter(tree, true));
                pw.append("\n" + input);
                pw.close();
                return true;
            }*/

            /*PrintWriter pw = new PrintWriter(new FileWriter(tree, true));
            pw.append(input+"\n");
            pw.close();
            return true;*/
            PrintWriter pw = new PrintWriter(new FileWriter(tree, true));
            if(checkContents(tree).equals("")){
                pw.append(input);//need some way to not have this extra \n
                pw.close();
                return true;
            }
            else {
                pw.append("\n" + input);
                pw.close();
                return true;
            }
        }
        return false;


    }

    private String checkContents(File tree2) throws IOException {
        String contents = "";
        BufferedReader br = new BufferedReader(new FileReader(tree2));
        while(br.ready()) {
            contents += "" + br.readLine();
        }
        br.close();
        return contents;
    }

    //checks if input line appears in a file
    private boolean entryExists(String inputLine, File tree2) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(tree));
        while(br.ready()) {
            String str = br.readLine();
            if(str.equals(inputLine)) {
                br.close();
                return true;
            }
        }
        br.close();
        return false;
    }

    public boolean deleteTree(String input) throws Exception {
        /*Blob bl = new Blob(origFileName);
        String newFileName = bl.getSha1();

        String newEntry = origFileName + " : " + newFileName;*/
        
        String dirName = "./objects/";
        File dir = new File (dirName);
        File inputFile = new File(dir,"tree");
        File tempFile = new File(dir,"myTempFile.txt");
        String lineToRemove = "";
        
        if(!entryExists2(input, tree)) {
            return false;
        }

        lineToRemove = findLine(input,tree);

        //removing line
        BufferedReader br = new BufferedReader(new FileReader(inputFile));
        PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
        
        /*while((currentLine = reader.readLine()) != null) {
            // trim newline when comparing with lineToRemove
            String trimmedLine = currentLine.trim();
            if(trimmedLine.equals(lineToRemove)) continue;
            writer.write(currentLine + System.getProperty("line.separator"));
        }*/

        String prevLine = br.readLine();
        if(prevLine.equals(lineToRemove)) {
            pw.print("");
        }
        else {
            pw.print(prevLine);
        }

        while(br.ready()) {
            if(!prevLine.equals(lineToRemove)) {
                if(checkContents(tempFile).equals("")){
                    pw.print(prevLine);
                }
                else {
                    pw.print("\n" + prevLine);
                }
            }
            prevLine = br.readLine();
        }
        if(!prevLine.equals(lineToRemove)) {
            pw.print("\n" + prevLine);
        }//what if prevLine = lineToRemove?


        pw.close(); 
        br.close(); 
        boolean successful = tempFile.renameTo(inputFile);
        return successful;
    }

    //checks if input appears in file
    private boolean entryExists2(String input, File tree2) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(tree));
        while(br.ready()) {
            String str = br.readLine();
            if(str.contains(input)) {
                br.close();
                return true;
            }
        }
        br.close();
        return false;
    }

    private String findLine(String input, File tree2) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(tree));
        while(br.ready()) {
            String str = br.readLine();
            if(str.contains(input)) {
                br.close();
                return str;
            }
        }
        br.close();
        throw new Exception("line not found", null);
    }

}