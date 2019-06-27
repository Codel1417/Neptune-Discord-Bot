package Neptune;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;

class FolderSearch {
    private final static Logger Log = Logger.getLogger(FolderSearch.class.getName());

    private ArrayList<File> folders = new ArrayList<>();
    private HashMap<File, File[]> folderContents = new HashMap<>();

    FolderSearch(File FileDirectory) {
        getfolderList(FileDirectory);
        generateFileList(folders);
    }
    private ArrayList<File> getfolderList(File FileDirectory) {
        //Generates the folder List
        for (File file : Objects.requireNonNull(FileDirectory.listFiles())) {
            if (file.isDirectory() && Objects.requireNonNull(file.listFiles()).length != 0){ //checks for empty folder
                folders.add(file);
            }
        }
        return folders;
    }

    //moves through an array of folders and pre-generates the file list
    private void generateFileList(ArrayList<File> folders){
        folderContents.clear(); //resets the hashmap
        Log.info("Custom Sound List");
        StringBuilder stringBuilder = new StringBuilder();
        for (File folder : folders) {
            folderContents.put(folder, folder.listFiles());
            stringBuilder.append(folder.getName()).append(":").append(Objects.requireNonNull(folder.listFiles()).length).append("\n");
        }
        Log.info(stringBuilder.toString());
    }
    //returns an array of the folder contents. Pre-generated to reduce file io.
    File getFolder(File folder){
        for (Map.Entry<File, File[]> file : folderContents.entrySet()) {
            if(file.getKey().getName().equalsIgnoreCase(folder.getName())){
                return file.getKey();
            }
        }
        return null;
    }
    boolean isFolder(File folder) {
        for (Map.Entry<File, File[]> file : folderContents.entrySet()) {
            if(file.getKey().getName().equalsIgnoreCase(folder.getName())){
                return true;
            }
        }
        return false;
    }
}
