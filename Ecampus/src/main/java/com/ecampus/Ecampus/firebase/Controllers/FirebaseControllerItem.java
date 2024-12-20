package com.ecampus.Ecampus.firebase.Controllers;

import com.ecampus.Ecampus.entities.Item;
import com.ecampus.Ecampus.firebase.Services.FirebaseServiceItem;
import com.google.firebase.FirebaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
}
