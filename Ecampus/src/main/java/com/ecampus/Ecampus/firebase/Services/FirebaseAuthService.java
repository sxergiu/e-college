package com.ecampus.Ecampus.firebase.Services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Service;

@Service
public class FirebaseAuthService {

    // Method to verify the Firebase ID token
    public FirebaseToken verifyIdToken(String idToken) throws Exception {
        return FirebaseAuth.getInstance().verifyIdToken(idToken);
    }

    // Method to check if the token is valid and retrieve user information
    public String getUidFromToken(String idToken) throws Exception {
        FirebaseToken decodedToken = verifyIdToken(idToken);
        return decodedToken.getUid(); // Get the UID of the authenticated user
    }
}
