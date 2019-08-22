package Neptune.Storage;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.v2.users.FullAccount;

import java.io.*;

public class DropboxConnection implements Runnable {
    DbxClientV2 client;
    public DropboxConnection(String AccessToken){
        DbxRequestConfig config = DbxRequestConfig.newBuilder("Neptune/Bot").withAutoRetryEnabled().build();
        client = new DbxClientV2(config, AccessToken);

        try {
            FullAccount account = client.users().getCurrentAccount();
            System.out.println(account.getName().getDisplayName());

        } catch (DbxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            //upload
            File file = new File("NepDB.db");
            System.out.println("Starting Backup");


            try (InputStream in = new FileInputStream(file)) {
                Thread.sleep(100000);
                FileMetadata metadata = client.files().uploadBuilder("/NepDB.db").withMode(WriteMode.OVERWRITE).uploadAndFinish(in);
                if(metadata.getId() != null)
                System.out.println("Backup Complete");

            } catch (DbxException | IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
