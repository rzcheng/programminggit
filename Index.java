import java.util.*;
import java.io.*;

/**
 * Index
 */
public class Index {

    HashMap<String, String> hashes;

    public Index() 
    {
        hashes = new HashMap<String, String>();
    }

    public void initialize() throws Exception 
    {

        File objects = new File("./objects");
        if (!objects.exists())
            objects.mkdirs();

        File index = new File("./index");
        if (!index.exists())
            index.createNewFile();

    }

    // This method updates an existing blob in the index for edited files.
    public void editBlob(String filename) throws Exception {
        // Check if the file already exists in the hash map
        for (String key : hashes.keySet()) {
            if (hashes.get(key).equals(filename)) {
                // File definitely should exist, let's update the entry
                Blob blob = new Blob(filename);
                hashes.put("blob : " + blob.getEncryption(), filename); // Update with new SHA1
                writeToIndex();
                return;
            }
        }
        // If we reached here, it means the file was not in the index, hence it's not an edit but an addition
        addBlob(filename);
    }

    // This method marks a file as deleted in the index.
    public void deleteBlobMark(String filename) throws Exception {
        // The 'filename' should be the original name of the file without any *deleted* prefix
        hashes.put("*deleted* " + filename, null); // We store null as the value to indicate deletion
        writeToIndex();
    }

    public void addBlob(String filename) throws Exception {

        Blob blob = new Blob(filename);
        hashes.put("blob : " + blob.getEncryption(), filename);

        writeToIndex();

    }

    public void deleteBlob(String filename) throws Exception {

        File tempFile = new File(filename);

        if (tempFile.exists()) {

            hashes.remove(filename);
            writeToIndex();

        } else {
            System.out.println("file does not exist");
        }

    }

    public void writeToIndex() throws Exception {

        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("index")));

        for (String key : hashes.keySet()) {

            String hash = hashes.get(key);
            writer.println(key + " : " + hash);

        }

        writer.close();
    }
    // Method to get the entire hashes map
    public HashMap<String, String> getFiles() 
    {
        return hashes;
    }

    public void addDirectory (String folderName) throws Exception
    {
        Tree t = new Tree ();
        t.addDirectory(folderName);
        t.saveToObjects(); 

        String entry = folderName + " : " + t.getEncryption();

        boolean contains = false;
        for (String key : hashes.keySet()) {
            if (hashes.get(key).equals(entry)) {
                contains = true;
            }
        }

        if (!contains) {
            hashes.put("tree: ", entry);
        }
        writeToIndex(); 
        
    }

}