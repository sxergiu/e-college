import React, { useEffect, useState } from 'react';
import ItemCard from '../itemCard';
import axios from 'axios';

const Wishlist = () => {
    const [wishlistItems, setWishlistItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchWishlistItems = async () => {
            try {
                const response = await axios.get('/api/wishlist'); // Replace with the actual endpoint
                setWishlistItems(response.data);
                setLoading(false);
            } catch (err) {
                setError('Failed to fetch wishlist items');
                setLoading(false);
            }
        };

        fetchWishlistItems();
    }, []);

    const handleAddToWishlist = (itemId) => {
        console.log(`Add item with ID ${itemId} to wishlist`);
    };

    if (loading) return <div>Loading wishlist items...</div>;

    if (error) return <div>Error: {error}</div>;

    return (
        <div className="p-4">
            <h1 className="text-2xl font-bold mb-4">My Wishlist</h1>
            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
                {wishlistItems.length > 0 ? (
                    wishlistItems.map((item) => (
                        <ItemCard
                            key={item.id}
                            id={item.id}
                            sellerId={item.sellerId}
                            name={item.name}
                            description={item.description}
                            price={item.price}
                            images={item.images}
                            isSold={item.isSold}
                            condition={item.condition}
                            category={item.category}
                            createdAt={item.createdAt}
                            onAddToWishlist={handleAddToWishlist}
                        />
                    ))
                ) : (
                    <div>No items in your wishlist.</div>
                )}
            </div>
        </div>
    );
};

export default Wishlist;
