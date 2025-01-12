package com.ecampus.Ecampus.firebase.Controllers;

import com.ecampus.Ecampus.entities.LoginRequest;
import com.ecampus.Ecampus.entities.User;
import com.ecampus.Ecampus.firebase.Services.FirebaseServiceItem;
import com.ecampus.Ecampus.firebase.Services.FirebaseServiceUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class FirebaseAuthController {

    private final FirebaseServiceItem firebaseServiceItem;
    private final FirebaseServiceUser firebaseServiceUser;

    @Autowired
    public FirebaseAuthController(FirebaseServiceItem firebaseServiceItem, FirebaseServiceUser firebaseServiceUser) {
        this.firebaseServiceItem = firebaseServiceItem;
        this.firebaseServiceUser = firebaseServiceUser;
    }

    // Login endpoint to authenticate the user using Firebase token and userId from request body
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest,
                                        @RequestHeader("Authorization") String authToken) {
        String idToken = authToken.replace("Bearer ", "");

        try {
            // Verify the Firebase ID token
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uid = decodedToken.getUid();

            // Compare the userId in the request body with the Firebase UID
            if (!uid.equals(loginRequest.getUserId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User ID mismatch");
            }

            // Authenticate the user (fetch user data and set the logged-in user)
            User user = authenticateUser(loginRequest.getUserId());

            // Set the logged-in user in your services
            System.out.println("Logged user id: " + user.getUserId());
            firebaseServiceItem.setLoggedUser(user);
            firebaseServiceUser.setLoggedUser(user);

            return ResponseEntity.ok("User logged in successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }

    // Method to validate the Firebase token
    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authToken) {
        String idToken = authToken.replace("Bearer ", "");
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uid = decodedToken.getUid();
            System.out.println("User ID: " + uid);
            return ResponseEntity.ok("User authenticated with UID: " + uid);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }

    // Helper method to fetch user based on the userId (could be from database)
    private User authenticateUser(String userId) {
        // This would normally be a service call to your user repository/database
        User user = new User();
        user.setUserId(userId);  // Assuming you fetch user data based on the userId
        return user;
    }
}
