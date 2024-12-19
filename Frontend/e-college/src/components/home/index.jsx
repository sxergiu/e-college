import React from 'react';
import { useAuth } from '../../auth_context';
import { auth } from '../../firebase/firebase';

const Home = () => {
    // Get current user from Firebase auth directly
    const currentUser = auth.currentUser;
    const { userLoggedIn } = useAuth();

    // Early return with loading state
    if (!userLoggedIn) {
        return (
            <div className="pt-16 px-4">
                <p>Please log in to view this page.</p>
            </div>
        );
    }

    return (
        <div className="pt-16 px-4">
            <h1 className="text-2xl font-bold mb-4">
                Welcome, {currentUser?.displayName || 'User'}!
            </h1>
            <div className="bg-white rounded-lg shadow p-6">
                <div className="space-y-4">
                    <p className="text-gray-600">
                        Email: {currentUser?.email || 'No email available'}
                    </p>
                    {/* Add more user information as needed */}
                </div>
            </div>
        </div>
    );
};

export default Home;