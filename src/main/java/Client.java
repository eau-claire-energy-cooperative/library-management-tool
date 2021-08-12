/**
 * This class is the main class from which to execute this program using command line arguments.
 */
public class Client {

    public static void main(String[] args) {

        LibraryManagementTool libraryManagementTool = new LibraryManagementTool();
        GUI gui = new GUI(libraryManagementTool);

    }

}
