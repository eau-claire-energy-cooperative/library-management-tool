package Objects;

import org.apache.commons.io.FileUtils;

import java.io.IOException;

public class MyThread extends Thread {

    private final ComparableFile comparableFile1;
    private final String[] comparableFile1StringArray;
    private final ComparableFile comparableFile2;
    private final String[] comparableFile2StringArray;

    public MyThread(ComparableFile comparableFile1, ComparableFile comparableFile2) {

        this.comparableFile1 = comparableFile1;
        this.comparableFile1StringArray = comparableFile1.getFile().getName().split("\\.");
        this.comparableFile2 = comparableFile2;
        this.comparableFile2StringArray = comparableFile2.getFile().getName().split("\\.");

    }

    /**
     * Compares two File objects using their file extensions, and their byte contents.
     */
    @Override
    public void start() {

        //Checks file extensions
        if (this.comparableFile1StringArray[this.comparableFile1StringArray.length - 1].equals(this.comparableFile2StringArray[this.comparableFile2StringArray.length - 1])) {

            //Compares byte content
            try {
                if (FileUtils.contentEquals(this.comparableFile1.getFile(), this.comparableFile2.getFile())) {

                    //Set flags to true to indicate files have been analyzed
                    comparableFile1.setFlag(true);
                    comparableFile2.setFlag(true);
                    comparableFile2.setPointer(comparableFile1.getIndex());

                    System.out.println(comparableFile1.getFile().getName() + "  ->  " + comparableFile2.getFile().getName());

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}