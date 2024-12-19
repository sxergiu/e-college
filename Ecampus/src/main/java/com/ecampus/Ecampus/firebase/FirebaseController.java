package com.ecampus.Ecampus.firebase;

//import com.ecampus.Ecampus.firebase.Services.FirebaseAuthService;
import com.ecampus.Ecampus.firebase.Services.FirebaseService;
import com.ecampus.Ecampus.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/user")
public class FirebaseController
{
//    @Autowired
//    private FirebaseAuthService firebaseAuthService;

    private final FirebaseService firebaseService;

    public FirebaseController(FirebaseService firebaseService)
    {
        this.firebaseService = firebaseService;
       // this.firebaseAuthService = firebaseAuthService;
    }

    @PostMapping("/addUser")
    public String addUser(@RequestBody User user) throws ExecutionException, InterruptedException
    {
        return firebaseService.addUser(user);
    }

    @GetMapping("/getUser")
    public ResponseEntity<User> getUser(@RequestParam(required = true) String username) throws ExecutionException, InterruptedException
    {
        User user = firebaseService.getUser(username);
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

}
