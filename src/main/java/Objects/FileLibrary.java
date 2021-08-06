package Objects;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class FileLibrary {

    private final ArrayList<File> filesArrayList;
    private final ArrayList<ComparableFile> comparableFilesArrayList;
    private final ArrayList<ArrayList<ComparableFile>> identicalSets;

    public FileLibrary() {
        this.filesArrayList = new ArrayList<>();
        this.comparableFilesArrayList = new ArrayList<>();
        this.identicalSets = new ArrayList<>();
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
    public void performComparisons(int numThreads) {

        MyThread[] threads = new MyThread[numThreads];
        int threadIndex = 0;

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
                        if (threads[threadIndex % numThreads] == null || !threads[threadIndex % numThreads].isAlive()) {
                            threads[threadIndex % numThreads] = new MyThread(FileLibrary.this.comparableFilesArrayList.get(i), FileLibrary.this.comparableFilesArrayList.get(j));
                            threads[threadIndex % numThreads].start();
                            threadIndex++;
                            //Otherwise, perform comparison in main thread
                        } else {

                            //Checks file extensions
                            if (FileLibrary.this.comparableFilesArrayList.get(i).getFile().getName().split("\\.")[FileLibrary.this.comparableFilesArrayList.get(i).getFile().getName().split("\\.").length - 1].equals(FileLibrary.this.comparableFilesArrayList.get(j).getFile().getName().split("\\.")[FileLibrary.this.comparableFilesArrayList.get(j).getFile().getName().split("\\.").length - 1])) {

                                //Compares byte content
                                try {
                                    if (FileUtils.contentEquals(FileLibrary.this.comparableFilesArrayList.get(i).getFile(), FileLibrary.this.comparableFilesArrayList.get(j).getFile())) {

                                        //Set flags to true to indicate files have been analyzed
                                        FileLibrary.this.comparableFilesArrayList.get(i).setFlag(true);
                                        FileLibrary.this.comparableFilesArrayList.get(j).setFlag(true);
                                        FileLibrary.this.comparableFilesArrayList.get(j).setPointer(FileLibrary.this.comparableFilesArrayList.get(i).getIndex());

                                        System.out.println(FileLibrary.this.comparableFilesArrayList.get(i).getFile().getName() + "  ->  " + FileLibrary.this.comparableFilesArrayList.get(j).getFile().getName());

                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

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
            if(FileLibrary.this.comparableFilesArrayList.get(i).getPointer() > -1 && FileLibrary.this.comparableFilesArrayList.get(i).getFlag()) {

                //Create new identicalSet ArrayList<> and add pointing and pointed at files
                identicalSets.add(new ArrayList<>());
                identicalSets.get(identicalSets.size() - 1).add(FileLibrary.this.comparableFilesArrayList.get(FileLibrary.this.comparableFilesArrayList.get(i).getPointer()));
                identicalSets.get(identicalSets.size() - 1).add(FileLibrary.this.comparableFilesArrayList.get(i));
                //Sets index to that of identical ComparableFile
                index = FileLibrary.this.comparableFilesArrayList.get(i).getPointer();

                //For each subsequent ComparableFile forward
                for(int j = i + 1; j <= FileLibrary.this.comparableFilesArrayList.size() - 1; j++) {

                    //Checks if each pointer directs toward the index, and if it has not already been analyzed.
                    if(FileLibrary.this.comparableFilesArrayList.get(j).getPointer() == index && FileLibrary.this.comparableFilesArrayList.get(j).getFlag()) {

                        //Adds ComparableFile to identicalSet, and sets flag to false
                        identicalSets.get(identicalSets.size() - 1).add(FileLibrary.this.comparableFilesArrayList.get(j));
                        FileLibrary.this.comparableFilesArrayList.get(j).setFlag(false);

                    }

                }

            }

        }

        //Prints identicalSets
        for(int i = 0; i <= identicalSets.size() - 1; i++) {

            System.out.print("[" + identicalSets.get(i).get(0).getFile().getName());
            for(int j = 1; j <= identicalSets.get(i).size() - 1; j++) {

                System.out.print(" -> " + identicalSets.get(i).get(j).getFile().getName());

            }
            System.out.print("]\n");

        }

    }

    public ArrayList<File> getFilesArrayList() { return filesArrayList; }

    public ArrayList<ArrayList<ComparableFile>> getIdenticalSets() { return identicalSets; }

}