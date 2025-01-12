import React, { useState } from 'react';
import { Clock, DollarSign, User, Heart } from "lucide-react";
import { wishlistService } from '../wishlistService';
import { createChat } from '../chatService';
import { useNavigate } from 'react-router-dom';

const ItemCard = ({ 
  id = '',
  sellerId = '',
  name = 'Untitled Item',
  description = '',
  price = 0,
  images = [],
  isSold = false,
  condition = '',
  category = '',
  createdAt,
  isWishlisted = false,
  userId,
  onWishlistUpdate,
  isMyItem = false,
  onEdit,
  onDelete,
}) => {
  const [currentImageIndex, setCurrentImageIndex] = useState(0); // For image carousel
  const [isLoading, setIsLoading] = useState(false);
  const [isItemWishlisted, setIsItemWishlisted] = useState(isWishlisted);
  const navigate = useNavigate();

  const handleWishlistToggle = async () => {
    if (!userId || isMyItem) return;

    try {
      setIsLoading(true);
      const newWishlistStatus = !isItemWishlisted;
      setIsItemWishlisted(newWishlistStatus);

      if (newWishlistStatus) {
        await wishlistService.addToWishlist(userId, id);
      } else {
        await wishlistService.removeFromWishlist(userId, id);
      }

      if (onWishlistUpdate) {
        onWishlistUpdate();
      }
    } catch (error) {
      console.error('Error updating wishlist:', error);
      setIsItemWishlisted(!isItemWishlisted);
    } finally {
      setIsLoading(false);
    }
  };

  const handleSendMessage = async () => {
    if (!sellerId || !userId) return;

    try {
      setIsLoading(true);
      const participants = [userId, sellerId];
      const response = await createChat(participants);
      console.log("Chatroom created successfully:", response.data);
      navigate(`/chat`);
    } catch (error) {
      console.error("Error creating chatroom:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const formatDate = (timestamp) => {
    return timestamp ? new Date(timestamp).toLocaleDateString() : 'No date';
  };

  const truncateDescription = (text, maxLength = 150) => {
    return text.length <= maxLength ? text : `${text.substr(0, maxLength)}...`;
  };

  const truncateId = (idString) => {
    return idString.length > 8 ? `${idString.substring(0, 8)}...` : idString;
  };

  const handleNextImage = () => {
    setCurrentImageIndex((prevIndex) => (prevIndex + 1) % images.length);
  };

  const handlePrevImage = () => {
    setCurrentImageIndex((prevIndex) => (prevIndex - 1 + images.length) % images.length);
  };

  return (
    <div className="w-full max-w-sm h-full flex flex-col bg-white rounded-lg shadow-md overflow-hidden">
      {/* Header */}
      <div className="p-4 border-b flex justify-between items-start">
        <div>
          <h3 className="text-2xl font-semibold">{name}</h3>
          <div className="flex items-center space-x-2 text-sm text-gray-500 mt-1">
            <Clock size={16} />
            <span>{formatDate(createdAt)}</span>
          </div>
        </div>
        {!isMyItem && (
          <button
            className="ml-2 p-2 rounded-full hover:bg-gray-100 disabled:opacity-50"
            title={isItemWishlisted ? "Remove from Wishlist" : "Add to Wishlist"}
            onClick={handleWishlistToggle}
            disabled={isLoading || !userId}
          >
            <Heart
              size={20}
              className={`transition-colors duration-300 ${
                isItemWishlisted
                  ? 'fill-red-500 text-red-500 hover:fill-gray-400 hover:text-gray-400'
                  : 'fill-transparent text-gray-400 hover:fill-red-500 hover:text-red-500'
              } ${isLoading ? 'animate-pulse' : ''}`}
            />
          </button>
        )}
      </div>

      {/* Image Carousel */}
      {images.length > 0 && (
        <div className="relative w-full h-96">
          <img
            src={images[currentImageIndex]}
            alt={name}
            className="w-full h-full object-cover"
            onError={(e) => { e.target.src = "/api/placeholder/400/320"; }}
          />
          {images.length > 1 && (
            <>
              <button
                onClick={handlePrevImage}
                className="absolute top-1/2 left-2 transform -translate-y-1/2 bg-gray-700 text-white p-2 rounded-full"
              >
                ◀
              </button>
              <button
                onClick={handleNextImage}
                className="absolute top-1/2 right-2 transform -translate-y-1/2 bg-gray-700 text-white p-2 rounded-full"
              >
                ▶
              </button>
            </>
          )}
        </div>
      )}

      {/* Body */}
      <div className="flex-grow p-4 space-y-4">
        <div className="space-y-2">
          <div className="flex gap-2">
            {category && (
              <span className="px-2 py-1 text-xs font-semibold text-gray-600 bg-gray-100 rounded-full">
                {category}
              </span>
            )}
            {condition && (
              <span className="px-2 py-1 text-xs font-semibold text-gray-600 bg-gray-100 rounded-full">
                {condition}
              </span>
            )}
          </div>
          <p className="text-sm text-gray-600">
            {truncateDescription(description)}
          </p>
        </div>
        {sellerId && (
          <div className="flex items-center space-x-2">
            <User size={16} className="text-gray-500" />
            <span className="text-sm text-gray-500">Seller ID: {truncateId(sellerId)}</span>
            {!isMyItem && (
              <button
                className="ml-2 px-3 py-1 text-sm font-semibold text-white bg-indigo-500 rounded hover:bg-indigo-600"
                onClick={handleSendMessage}
              >
                Send Message
              </button>
            )}
          </div>
        )}
      </div>

      {/* Footer */}
      <div className="border-t p-4">
        <div className="flex items-center justify-between w-full">
          <div className="flex items-center">
            <span className="text-xl font-bold text-green-600">
              ${price.toFixed(2)}
            </span>
          </div>
          {id && (
            <div className="text-xs text-gray-500">
              ID: {truncateId(id)}
            </div>
          )}
        </div>
        {isMyItem && (
          <div className="flex justify-between mt-4">
            <button
              className="bg-indigo-500 text-white px-4 py-2 rounded-lg hover:bg-indigo-600"
              onClick={() => onEdit(id)}
            >
              Edit
            </button>
            <button
              className="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600"
              onClick={() => onDelete(id)}
            >
              Delete
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default ItemCard;
