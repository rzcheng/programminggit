import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Tree {
    private File tree = new File ("tree");

    public Tree () throws IOException {
        if(!tree.exists()) {
            tree.createNewFile();
        }

        String dirName = "./objects/";
        File dir = new File (dirName);
        dir.mkdir();

    }

    

}