package Functions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DeleteFileByName {

    public DeleteFileByName() {}

    /**
     * Walks through every directory within a location specified by a file path,
     * and deletes all instances of a file with a given name.
     * @param path The location from which to walk through all contents.
     * @param fileName The name of the file to be deleted.
     * @return The number of files traversed.
     * @throws IOException Normal IO exception.
     */
    public int walk(String path, String fileName) throws IOException {

        File root = new File(path);
        File[] list = root.listFiles();
        boolean deletedSuccessfully;

        if (list == null) return 0;

        int count = 0;
        for(File file : list) {

            if(file.getName().equals(fileName)) {

                System.out.print(count + " --- Attempting to delete " + file.getAbsolutePath() + "... ");
                deletedSuccessfully = Files.deleteIfExists(Paths.get(file.getAbsolutePath()));

                if(deletedSuccessfully) {
                    System.out.print("success!\n");
                    count++;
                } else {
                    System.out.print("failed\n");
                }

            } else if(file.isDirectory()) {
                count = count + walk(file.getAbsolutePath(), fileName);
            }

        }

        return count;

    }

}
