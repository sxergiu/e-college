package com.ecampus.Ecampus.firebase.Controllers;

import com.ecampus.Ecampus.entities.Item;
import com.ecampus.Ecampus.entities.User;
import com.ecampus.Ecampus.firebase.Services.FirebaseServiceItem;
import com.google.firebase.FirebaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/item")
public class FirebaseControllerItem {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseControllerItem.class);
    private final FirebaseServiceItem firebaseServiceItem;

    public FirebaseControllerItem(FirebaseServiceItem firebaseServiceItem) {
        this.firebaseServiceItem = firebaseServiceItem;
    }

    @PostMapping("/addItem")
    public ResponseEntity<String> addItem(@Valid @RequestBody Item item) {
        try {
            System.out.println(item.toString());
            String documentId = firebaseServiceItem.addItem(item);
            return ResponseEntity.ok("Item added successfully with ID: " + documentId);
        } catch (Exception e) {
            logger.error("Failed to add item", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add item: " + e.getMessage());
        }
    }

    @GetMapping("/getItem/{id}")
    public ResponseEntity<Item> getItem(@PathVariable String id) throws ExecutionException, InterruptedException, FirebaseException {
        Item item = firebaseServiceItem.getItem(id);
        System.out.println(item.toString());
        if (item != null) {
            return ResponseEntity.ok(item);
        } else {
            return ResponseEntity.status(404).body(null); // Return 404 if item is not found
        }
    }

    @PutMapping("/updateItem/{id}")
    public ResponseEntity<String> updateItem(@PathVariable String id, @RequestBody Item item) throws ExecutionException, InterruptedException, FirebaseException
    {
        item.setId(id);
        System.out.println("Received Item: " + item);
        firebaseServiceItem.updateItem(id, item);
        return ResponseEntity.ok("Item updated successfully with ID: " + id);
    }

    @DeleteMapping("/deleteItem/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable String id) throws ExecutionException, InterruptedException
    {
        String response = firebaseServiceItem.deleteItem(id);
        if (response.contains("Not found"))
        {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        else
        {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/getItemBySellerId/{id}")
    public ResponseEntity<List<Item>> getItemBySellerId(@PathVariable String id) throws FirebaseException, ExecutionException, InterruptedException
    {
        try
        {
            List<Item> items  = firebaseServiceItem.getItemsBySellerId(id);
            if (items.isEmpty())
            {
                return ResponseEntity.ok(items);
            }
            return ResponseEntity.ok(items);
        }
        catch (Exception e)
        {
            System.err.println("Error fetching items by seller ID: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/returnAllItems")
    public ResponseEntity<Map<String, List<Item>>> returnAllItems( )throws ExecutionException, InterruptedException, FirebaseException
    {
        try
        {
            // Call the service method
            Map<String, List<Item>> allItems = firebaseServiceItem.returnAllItems();

            // Return the result
            return ResponseEntity.ok(allItems);
        } catch (Exception e)
        {
            // Handle errors and return 500 Internal Server Error
            System.err.println("Error returning all items: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> search(@RequestParam String q)
    {
        try
        {
            List<Map<String, Object>> searchedItems = firebaseServiceItem.searchItems(q);
            return ResponseEntity.ok(searchedItems);
        }
        catch (Exception e)
        {
            System.err.println("Error returning searched item: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getItemById/{id}")
    public ResponseEntity<?> getItemById(@PathVariable String id) {
        try {
            // Fetch item using the service layer
            Item item = firebaseServiceItem.getItem(id);

            // Log the item (using logger instead of System.out.println)
            logger.info("Fetched item: {}", item);

            // Return the item as a response
            return ResponseEntity.ok(item);
        } catch (ExecutionException | InterruptedException e) {
            logger.error("Error while fetching item with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(500).body("Error while fetching item data.");
        } catch (FirebaseException e) {
            logger.error("FirebaseException occurred while fetching item with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(500).body("Failed to load item from Firebase.");
        } catch (Exception e) {
            logger.error("Unexpected error occurred while fetching item with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(500).body("Unexpected error occurred.");
        }
    }


}
