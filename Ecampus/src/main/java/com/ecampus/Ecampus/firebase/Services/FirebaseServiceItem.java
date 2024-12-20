package com.ecampus.Ecampus.firebase.Services;

import com.ecampus.Ecampus.entities.Item;
import com.ecampus.Ecampus.entities.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseServiceItem {

    private final Firestore firestore;

    public FirebaseServiceItem(FirebaseApp firebaseApp)
    {
        if (firebaseApp == null) {
            throw new IllegalStateException("FirebaseApp is not initialized.");
        }
        this.firestore = FirestoreClient.getFirestore();
    }


    public String addItem(Item item) throws ExecutionException, InterruptedException {
        try {
            ApiFuture<DocumentReference> collectionApiFuture = firestore.collection("items").add(item);
            String documentId = collectionApiFuture.get().getId();

            item.setId(documentId);
            ApiFuture<WriteResult> updateFuture = firestore.collection("items").document(documentId).set(item);

            updateFuture.get();
            return documentId;
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to add item", e);
        }
    }


    public Item getItem(String documentID) throws FirebaseException, ExecutionException, InterruptedException {
        try {
            // Fetch the document reference from Firestore
            DocumentReference documentReference = firestore.collection("items").document(documentID);
            ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
            DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();

            if (documentSnapshot.exists()) {
                System.out.println("Retrieved document data: " + documentSnapshot.getData());

                // Create the Item object
                Item item = new Item();
                item.setId(documentSnapshot.getId());
                // Check the type of each field and convert accordingly

                // Check and set sellerId (String)
                if (documentSnapshot.contains("sellerId")) {
                    Object sellerId = documentSnapshot.get("sellerId");
                    if (sellerId instanceof String) {
                        item.setSellerId((String) sellerId);
                    } else {
                        System.err.println("Invalid type for sellerId");
                    }
                }

                // Check and set name (String)
                if (documentSnapshot.contains("name")) {
                    Object name = documentSnapshot.get("name");
                    if (name instanceof String) {
                        item.setName((String) name);
                    } else {
                        System.err.println("Invalid type for name");
                    }
                }

                // Check and set description (String)
                if (documentSnapshot.contains("description")) {
                    Object description = documentSnapshot.get("description");
                    if (description instanceof String) {
                        item.setDescription((String) description);
                    } else {
                        System.err.println("Invalid type for description");
                    }
                }

                // Check and set price (Double)
                if (documentSnapshot.contains("price")) {
                    Object price = documentSnapshot.get("price");
                    if (price instanceof Double) {
                        item.setPrice((Double) price);
                    } else if (price instanceof Long) {
                        item.setPrice(((Long) price).doubleValue());
                    } else {
                        System.err.println("Invalid type for price");
                    }
                }

                // Check and set category (ItemCategory)
                if (documentSnapshot.contains("category")) {
                    Object category = documentSnapshot.get("category");
                    if (category instanceof String) {
                        item.setCategory((String) category); // Converts string to ItemCategory
                    } else {
                        System.err.println("Invalid type for category");
                    }
                }

                // Check and set images (List<String>)
                if (documentSnapshot.contains("images")) {
                    Object images = documentSnapshot.get("images");
                    if (images instanceof List) {
                        List<?> imagesList = (List<?>) images;
                        // Check that the list contains only Strings
                        if (imagesList.isEmpty() || imagesList.get(0) instanceof String) {
                            item.setImages((List<String>) images);
                        } else {
                            System.err.println("Invalid type for images");
                        }
                    } else {
                        System.err.println("Invalid type for images");
                    }
                }

                // Check and set condition (ItemCondition)
                if (documentSnapshot.contains("condition")) {
                    Object condition = documentSnapshot.get("condition");
                    if (condition instanceof String) {
                        item.setCondition((String) condition); // Converts string to ItemCondition
                    } else {
                        System.err.println("Invalid type for condition");
                    }
                }

                // Check and set createdAt (Timestamp)
                if (documentSnapshot.contains("createdAt")) {
                    Object createdAt = documentSnapshot.get("createdAt");
                    if (createdAt instanceof Timestamp) {
                        item.setCreatedAt(((Timestamp) createdAt).toSqlTimestamp());
                    } else {
                        System.err.println("Invalid type for createdAt");
                    }
                }

                // Check and set updatedAt (Timestamp)
                if (documentSnapshot.contains("updatedAt")) {
                    Object updatedAt = documentSnapshot.get("updatedAt");
                    if (updatedAt instanceof Timestamp) {
                        item.setUpdatedAt(((Timestamp) updatedAt).toSqlTimestamp());
                    } else {
                        System.err.println("Invalid type for updatedAt");
                    }
                }

                return item;
            } else {
                System.out.println("Item with document ID " + documentID + " does not exist.");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error fetching item: " + e.getMessage());
            throw e;
        }
    }


    public String updateItem(String id, Item item) throws ExecutionException, InterruptedException
    {
            item.setId(id);
            ApiFuture<WriteResult> collectionApiFuture = firestore.collection("items").document(id).set(item);
            return collectionApiFuture.get().getUpdateTime().toString();
    }

    public String deleteItem(String documentID) throws ExecutionException, InterruptedException {
        // Reference the document in the Firestore collection
        DocumentReference documentReference = firestore.collection("items").document(documentID);

        // Check if the document exists
        ApiFuture<DocumentSnapshot> documentSnapshotFuture = documentReference.get();
        DocumentSnapshot documentSnapshot = documentSnapshotFuture.get();

        if (documentSnapshot.exists()) {
            // If the document exists, delete it
            ApiFuture<WriteResult> writeResult = documentReference.delete();
            writeResult.get(); // Wait for the deletion to complete
            return "Successfully deleted item with id: " + documentID;
        } else {
            // If the document does not exist, return a not-found message
            return "Item with id: " + documentID + " not found in the database.";
        }
    }
}