package com.ecampus.Ecampus.firebase.Controllers;

//import com.ecampus.Ecampus.firebase.Services.FirebaseAuthService;
import com.ecampus.Ecampus.entities.ProfileUpdateRequest;
import com.ecampus.Ecampus.firebase.Services.FirebaseServiceUser;
import com.ecampus.Ecampus.entities.User;
import com.ecampus.Ecampus.firebase.Services.FirebaseServiceItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/user")
public class FirebaseControllerUser
{

    private final FirebaseServiceUser firebaseService;

    public FirebaseControllerUser(FirebaseServiceUser firebaseService, FirebaseServiceItem firebaseServiceItem, FirebaseServiceUser firebaseServiceUser)
    {
        this.firebaseService = firebaseService;
    }

    @PostMapping("/addUser")
    public String addUser(@RequestBody User user) throws ExecutionException, InterruptedException
    {
        return firebaseService.addUser(user);
    }

    @GetMapping("/getUser/{uid}")
    public ResponseEntity<User> getUser(@PathVariable String uid) throws ExecutionException, InterruptedException
    {
        User user = firebaseService.getUser(uid);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(404).body(null); // Return 404 if user is not found
        }
    }

    @PutMapping("/updateUser")
    public String updateUser(@RequestBody User user) throws ExecutionException, InterruptedException
    {
        return firebaseService.updateUser(user);
    }

    @DeleteMapping("/deleteUser")
    public String deleteUser(@RequestParam(required = true) String username) throws ExecutionException, InterruptedException
    {
        return firebaseService.deleteUser(username);
    }

    @PatchMapping("/editProfile/{uid}")
    public ResponseEntity<String> updateUserProfile(
            @PathVariable String uid,
            @RequestBody ProfileUpdateRequest request
    ) {
        try {
            String updateTime = firebaseService.updateUserProfile(
                    uid,
                    request.getName(),
                    request.getUniversity(),
                    request.getPhoneNumber(),
                    request.getAddress()
            );

            return ResponseEntity.ok("Profile updated successfully at: " + updateTime);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating profile: " + e.getMessage());
        }
    }

}
