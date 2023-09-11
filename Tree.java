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

    public boolean addTree(String name) throws IOException {
       if(!entryExists(name, tree)) {
            PrintWriter pw = new PrintWriter(new FileWriter(tree, true));
            pw.append(name + "\n");
            pw.close();
            return true;
        }
        return false;
    }

    private boolean entryExists(String name, File tree2) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(tree));
        while(br.ready()) {
            String str = br.readLine();
            if(str.equals(name)) {
                br.close();
                return true;
            }
        }
        br.close();
        return false;
    }

    

}