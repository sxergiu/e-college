package com.ecampus.Ecampus.firebase.Controllers;

import com.ecampus.Ecampus.entities.Item;
import com.ecampus.Ecampus.entities.Wishlist;
import com.ecampus.Ecampus.firebase.Services.FirebaseServiceWishlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/wishlist")
public class FirebaseControllerWishlist {

    @Autowired
    private FirebaseServiceWishlist firebaseServiceWishlist;

    /**
     * Get the wishlist for a user by their user ID.
     *
     * @param userId The ID of the user.
     * @return Wishlist object containing product IDs.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Wishlist> getWishlist(@PathVariable String userId) {
        try {
            Wishlist wishlist = firebaseServiceWishlist.getWishlistByUserId(userId);
            if (wishlist == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(wishlist);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    /**
     * Get all items in a user's wishlist by their user ID.
     *
     * @param userId The ID of the user.
     * @return List of Item objects in the wishlist.
     */
    @GetMapping("/{userId}/items")
    public ResponseEntity<List<Item>> getWishlistItems(@PathVariable String userId) {
        try {
            List<Item> items = firebaseServiceWishlist.getWishlistItems(userId);
            return ResponseEntity.ok(items);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    /**
     * Add a product to a user's wishlist.
     *
     * @param userId    The ID of the user.
     * @param productId The ID of the product to add.
     * @return Updated Wishlist object.
     */
    @PostMapping("/{userId}/add")
    public ResponseEntity<Wishlist> addItemToWishlist(@PathVariable String userId, @RequestParam String productId) {
        try {
            Wishlist updatedWishlist = firebaseServiceWishlist.addItemToWishlist(userId, productId);
            System.out.println("Tried adding product: " + productId + " to wishlist of user: " + userId);
            return ResponseEntity.ok(updatedWishlist);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    /**
     * Remove a product from a user's wishlist.
     *
     * @param userId    The ID of the user.
     * @param productId The ID of the product to remove.
     * @return Updated Wishlist object.
     */
    @DeleteMapping("/{userId}/remove")
    public ResponseEntity<Wishlist> removeItemFromWishlist(@PathVariable String userId, @RequestParam String productId) {
        try {
            Wishlist updatedWishlist = firebaseServiceWishlist.removeItemFromWishlist(userId, productId);
            if (updatedWishlist == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedWishlist);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
