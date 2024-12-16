package com.ecampus.Ecampus;

import com.google.firebase.FirebaseApp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EcampusApplication {

	public static void main(String[] args) {
		if (FirebaseApp.getApps().isEmpty()) {
			System.out.println("Initializing Firebase App...");
		} else {
			System.out.println("Firebase already initialized!");
		}
		SpringApplication.run(EcampusApplication.class, args);
	}

}
