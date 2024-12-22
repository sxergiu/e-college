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
        image: null,
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
            const formDataToSend = new FormData();
            formDataToSend.append('category', formData.category);
            formDataToSend.append('condition', formData.condition);
            formDataToSend.append('name', formData.name);
            formDataToSend.append('description', formData.description);
            formDataToSend.append('price', parseFloat(formData.price));
            formDataToSend.append('userId', currentUser.uid);

            if (formData.image) {
                formDataToSend.append('image', formData.image);
            }

            const response = await fetch('http://localhost:8080/item/create', {
                method: 'POST',
                body: formDataToSend,
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
                image: null,
            });
        } catch (err) {
            setError(err.message);
        } finally {
            setIsSubmitting(false);
        }
    };

    if (!userLoggedIn) {
        return (
            <div className="pt-16 px-4">
                <p>Please log in to add an item.</p>
            </div>
        );
    }

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
                        <option value="Electronics">Electronics</option>
                        <option value="Furniture">Furniture</option>
                        <option value="Books">Books</option>
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
                        <option value="Used - Like New">Used - Like New</option>
                        <option value="Used - Good">Used - Good</option>
                        <option value="Used - Fair">Used - Fair</option>
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
