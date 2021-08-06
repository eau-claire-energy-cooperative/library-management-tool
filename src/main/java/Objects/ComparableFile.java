package Objects;

import java.io.File;

public class ComparableFile {

    private final File file;
    private boolean flag;
    private int pointer;
    private int index;

    public ComparableFile(File file) {
        this.file = file;
        this.flag = false;
        this.pointer = -1;
    }

    public File getFile() { return file; }

    public boolean getFlag() { return flag; }

    public int getPointer() { return pointer; }

    public int getIndex() { return index; }

    public void setIndex(int index) { this.index = index; }

    public void setFlag(boolean flag) { this.flag = flag; }

    public void setPointer(int pointer) { this.pointer = pointer; }

}
