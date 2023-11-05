import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

enum EntryType
    {
        BLOB("blob"),
        TREE("tree"),
        UNKNOWN("");

        private final String label;

        EntryType(String label) {
            this.label = label;
        }

        // A method to determine the type based on the input string
        public static EntryType fromString(String input) {
            for (EntryType type : values()) {
                if (input.startsWith(type.label)) {
                    return type;
                }
            }
            return UNKNOWN;
        }
    }

public class Tree 
{
    private Map<String, String> fileSHA1Map = new HashMap<>();

    // Constructor and other methods ...

    // Method to get the SHA1 of a file by its name
    public String getFileSHA1(String fileName) {
        return fileSHA1Map.get(fileName);
    }

    // Method to load a file and its SHA1 into the map (you may already have a similar method)
    public void addFileSHA1(String fileName, String sha1) {
        fileSHA1Map.put(fileName, sha1);
    }
    //might need a hashmap but made it with arraylist
    private ArrayList<String> blobList;
    private ArrayList<String> treeList;
    String encryption = "";
    File tree;
    private Index index;
    public Tree () throws IOException
    {
        blobList = new ArrayList<String>();
        treeList = new ArrayList<String>();
        initialize(); 
    }
    public Tree(Index index) throws Exception 
    {
        if (index == null) 
        {
            this.index = new Index();
        } 
        else 
        {
            this.index = index;
        }
        // Initialize lists and potentially load existing tree contents
        this.blobList = new ArrayList<>();
        this.treeList = new ArrayList<>();
        this.initialize();



        // Loop over the files in the index
        for (Map.Entry<String, String> fileEntry : index.getFiles().entrySet()) {
            String key = fileEntry.getKey();
            String value = fileEntry.getValue();

            if (key.startsWith("*deleted*")) {
                // Do not include deleted files in the tree
                continue;
            }

            if (key.startsWith("*edited*")) {
                // Replace the file's SHA1 with the new one
                String filename = key.replace("*edited*", "");
                updateFileInTree(filename, value); // This method needs to be implemented
            } else {
                // Add or update the file in the tree
                addFileToTree(key, value); // This method needs to be implemented
            }
        }

        // Save the updated tree
        this.saveToObjects();
    }

    // Method to add a file to the tree
    private void addFileToTree(String filename, String sha1) throws IOException {
        String treeEntry = "blob : " + sha1 + " : " + filename;
        addToTree(treeEntry);
    }

    // Method to update a file in the tree
    private void updateFileInTree(String filename, String newSha1) throws Exception 
    {
        // Find the existing entry for the file
        String existingEntry = findLine(filename, tree);
        if (existingEntry != null) {
            // Remove the old entry
            deleteTree(existingEntry);
            // Add the new entry
            addFileToTree(filename, newSha1);
        } 
        else 
        {
            // If the file wasn't part of the tree yet, just add it
            addFileToTree(filename, newSha1);
        }
    }

    private void loadTreeContents() throws IOException {
        tree = new File ("./tree");
        //blobList.clear();  // Clear the blobList
        //treeList.clear();  // Clear the treeList
    
        if(tree.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(tree));
            String line;
            while((line = br.readLine()) != null) {
                EntryType type = EntryType.fromString(line);
                if(type == EntryType.BLOB && !blobList.contains(line)) 
                {  
                    blobList.add(line);
                } else if(type == EntryType.TREE && !treeList.contains(line)) 
                {  
                    treeList.add(line);
                }
            }
            br.close();
        }
    }

    public boolean hasFile(String filePath) 
    {
        for (String blobEntry : blobList) 
        {
            // Extract the file path part from the blob entry. Assuming the format "blob : sha1 : path"
            String[] parts = blobEntry.split(" : ");
            if (parts.length > 2 && parts[2].equals(filePath)) 
            {
                return true;
            }
        }
        return false;
    }
    

  
    public void saveToObjects() throws Exception 
    {
        loadTreeContents();  // Make sure to have the latest tree contents
    
        String treeContent = "";
    
        treeContent += Utils.arrayListToFileFormat(blobList);
        treeContent += Utils.arrayListToFileFormat(treeList);
    
        encryption = Utils.stringtoSHA(treeContent);
        Utils.writeToFile("./objects/" + encryption, treeContent);
        Utils.writeToFile("./tree", treeContent);          
    }

     

    public String getEncryption() 
    {
        return encryption;
    }
    
    // fixed method addToTree
    public boolean addToTree(String input) throws IOException {
        if (!entryExists(input)) 
        {
            EntryType type = EntryType.fromString(input);
            switch (type)
             {
                case BLOB:
                    if(!blobList.contains(input)) 
                    {  
                        blobList.add(input);
                    }
                    break;
                case TREE:
                    if(!treeList.contains(input)) 
                    {  
                        treeList.add(input);
                    }
                    break;
                default:
                    // Handle an unrecognized type if necessary
                    return false;
            }
            saveListsToFile();
            return true;
        }
        return false;
    }
    
    //checks if input line appears in our tree
    private boolean entryExists(String inputLine) throws IOException
    {
        return blobList.contains(inputLine) || treeList.contains(inputLine);
    }

    private void saveListsToFile() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("./tree"))) {
            for (String blob : blobList) {
                bw.write(blob);
                bw.newLine();
            }
            for (String t : treeList) {
                bw.write(t);
                bw.newLine();
            }
        }
    }
  

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

    // public void initialize() throws IOException {
    //     File objects = new File("./objects");
    //     if (!objects.exists())
    //         objects.mkdirs();
    //     tree = new File ("tree");

    //     if(!tree.exists()) {
    //         tree.createNewFile();
    //     }
    // }
    public void initialize() throws IOException 
    {
        File objects = new File("./objects");
        if (!objects.exists()) {
            objects.mkdirs();
        }
        tree = new File("tree");
        if (!tree.exists()) {
            tree.createNewFile();
        } else {
            loadTreeContents(); // Load tree contents here.
        }
    }


    public void rename(File fileName) throws IOException 
    {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String str = "";

        while(br.ready()) {
            str += br.readLine()+"\n";
        }
        str = str.trim();//get rid of extra line

        br.close();

        //converting to sha1

        String sha1 = encryptPassword(str);

        //printing to objects folder
        String dirName = "./objects/";
        File dir = new File (dirName);//create this directory (File class java)
        //dir.mkdir();
        File newFile = new File (dir, sha1);

        PrintWriter pw = new PrintWriter(newFile);

        pw.print(str);

        pw.close();
    }

    public String encryptPassword(String password)
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
    
    public boolean deleteTree(String input) throws Exception 
    {
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

        boolean successful = tempFile.renameTo(inputFile);
        return successful;
    }


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
        // Method to load the Tree from a SHA1 file location
    public void loadFromSHA1(String fileLocation) throws IOException 
    {
        // Read the contents of the file
        String content = new String(Files.readAllBytes(Paths.get(fileLocation)));
    
        // Process the content to initialize the savedTree
        // This depends on how your Tree is structured and how the data in the file needs to be parsed
        // For example, if your Tree class has a method to add blobs from a string you would call it here
        this.parseAndAddBlobs(content);
    }
    
        // Helper method to parse the content of the file and add it to the Tree
    private void parseAndAddBlobs(String content) 
    {
        // Assuming the content is a list of blob entries separated by newlines
        String[] blobEntries = content.split("\\r?\\n");
        for (String blobEntry : blobEntries) 
        {
            // Here you would parse each blob entry and add it to the Tree
            // For example, if you just add the string to a list:
             this.blobList.add(blobEntry);
            // Or if you have a more complex structure, you'd parse the blob entry accordingly
        }
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