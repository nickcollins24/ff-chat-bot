package ncollins.data;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import ncollins.model.chat.Pin;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class PinCollection {
    // these only get set when testing locally, dont set these if running in GCP
    String GCP_PROJECT_ID =                 System.getenv("GCP_PROJECT_ID");
    String GCP_KEY =                        System.getenv("GCP_KEY");
    private CollectionReference collection;

    public PinCollection() {}

    private CollectionReference getCollection(){
        if(collection == null){
            try {
                GoogleCredentials credentials;
                String pinCollection;

                // implies we are running in GCP
                if(GCP_KEY == null){
                    credentials = GoogleCredentials.getApplicationDefault();
                    pinCollection = "pin";
                    // implies we are running locally
                } else {
                    InputStream serviceAccount = new ByteArrayInputStream(GCP_KEY.getBytes());
                    credentials = GoogleCredentials.fromStream(serviceAccount);
                    pinCollection = "integ-pin";
                }

                FirestoreOptions firestoreOptions =
                        FirestoreOptions.getDefaultInstance().toBuilder()
                                .setProjectId(GCP_PROJECT_ID)
                                .setCredentials(credentials)
                                .build();
                Firestore db = firestoreOptions.getService();
                this.collection = db.collection(pinCollection);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return collection;
    }

    public List<Pin> getPins(){
        List<Pin> pins = new ArrayList();
        ApiFuture<QuerySnapshot> query = getCollection().get();
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
        getCollection().add(pin);
    }

    public void deletePin(int index){
        try {
            ApiFuture<QuerySnapshot> query = getCollection().get();
            QueryDocumentSnapshot document = query.get().getDocuments().get(index);
            getCollection().document(document.getId()).delete();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
