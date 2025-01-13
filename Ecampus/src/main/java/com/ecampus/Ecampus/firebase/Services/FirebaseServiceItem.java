package com.ecampus.Ecampus.firebase.Services;

import com.ecampus.Ecampus.entities.Item;
import com.ecampus.Ecampus.entities.Transaction;
import com.ecampus.Ecampus.entities.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class FirebaseServiceItem {

    private final Firestore firestore;
    private final User user;
    private User loggedUser;

    public FirebaseServiceItem(FirebaseApp firebaseApp, User user)
    {
        if (firebaseApp == null) {
            throw new IllegalStateException("FirebaseApp is not initialized.");
        }
        this.firestore = FirestoreClient.getFirestore();
        this.user = user;
    }

    public void setLoggedUser(User user) {
        this.loggedUser = user;
    }

    public User getLoggedUser() {
        return this.loggedUser;
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
                if (documentSnapshot.contains("condition")) {
                    Object condition = documentSnapshot.get("condition");
                    System.out.println("Condition received: " + condition);
                    if (condition instanceof String) {
                        item.setCondition((String) condition);
                    } else {
                        assert condition != null;
                        System.err.println("Invalid type for condition: " + condition.getClass());
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
                if (documentSnapshot.contains("category")) {
                    Object category = documentSnapshot.get("category");
                    System.out.println("Category received: " + category);
                    if (category instanceof String) {
                        item.setCategory((String) category);
                    } else {
                        assert category != null;
                        System.err.println("Invalid type for category: " + category.getClass());
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


    public void updateItem(String id, Item item) throws ExecutionException, InterruptedException
    {
            item.setId(id);
            ApiFuture<WriteResult> collectionApiFuture = firestore.collection("items").document(id).set(item);
        collectionApiFuture.get();
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


    public List<Item> getItemsBySellerId(String userId) throws FirebaseException, ExecutionException, InterruptedException {
        List<Item> items = new ArrayList<>();

        try {
            // Query the Firestore collection for documents where "sellerId" matches the provided userId
            CollectionReference collectionReference = firestore.collection("items");
            Query query = collectionReference.whereEqualTo("sellerId", userId);
            ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();
            QuerySnapshot querySnapshot = querySnapshotApiFuture.get();

            if (!querySnapshot.isEmpty()) {
                for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                    System.out.println("Retrieved document data: " + documentSnapshot.getData());

                    // Create the Item object
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
            } else {
                //System.out.println("No items found for sellerId: " + userId);
            }

        } catch (Exception e) {
            System.err.println("Error fetching items: " + e.getMessage());
            throw e;
        }

        return items;
    }

    public Map<String, List<Item>> returnAllItems() throws FirebaseException, ExecutionException, InterruptedException {
        Map<String, List<Item>> userItemsMap = new HashMap<>();
        System.out.println("FirbaseServiceItem::returnAllItems for the logged user: " + loggedUser.getUserId());
        try {
            // Fetch all users from the "users" collection
            CollectionReference usersCollection = firestore.collection("users");
            ApiFuture<QuerySnapshot> usersFuture = usersCollection.get();
            QuerySnapshot usersSnapshot = usersFuture.get();

            // Iterate over each user document
            for (DocumentSnapshot userDoc : usersSnapshot.getDocuments()) {
                if (userDoc.exists()) {
                    String userId = userDoc.getId(); // Assuming userId is the document ID

                    if (!userId.equals(this.loggedUser.getUserId()))
                    {
                        String username = userDoc.contains("username") ? userDoc.getString("username") : "Unknown";
                        // Fetch items for this user using getItemBySellerId
                        List<Item> items = getItemsBySellerId(userId);
                        // Add to the map, even if the user has no items (empty list)
                        if (items.isEmpty())
                        {
                            //System.out.println("No items found for sellerId: " + userId);
                        } else
                            userItemsMap.put(username, items);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching all items: " + e.getMessage());
            throw e;
        }
        System.out.println("aaaaaaaaaaaaa   " + userItemsMap);
        return userItemsMap;
    }

    public List<Map<String, Object>> searchItems(String q) throws Exception
    {
        // Initialize Firestore Collection reference
        CollectionReference itemsRef = firestore.collection("items");

        // Query Firestore to find items where name, description, or category contains the search query (case insensitive)
        ApiFuture<QuerySnapshot> future = itemsRef.get();

        // Get the results as a List of DocumentSnapshots
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        // Convert Firestore documents to a list of items
        List<Map<String, Object>> matchedItems = documents.stream()
                .map(doc -> doc.getData())
                .filter(item -> {
                    // Perform case-insensitive substring matching on name, description, and category
                    String name = (String) item.get("name");
                    String description = (String) item.get("description");
                    String category = (String) item.get("category");

                    // Check if query is found in any of the fields
                    return (name != null && name.toLowerCase().contains(q.toLowerCase())) ||
                            (description != null && description.toLowerCase().contains(q.toLowerCase())) ||
                            (category != null && category.toLowerCase().contains(q.toLowerCase()));
                })
                .collect(Collectors.toList());

        return matchedItems;  // Return matched items
    }

    public Item getItemById(String itemId) throws FirebaseException, ExecutionException, InterruptedException {
        // Reference to the 'items' collection
        DocumentReference itemRef = firestore.collection("items").document(itemId);

        // Fetch the document synchronously
        ApiFuture<DocumentSnapshot> future = itemRef.get();
        DocumentSnapshot document = future.get();  // Get the document

        if (document.exists())
        {
            // Convert the document to an Item object
            Item item = document.toObject(Item.class);
            if (item != null)
            {
                return item;
            }
        }
        return null;
    }

    public Double handleItemTransaction(Item item, String buyerId, String itemId) throws ExecutionException, InterruptedException
    {
        DocumentReference buyerRef = firestore.collection("users").document(buyerId);
        ApiFuture<DocumentSnapshot> buyerSnapshotFuture = buyerRef.get();
        DocumentSnapshot buyerSnapshot = buyerSnapshotFuture.get();

        if (!buyerSnapshot.exists()) {
            System.err.println("Buyer not found.");
            return null;
        }

        User buyer = buyerSnapshot.toObject(User.class);
        if (buyer == null) {
            System.err.println("Error retrieving buyer data.");
            return null;
        }

        // Check if the buyer has enough balance
        if (buyer.getBalance() < item.getPrice()) {
            System.err.println("Insufficient funds.");
            return null;
        }

        // Fetch the seller details (stored in the item document)
        String sellerId = item.getSellerId();
        DocumentReference sellerRef = firestore.collection("users").document(sellerId);
        ApiFuture<DocumentSnapshot> sellerSnapshotFuture = sellerRef.get();
        DocumentSnapshot sellerSnapshot = sellerSnapshotFuture.get();

        if (!sellerSnapshot.exists()) {
            System.err.println("Seller not found.");
            return null;
        }

        User seller = sellerSnapshot.toObject(User.class);
        if (seller == null) {
            System.err.println("Error retrieving seller data.");
            return null;
        }

        // Deduct the price from buyer's balance and add to seller's balance
        double price = item.getPrice();
        buyer.setBalance(buyer.getBalance() - price);
        seller.setBalance(seller.getBalance() + price);

        // Update buyer and seller balances in Firestore
        ApiFuture<WriteResult> buyerUpdate = buyerRef.set(buyer);
        ApiFuture<WriteResult> sellerUpdate = sellerRef.set(seller);

        // Wait for the balance updates to complete
        buyerUpdate.get();
        sellerUpdate.get();
        item.setSold(true);
        return price;
    }
    public Transaction buyItem(String buyerId, String itemId) throws ExecutionException, InterruptedException
    {
        try
        {
            // Fetch the document reference from Firestore
            DocumentReference documentReference = firestore.collection("items").document(itemId);
            ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = documentReference.get();
            DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();

            if (documentSnapshot.exists())
            {
                System.out.println("Retrieved document data: " + documentSnapshot.getData());

                // Create the Item object
                Item item = new Item();
                item.setId(documentSnapshot.getId());
                // Check the type of each field and convert accordingly

                // Check and set sellerId (String)
                if (documentSnapshot.contains("sellerId"))
                {
                    Object sellerId = documentSnapshot.get("sellerId");
                    if (sellerId instanceof String)
                    {
                        item.setSellerId((String) sellerId);
                    } else
                    {
                        System.err.println("Invalid type for sellerId");
                    }
                }

                // Check and set name (String)
                if (documentSnapshot.contains("name"))
                {
                    Object name = documentSnapshot.get("name");
                    if (name instanceof String)
                    {
                        item.setName((String) name);
                    } else
                    {
                        System.err.println("Invalid type for name");
                    }
                }

                // Check and set description (String)
                if (documentSnapshot.contains("description"))
                {
                    Object description = documentSnapshot.get("description");
                    if (description instanceof String)
                    {
                        item.setDescription((String) description);
                    } else
                    {
                        System.err.println("Invalid type for description");
                    }
                }

                // Check and set price (Double)
                if (documentSnapshot.contains("price"))
                {
                    Object price = documentSnapshot.get("price");
                    if (price instanceof Double)
                    {
                        item.setPrice((Double) price);
                    } else if (price instanceof Long)
                    {
                        item.setPrice(((Long) price).doubleValue());
                    } else
                    {
                        System.err.println("Invalid type for price");
                    }
                }

                // Check and set category (ItemCategory)
                if (documentSnapshot.contains("condition"))
                {
                    Object condition = documentSnapshot.get("condition");
                    System.out.println("Condition received: " + condition);
                    if (condition instanceof String)
                    {
                        item.setCondition((String) condition);
                    } else
                    {
                        assert condition != null;
                        System.err.println("Invalid type for condition: " + condition.getClass());
                    }
                }

                // Check and set images (List<String>)
                if (documentSnapshot.contains("images"))
                {
                    Object images = documentSnapshot.get("images");
                    if (images instanceof List)
                    {
                        List<?> imagesList = (List<?>) images;
                        // Check that the list contains only Strings
                        if (imagesList.isEmpty() || imagesList.get(0) instanceof String)
                        {
                            item.setImages((List<String>) images);
                        } else
                        {
                            System.err.println("Invalid type for images");
                        }
                    } else
                    {
                        System.err.println("Invalid type for images");
                    }
                }

                // Check and set condition (ItemCondition)
                if (documentSnapshot.contains("category"))
                {
                    Object category = documentSnapshot.get("category");
                    System.out.println("Category received: " + category);
                    if (category instanceof String)
                    {
                        item.setCategory((String) category);
                    } else
                    {
                        assert category != null;
                        System.err.println("Invalid type for category: " + category.getClass());
                    }
                }

                // Check and set createdAt (Timestamp)
                if (documentSnapshot.contains("createdAt"))
                {
                    Object createdAt = documentSnapshot.get("createdAt");
                    if (createdAt instanceof Timestamp)
                    {
                        item.setCreatedAt(((Timestamp) createdAt).toSqlTimestamp());
                    } else
                    {
                        System.err.println("Invalid type for createdAt");
                    }
                }

                // Check and set updatedAt (Timestamp)
                if (documentSnapshot.contains("updatedAt"))
                {
                    Object updatedAt = documentSnapshot.get("updatedAt");
                    if (updatedAt instanceof Timestamp)
                    {
                        item.setUpdatedAt(((Timestamp) updatedAt).toSqlTimestamp());
                    } else
                    {
                        System.err.println("Invalid type for updatedAt");
                    }
                }
                if (handleItemTransaction(item, buyerId, itemId) != null)
                {
                    Transaction transaction = new Transaction();
                    transaction.setItemId(item.getId());
                    transaction.setSellerId(item.getSellerId());
                    transaction.setBuyerId(buyerId);
                    transaction.setTimestamp(new Date());
                    System.out.println("Transaction created: " + transaction.toString());
                    CollectionReference transactionsRef = firestore.collection("transactions");
                    ApiFuture<DocumentReference> transactionRefFuture = transactionsRef.add(transaction);
                    transactionRefFuture.get();
                    return transaction;
                }
                return null;
            } else
            {
                System.out.println("Item with document ID " + itemId + " does not exist.");
                return null;
            }
        } catch (Exception e)
        {
            System.err.println("Error fetching item: " + e.getMessage());
            throw e;
        }
    }


}