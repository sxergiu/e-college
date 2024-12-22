// src/userid/index.js (or wherever you have this function)
import axios from 'axios';
import { auth } from '../../firebase/firebase';

export const sendUserIdToBackend = async () => {
    try {
        const user = auth.currentUser;
        if (!user) {
            console.error("No user found when sending token to backend");
            return;
        }

        // Get the ID token from Firebase
        const idToken = await user.getIdToken();
        
        // Get the user ID from the current user object (firebase auth)
        const userId = user.uid; // Firebase automatically provides the user ID as `uid`

        // Send the ID token and userId to the backend
        const response = await axios.post("http://localhost:8080/user/login", 
        {
            userId: userId, // Send user ID in the request body
        }, 
        {
            headers: {
                Authorization: `Bearer ${idToken}`,
                'Content-Type': 'application/json',
            },
        });
        
        return response.data; // Handle the backend response
    } catch (error) {
        console.error("Error sending token to backend:", error.message);
        throw error; // Propagate error to caller
    }
};
