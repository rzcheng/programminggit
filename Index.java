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
        try {
            File objects = new File("./objects");
            objects.mkdirs();
        }
    }
    
}