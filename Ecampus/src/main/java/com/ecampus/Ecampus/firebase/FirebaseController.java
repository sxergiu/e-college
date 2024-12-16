package com.ecampus.Ecampus.firebase;

import com.ecampus.Ecampus.firebase.Services.FirebaseAuthService;
import com.ecampus.Ecampus.firebase.Services.FirebaseService;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirebaseController
{
    @Autowired
    private FirebaseAuthService firebaseAuthService;

    private final FirebaseService firebaseService;

    public FirebaseController(FirebaseService firebaseService)
    {
        this.firebaseService = firebaseService;
    }

    @GetMapping("/addUser")
    public String addUser(){
        firebaseService.addData();
        return "User added";
    }

//    @GetMapping("/protected")
//    public ResponseEntity<String> protectedEndpoint(@RequestHeader("Authorization") String authHeader) {
//        try {
//            // Extract the token from the Authorization header (Bearer <token>)
//            if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                String idToken = authHeader.substring(7); // Get the token part of the header
//                FirebaseToken decodedToken = firebaseAuthService.verifyIdToken(idToken);
//                String uid = decodedToken.getUid(); // Get the UID of the authenticated user
//
//                // Proceed with the request logic (e.g., user-specific data)
//                return ResponseEntity.ok("Hello, user with UID: " + uid);
//            } else {
//                return ResponseEntity.status(400).body("Invalid authorization header");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(401).body("Unauthorized: " + e.getMessage());
//        }
//    }
}
