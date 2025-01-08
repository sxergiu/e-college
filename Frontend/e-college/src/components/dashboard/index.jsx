import React, { useEffect, useState } from "react";
import axios from "axios";
import ItemCard from '../itemCard/index'; // Import the ItemCard component
import { auth } from "../../firebase/firebase";

// SearchBar component
const SearchBar = ({ onSearch }) => {
  const [query, setQuery] = useState("");  // State for search query

  const handleChange = (e) => {
    setQuery(e.target.value);
  };

  const handleSearch = () => {
    onSearch(query); // Call onSearch passed from parent (Dashboard) with the search query
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleSearch();
    }
  };

  return (
    <div className="search-bar mb-6">
      <input
        type="text"
        value={query}
        onChange={handleChange}
        onKeyPress={handleKeyPress}
        placeholder="Search for items..."
        className="border p-2 rounded w-full sm:w-80"
      />
      <button onClick={handleSearch} className="ml-2 p-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700">
        Search
      </button>
    </div>
  );
};

const Dashboard = () => {
  const currentUser = auth.currentUser;
  const [items, setItems] = useState({});
  const [wishlist, setWishlist] = useState([]);
  const [filteredItems, setFilteredItems] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false); // Add this line to define the isLoading state

  // Fetch items on component load
  useEffect(() => {
    const fetchItems = async () => {
      try {
        setLoading(true); // Start loading
        const response = await axios.get("http://localhost:8080/item/returnAllItems");
        setItems(response.data);
        setFilteredItems(response.data);

        const wishlistResponse = await axios.get(`http://localhost:8080/wishlist/${currentUser.uid}`);
        setWishlist(wishlistResponse.data);

        setLoading(false); // Stop loading
      } catch (err) {
        console.error("Error fetching items:", err);
        setError("Failed to fetch items. Please try again later.");
        setLoading(false); // Stop loading on error
      }
    };

    fetchItems();
  }, []);

  // Search function to filter items
  const handleSearch = (query) => {
    if (!query) {
      setFilteredItems(items);
      return;
    }

    const lowerCaseQuery = query.toLowerCase();
    const filtered = {};

    Object.entries(items).forEach(([username, sellerItems]) => {
      const filteredSellerItems = sellerItems.filter(item =>
        item.name.toLowerCase().includes(lowerCaseQuery) ||
        item.description.toLowerCase().includes(lowerCaseQuery) ||
        item.category.toLowerCase().includes(lowerCaseQuery)
      );

      if (filteredSellerItems.length > 0) {
        filtered[username] = filteredSellerItems;
      }
    });

    setFilteredItems(filtered);
  };

  const handleWishlistUpdate = async () => {
    if (currentUser.uid) {
      try {
        setIsLoading(true); // Start loading
        const updatedWishlist = await axios.get(`http://localhost:8080/wishlist/${currentUser.uid}`);
        setWishlist(updatedWishlist.data);
      } catch (error) {
        console.error("Error updating wishlist:", error);
      } finally {
        setIsLoading(false); // Stop loading
      }
    }
  };

  return (
    <div className="pt-12 px-4">
      <div className="max-w-7xl mx-auto">
        <h1 className="text-2xl font-bold mb-6">Dashboard</h1>

        {/* Search Bar */}
        <SearchBar onSearch={handleSearch} />

        {/* Item Listing Section */}
        <div className="mt-8">
          <h2 className="text-xl font-bold mb-4">Items Listed by Sellers</h2>

          {loading ? (
            <p>Loading items...</p>
          ) : error ? (
            <p className="text-red-500">{error}</p>
          ) : Object.keys(filteredItems).length === 0 ? (
            <p>No items available.</p>
          ) : (
            Object.entries(filteredItems).map(([username, sellerItems]) => (
              <div key={username} className="mb-6">
                <h3 className="text-lg font-semibold mb-4">Seller: {username}</h3>

                {/* Display a grid of ItemCard components */}
                <div className="grid gap-6 grid-cols-1 sm:grid-cols-2 md:grid-cols-3">
                  {sellerItems.map((item) => (
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
                      onWishlistUpdate={handleWishlistUpdate}
                      userId={currentUser.uid} // Updated to use uid
                      isWishlisted={wishlist.productIds.includes(item.id)} // Check if item is in wishlist
                    />
                  ))}
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
