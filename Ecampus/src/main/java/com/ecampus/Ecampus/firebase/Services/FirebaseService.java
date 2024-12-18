package com.ecampus.Ecampus.firebase.Services;

import com.ecampus.Ecampus.user.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;
import com.google.protobuf.Api;
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

    public User getUser(String documentID) throws ExecutionException, InterruptedException {
        try {
            DocumentReference documentReference = firestore.collection("users").document(documentID);
            ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
            DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();

            if (documentSnapshot.exists()) {
                return documentSnapshot.toObject(User.class);
            } else {
                System.out.println("User with document ID " + documentID + " does not exist.");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error fetching user: " + e.getMessage());
            throw e;
        }
    }


    public String updateUser(User user) throws ExecutionException, InterruptedException
    {
        ApiFuture<WriteResult> collectionApiFuture = firestore.collection("users").document(user.getUsername()).set(user);
        return collectionApiFuture.get().getUpdateTime().toString();
    }

    public String deleteUser(String documentID) throws ExecutionException, InterruptedException {
        // Reference the document in the Firestore collection
        DocumentReference documentReference = firestore.collection("users").document(documentID);

        // Check if the document exists
        ApiFuture<DocumentSnapshot> documentSnapshotFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = documentSnapshotFuture.get();

        if (documentSnapshot.exists()) {
            // If the document exists, delete it
            ApiFuture<WriteResult> writeResult = documentReference.delete();
            writeResult.get(); // Wait for the deletion to complete
            return "Successfully deleted user with username: " + documentID;
        } else {
            // If the document does not exist, return a not-found message
            return "User with username: " + documentID + " not found in the database.";
        }
    }


}
