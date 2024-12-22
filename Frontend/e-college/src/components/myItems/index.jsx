import React, { useState, useEffect } from 'react';
import { useAuth } from '../../auth_context';
import { auth } from '../../firebase/firebase';
import ItemCard from '../itemCard';

const MyItems = () => {
    const currentUser = auth.currentUser;
    const { userLoggedIn } = useAuth();
    const [items, setItems] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchUserItems = async () => {
            if (!currentUser?.uid) return;
            
            setIsLoading(true);
            setError(null);
            
            try {
                const response = await fetch(`http://localhost:8080/item/getUserItems/${currentUser.uid}`);
                
                if (!response.ok) {
                    // If response is not 2xx, throw an error
                    if (response.status === 404) {
                        // No items found - set empty array
                        setItems([]);
                        return;
                    }
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                
                const data = await response.json();
                setItems(data);
            } catch (err) {
                setError('Failed to fetch your items. Please try again later.');
                console.error('Error fetching user items:', err);
            } finally {
                setIsLoading(false);
            }
        };

        if (userLoggedIn) {
            fetchUserItems();
        }
    }, [userLoggedIn, currentUser?.uid]);

    if (!userLoggedIn) {
        return (
            <div className="pt-16 px-4">
                <p>Please log in to view your items.</p>
            </div>
        );
    }

    return (
        <div className="pt-16 px-4 max-w-7xl mx-auto">
            <div className="mb-6">
                <h1 className="text-2xl font-bold mb-2">My Items</h1>
                <div className="flex justify-between items-center">
                    <p className="text-gray-600">
                        {items.length} item{items.length !== 1 ? 's' : ''} listed
                    </p>
                    <button 
                        className="bg-indigo-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors"
                        onClick={() => {
                            // Add navigation to create item page
                            // Example: navigate('/items/new');
                        }}
                    >
                        List New Item
                    </button>
                </div>
            </div>

            {isLoading ? (
                <div className="text-center py-8">
                    <p>Loading your items...</p>
                </div>
            ) : error ? (
                <div className="text-center py-8 text-red-600">
                    <p>{error}</p>
                </div>
            ) : items.length === 0 ? (
                <div className="text-center py-8 bg-gray-50 rounded-lg">
                    <p className="text-gray-600 mb-4">You haven't listed any items yet</p>
                    <button 
                        className="bg-indigo-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors"
                        onClick={() => {
                            // Add navigation to create item page
                            // Example: navigate('/items/new');
                        }}
                    >
                        List Your First Item
                    </button>
                </div>
            ) : (
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                    {items.map(item => (
                        <ItemCard key={item.id} {...item} />
                    ))}
                </div>
            )}
        </div>
    );
};

export default MyItems;