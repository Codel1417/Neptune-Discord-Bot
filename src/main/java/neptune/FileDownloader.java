package neptune;

import org.apache.commons.io.FileUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class FileDownloader {

    public void DownloadFile(String fileURL, String FilePath, String type) throws IOException {
            System.out.println("Downloading: " + fileURL);
            if (type.equals("image")) downloadImage(fileURL, FilePath);
            else
                downloadBuffered(fileURL, FilePath);
    }

    private void downloadBuffered(String fileURL, String FilePath) throws IOException {
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(fileURL).openStream());
             FileOutputStream fileOS = new FileOutputStream(FilePath)) {
            byte[] data = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
            }
        }
    }

    private void downloadImage(String fileURL, String FilePath) throws IOException {
        File file = new File(FilePath);
        URL myUrl = new URL(fileURL);
        FileUtils.copyURLToFile(myUrl, file);
    }

}
