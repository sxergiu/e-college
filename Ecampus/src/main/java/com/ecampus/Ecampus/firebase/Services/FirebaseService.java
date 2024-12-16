package com.ecampus.Ecampus.firebase.Services;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.ecampus.Ecampus.user.User;
import org.springframework.stereotype.Service;

@Service
public class FirebaseService
{
    public final Firestore firestore;

    public FirebaseService()
    {
        this.firestore = FirestoreClient.getFirestore();
    }

    public void addData()
    {
        firestore.collection("user").document("user")
                .set(new User("alex@gmail.com", "alexetare", "alex123"));
    }
}
