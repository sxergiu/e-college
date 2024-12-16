import firebase from "firebase/compat";

const db = firebase.firestore();

async function getUsersByUsername(username) {
    try {
        const ref = db.collection('user');
        const querySnapshot = await ref.where('username', '==', username).get();

        if (querySnapshot.empty) {
            console.log("No matching documents.");
            return;
        }

        querySnapshot.forEach((doc) => {
            console.log(`User ID: ${doc.id}`, doc.data());
        });
    } catch (error) {
        console.error("Error querying Firestore:", error);
    }
}

getUsersByUsername('jeff')