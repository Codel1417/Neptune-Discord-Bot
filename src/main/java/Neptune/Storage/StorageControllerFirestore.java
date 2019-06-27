package Neptune.Storage;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import net.dv8tion.jda.core.entities.Guild;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class StorageControllerFirestore implements StorageCommands {
    private Firestore db;

    StorageControllerFirestore() {
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
    public boolean isGuildStored(Guild Guild) {
        try {
            if (db.collection("Guilds").document(Guild.getId()).get().get().exists()) {
                return true;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean setCustomSoundsEnabled(Guild Guild, boolean value) {
        db.collection("Guilds").document(Guild.getId()).update("Custom-Sounds", value);
        return true;
    }

    @Override
    public boolean getCustomSoundsEnabled(Guild Guild) {
        boolean result = false;
        try {
            DocumentReference docRef = db.collection("Guilds").document(Guild.getId());
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            result = (boolean) Objects.requireNonNull(document.getData()).get("Custom-Sounds");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return result;
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

    @Override
    public String printCollectionList(String Collection) {
        return null;
    }

    @Override
    public String printDocumentValues(String collection, String document) {
        DocumentReference docRef = db.collection(collection).document(document);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        try {
            if (future.get().exists()) {
                Map<String, Object> data = future.get().getData();
                StringBuilder values = new StringBuilder();
                for (Map.Entry<String,Object> entry : Objects.requireNonNull(data).entrySet()){
                    values.append(entry.getKey()).append(":").append(entry.getValue().toString()).append("\n");
                }
                return values.toString();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean getTtsEnabled(Guild Guild) {
        boolean result = false;
        try {
            DocumentReference docRef = db.collection("Guilds").document(Guild.getId());
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            result = (boolean) Objects.requireNonNull(document.getData()).get("USE_TTS");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return result;    }

    @Override
    public boolean setTtsEnabled(Guild Guild, boolean value) {
        db.collection("Guilds").document(Guild.getId()).update("USE_TTS", value);
        return true;
    }

    @Override
    public Map<String, Object> getBotInfo() {
        try {
            DocumentReference docRef = db.collection("Admin").document("Bot_Info");
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            return document.getData();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
