package com.ecampus.Ecampus.firebase.Services;

import com.ecampus.Ecampus.entities.Item;
import com.ecampus.Ecampus.entities.NotificationRequest;
import com.ecampus.Ecampus.entities.Wishlist;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
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
    private final NotificationService notificationService;
    private final FirebaseServiceItem firebaseServiceItem;

    public FirebaseServiceWishlist(FirebaseApp firebaseApp, NotificationService notificationService, FirebaseServiceItem firebaseServiceItem) {
        if (firebaseApp == null) {
            throw new IllegalStateException("FirebaseApp is not initialized.");
        }
        this.firestore = FirestoreClient.getFirestore();
        this.notificationService = notificationService;
        this.firebaseServiceItem = firebaseServiceItem;
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
            System.out.println("retrieved wishlist: " + wishlist);

            if (wishlist == null || wishlist.getProductIds().isEmpty()) {
                System.out.println("No items found in the wishlist for user ID: " + userId);
                return items; // Return an empty list
            }

            // Fetch each item from Firestore using the product IDs
            for (String productId : wishlist.getProductIds()) {

                DocumentReference itemRef = firestore.collection("items").document(productId);

                System.out.println(productId+ "fetching product");
                ApiFuture<DocumentSnapshot> itemFuture = itemRef.get();

                System.out.println(productId+ "fetching product");
                DocumentSnapshot documentSnapshot = itemFuture.get();

                Item item = new Item();
                item.setId(documentSnapshot.getId());

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

                // Add the item to the list
                items.add(item);
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
    public Wishlist addItemToWishlist(String userId, String productId) throws ExecutionException, InterruptedException, FirebaseException {

        try {
            Wishlist wishlist = getWishlistByUserId(userId);
            System.out.println("Attempting to add item to wishlist: " + wishlist);

            // If the wishlist doesn't exist, create a new one
            if (wishlist == null) {
                wishlist = new Wishlist(userId);
            }

            // Add the productId to the wishlist if not already present
            if (!wishlist.getProductIds().contains(productId)) {
                wishlist.getProductIds().add(productId);

                // Update Firestore with the new wishlist
                ApiFuture<WriteResult> writeResult = firestore.collection("wishlists").document(userId).set(wishlist);
                writeResult.get(); // Wait for the write to complete

                // Send a notification to the seller that their item has been wishlisted
                Item item = firebaseServiceItem.getItem(productId); // Fetch the item details (e.g., sellerId)
                if (item != null && item.getSellerId() != null) {
                    String sellerId = item.getSellerId();
                    String title = "Item Wishlisted!";
                    String message = "Your item '" + item.getName() + "' has been added to someone's wishlist.";

                    // Create a notification request
                    NotificationRequest notificationRequest = new NotificationRequest(sellerId, title, message, productId);

                    // Post the notification
                    notificationService.postNotification(notificationRequest);
                }
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
            System.out.println("Attempting to remove item from wishlist: " + wishlist);

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
