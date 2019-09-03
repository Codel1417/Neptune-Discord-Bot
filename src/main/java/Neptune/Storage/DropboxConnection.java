package Neptune.Storage;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.v2.users.FullAccount;

import java.io.*;

public class DropboxConnection implements Runnable {
    private DbxClientV2 client;
    private int BackUpWaitTime = 1000000;
    public DropboxConnection(String AccessToken){
        System.out.println("Init Dropbox");
        System.out.println("    Connecting to Dropbox");
        DbxRequestConfig config = DbxRequestConfig.newBuilder("Neptune/Bot").withAutoRetryEnabled().build();
        System.out.println("    Logging In");
        client = new DbxClientV2(config, AccessToken);

        try {
            FullAccount account = client.users().getCurrentAccount();
            System.out.println("    Connected to Dropbox");
            System.out.println("    Backing up every " + (BackUpWaitTime/1000)/60 + " minutes");

        } catch (DbxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            //upload
            try {
                Thread.sleep(BackUpWaitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            File file = new File("NepDB.db");
            System.out.println("Starting Backup");


            try (InputStream in = new FileInputStream(file)) {
                FileMetadata metadata = client.files().uploadBuilder("/NepDB.db").withMode(WriteMode.OVERWRITE).uploadAndFinish(in);
                if(metadata.getId() != null)
                System.out.println("    Backup Complete");
            } catch (DbxException | IOException  e) {
                e.printStackTrace();
            }
        }
    }
}
