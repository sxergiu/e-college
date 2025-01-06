import React from 'react';
import { Clock, DollarSign, User, Heart } from "lucide-react";

const ItemCard = ({ 
  id = '',
  sellerId = '',
  name = '',
  description = '',
  price = 0,
  images = [],
  isSold = false,
  condition = '',
  category = '',
  createdAt,
  onAddToWishlist, // New prop for wishlist action
  isMyItem = false, // New prop to check if it's the user's own item
}) => {
  const formatDate = (timestamp) => {
    if (!timestamp) return 'No date';
    try {
      return new Date(timestamp).toLocaleDateString();
    } catch (e) {
      return 'Invalid date';
    }
  };

  const truncateDescription = (text, maxLength = 150) => {
    if (!text) return '';
    return text.length <= maxLength ? text : `${text.substr(0, maxLength)}...`;
  };

  const truncateId = (idString) => {
    if (!idString) return '';
    return idString.length > 8 ? `${idString.substring(0, 8)}...` : idString;
  };

  return (
    <div className="w-full max-w-sm h-full flex flex-col bg-white rounded-lg shadow-md overflow-hidden">
      {/* Header */}
      <div className="p-4 border-b flex justify-between items-start">
        <div>
          <h3 className="text-2xl font-semibold">{name || 'Untitled Item'}</h3>
          <div className="flex items-center space-x-2 text-sm text-gray-500 mt-1">
            <Clock size={16} />
            <span>{formatDate(createdAt)}</span>
          </div>
        </div>
        {/* Conditionally render heart button */}
        {!isMyItem && (
          <button
            className="ml-2 p-2 rounded-full hover:bg-gray-100"
            title="Add to Wishlist"
            onClick={() => onAddToWishlist(id)}
          >
            <Heart size={20} className="text-red-500" />
          </button>
        )}
      </div>

      {/* Image */}
      {Array.isArray(images) && images.length > 0 && (
        <div className="relative w-full h-48">
          <img
            src={images[0]}
            alt={name}
            className="w-full h-full object-cover"
            onError={(e) => {
              e.target.src = "/api/placeholder/400/320";
            }}
          />
          {images.length > 1 && (
            <span className="absolute bottom-2 right-2 px-2 py-1 text-xs text-white bg-black/50 rounded-full">
              +{images.length - 1} more
            </span>
          )}
        </div>
      )}

      {/* Content */}
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
            <span className="text-sm text-gray-500">Seller ID: {sellerId}</span>
          </div>
        )}
      </div>

      {/* Footer */}
      <div className="border-t p-4">
        <div className="flex items-center justify-between w-full">
          <div className="flex items-center">
            <DollarSign size={20} className="text-green-600" />
            <span className="text-xl font-bold text-green-600">
              {typeof price === 'number' ? price.toFixed(2) : '0.00'}
            </span>
          </div>
          {id && (
            <div className="text-xs text-gray-500">
              ID: {truncateId(id)}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ItemCard;
