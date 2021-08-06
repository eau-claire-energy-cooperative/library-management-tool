import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * This class is the main class from which to execute this program using command line arguments.
 */
public class Client {

    public static void main(String[] args) throws IOException, ParseException {

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        LibraryManagementTool libraryManagementTool = new LibraryManagementTool();

        switch (args[0]) {
            case "Gen_Arch", "1" -> libraryManagementTool.generateArchive(args[1] + "\\", args[2] + "\\");
            case "Arch_Files", "2" -> libraryManagementTool.archiveFiles(args[1] + "\\", args[2] + "\\", simpleDateFormat.parse(args[3]));
            case "Gen_SS", "3" -> {
                if(args.length > 5) {
                    libraryManagementTool.generateSpreadsheet(args[1] + "\\", args[2], simpleDateFormat.parse(args[3]), Integer.parseInt(args[4]), simpleDateFormat.parse(args[5]), simpleDateFormat.parse(args[6]), simpleDateFormat.parse(args[7]));
                } else {
                    libraryManagementTool.generateSpreadsheet(args[1] + "\\", args[2], simpleDateFormat.parse(args[3]), Integer.parseInt(args[4]), null, null, null);
                }
            }
            case "Del_Empt_Dirs", "4" -> libraryManagementTool.deleteEmptyDirectories(args[1] + "\\");
            case "Del_File_By_Name", "5" -> libraryManagementTool.deleteFileByName(args[1] + "\\", args[2]);
        }

    }

}
