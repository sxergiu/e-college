package com.ecampus.Ecampus.firebase.Services;

import com.ecampus.Ecampus.user.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService {

    private final Firestore firestore;

    public FirebaseService(FirebaseApp firebaseApp) {
        // Ensure Firestore is initialized after FirebaseApp is configured
        this.firestore = FirestoreClient.getFirestore();
    }

    public String addUser(User user) throws ExecutionException, InterruptedException
    {
            ApiFuture<WriteResult> collectionApiFuture = firestore.collection("users").document(user.getUsername()).set(user);
            return collectionApiFuture.get().getUpdateTime().toString();
    }

    public User getUser(String documentID) throws ExecutionException, InterruptedException
    {
        DocumentReference documentReference = firestore.collection("users").document(documentID);
        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();
        User user;
        if (documentSnapshot.exists())
        {
            user = documentSnapshot.toObject(User.class);
            return user;
        }
        return null;
    }

    public String updateUser()
    {
        return null;
    }

    public String deleteUser(String documentID) throws ExecutionException, InterruptedException
    {
        ApiFuture<WriteResult> writeResult = firestore.collection("users").document(documentID).delete();
        return "Successfully deleted" + documentID;
    }

}
