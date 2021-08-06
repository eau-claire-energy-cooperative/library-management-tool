package Functions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GenerateArchive {

    private final String sourcePath;
    private final String destPath;

    public GenerateArchive(String sourcePath, String destPath) {
        this.sourcePath = sourcePath;
        this.destPath = destPath;
    }

    /**
     * Walks through every directory contained within a location specified by a file path,
     * and generates an equivalent folder structure in a specified archive location.
     * @param path The location from which to walk through all contents.
     * @return The number of folders created in the archive location.
     * @throws IOException Normal IO exception.
     */
    public int walk(String path) throws IOException {

        Files.createDirectories(Paths.get(this.destPath));

        File root = new File(path);
        File[] list = root.listFiles();

        if (list == null) return 0;

        int count = 0;
        for(File file : list) {

            if(file.isDirectory()) {
                Files.createDirectories(Paths.get(this.destPath + file.getAbsolutePath().substring(this.sourcePath.length())));
                count++;
                System.out.println(count + " --- " + Paths.get(this.destPath + file.getAbsolutePath().substring(this.sourcePath.length())));
                count = count + walk(file.getAbsolutePath());
            }

        }

        return count;

    }

}