import React, { useState, useEffect } from 'react';
import { useAuth } from '../../auth_context';
import { auth } from '../../firebase/firebase';
import { useNavigate } from 'react-router-dom'; // Import useNavigate
import ItemCard from '../itemCard';

const MyItems = () => {
    const navigate = useNavigate(); // Initialize navigate
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
                const response = await fetch(`http://localhost:8080/item/getItemBySellerId/${currentUser.uid}`);

                if (!response.ok) {
                    if (response.status === 404) {
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

        const refreshItems = async () => {
            const response = await fetch("http://localhost:8080/item/getItems");
            if (!response.ok) {
              throw new Error("Failed to fetch items.");
            }
            const items = await response.json();
            setItems(items);  // Update the state with the new list
          };
        
          // Navigate to the edit page
          const editItem = (id) => {
            navigate(`/my-items/edit/${id}`);
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

    const editItem = (itemId) => {
        console.log('Navigating to edit item:', itemId);
        navigate(`/my-items/edit-item/${itemId}`);
      };

    const deleteItem = async (itemId) => {
        if (!window.confirm("Are you sure you want to delete this item?")) {
            return; // Exit if user cancels
        }

        try {
            const response = await fetch(`http://localhost:8080/item/deleteItem/${itemId}`, {
                method: 'DELETE',
            });

            if (!response.ok) {
                throw new Error(`Failed to delete item with ID: ${itemId}`);
            }

            // Remove the deleted item from the list
            setItems((prevItems) => prevItems.filter((item) => item.id !== itemId));
            alert('Item deleted successfully!');
        } catch (error) {
            console.error('Error deleting item:', error);
            alert('Failed to delete item. Please try again.');
        }
    };

    return (
        <div className="pt-16 px-4 max-w-7xl mx-auto">
            <div className="mb-6">
                <h1 className="text-2xl font-bold mb-4">My Items</h1>
                <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
                    <p className="text-gray-600">
                        {items.length} item{items.length !== 1 ? 's' : ''} listed
                    </p>
                    <button 
                        className="bg-indigo-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors"
                        onClick={() => navigate('/my-items/add-item')} // Navigate to item upload page
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
                </div>
            ) : (
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                    {items.map(item => (
                        <ItemCard 
                            key={item.id} 
                            {...item} 
                            isMyItem={item.sellerId === currentUser.uid}
                            onEdit={() => editItem(item.id)} 
                            onDelete={() => deleteItem(item.id)} 
                        />
                    ))}
                </div>
            )}
        </div>
    );
};

export default MyItems;
