// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
    apiKey: "AIzaSyDcDtx04xieyjnOuaEqfF3MivDPqrpwSek",
    authDomain: "ecollege-a4273.firebaseapp.com",
    projectId: "ecollege-a4273",
    storageBucket: "ecollege-a4273.firebasestorage.app",
    messagingSenderId: "1085583617375",
    appId: "1:1085583617375:web:e75073612c258f1fecdaea",
    measurementId: "G-DTQJC7EF36"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);

export { app, analytics };