import java.util.*;
import java.io.*;

/**
 * Index
 */
public class Index {

    HashMap<String, String> hashes;

    public Index() {
        hashes = new HashMap<String, String>();
    }

    public void initialize() throws Exception {

        File objects = new File("./objects");
        if (!objects.exists())
            objects.mkdirs();

        File index = new File("732d12f7e4f2e629e2954acbb720c32c0be985d1");
        if (!index.exists())
            index.createNewFile();

    }

    public void addBlob(String filename) throws Exception {

        Blob blob = new Blob(filename);
        hashes.put(filename, blob.getEncryption());

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

}