package Neptune.Storage;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.v2.users.FullAccount;

import java.io.*;

public class DropboxBackupConnection implements Runnable {
    private DbxClientV2 client;
    private int BackUpWaitTime = 1000000;
    String accessToken;
    public DropboxBackupConnection(String AccessToken){
    accessToken = AccessToken;
    }

    @Override
    public void run() {
        login();

        while (true) {
            //upload
            try {
                Thread.sleep(BackUpWaitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            File file = new File("NepDB.db");
            System.out.println("DROPBOX: Starting Backup");

            try (InputStream in = new FileInputStream(file)) {
                FileMetadata metadata = client.files().uploadBuilder("/NepDB.db").withMode(WriteMode.OVERWRITE).uploadAndFinish(in);
                if(metadata.getId() != null)
                System.out.println("DROPBOX: Backup Complete");
            } catch (DbxException | IOException  e) {
                e.printStackTrace();
            }
        }
    }
    private void login(){
        System.out.println("DROPBOX: Logging in to Dropbox");
        DbxRequestConfig config = DbxRequestConfig.newBuilder("Neptune/Bot").withAutoRetryEnabled().build();
        client = new DbxClientV2(config, accessToken);

        try {
            FullAccount account = client.users().getCurrentAccount();
            System.out.println("DROPBOX: Connected to Dropbox. Backing up every " + (BackUpWaitTime/1000)/60 + " minutes");

        } catch (DbxException e) {
            e.printStackTrace();
        }
    }
}
