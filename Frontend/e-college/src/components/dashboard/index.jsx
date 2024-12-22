import React, { useEffect, useState } from "react";
import axios from "axios";
import ItemCard from '../itemCard/index'; // Import the ItemCard component

const Dashboard = () => {
  const [items, setItems] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  // Fetch items on component load
  useEffect(() => {
    const fetchItems = async () => {
      try {
        const response = await axios.get("http://localhost:8080/item/returnAllItems"); // Adjust URL as needed
        setItems(response.data); // Set the fetched data
        setLoading(false);
      } catch (err) {
        console.error("Error fetching items:", err);
        setError("Failed to fetch items. Please try again later.");
        setLoading(false);
      }
    };

    fetchItems();
  }, []);

  return (
    <div className="pt-12 px-4">
      <div className="max-w-7xl mx-auto">
        <h1 className="text-2xl font-bold mb-6">Dashboard</h1>

        {/* Item Listing Section */}
        <div className="mt-8">
          <h2 className="text-xl font-bold mb-4">Items Listed by Sellers</h2>

          {loading ? (
            <p>Loading items...</p>
          ) : error ? (
            <p className="text-red-500">{error}</p>
          ) : Object.keys(items).length === 0 ? (
            <p>No items available.</p>
          ) : (
            Object.entries(items).map(([username, sellerItems]) => (
              <div key={username} className="mb-6">
                <h3 className="text-lg font-semibold mb-4">Seller: {username}</h3>

                {/* Display a grid of ItemCard components */}
                <div className="grid gap-6 grid-cols-1 sm:grid-cols-2 md:grid-cols-3">
                  {sellerItems.map((item) => (
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
