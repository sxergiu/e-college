import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/item'; // Adjusted to match backend

export const purchaseItem = async ({ buyerId, itemId }) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/buyItem/${itemId}`, null, {
      params: { buyerId }, // Send buyerId as a query parameter
    });
    return response;
  } catch (error) {
    console.error('Error making purchase request:', error);
    throw error;
  }
};