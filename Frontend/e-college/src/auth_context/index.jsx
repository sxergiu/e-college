import React, { useContext, useState, useEffect } from "react";
import { auth } from "../firebase/firebase";
import { getFirestore, doc, setDoc, getDoc } from "firebase/firestore"; // Firestore imports
import { onAuthStateChanged } from "firebase/auth";

const AuthContext = React.createContext();

export function useAuth() {
  return useContext(AuthContext);
}

export function AuthProvider({ children }) {
  const [currentUser, setCurrentUser] = useState(null);
  const [userLoggedIn, setUserLoggedIn] = useState(false);
  const [isEmailUser, setIsEmailUser] = useState(false);
  const [isGoogleUser, setIsGoogleUser] = useState(false);
  const [loading, setLoading] = useState(true);

  const db = getFirestore(); // Initialize Firestore

  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, initializeUser);
    return unsubscribe;
  }, []);

  async function initializeUser(user) {
    if (user) {
      setCurrentUser({ ...user });

      // Check if provider is email/password login
      const isEmail = user.providerData.some(
        (provider) => provider.providerId === "password"
      );
      setIsEmailUser(isEmail);

      // Check if provider is Google login (optional logic)
      // const isGoogle = user.providerData.some(
      //   (provider) => provider.providerId === GoogleAuthProvider.PROVIDER_ID
      // );
      // setIsGoogleUser(isGoogle);

      setUserLoggedIn(true);

      // Save user info to Firestore
      await linkUserWithFirestore(user);
    } else {
      setCurrentUser(null);
      setUserLoggedIn(false);
    }

    setLoading(false);
  }

  // Firestore logic to link user data
  async function linkUserWithFirestore(user) {
    try {
      const userRef = doc(db, "users", user.uid); // Firestore 'users' collection
      const userDoc = await getDoc(userRef);

      if (!userDoc.exists()) {
        // If user doesn't exist in Firestore, create a new document
        await setDoc(userRef, {
          uid: user.uid,
          email: user.email,
          username: user.usename || "Anonymous",
          createdAt: new Date().toISOString(),
          provider: user.providerData.map((provider) => provider.providerId),
        });
        console.log("User added to Firestore.");
      } else {
        console.log("User already exists in Firestore.");
      }
    } catch (error) {
      console.error("Error linking user with Firestore:", error.message);
    }
  }

  const value = {
    userLoggedIn,
    isEmailUser,
    isGoogleUser,
    currentUser,
    setCurrentUser,
  };

  return (
    <AuthContext.Provider value={value}>
      {!loading && children}
    </AuthContext.Provider>
  );
}
