import Objects.FileLibrary;

public class test {

    public static void main(String[] args) {

        FileLibrary fileLibrary = new FileLibrary();
        fileLibrary.walk("/Users/spencerkeith/Desktop/Test");

        fileLibrary.performComparisons(4, false);
        fileLibrary.generateResults();

    }

}
