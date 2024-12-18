package com.ecampus.Ecampus.firebase;

import com.ecampus.Ecampus.firebase.Services.FirebaseAuthService;
import com.ecampus.Ecampus.firebase.Services.FirebaseService;
import com.ecampus.Ecampus.user.User;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/user")
public class FirebaseController
{
    @Autowired
    private FirebaseAuthService firebaseAuthService;

    private final FirebaseService firebaseService;

    public FirebaseController(FirebaseService firebaseService, FirebaseAuthService firebaseAuthService)
    {
        this.firebaseService = firebaseService;
        this.firebaseAuthService = firebaseAuthService;
    }

    @PostMapping("/addUser")
    public String addUser(@RequestBody User user) throws ExecutionException, InterruptedException
    {
        return firebaseService.addUser(user);
    }

    @GetMapping("/getUser")
    public User getUser(@RequestParam String documentID) throws ExecutionException, InterruptedException
    {
        return firebaseService.getUser(documentID);
    }

//    @PutMapping("/updateUser")
//    public String updateUser(@RequestBody User user) throws ExecutionException, InterruptedException
//    {
//        return firebaseService.updateUser(user);
//    }

    @DeleteMapping("/deleteUser")
    public String deleteUser(@RequestParam String documentID) throws ExecutionException, InterruptedException
    {
        return firebaseService.deleteUser(documentID);
    }

}
