package Functions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class ArchiveFiles {

    private final String sourcePath;
    private final String destPath;
    private final Date dateThreshold;

    public ArchiveFiles(String sourcePath, String destPath, Date dateThreshold) {
        this.sourcePath = sourcePath;
        this.destPath = destPath;
        this.dateThreshold = dateThreshold;
    }

    /**
     * Walks through every directory and file contained within a location specified by a file path,
     * and moves files last modified before the date threshold to their equivalent location in
     * the specified archive.
     * @param path The location from which to walk through all contents.
     * @return The number of files archived.
     * @throws IOException Normal IO exception.
     */
    public int walk(String path) throws IOException {

        File root = new File(path);
        File[] list = root.listFiles();

        if (list == null) return 0;

        int count = 0;
        for(File file : list) {

            if(file.isDirectory()) {
                count = count + walk(file.getAbsolutePath());
            } else {

                if(new Date(file.lastModified()).compareTo(this.dateThreshold) < 0) {
                    Files.move(Paths.get(file.getAbsolutePath()), Paths.get(this.destPath + file.getAbsolutePath().substring(this.sourcePath.length())), REPLACE_EXISTING);
                    count++;
                    System.out.println(count + " --- " + Paths.get(this.destPath + file.getAbsolutePath().substring(this.sourcePath.length())));
                }

            }

        }

        return count;

    }

}