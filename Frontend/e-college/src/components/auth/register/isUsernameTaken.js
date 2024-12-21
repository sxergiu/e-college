import { query, collection, where, getDocs } from "firebase/firestore";
import { db } from "../../../firebase/auth"; // Import your Firestore instance

export const isUsernameTaken = async (username) => {
  const q = query(collection(db, "users"), where("username", "==", username));
  const querySnapshot = await getDocs(q);
  return !querySnapshot.empty; // Return true if the username already exists
};
