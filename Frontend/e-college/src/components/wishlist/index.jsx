import React, { useState, useEffect } from 'react';
import ItemCard from '../itemCard';
import { wishlistService } from '../wishlistService';
import { auth } from '../../firebase/firebase'

const Wishlist = () => {
  const currentUser = auth.currentUser
  const [wishlistItems, setWishlistItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchWishlistItems = async () => {
    if (!currentUser.uid) {
      setLoading(false);
      return;
    }

    try {
      setLoading(true);
      setError(null);
      const items = await wishlistService.getWishlistItems(currentUser.uid);
      setWishlistItems(items);
    } catch (error) {
      console.error('Error fetching wishlist:', error);
      setError('Failed to load wishlist items. Please try again later.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchWishlistItems();
  }, [currentUser.uid]);

  if (!currentUser.uid) {
    return (
      <div className="pt-12 px-4">
        <div className="max-w-7xl mx-auto">
          <div className="text-center p-8 bg-gray-50 rounded-lg">
            <h2 className="text-xl font-semibold text-gray-600">Please log in to view your wishlist</h2>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="pt-12 px-4">
      <div className="max-w-7xl mx-auto">
        <h1 className="text-2xl font-bold mb-6">My Wishlist</h1>

        {loading ? (
          <div className="text-center p-8">
            <p>Loading wishlist items...</p>
          </div>
        ) : error ? (
          <div className="text-center p-8 bg-red-50 rounded-lg">
            <p className="text-red-600">{error}</p>
          </div>
        ) : wishlistItems.length === 0 ? (
          <div className="text-center p-8 bg-gray-50 rounded-lg">
            <h2 className="text-xl font-semibold text-gray-600">Your wishlist is empty</h2>
            <p className="text-gray-500 mt-2">Items you add to your wishlist will appear here</p>
          </div>
        ) : (
          <div className="grid gap-6 grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4">
            {wishlistItems.map((item) => (
              <ItemCard
              key={item.id} // Use item ID as key
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
              isWishlisted={true}
              onWishlistUpdate={fetchWishlistItems}
              userId={currentUser.uid}
              isMyItem={item.sellerId === currentUser.uid}
            />
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default Wishlist;