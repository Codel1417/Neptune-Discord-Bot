package Neptune.Storage;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import net.dv8tion.jda.core.entities.Guild;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class FirestoreConnection implements StorageCommands {
    private Firestore db;

    FirestoreConnection() {
        String googleCloudProjectId = "neeptune-bot";
        FirestoreOptions firestoreOptions =
                FirestoreOptions.getDefaultInstance().toBuilder()
                        .setProjectId(googleCloudProjectId)
                        .build();
        db = firestoreOptions.getService();
    }

    @Override
    public boolean addGuild(Guild Guild) {
        DocumentReference docRef = db.collection("Guilds").document(Guild.getId());
        Map<String, Object> data = new HashMap<>();
        data.put("ID", Guild.getId());
        data.put("Custom-Sounds", false);
        data.put("USE_TTS", false);
        data.put("Name", Guild.getName());
//asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);
// ...
// result.get() blocks on response
        try {
            System.out.println("Update time : " + result.get().getUpdateTime());
            return true;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Map getGuild(Guild Guild) {
        try {
            DocumentReference docRef = db.collection("Guilds").document(Guild.getId());
            ApiFuture<DocumentSnapshot> future = docRef.get();
            return future.get().getData();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateGuildField(Guild Guild, String Field, Object Object) {
        ApiFuture<WriteResult> update = db.collection("Guilds").document(Guild.getId()).update("Field", Object);
        return true;
    }


    @Override
    public boolean incrementAnalyticForCommand(String type, String command) {
        try {
            DocumentReference docRef = db.collection("Analytics").document(type);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                if (Objects.requireNonNull(document.getData()).containsKey(command)){
                    docRef.update(command,(Long) document.getData().get(command) + 1);
                }
                else  docRef.update(command, 1);
            }
            else {
                Map<String, Object> data = new HashMap<>();
                data.put(command, 1);
                docRef.set(data);
            }
            return true;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return false;
    }
    
}
