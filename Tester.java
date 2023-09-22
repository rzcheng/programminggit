public class Tester {
    public static void main(String[] args) throws Exception {

        // Blob blob = new Blob("something.txt");
        // Blob blob2 = new Blob("example.txt");

        //System.out.println(blob2.getEncryption());
        /*
        Index index = new Index();

        index.addBlob("example.txt");
        index.addBlob("example.txt");

        index.deleteBlob("example.txt");
        index.addBlob("something.txt");

        System.out.println("hi");
        */
        Commit myCommit = new Commit ("djkejklj2892820829042069", "chrisdagoat", "thebestcommit");
        myCommit.writeToFile ();
    }
}
