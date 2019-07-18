package ncollins.data;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import ncollins.model.chat.Pin;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PinCollection {
    private CollectionReference collection;

    public PinCollection(String projectId, String key) {
        try {
            GoogleCredentials credentials;
            String collection;

            // implies we are running in GCP
            if(key == null){
                credentials = GoogleCredentials.getApplicationDefault();
                collection = "pin";
            // implies we are running locally
            } else {
                InputStream serviceAccount = new ByteArrayInputStream(key.getBytes());
                credentials = GoogleCredentials.fromStream(serviceAccount);
                collection = "integ-pin";
            }

            FirestoreOptions firestoreOptions =
                    FirestoreOptions.getDefaultInstance().toBuilder()
                            .setProjectId(projectId)
                            .setCredentials(credentials)
                            .build();
            Firestore db = firestoreOptions.getService();
            this.collection = db.collection(collection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Pin> getPins(){
        List<Pin> pins = new ArrayList();
        ApiFuture<QuerySnapshot> query = collection.get();
        QuerySnapshot querySnapshot = null;

        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            pins.add(document.toObject(Pin.class));
        }

        return pins;
    }

    public void addPin(Pin pin){
        collection.add(pin);
    }

    public void deletePin(int index){
        try {
            ApiFuture<QuerySnapshot> query = collection.get();
            QueryDocumentSnapshot document = query.get().getDocuments().get(index);
            collection.document(document.getId()).delete();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
