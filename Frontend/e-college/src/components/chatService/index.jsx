import axios from "axios";

const API_BASE_URL = "http://localhost:8080/chats"; // Replace with your backend's URL

// Create a new chat
export const createChat = async (participants) => {
  try {
    const response = await axios.post(API_BASE_URL, { participants });
    return response.data; // Return chat object with chatId
  } catch (error) {
    console.error("Error creating chat:", error);
    throw error;
  }
};

// Get chats for a user
export const getUserChats = async (userId) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/user/${userId}`);
    return response.data; // Return list of chats
  } catch (error) {
    console.error("Error fetching user chats:", error);
    throw error;
  }
};

// Add a message to a chat
export const addMessage = async (chatId, senderId, message) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/${chatId}/messages`, {
      senderId: senderId,
      message: message,
      timestamp: new Date(),
    });
    return response.data; // Return message data
  } catch (error) {
    console.error("Error adding message:", error);
    throw error;
  }
};

export const getMessagesForChat = async (chatId) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${chatId}/messages`);
    return response.data; // Return list of messages with usernames
  } catch (error) {
    console.error("Error fetching messages:", error);
    throw error;
  }
};
