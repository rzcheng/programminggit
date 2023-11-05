import java.util.*;
import java.io.*;
import java.security.MessageDigest;

public class Utils {
   public static String arrayListToFileFormat (ArrayList<String> list) 
   {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < list.size(); i++)
        {
            if (i == list.size() - 1)
            {
                sb.append(list.get(i));
            }
            else
            {
                sb.append(list.get(i) + "\n");
            }
        }


        return sb.toString();
   }


   public static String stringtoSHA (String input) throws Exception
   {
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

    public static void writeToFile (String file, String fileContent) throws Exception
    {
        File f = new File (file);
        f.createNewFile();

        FileWriter fw = new FileWriter(f);
        fw.write(fileContent);
        fw.close(); 
    }

    public static String readFromFile (String file) throws IOException
    {
        BufferedReader br = new BufferedReader (new FileReader (file));

        StringBuilder sb = new StringBuilder();
        while (br.ready())
        {
            sb.append((char) br.read());
        }

        br.close();
        return sb.toString(); 
    }
}
