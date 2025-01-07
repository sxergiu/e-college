package com.ecampus.Ecampus.firebase.Services;

import com.ecampus.Ecampus.entities.Item;
import com.ecampus.Ecampus.entities.Wishlist;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseServiceWishlist {

    private final Firestore firestore;

    public FirebaseServiceWishlist(FirebaseApp firebaseApp) {
        if (firebaseApp == null) {
            throw new IllegalStateException("FirebaseApp is not initialized.");
        }
        this.firestore = FirestoreClient.getFirestore();
    }

    /**
     * Retrieves the wishlist for the given user ID.
     *
     * @param userId The ID of the user whose wishlist is to be fetched.
     * @return Wishlist object containing the product IDs.
     */
    public Wishlist getWishlistByUserId(String userId) throws ExecutionException, InterruptedException {
        try {
            DocumentReference wishlistRef = firestore.collection("wishlists").document(userId);
            ApiFuture<DocumentSnapshot> wishlistFuture = wishlistRef.get();
            DocumentSnapshot documentSnapshot = wishlistFuture.get();

            if (documentSnapshot.exists()) {
                return documentSnapshot.toObject(Wishlist.class);
            } else {
                System.out.println("Wishlist not found for user ID: " + userId);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error fetching wishlist: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Fetches the items in a wishlist using their product IDs.
     *
     * @param userId The ID of the user whose wishlist items are to be retrieved.
     * @return List of Item objects in the user's wishlist.
     */
    public List<Item> getWishlistItems(String userId) throws ExecutionException, InterruptedException {
        List<Item> items = new ArrayList<>();
        try {
            // Retrieve the wishlist
            Wishlist wishlist = getWishlistByUserId(userId);

            if (wishlist == null || wishlist.getProductIds().isEmpty()) {
                System.out.println("No items found in the wishlist for user ID: " + userId);
                return items; // Return an empty list
            }

            // Fetch each item from Firestore using the product IDs
            for (String productId : wishlist.getProductIds()) {
                DocumentReference itemRef = firestore.collection("items").document(productId);
                ApiFuture<DocumentSnapshot> itemFuture = itemRef.get();
                DocumentSnapshot documentSnapshot = itemFuture.get();

                if (documentSnapshot.exists()) {
                    Item item = documentSnapshot.toObject(Item.class);
                    if (item != null) {
                        item.setId(documentSnapshot.getId()); // Ensure the ID is set
                        items.add(item);
                    }
                } else {
                    System.out.println("Item not found for product ID: " + productId);
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching wishlist items: " + e.getMessage());
            throw e;
        }

        return items;
    }

    /**
     * Adds a product ID to the user's wishlist.
     *
     * @param userId    The ID of the user.
     * @param productId The ID of the product to add.
     * @return Updated wishlist.
     */
    public Wishlist addItemToWishlist(String userId, String productId) throws ExecutionException, InterruptedException {
        try {
            Wishlist wishlist = getWishlistByUserId(userId);

            if (wishlist == null ) {
                wishlist = new Wishlist(userId);
            }

            if (!wishlist.getProductIds().contains(productId)) {
                wishlist.getProductIds().add(productId);

                // Update Firestore
                ApiFuture<WriteResult> writeResult = firestore.collection("wishlists").document(userId).set(wishlist);
                writeResult.get(); // Wait for the write to complete
            }

            return wishlist;
        } catch (Exception e) {
            System.err.println("Error adding item to wishlist: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Removes a product ID from the user's wishlist.
     *
     * @param userId    The ID of the user.
     * @param productId The ID of the product to remove.
     * @return Updated wishlist.
     */
    public Wishlist removeItemFromWishlist(String userId, String productId) throws ExecutionException, InterruptedException {
        try {
            Wishlist wishlist = getWishlistByUserId(userId);

            if (wishlist != null && wishlist.getProductIds().contains(productId)) {
                wishlist.getProductIds().remove(productId);

                // Update Firestore
                ApiFuture<WriteResult> writeResult = firestore.collection("wishlists").document(userId).set(wishlist);
                writeResult.get(); // Wait for the write to complete
            }

            return wishlist;
        } catch (Exception e) {
            System.err.println("Error removing item from wishlist: " + e.getMessage());
            throw e;
        }
    }
}
