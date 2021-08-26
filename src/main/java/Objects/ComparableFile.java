package Objects;

import java.io.File;

public class ComparableFile {

    private final File file;
    private boolean flag;
    private int identicalPointer;
    private int nameSimilarPointer;
    private int index;

    public ComparableFile(File file) {
        this.file = file;
        this.flag = false;
        this.identicalPointer = -1;
        this.nameSimilarPointer = -1;
    }

    public File getFile() { return file; }

    public boolean getFlag() { return flag; }

    public int getIdenticalPointer() { return identicalPointer; }

    public int getNameSimilarPointer() { return nameSimilarPointer; }

    public int getIndex() { return index; }

    public void setIndex(int index) { this.index = index; }

    public void setFlag(boolean flag) { this.flag = flag; }

    public void setIdenticalPointer(int identicalPointer) { this.identicalPointer = identicalPointer; }

    public void setNameSimilarPointer(int nameSimilarPointer) { this.nameSimilarPointer = nameSimilarPointer; }

}
