package Objects;

import Functions.CompareFiles;
import net.ricecode.similarity.JaroWinklerStrategy;
import net.ricecode.similarity.SimilarityStrategy;
import net.ricecode.similarity.StringSimilarityServiceImpl;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class FileLibrary {

    private final ArrayList<File> filesArrayList;
    private final ArrayList<ComparableFile> comparableFilesArrayList;
    private final ArrayList<ArrayList<ComparableFile>> identicalSets;
    private final ArrayList<ArrayList<ComparableFile>> similarNameSets;

    public FileLibrary() {
        this.filesArrayList = new ArrayList<>();
        this.comparableFilesArrayList = new ArrayList<>();
        this.identicalSets = new ArrayList<>();
        this.similarNameSets = new ArrayList<>();
    }

    /**
     * Walks through every directory and file contained within a location specified by a file path,
     * and adds them to an ArrayList of File objects.
     * @param path The location from which to walk through all contents.
     * @return The number of files (not folders) contained and added to the ArrayList.
     */
    public int walk(String path) {

        File root = new File(path);
        File[] list = root.listFiles();

        if (list == null) return 0;

        int count = 0;
        for(File file : list) {

            if(file.isDirectory()) {
                count = count + walk(file.getAbsolutePath());
            } else {
                this.filesArrayList.add(file);
                FileLibrary.this.comparableFilesArrayList.add(new ComparableFile(file));
                FileLibrary.this.comparableFilesArrayList.get(FileLibrary.this.comparableFilesArrayList.size() - 1).setIndex(FileLibrary.this.comparableFilesArrayList.size() - 1);
                System.out.println(count + " --- " + file.getAbsoluteFile());
                count++;
            }

        }

        return count;

    }

    /**
     * Compares files from ComparableFile ArrayList forward using threading.
     * @param numThreads The number of threads a user wants to utilize for comparisons.
     */
    public void performComparisons(int numThreads, boolean compareNamesOnly) {

        Thread[] threads = new Thread[numThreads];
        int threadIndex = 0;
        SimilarityStrategy strategy = new JaroWinklerStrategy();

        //For each ComparableFile
        for(int i = 0; i <= FileLibrary.this.comparableFilesArrayList.size() - 1; i++) {

            System.out.println((i + 1) + "/" + FileLibrary.this.comparableFilesArrayList.size() + " -- " + "Checking " + FileLibrary.this.comparableFilesArrayList.get(i).getFile().getAbsolutePath() + "..." + " " + LocalDateTime.now());
            //If ComparableFile flag is not true (has not already been analyzed)
            if(!FileLibrary.this.comparableFilesArrayList.get(i).getFlag()) {

                //For each subsequent ComparableFile forward
                for (int j = i + 1; j <= FileLibrary.this.comparableFilesArrayList.size() - 1; j++) {

                    //If ComparableFile flag is not true (has not already been analyzed)
                    if(!FileLibrary.this.comparableFilesArrayList.get(j).getFlag()) {

                        //Checks if threads are available
                        while(true) {
                            if (threads[threadIndex % numThreads] == null || !threads[threadIndex % numThreads].isAlive()) {
                                threads[threadIndex % numThreads] = new Thread(new CompareFiles(compareNamesOnly, new StringSimilarityServiceImpl(strategy), FileLibrary.this.comparableFilesArrayList.get(i), FileLibrary.this.comparableFilesArrayList.get(j)));
                                threads[threadIndex % numThreads].start();
                                threadIndex++;
                                break;
                            }
                        }

                    }

                }

            }

        }

    }

    /**
     * Generates ArrayList of ArrayLists of comparableFilesArrayList; each ArrayList is a set of identical files.
     */
    public void generateResults() {

        //Create new identicalSets ArrayList<ArrayList<>>
        int index;

        //For each ComparableFile
        for(int i = 0; i <= FileLibrary.this.comparableFilesArrayList.size() - 1; i++) {

            //Checks if it has a pointer to identical file, and if it has not already been analyzed
            //Now, comparableFilesArrayList that have already been analyzed have a flag value of false
            if(FileLibrary.this.comparableFilesArrayList.get(i).getIdenticalPointer() > -1 && FileLibrary.this.comparableFilesArrayList.get(i).getFlag()) {

                //Create new identicalSet ArrayList<> and add pointing and pointed at files
                identicalSets.add(new ArrayList<>());
                identicalSets.get(identicalSets.size() - 1).add(FileLibrary.this.comparableFilesArrayList.get(FileLibrary.this.comparableFilesArrayList.get(i).getIdenticalPointer()));
                identicalSets.get(identicalSets.size() - 1).add(FileLibrary.this.comparableFilesArrayList.get(i));
                //Sets index to that of identical ComparableFile
                index = FileLibrary.this.comparableFilesArrayList.get(i).getIdenticalPointer();

                //For each subsequent ComparableFile forward
                for(int j = i + 1; j <= FileLibrary.this.comparableFilesArrayList.size() - 1; j++) {

                    //Checks if each pointer directs toward the index, and if it has not already been analyzed.
                    if(FileLibrary.this.comparableFilesArrayList.get(j).getIdenticalPointer() == index && FileLibrary.this.comparableFilesArrayList.get(j).getFlag()) {

                        //Adds ComparableFile to identicalSet, and sets flag to false
                        identicalSets.get(identicalSets.size() - 1).add(FileLibrary.this.comparableFilesArrayList.get(j));
                        FileLibrary.this.comparableFilesArrayList.get(j).setFlag(false);

                    }

                }

            } else if(FileLibrary.this.comparableFilesArrayList.get(i).getNameSimilarPointer() > -1 && FileLibrary.this.comparableFilesArrayList.get(i).getFlag()) {

                //Create new similarNameSet ArrayList<> and add pointing and pointed at files
                similarNameSets.add(new ArrayList<>());
                similarNameSets.get(similarNameSets.size() - 1).add(FileLibrary.this.comparableFilesArrayList.get(FileLibrary.this.comparableFilesArrayList.get(i).getNameSimilarPointer()));
                similarNameSets.get(similarNameSets.size() - 1).add(FileLibrary.this.comparableFilesArrayList.get(i));
                //Sets index to that of similar name ComparableFile
                index = FileLibrary.this.comparableFilesArrayList.get(i).getNameSimilarPointer();

                //For each subsequent ComparableFile forward
                for(int j = i + 1; j <= FileLibrary.this.comparableFilesArrayList.size() - 1; j++) {

                    //Checks if each pointer directs toward the index, and if it has not already been analyzed.
                    if(FileLibrary.this.comparableFilesArrayList.get(j).getNameSimilarPointer() == index && FileLibrary.this.comparableFilesArrayList.get(j).getFlag()) {

                        //Adds ComparableFile to identicalSet, and sets flag to false
                        similarNameSets.get(similarNameSets.size() - 1).add(FileLibrary.this.comparableFilesArrayList.get(j));
                        FileLibrary.this.comparableFilesArrayList.get(j).setFlag(false);

                    }

                }

            }

        }

        //Prints identicalSets
        for(int i = 0; i <= identicalSets.size() - 1; i++) {

            System.out.print("[" + identicalSets.get(i).get(0).getFile().getName());
            for(int j = 1; j <= identicalSets.get(i).size() - 1; j++) {

                System.out.print(" -=-> " + identicalSets.get(i).get(j).getFile().getName());

            }
            System.out.print("]\n");

        }

        //Prints similarNameSets
        for(int i = 0; i <= similarNameSets.size() - 1; i++) {

            System.out.print("[" + similarNameSets.get(i).get(0).getFile().getName());
            for(int j = 1; j <= similarNameSets.get(i).size() - 1; j++) {

                System.out.print(" -/-> " + similarNameSets.get(i).get(j).getFile().getName());

            }
            System.out.print("]\n");

        }

    }

    public ArrayList<File> getFilesArrayList() { return filesArrayList; }

    public ArrayList<ArrayList<ComparableFile>> getIdenticalSets() { return identicalSets; }

    public ArrayList<ArrayList<ComparableFile>> getSimilarNameSets() { return similarNameSets; }

}