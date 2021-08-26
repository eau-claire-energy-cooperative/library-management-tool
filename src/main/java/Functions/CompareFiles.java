package Functions;

import Objects.ComparableFile;
import net.ricecode.similarity.StringSimilarityService;
import org.apache.commons.io.FileUtils;

import java.io.IOException;

public class CompareFiles implements Runnable {

    private boolean comparisonType;

    private final StringSimilarityService service;

    private final ComparableFile file1;
    private final ComparableFile file2;

    public CompareFiles(boolean compareNamesOnly, StringSimilarityService service, ComparableFile file1, ComparableFile file2) {
        this.comparisonType = compareNamesOnly;

        this.service = service;

        this.file1 = file1;
        this.file2 = file2;
    }

    public void run() {

        if (!this.comparisonType) {
            this.comparisonType = compareFileExtensions();
            if (!this.comparisonType) {
                compareBytes();
            }
        }
        compareNames();

    }

    public boolean compareFileExtensions() {

        String[] file1Split = this.file1.getFile().getName().split("\\.");
        String[] file2Split = this.file2.getFile().getName().split("\\.");

        return !file1Split[file1Split.length - 1].equals(file2Split[file2Split.length - 1]);

    }

    public void compareBytes() {

        try {
            if (FileUtils.contentEquals(this.file1.getFile(), this.file2.getFile())) {

                //Set flags to true to indicate files have been analyzed
                this.file1.setFlag(true);
                this.file2.setFlag(true);
                this.file2.setIdenticalPointer(this.file1.getIndex());

                System.out.println(this.file1.getFile().getName() + "  -=->  " + this.file2.getFile().getName());

            }
        } catch (IOException e) {
                e.printStackTrace();
        }

    }

    public void compareNames() {

        if(this.service.score(this.file1.getFile().getName(), this.file2.getFile().getName()) >= 0.8) {

            this.file1.setFlag(true);
            this.file2.setFlag(true);
            this.file2.setNameSimilarPointer(this.file1.getIndex());

            System.out.println(this.file1.getFile().getName() + "  -/->  " + this.file2.getFile().getName());

        }

    }


}
