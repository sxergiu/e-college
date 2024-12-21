import { getFirestore, doc, setDoc } from "firebase/firestore";
import { auth } from "./firebase";
import {
  createUserWithEmailAndPassword,
  signInWithEmailAndPassword,
  sendPasswordResetEmail,
  sendEmailVerification,
  updatePassword,
  signInWithPopup,
  GoogleAuthProvider,
} from "firebase/auth";

// Initialize Firestore
const db = getFirestore();

// Function to create a user and save their data to Firestore
export async function doCreateUserWithEmailAndPassword(email, password, username) {
  try {
    // Create user with email and password in Firebase Authentication
    const userCredential = await createUserWithEmailAndPassword(auth, email, password);
    const user = userCredential.user;

    // Save the user to Firestore
    const userRef = doc(db, "users", user.uid); // 'users' collection with document ID = uid
    await setDoc(userRef, {
      uid: user.uid,
      email: user.email,
      username: username, // Use the passed `username` argument
      createdAt: new Date().toISOString(),
    });

    console.log("User successfully registered and added to Firestore!");
    return user; // Optionally return the user object
  } catch (error) {
    console.error("Error creating user:", error.message);
    throw error; // Re-throw the error to handle it in the calling component
  }
}

// Function to sign in with email and password
export const doSignInWithEmailAndPassword = (email, password) => {
  return signInWithEmailAndPassword(auth, email, password);
};

// Function to sign in with Google and save the user to Firestore (if needed)
export const doSignInWithGoogle = async () => {
  const provider = new GoogleAuthProvider();
  const result = await signInWithPopup(auth, provider);
  const user = result.user;

  // Save the user to Firestore if it's their first login
  const userRef = doc(db, "users", user.uid);
  await setDoc(userRef, {
    uid: user.uid,
    email: user.email,
    username: user.email.split("@")[0], // Default username to part of their email
    createdAt: new Date().toISOString(),
  }, { merge: true }); // Use merge to avoid overwriting existing data
};

// Other Firebase utility functions
export const doSignOut = () => {
  return auth.signOut();
};

export const doPasswordReset = (email) => {
  return sendPasswordResetEmail(auth, email);
};

export const doPasswordChange = (password) => {
  return updatePassword(auth.currentUser, password);
};

export const doSendEmailVerification = () => {
  return sendEmailVerification(auth.currentUser, {
    url: `${window.location.origin}/home`,
  });
};

// Export Firestore instance
export { db };
