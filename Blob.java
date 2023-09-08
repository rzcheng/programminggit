import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.*;

/**
 * Blob
 */
public class Blob {

    String encryption;

    public Blob(String filePath) throws Exception {

        StringBuilder builder = new StringBuilder();

        BufferedReader buffer = new BufferedReader(new FileReader(filePath));

        while (buffer.ready()) {
            builder.append((char) buffer.read());
        }

        buffer.close();

        encryption = encryptThisString(builder.toString());

        File file = new File("./objects/" + encryption);
        file.createNewFile();

    }

    public static String encryptThisString(String input) {
        
        try {

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

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String getEncryption() {
        return encryption;
    }

}