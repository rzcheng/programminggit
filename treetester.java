import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class treetester {
    public static void main (String[] args) throws Exception {
        //Blob test = new Blob("example.txt");

        /*
        Tree test1 = new Tree();
        test1.initialize();

        String input1 = "blob : 732d12f7e4f2e629e2954acbb720c32c0be985d1 : file1";
        String input2 = "tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b";
        String input3 = "blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file2.txt";
        String input4 = "blob : 01d82591292494afd1602d175e165f94992f6f5f : file2";
        String input5 = "tree : e7d79898d3342fd15daf6ec36f4cb095b52fd976";

        System.out.println("\nADDED? " + test1.addTree(input1));
        System.out.println("\nADDED? " + test1.addTree(input2));
        System.out.println("\nADDED? " + test1.addTree(input2));
        System.out.println("\nADDED? " + test1.addTree(input3));
        System.out.println("\nADDED? " + test1.addTree(input4));
        System.out.println("\nADDED? " + test1.addTree(input5));

        //System.out.println("\nREMOVED? " + test1.deleteTree("file2.txt"));
        System.out.println("\nREMOVED? " + test1.deleteTree("file1"));
        //System.out.println("\nREMOVED? " + test1.deleteTree("bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b"));//RIGHT, it removed the tree
        //System.out.println("\nREMOVED? " + test1.deleteTree("tree : e7d79898d3342fd15d"));
        */

        Tree22222 test1 = new Tree22222();
        test1.initialize();

        String input1 = "blob : 732d12f7e4f2e629e2954acbb720c32c0be985d1 : file1";
        String input2 = "tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b";
        String input3 = "blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file2.txt";
        String input4 = "blob : 01d82591292494afd1602d175e165f94992f6f5f : file2";
        String input5 = "tree : e7d79898d3342fd15daf6ec36f4cb095b52fd976";

        System.out.println("\nADDED? " + test1.addTree(input1));
        System.out.println("\nADDED? " + test1.addTree(input2));
        System.out.println("\nADDED? " + test1.addTree(input2));
        System.out.println("\nADDED? " + test1.addTree(input3));
        System.out.println("\nADDED? " + test1.addTree(input4));
        System.out.println("\nADDED? " + test1.addTree(input5));

        //System.out.println("\nREMOVED? " + test1.deleteTree("file2.txt"));
        //System.out.println("\nREMOVED? " + test1.deleteTree("file1"));
        //System.out.println("\nREMOVED? " + test1.deleteTree("bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b"));//RIGHT, it removed the tree
        //System.out.println("\nREMOVED? " + test1.deleteTree("tree : e7d79898d3342fd15d"));
    }
}
