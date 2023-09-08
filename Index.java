import java.io.File;
import java.util.*;
import java.io.*;
/**
 * Index
 */
public class Index {

    HashMap<String, String> hashes;

    public Index () {
        hashes = new HashMap<String, String>();
    }

    public void initialize () throws Exception {

            File objects = new File("./objects");
            if (!objects.exists())
                objects.mkdirs();
        
            File index = new File("index");
            if (!index.exists())
                index.createNewFile();

    }
    
    public void addBlob (String filename) throws Exception {
        Blob blob = new Blob(filename);
        hashes.put(filename, blob.getEncryption());
    }

    public void writeToIndex() {
        
    }
}