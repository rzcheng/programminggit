import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Blob
 */
public class Blob {

    public Blob (String filePath) throws Exception {

        StringBuilder builder = new StringBuilder();

        BufferedReader buffer = new BufferedReader (new FileReader(filePath));
 
            String str;
            while ((str = buffer.readLine()) != null) {
                builder.append(str).append("\n");
            }

            buffer.close();

            String hash = encryptThisString(builder.toString());

            File file = new File("./objects/" + hash);
            file.createNewFile();
 
    }

    public static String encryptThisString(String input) {
        try {
            // getInstance() method is called with algorithm SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}