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
    private File tree;
    //might need a hashmap
    private ArrayList<String> blobList;
    private ArrayList<String> treeList;

    public Tree () {
        blobList = new ArrayList<String>();
        treeList = new ArrayList<String>();
    }

    public void initialize() throws IOException {
        File objects = new File("./objects");
        if (!objects.exists())
            objects.mkdirs();
        //String dirName = "./objects/";
        //File dir = new File (dirName);
        //dir.mkdir();
        tree = new File ("tree");
        //tree = new File ("tree");

        if(!tree.exists()) {
            tree.createNewFile();
        }
    }

    
    public void saveToObjects() throws IOException {
        rename(tree);
    }

    public void rename(File fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String str = "";

        while(br.ready()) {
            str += br.readLine()+"\n";
        }
        str = str.trim();//get rid of extra line

        br.close();

        //converting to sha1

        String sha1 = encryptPassword(str);
        //System.out.println("TEST BYTE: " + str);
        
        //System.out.println("SHA : " + sha1);

        //printing to objects folder
        String dirName = "./objects/";
        File dir = new File (dirName);//create this directory (File class java)
        //dir.mkdir();
        File newFile = new File (dir, sha1);

        PrintWriter pw = new PrintWriter(newFile);

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
    }
    




    public boolean addTree(String input) throws IOException {
       if(!entryExists(input, tree)) {
            //PrintWriter pw = new PrintWriter(new FileWriter(tree));
            String type = "" + input.substring(0,4);
            if(type.equals("blob")) {
                blobList.add(input);
            }
            else if(type.equals("tree")) {
                treeList.add(input);
            }
            
            printList(tree);

            return true;
        }
        return false;


    }

    /*private String checkContents(File tree2) throws IOException {
        String contents = "";
        BufferedReader br = new BufferedReader(new FileReader(tree2));
        while(br.ready()) {
            contents += "" + br.readLine();
        }
        br.close();
        return contents;
    }*/

    //checks if input line appears in a file
    private boolean entryExists(String inputLine, File tree2) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(tree2));
        while(br.ready()) {
            String str = br.readLine();
            if(str.equals(inputLine)) {
                br.close();
                return true;
            }
        }
        br.close();
        return false;

        /*String type = inputLine.substring(0,4);
        if(type.equals("blob")) {
            for(int i=0; i<blobList.size();i ++) {
                String str = blobList.get(i);
                if(str.equals(inputLine)) {
                    return true;
                }
            }
        }
        else {
            for(int i=0; i<treeList.size();i ++) {
                String str = treeList.get(i);
                if(str.equals(inputLine)) {
                    return true;
                }
            }
        }
        return false;*/
    }

    private void printList(File fileName) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(fileName));
        for(int i=0; i<blobList.size(); i++) {
            String str = blobList.get(i);
            if(treeList.size()!=0 && i==blobList.size()-1) {
                pw.println(str);
            }
            else if(i!=blobList.size()-1) {
                pw.println(str);
            }
            else {
                pw.print(str);
            }
        }
        for(int i=0; i<treeList.size(); i++) {
            String str = treeList.get(i);
            if(i!=treeList.size()-1) {
                pw.println(str);
            }
            else {
                pw.print(str);
            }
        }
        pw.close();
    }

    public boolean deleteTree(String input) throws Exception {
        
        //String dirName = "./objects/";
        //File dir = new File (dirName);
        File inputFile = new File("tree");
        File tempFile = new File("myTempFile.txt");
        String lineToRemove = "";
        
        if(!entryExists2(input, tree)) {
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

        //removing line
        //BufferedReader br = new BufferedReader(new FileReader(inputFile));
        /*PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
        
        for(int i=0; i<blobList.size(); i++) {
            String str = blobList.get(i);
            if(!str.equals(lineToRemove)) {
                if(treeList.size()==0 && i==blobList.size()-2 && blobList.get(i+1).equals(lineToRemove)) {
                    pw.print(str);
                }
                else if(i==blobList.size()-1 && treeList.size()==0) {
                    pw.print(str);
                }
                else if(i==blobList.size()-1 && treeList.size()>0) {
                    pw.println(str);
                }
                else if(i!=blobList.size()-1) {
                    pw.println(str);
                }
            }
        }
        for(int i=0; i<treeList.size(); i++) {
            String str = treeList.get(i);
            if(!str.equals(lineToRemove)) {
                if(i==treeList.size()-2 && treeList.get(i+1).equals(lineToRemove)) {
                    pw.print(str);
                }
                else if(i==treeList.size()-1) {
                    pw.print(str);
                }
                else {
                    pw.println(str);
                }
            }
        }


        pw.close(); */
        //br.close(); 
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