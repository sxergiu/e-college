import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { auth } from '../../firebase/firebase';

const EditItemPage = () => {
  const currentUser = auth.currentUser;
  const navigate = useNavigate();
  const { itemId } = useParams(); // Assuming dynamic routing
  const [itemData, setItemData] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [formData, setFormData] = useState({
    name: "",
    description: "",
    price: "",
    condition: "",
    category: "",
    images: [],
  });

  // Fetch item data on component mount
  useEffect(() => {

    if (!currentUser?.uid) {
      // If no user is logged in, handle accordingly (e.g., redirect or show error)
      navigate("/login");  // Redirect to login page
      return;
    }

    const fetchItem = async () => {
      setIsLoading(true);
      try {
        // Update the URL to include the currentUser.uid in the request.
        const response = await fetch(`http://localhost:8080/item/getItemById/${itemId}`);
        if (!response.ok) {
          throw new Error("Failed to fetch item.");
        }
        const item = await response.json();
        
        console.log("Fetched item:", item);  // Log fetched item

        // Update the state with fetched item data
        setItemData(item);

        // Set form data directly from the fetched item
        setFormData({
          name: item.name || "",
          description: item.description || "",
          price: item.price || "",
          condition: item.condition || "",
          category: item.category || "",
          images: item.images || [],
        });
      } catch (error) {
        console.error("Failed to fetch item:", error);
        alert("Failed to load item data.");
      } finally {
        setIsLoading(false);
      }
    };

    fetchItem();
  }, [itemId, currentUser?.uid, navigate]);


  // Handle input changes
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  // Handle image upload
  const handleImageUpload = (e) => {
    const files = Array.from(e.target.files);
    const uploadedImages = files.map((file) => URL.createObjectURL(file)); // Temporary URLs for preview
    setFormData((prev) => ({
      ...prev,
      images: [...prev.images, ...uploadedImages],
    }));
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
  
    // Ensure sellerId is set correctly (currentUser.uid)
    const sellerId = currentUser?.uid;  // Get the seller's user ID from the current logged-in user
  
    if (!sellerId) {
      alert("You must be logged in to update an item.");
      return;
    }
  
    // Create the updated data object with all form fields and the sellerId
    const updatedData = {
      sellerId: sellerId,  // Set sellerId
      name: formData.name,
      description: formData.description,
      price: parseFloat(formData.price),  // Ensure price is sent as a number
      condition: formData.condition,
      category: formData.category,
      images: formData.images, // Assuming images are updated or unchanged
    };
  
    console.log("Updated data to send:", updatedData);
  
    try {
      setIsLoading(true);
      const response = await fetch(`http://localhost:8080/item/updateItem/${itemId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(updatedData),
      });
  
      if (!response.ok) {
        throw new Error("Failed to update item.");
      }
  
      alert("Item updated successfully!");
      navigate("/my-items"); // Navigate back to "My Items" page
    } catch (error) {
      console.error("Failed to update item:", error);
      alert("Failed to update item.");
    } finally {
      setIsLoading(false);
    }
  };
  

  return (
    <div className="max-w-3xl mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Edit Item</h1>
      <form onSubmit={handleSubmit}>
        <div className="mb-4">
          <label className="block text-sm font-medium mb-1">Name</label>
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleInputChange}
            className="w-full border rounded-lg p-2"
          />
        </div>

        <div className="mb-4">
          <label className="block text-sm font-medium mb-1">Description</label>
          <textarea
            name="description"
            value={formData.description}
            onChange={handleInputChange}
            className="w-full border rounded-lg p-2"
          ></textarea>
        </div>

        <div className="mb-4">
          <label className="block text-sm font-medium mb-1">Price</label>
          <input
            type="number"
            name="price"
            value={formData.price}
            onChange={handleInputChange}
            className="w-full border rounded-lg p-2"
          />
        </div>

        <div className="mb-4">
          <label className="block text-sm font-medium mb-1">Condition</label>
          <select
            name="condition"
            value={formData.condition}
            onChange={handleInputChange}
            className="w-full border rounded-lg p-2"
          >
            <option value="">Select Condition</option>
            <option value="new">New</option>
            <option value="used">Used</option>
          </select>
        </div>

        <div className="mb-4">
          <label className="block text-sm font-medium mb-1">Category</label>
          <input
            type="text"
            name="category"
            value={formData.category}
            onChange={handleInputChange}
            className="w-full border rounded-lg p-2"
          />
        </div>

        <div className="mb-4">
          <label className="block text-sm font-medium mb-1">Images</label>
          <input
            type="file"
            multiple
            onChange={handleImageUpload}
            className="w-full border rounded-lg p-2"
          />
          <div className="flex gap-2 mt-2">
            {formData.images.map((image, index) => (
              <div key={index} className="relative">
                <img src={image} alt={`Item ${index}`} className="w-24 h-24 object-cover rounded-lg" />
                <button
                  type="button"
                  className="absolute top-0 right-0 bg-red-500 text-white p-1 rounded-full"
                  onClick={() => handleImageRemove(index)}
                >
                  ✕
                </button>
              </div>
            ))}
          </div>
        </div>

        <div className="mt-4">
          <button
            type="submit"
            className="bg-blue-500 text-white px-4 py-2 rounded-lg"
            disabled={isLoading}
          >
            {isLoading ? "Updating..." : "Update Item"}
          </button>
        </div>
      </form>
    </div>
  );
};

export default EditItemPage;
