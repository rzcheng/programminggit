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

    public boolean deleteTree(String input) throws IOException {
        /*Blob bl = new Blob(origFileName);
        String newFileName = bl.getSha1();

        String newEntry = origFileName + " : " + newFileName;*/
        
        File inputFile = new File("tree");
        File tempFile = new File("myTempFile.txt");
        String lineToRemove = "";
        
        if(!entryExists2(input, tree, lineToRemove)) {
            return false;
        }

        //removing line
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        
        String currentLine;
        
        while((currentLine = reader.readLine()) != null) {
            // trim newline when comparing with lineToRemove
            String trimmedLine = currentLine.trim();
            if(trimmedLine.equals(lineToRemove)) continue;
            writer.write(currentLine + System.getProperty("line.separator"));
        }
        writer.close(); 
        reader.close(); 
        boolean successful = tempFile.renameTo(inputFile);
        return successful;
    }

    private boolean entryExists2(String name, File tree2, String line) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(tree));
        while(br.ready()) {
            String str = br.readLine();
            if(str.contains(name)) {
                line = str;
                br.close();
                return true;
            }
        }
        br.close();
        return false;
    }


}