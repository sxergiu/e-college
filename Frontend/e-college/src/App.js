import React, { useEffect, useState } from "react";
import firebase from "firebase/compat/app";
import "firebase/compat/firestore";

// Firebase Initialization
const firebaseConfig = {
    apiKey: "AIzaSyDcDtx04xieyjnOuaEqfF3MivDPqrpwSek",
    authDomain: "ecollege-a4273.firebaseapp.com",
    projectId: "ecollege-a4273",
    storageBucket: "ecollege-a4273.firebasestorage.app",
    messagingSenderId: "1085583617375",
    appId: "1:1085583617375:web:e75073612c258f1fecdaea",
    measurementId: "G-DTQJC7EF36"
};

if (!firebase.apps.length) {
    firebase.initializeApp(firebaseConfig);
}

const db = firebase.firestore();

// Function to get all users (removes the username filter)
async function getAllUsers() {
    try {
        const ref = db.collection("user");
        const querySnapshot = await ref.get();  // No `where` clause, fetching all users

        if (querySnapshot.empty) {
            console.log("No users found.");
            return [];
        }

        const users = [];
        querySnapshot.forEach((doc) => {
            users.push({ id: doc.id, ...doc.data() });
        });

        return users;
    } catch (error) {
        console.error("Error querying Firestore:", error);
        return [];
    }
}

const App = () => {
    const [users, setUsers] = useState([]);

    useEffect(() => {
        // Fetch all users on component mount
        async function fetchUsers() {
            const result = await getAllUsers();
            setUsers(result); // Update state with the fetched users
        }

        fetchUsers();
    }, []); // Empty dependency array means this runs once after the component mounts

    return (
        <div>
            <h1>All Users</h1>
            <ul>
                {users.length > 0 ? (
                    users.map((user) => (
                        <li key={user.id}>
                            <strong>{user.username}</strong>: {user.email}
                        </li>
                    ))
                ) : (
                    <p>No users found</p>
                )}
            </ul>
        </div>
    );
};

export default App;
