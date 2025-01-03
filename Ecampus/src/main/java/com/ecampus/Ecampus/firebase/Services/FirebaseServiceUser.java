package com.ecampus.Ecampus.firebase.Services;

import com.ecampus.Ecampus.entities.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseServiceUser
{

    private final Firestore firestore;
    private User loggedUser;

    public FirebaseServiceUser(FirebaseApp firebaseApp) {
        // Ensure Firestore is initialized after FirebaseApp is configured
        this.firestore = FirestoreClient.getFirestore();
    }

    public User getLoggedUser()
    {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser)
    {
        this.loggedUser = loggedUser;
    }

    public String addUser(User user) throws ExecutionException, InterruptedException
    {
            ApiFuture<WriteResult> collectionApiFuture = firestore.collection("users").document(user.getUsername()).set(user);
            return collectionApiFuture.get().getUpdateTime().toString();
    }

    public User getUser(String uid) throws ExecutionException, InterruptedException {
        try {
            DocumentReference documentReference = firestore.collection("users").document(uid);
            ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
            DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();

            if (documentSnapshot.exists()) {
                return documentSnapshot.toObject(User.class);
            } else {
                System.out.println("User with document ID " + uid + " does not exist.");
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

    public String updateUserProfile(String uid,
                                    String name,
                                    String university,
                                    String phone,
                                    String address ) throws ExecutionException, InterruptedException, IOException {

        System.out.println("Updating user profile: " + uid);
        System.out.println("Name: " + name);
        System.out.println("University: " + university);
        System.out.println("Phone: " + phone);
        System.out.println("Address: " + address);

        try {
            // First, get the existing user document
            DocumentReference userRef = firestore.collection("users").document(uid);
            ApiFuture<DocumentSnapshot> future = userRef.get();
            DocumentSnapshot document = future.get();

            if (!document.exists()) {
                throw new IllegalArgumentException("User not found with uid: " + uid);
            }

            // Convert document to User object
            User existingUser = document.toObject(User.class);
            if (existingUser == null) {
                throw new IllegalStateException("Failed to convert document to User object");
            }

            // Update only the fields that are provided (not null)
            if (name != null) existingUser.setName(name);
            if (university != null) existingUser.setUniversity(university);
            if (phone != null) existingUser.setPhone(phone);
            if (address != null) existingUser.setAddress(address);

            System.out.println(existingUser + " PHONE: " + phone);

//            // Handle profile image upload if provided
//            if (profileImage != null && !profileImage.isEmpty()) {
//                String profileImageUrl = uploadProfileImage(profileImage, username);
//                existingUser.setProfileImageUrl(profileImageUrl);
//            }

            // Update the document in Firestore
            ApiFuture<WriteResult> updateFuture = userRef.set(existingUser);
            return updateFuture.get().getUpdateTime().toString();

        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error updating user profile: " + e.getMessage());
            throw e;
        }
    }


}
