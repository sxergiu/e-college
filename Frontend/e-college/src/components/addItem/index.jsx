import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom'; // Import useNavigate for navigation
import { useAuth } from '../../auth_context';
import { auth } from '../../firebase/firebase';

const AddItem = () => {
    const currentUser = auth.currentUser;
    const { userLoggedIn } = useAuth();
    const navigate = useNavigate(); // Initialize useNavigate

    const [formData, setFormData] = useState({
        category: '',
        condition: '',
        name: '',
        description: '',
        price: '',
        image: '',
    });

    const [isSubmitting, setIsSubmitting] = useState(false);
    const [error, setError] = useState(null);
    const [successMessage, setSuccessMessage] = useState('');

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleImageChange = (e) => {
        setFormData({ ...formData, image: e.target.files[0] });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsSubmitting(true);
        setError(null);
        setSuccessMessage('');
      
        if (!currentUser?.uid) {
            setError('You must be logged in to add an item.');
            setIsSubmitting(false);
            return;
        }
    
        try {
            // Prepare image upload to Cloudinary
            let imageUrl = null;
            console.log(formData.image)
            if (formData.image) {
                const formDataToUpload = new FormData();
                formDataToUpload.append('file', formData.image);
                formDataToUpload.append('upload_preset', 'image_upload'); // Upload preset
                formDataToUpload.append('cloud_name', 'dqfiftv8y'); // Cloud name
                
                const cloudinaryResponse = await fetch('https://api.cloudinary.com/v1_1/dqfiftv8y/upload', {
                    method: 'POST',
                    body: formDataToUpload,
                });

                if (!cloudinaryResponse.ok) {
                    const errorData = await cloudinaryResponse.json(); // Try to get JSON error
                    console.error("Cloudinary Error:", errorData); // Log the full error
                    throw new Error(`Image upload to Cloudinary failed: ${cloudinaryResponse.status} ${cloudinaryResponse.statusText}`);
                }
    
                const cloudinaryData = await cloudinaryResponse.json();
                console.log("Cloudinary Response:", cloudinaryData);
                if (cloudinaryData.secure_url) {
                    imageUrl = cloudinaryData.secure_url; // Get the image URL
                    console.log(imageUrl)
                } else {
                    throw new Error('Image upload to Cloudinary failed.');
                }
            }
    
            // Build the payload for backend
            console.log("The item sent to backend",
                formData.category,
                formData.condition,
                formData.name,
                formData.description,
                parseFloat(formData.price),
                currentUser.uid,
                imageUrl,)
            const jsonPayload = {
                category: formData.category,
                condition: formData.condition,
                name: formData.name,
                description: formData.description,
                price: parseFloat(formData.price),
                sellerId: currentUser.uid,
                images: [imageUrl], // Set the image URL returned from Cloudinary
            };
    
            // Send the data to the backend Spring application
            const response = await fetch('http://localhost:8080/item/addItem', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(jsonPayload),
            });
    
            if (!response.ok) {
                throw new Error('Failed to add item. Please try again.');
            }
    
            setSuccessMessage('Item added successfully!');
            setFormData({
                category: '',
                condition: '',
                name: '',
                description: '',
                price: '',
                image: '',
            });
        } catch (err) {
            setError(err.message);
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="pt-16 px-4 max-w-4xl mx-auto">
            <h1 className="text-2xl font-bold mb-6">Add New Item</h1>
            <div className="flex justify-between items-center mb-6">
                <p className="text-lg font-medium text-gray-700">
                    Complete the form to list a new item.
                </p>
            </div>
            <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                    <label className="block text-gray-700 mb-2">Category</label>
                    <select
                        name="category"
                        value={formData.category}
                        onChange={handleInputChange}
                        className="border border-gray-300 rounded w-full p-2"
                        required
                    >
                        <option value="" disabled>Select category</option>
                        <option value="Electronic">Electronics</option>
                        <option value="Furniture">Furniture</option>
                        <option value="Book">Book</option>
                        <option value="Clothing">Clothing</option>
                    </select>
                </div>
                <div>
                    <label className="block text-gray-700 mb-2">Condition</label>
                    <select
                        name="condition"
                        value={formData.condition}
                        onChange={handleInputChange}
                        className="border border-gray-300 rounded w-full p-2"
                        required
                    >
                        <option value="" disabled>Select condition</option>
                        <option value="New">New</option>
                        <option value="Used">Used</option>
                        <option value="Like new">Like new</option>
                    </select>
                </div>
                <div>
                    <label className="block text-gray-700 mb-2">Name</label>
                    <input
                        type="text"
                        name="name"
                        value={formData.name}
                        onChange={handleInputChange}
                        className="border border-gray-300 rounded w-full p-2"
                        placeholder="Item name"
                        required
                    />
                </div>
                <div>
                    <label className="block text-gray-700 mb-2">Description</label>
                    <textarea
                        name="description"
                        value={formData.description}
                        onChange={handleInputChange}
                        className="border border-gray-300 rounded w-full p-2"
                        placeholder="Item description"
                        rows="4"
                        required
                    />
                </div>
                <div>
                    <label className="block text-gray-700 mb-2">Price ($)</label>
                    <input
                        type="number"
                        name="price"
                        value={formData.price}
                        onChange={handleInputChange}
                        className="border border-gray-300 rounded w-full p-2"
                        placeholder="Item price"
                        min="0"
                        step="0.01"
                        required
                    />
                </div>
                <div>
                    <label className="block text-gray-700 mb-2">Upload Image</label>
                    <input
                        type="file"
                        accept="image/*"
                        onChange={handleImageChange}
                        className="border border-gray-300 rounded w-full p-2"
                    />
                </div>
                {error && <p className="text-red-600">{error}</p>}
                {successMessage && <p className="text-green-600">{successMessage}</p>}
                <div className="flex justify-end gap-20">
            <button
                type="submit"
                className="bg-indigo-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors"
                disabled={isSubmitting}
            >
                {isSubmitting ? 'Adding...' : 'Add Item'}
            </button>
            <button
                className="bg-gray-200 text-gray-700 px-4 py-2 rounded-lg hover:bg-gray-300 transition-colors"
                onClick={() => navigate('/my-items')} // Go back to My Items
            >
                Go Back
            </button>
        </div>

            </form>
        </div>
    );
};

export default AddItem;
