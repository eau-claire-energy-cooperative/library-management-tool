package Functions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DeleteEmptyDirectories {

    public DeleteEmptyDirectories() {}

    /**
     * Walks through every directory within a location specified by a file path,
     * and deletes empty directories.
     * @param path The location from which to walk through all contents.
     * @return The number of files archived.
     * @throws IOException Normal IO exception.
     */
    public int walk(String path) throws IOException {

        File root = new File(path);
        File[] list = root.listFiles();
        int count = 0;
        boolean successfulDeletion;

        if (list == null) {
            System.out.print("Attempting to delete " + root.getName() + "... ");
            successfulDeletion = Files.deleteIfExists(Paths.get(root.getAbsolutePath()));
            if(successfulDeletion) {
                System.out.print("success!\n");
            } else {
                System.out.print("failed\n");
            }
            return 0;
        }

        for(File file : list) {

            if(file.isDirectory()) {

                count = count + walk(file.getAbsolutePath());

            } else {
                count++;
            }

        }

        if(count == 0) {
            System.out.print("Attempting to delete " + root.getName() + "... ");
            successfulDeletion = Files.deleteIfExists(Paths.get(root.getAbsolutePath()));
            if(successfulDeletion) {
                System.out.print("success!\n");
            } else {
                System.out.print("failed\n");
            }
        }

        return count;

    }

}