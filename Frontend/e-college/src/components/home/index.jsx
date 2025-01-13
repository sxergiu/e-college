import React, { useState, useCallback, useEffect, useRef } from 'react';
import { useAuth } from '../../auth_context';
import { auth } from '../../firebase/firebase';
import axios from 'axios';

const Home = () => {
  const { userLoggedIn } = useAuth();
  const currentUser = auth.currentUser;
  const fileInputRef = useRef(null);

  const [profileData, setProfileData] = useState({
    balance: 0,
    name: '',
    username: '',
    email: '',
    phone: '',
    address: '',
    university: '',
    bio: '',
    image: '',
    rating: 0,
  });
  const [isEditing, setIsEditing] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [isDataChanged, setIsDataChanged] = useState(false);
  const [uploadingImage, setUploadingImage] = useState(false);
  const [isModalOpen, setModalOpen] = useState(false);

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const response = await fetch(`http://localhost:8080/user/getUser/${currentUser.uid}`);
        if (!response.ok) throw new Error('Failed to fetch user data');

        const userData = await response.json();
        setProfileData({
          id: userData.id,
          name: userData.name || 'No name set',
          username: userData.username || 'No username set',
          email: userData.email,
          phone: userData.phone || '',
          address: userData.address || 'No address set',
          university: userData.university || 'Not specified',
          bio: userData.bio || '',
          balance: userData.balance || 0,
          image: userData.image || 'https://via.placeholder.com/80',
          rating: userData.rating || 0,
        });
        setIsLoading(false);
      } catch (error) {
        console.error('Error fetching user data:', error);
        alert('Failed to load user data.');
      }
    };

    if (currentUser) fetchUserData();
  }, [currentUser]);

  const handleImageUpload = async (event) => {
    const file = event.target.files[0];
    if (!file) return;

    try {
      setUploadingImage(true);

      const formData = new FormData();
      formData.append('file', file);
      formData.append('upload_preset', 'image_upload'); // Cloudinary preset
      formData.append('cloud_name', 'dqfiftv8y'); // Cloudinary cloud name

      const response = await axios.post(
        `https://api.cloudinary.com/v1_1/dqfiftv8y/image/upload`,
        formData
      );

      if (response.data.secure_url) {
        setProfileData((prev) => ({ ...prev, image: response.data.secure_url }));
        setIsDataChanged(true); // Mark as data changed
      } else {
        console.error('Failed to upload image, no URL returned.');
      }
    } catch (error) {
      console.error('Error uploading image to Cloudinary:', error);
      alert('Failed to upload image. Please try again.');
    } finally {
      setUploadingImage(false);
    }
  };

  const handleDataChanged = useCallback((field, value) => {
    setProfileData((prev) => ({
      ...prev,
      [field]: value,
    }));
    setIsDataChanged(true);
  }, []);

  const handleSave = useCallback(async () => {
    try {
      if (!profileData.name || !profileData.university) {
        alert('Please fill out all required fields before saving.');
        return;
      }

      if (!profileData.image) {
        console.error('No image URL set.');
        alert('Image upload failed or URL is missing.');
        return;
      }

      const response = await fetch(`http://localhost:8080/user/updateUser`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(profileData),
      });

      if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(errorMessage);
      }

      alert('Profile updated successfully!');
      setIsEditing(false);
    } catch (error) {
      console.error('Error updating profile:', error);
      alert(`Failed to update profile: ${error.message}`);
    }
  }, [profileData]);

  const handleCancel = useCallback(() => {
    setIsEditing(false);
    setIsDataChanged(false);
  }, []);

  const AddFundsModal = ({ isOpen, onClose, onFundsAdded }) => {
    const [amount, setAmount] = useState('');
    const [isAdding, setIsAdding] = useState(false);

    const handleAddFunds = async () => {
      if (!amount || isNaN(amount) || amount <= 0) {
        alert('Please enter a valid amount.');
        return;
      }

      try {
        setIsAdding(true);
        const response = await axios.post(
          `http://localhost:8080/user/addFunds/${auth.currentUser.uid}`,
          null,
          { params: { amount } }
        );

        alert('Funds added successfully!');
        onFundsAdded(parseFloat(amount)); // Notify parent component about the updated balance
        onClose(); // Close the modal
      } catch (error) {
        console.error('Error adding funds:', error);
        alert('Failed to add funds. Please try again later.');
      } finally {
        setIsAdding(false);
      }
    };

    if (!isOpen) return null;

    return (
      <div className="fixed inset-0 bg-gray-500 bg-opacity-50 flex justify-center items-center">
        <div className="bg-white p-6 rounded shadow-lg w-96">
          <h2 className="text-xl font-bold mb-4">Add Funds</h2>
          <input
            type="number"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
            placeholder="Enter amount"
            className="border rounded px-4 py-2 w-full mb-4"
          />
          <div className="flex justify-end space-x-2">
            <button
              className="px-4 py-2 bg-gray-300 rounded"
              onClick={onClose}
              disabled={isAdding}
            >
              Cancel
            </button>
            <button
              className={`px-4 py-2 rounded ${isAdding ? 'bg-indigo-300' : 'bg-indigo-600 text-white'}`}
              onClick={handleAddFunds}
              disabled={isAdding}
            >
              {isAdding ? 'Adding...' : 'Add Funds'}
            </button>
          </div>
        </div>
      </div>
    );
  };

  if (!userLoggedIn) {
    return (
      <div className="pt-16 px-4">
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
          Please log in to view this page.
        </div>
      </div>
    );
  }

  if (isLoading) {
    return (
      <div className="pt-16 px-4">
        <p>Loading profile...</p>
      </div>
    );
  }

  return (
    <div className="pt-16 px-4 max-w-4xl mx-auto">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">User Profile</h1>
        <div className="space-x-2">
          {isEditing ? (
            <>
              <button
                className="px-4 py-2 border rounded-lg bg-white text-gray-700 hover:bg-gray-50"
                onClick={handleCancel}
              >
                Cancel
              </button>
              <button
                className={`px-4 py-2 rounded-lg ${!isDataChanged ? 'bg-indigo-300 cursor-not-allowed' : 'bg-indigo-600 hover:bg-indigo-700'
                  } text-white`}
                disabled={!isDataChanged}
                onClick={handleSave}
              >
                Save Changes
              </button>
            </>
          ) : (
            <button
              className="px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700"
              onClick={() => setIsEditing(true)}
            >
              Edit Profile
            </button>
          )}
          <button
            className="px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700"
            onClick={() => setModalOpen(true)}
          >
            Add Funds
          </button>
        </div>
      </div>

      <div className="flex space-x-6">
        <div className="flex-1 bg-white rounded-lg shadow p-6">
          <h2 className="text-xl font-semibold mb-4">Basic Information</h2>
          <div className="flex items-center space-x-4 mb-4">
            <div className="relative">
              <img
                src={profileData.image}
                alt="Profile"
                className="w-20 h-20 rounded-full object-cover"
              />
              {isEditing && (
                <>
                  <input
                    type="file"
                    ref={fileInputRef}
                    onChange={handleImageUpload}
                    accept="image/*"
                    className="hidden"
                  />
                  <button
                    onClick={() => fileInputRef.current?.click()}
                    className="absolute bottom-0 right-0 bg-white p-1 rounded-full shadow-lg border"
                    disabled={uploadingImage}
                  >
                    {uploadingImage ? (
                      <span className="w-6 h-6 block">...</span>
                    ) : (
                      <svg
                        xmlns="http://www.w3.org/2000/svg"
                        className="h-6 w-6 text-gray-600"
                        fill="none"
                        viewBox="0 0 24 24"
                        stroke="currentColor"
                      >
                        <path
                          strokeLinecap="round"
                          strokeLinejoin="round"
                          strokeWidth={2}
                          d="M3 9a2 2 0 012-2h.93a2 2 0 001.664-.89l.812-1.22A2 2 0 0110.07 4h3.86a2 2 0 011.664.89l.812 1.22A2 2 0 0018.07 7H19a2 2 0 012 2v9a2 2 0 01-2 2H5a2 2 0 01-2-2V9z"
                        />
                        <path
                          strokeLinecap="round"
                          strokeLinejoin="round"
                          strokeWidth={2}
                          d="M15 13a3 3 0 11-6 0 3 3 0 016 0z"
                        />
                      </svg>
                    )}
                  </button>
                </>
              )}
            </div>
            <div>
              <h2 className="text-xl font-semibold">
                {isEditing ? (
                  <input
                    type="text"
                    value={profileData.name}
                    onChange={(e) => handleDataChanged('name', e.target.value)}
                    className="border rounded px-2 py-1"
                  />
                ) : (
                  profileData.name
                )}
              </h2>
              <p className="text-gray-600">Username: {profileData.username}</p>
            </div>
          </div>
          <div className="mb-4">
            <h3 className="text-lg font-bold">Balance: ${profileData.balance.toFixed(
              2)}</h3>
          </div>

          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700">Phone Number</label>
              {isEditing ? (
                <input
                  type="tel"
                  value={profileData.phone}
                  onChange={(e) => handleDataChanged('phone', e.target.value)}
                  className="border rounded px-2 py-1 w-full"
                  placeholder="Enter phone number"
                />
              ) : (
                <p className="mt-1">{profileData.phone || 'No phone number set'}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">Address</label>
              {isEditing ? (
                <input
                  type="text"
                  value={profileData.address}
                  onChange={(e) => handleDataChanged('address', e.target.value)}
                  className="border rounded px-2 py-1 w-full"
                />
              ) : (
                <p className="mt-1">{profileData.address || 'No address set'}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">University</label>
              {isEditing ? (
                <input
                  type="text"
                  value={profileData.university}
                  onChange={(e) => handleDataChanged('university', e.target.value)}
                  className="border rounded px-2 py-1 w-full"
                  placeholder="Enter university name"
                />
              ) : (
                <p className="mt-1">{profileData.university}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">Rating</label>
              <div className="flex items-center space-x-2">
                <span className="text-xl">⭐</span>
                <span className="text-lg font-medium">{profileData.rating.toFixed(1)}</span>
                <span className="text-gray-500">/ 5.0</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <AddFundsModal
        isOpen={isModalOpen}
        onClose={() => setModalOpen(false)}
        onFundsAdded={(addedFunds) => {
          setProfileData((prev) => ({
            ...prev,
            balance: prev.balance + addedFunds,
          }));
        }}
      />
    </div>
  );
};
export default Home;