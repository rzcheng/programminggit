import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;

public class Utils {
    public static String arrayListToFileFormat(ArrayList<String> list) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                sb.append(list.get(i));
            } else {
                sb.append(list.get(i) + "\n");
            }
        }

        return sb.toString();
    }

    public static String stringtoSHA(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(input.getBytes());
        byte[] b = md.digest();

        char hexDigit[] = { '0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        StringBuffer buf = new StringBuffer();
        for (int j = 0; j < b.length; j++) {
            buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
            buf.append(hexDigit[b[j] & 0x0f]);
        }
        return buf.toString();
    }

    public static void writeToFile(String file, String fileContent) throws Exception {
        File f = new File(file);
        f.createNewFile();

        FileWriter fw = new FileWriter(f);
        fw.write(fileContent);
        fw.close();
    }

    public static String readFromFile(String file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));

        StringBuilder sb = new StringBuilder();
        while (br.ready()) {
            sb.append((char) br.read());
        }

        br.close();
        return sb.toString();
    }

    public static void writeFileLine(String filePath, int lineNumber, String newLineContent) throws Exception {
        Path path = Paths.get(filePath);
        List<String> fileContent = new ArrayList<>(Files.readAllLines(path, StandardCharsets.UTF_8));

        // Check if the line number is valid
        if (lineNumber < 1 || lineNumber > fileContent.size()) {
            throw new IllegalArgumentException("Line number is out of range: " + lineNumber);
        }

        // Replace the line with new text
        fileContent.set(lineNumber - 1, newLineContent);

        // Write the new content back to the file without an extra new line at the end
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            for (int i = 0; i < fileContent.size(); i++) {
                writer.write(fileContent.get(i));
                // Add a new line for all but the last line
                if (i < fileContent.size() - 1) {
                    writer.newLine();
                }
            }
        }
    }
}
