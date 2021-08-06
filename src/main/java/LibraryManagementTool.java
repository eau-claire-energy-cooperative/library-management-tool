import Functions.*;
import Objects.FileLibrary;
import Objects.Spreadsheet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class is the class from which the functionality of this program is performed.
 * It contains one method for each function, and manages the objects which perform them.
 */
public class LibraryManagementTool {

    public LibraryManagementTool() {}

    /**
     * Method for Generate Archive function ("Gen_Arch", "1")
     * @param sourcePath The path of the source location of which an archive structure is being created.
     * @param destPath The path of the output archive.
     * @throws IOException Normal IO Exception.
     */
    public void generateArchive(String sourcePath, String destPath) throws IOException {

        GenerateArchive generateArchive = new GenerateArchive(sourcePath, destPath);
        System.out.println(generateArchive.walk(sourcePath) + " archive folders created");

    }

    /**
     * Method for Archive Files function ("Arch_Files", "2")
     * @param sourcePath The path of the source location from which files are being archived.
     * @param destPath The path of the archive location to which files are being archived.
     * @param dateThreshold The date through which files last modified by are archived.
     * @throws IOException Normal IO Exception.
     */
    public void archiveFiles(String sourcePath, String destPath, Date dateThreshold) throws IOException {

        ArchiveFiles archiveFiles = new ArchiveFiles(sourcePath, destPath, dateThreshold);
        System.out.println(archiveFiles.walk(sourcePath) + " total files archived");

    }

    /**
     * Method for Generate Spreadsheet function ("Gen_SS", "3")
     * @param sourcePath The path of the source location of which files are being analyzed.
     * @param destPath The path to the output .xlsx spreadsheet.
     * @param dateThreshold The date through which files last modified by are considered to be "old".
     * @param numThreads The number of threads to use for file comparisons.
     * @param bottomDateThreshold The date through which files are highlighted red and considered oldest.
     * @param middleDateThreshold The date through which files are highlighted orange and considered second oldest.
     * @param topDateThreshold The date through which files are highlighted yellow and considered least oldest.
     */
    public void generateSpreadsheet(String sourcePath, String destPath, Date dateThreshold, int numThreads, Date bottomDateThreshold, Date middleDateThreshold, Date topDateThreshold) {

        FileLibrary fileLibrary = new FileLibrary();
        Spreadsheet spreadsheet = new Spreadsheet();
        GenerateSpreadsheet generateSpreadsheet = new GenerateSpreadsheet(spreadsheet);

        System.out.println(fileLibrary.walk(sourcePath) + " total files accounted");
        generateSpreadsheet.fillSpreadsheetWithList(fileLibrary.getFilesArrayList(), "All files");
        if(bottomDateThreshold != null && middleDateThreshold != null && topDateThreshold != null) {
            generateSpreadsheet.setConditionalFormatting(0, bottomDateThreshold, middleDateThreshold, topDateThreshold);
        }

        ArrayList<File> oldFilesArrayList = new ArrayList<>();
        for(int i = 0; i <= fileLibrary.getFilesArrayList().size() - 1; i++) {
            if(new Date(fileLibrary.getFilesArrayList().get(i).lastModified()).compareTo(dateThreshold) < 0) {
                oldFilesArrayList.add(fileLibrary.getFilesArrayList().get(i));
            }
        }
        generateSpreadsheet.fillSpreadsheetWithList(oldFilesArrayList, "Old files");
        if(bottomDateThreshold != null && middleDateThreshold != null && topDateThreshold != null) {
            generateSpreadsheet.setConditionalFormatting(1, bottomDateThreshold, middleDateThreshold, topDateThreshold);
        }

        fileLibrary.performComparisons(numThreads);
        fileLibrary.generateResults();
        generateSpreadsheet.fillSpreadsheetWithSets(fileLibrary.getIdenticalSets(), "Duplicate files");

        generateSpreadsheet.saveSpreadsheet(destPath);

    }

    /**
     * Method for Delete Empty Directories function ("Del_Empt_Dirs", "4")
     * @param sourcePath The path of the source location of which folders are being deleted.
     * @throws IOException Normal IO Exception.
     */
    public void deleteEmptyDirectories(String sourcePath) throws IOException {

        DeleteEmptyDirectories deleteEmptyDirectories = new DeleteEmptyDirectories();
        deleteEmptyDirectories.walk(sourcePath);

    }

    /**
     * Method for Delete Files By Name function ("Del_File_By_Name", "5")
     * @param path The path of the source location of which files are being deleted.
     * @param fileName The name of the file to delete.
     * @throws IOException Normal IO Exception.
     */
    public void deleteFileByName(String path, String fileName) throws IOException {

        DeleteFileByName deleteFileByName = new DeleteFileByName();
        System.out.println(deleteFileByName.walk(path, fileName) + " total files deleted");

    }

}
