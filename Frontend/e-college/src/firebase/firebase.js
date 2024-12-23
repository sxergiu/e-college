import { initializeApp } from "firebase/app";
import { getAuth } from "firebase/auth";
import { getStorage } from "firebase/storage";  // Add this import

const firebaseConfig = {
  apiKey: "AIzaSyDcDtx04xieyjnOuaEqfF3MivDPqrpwSek",
  authDomain: "ecollege-a4273.firebaseapp.com",
  projectId: "ecollege-a4273",
  storageBucket: "ecollege-a4273.firebasestorage.app",
  messagingSenderId: "1085583617375",
  appId: "1:1085583617375:web:e75073612c258f1fecdaea",
  measurementId: "G-DTQJC7EF36"
};

const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
const storage = getStorage(app);  // Add this line

export { app, auth, storage };  // Export storage