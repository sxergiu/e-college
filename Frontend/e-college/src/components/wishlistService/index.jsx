// api/wishlistService.js
const API_BASE_URL = 'http://localhost:8080'; // adjust to match your Spring Boot server URL

export const wishlistService = {
  // Get all items in user's wishlist
  getWishlistItems: async (userId) => {
    const response = await fetch(`${API_BASE_URL}/wishlist/${userId}/items`);
    if (!response.ok) throw new Error('Failed to fetch wishlist items');
    return response.json();
  },

  getWishlist: async (userId) => {
    const response = await fetch(`${API_BASE_URL}/wishlist/${userId}`);
    if (!response.ok) throw new Error('Failed to fetch wishlist items');
    return response.json();
  },

  // Add item to wishlist
  addToWishlist: async (userId, productId) => {
    const response = await fetch(`${API_BASE_URL}/wishlist/${userId}/add?productId=${productId}`, {
      method: 'POST',
    });
    if (!response.ok) throw new Error('Failed to add item to wishlist');
    return response.json();
  },

  // Remove item from wishlist
  removeFromWishlist: async (userId, productId) => {
    const response = await fetch(`${API_BASE_URL}/wishlist/${userId}/remove?productId=${productId}`, {
      method: 'DELETE',
    });
    if (!response.ok) throw new Error('Failed to remove item from wishlist');
    return response.json();
  },
};