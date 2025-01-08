import React, { useState, useEffect } from "react";
import { getUserChats, createChat, addMessage } from "../chatService";
import { auth } from "../../firebase/firebase";

const ChatComponent = () => {
  const [chats, setChats] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [newParticipants, setNewParticipants] = useState("");
  const [newMessage, setNewMessage] = useState("");
  const [selectedChatId, setSelectedChatId] = useState("");
  const [messages, setMessages] = useState([]);
  const [userId, setUserId] = useState(null);

  useEffect(() => {
    // Ensure Firebase is initialized and auth.currentUser is available
    const currentUser = auth.currentUser;
    if (currentUser) {
      setUserId(currentUser.uid); // Extract userId from currentUser
    } else {
      setError("User not authenticated");
    }
  }, []);

  useEffect(() => {
    if (userId) {
      const fetchChats = async () => {
        try {
          const userChats = await getUserChats(userId);
          setChats(userChats);
        } catch (err) {
          setError("Failed to load chats");
        } finally {
          setLoading(false);
        }
      };
      fetchChats();
    }
  }, [userId]);

  const handleCreateChat = async () => {
    if (!newParticipants.trim()) return;
    
    const participants = newParticipants.split(",").map((p) => p.trim());
    try {
      const newChat = await createChat(participants);
      setChats([...chats, { chatId: newChat.chatId, participants }]);
      setNewParticipants("");
    } catch (err) {
      setError("Failed to create chat");
    }
  };

  const handleAddMessage = async (e) => {
    e.preventDefault();
    if (!newMessage || !selectedChatId || !userId) return;

    try {
      await addMessage(selectedChatId, userId, newMessage);
      setMessages([...messages, { id: Date.now(), sender: userId, text: newMessage }]);
      setNewMessage("");
    } catch (err) {
      setError("Failed to send message");
    }
  };

  if (loading) return (
    <div className="flex items-center justify-center h-[calc(100vh-4rem)] mt-16">
      <p className="text-gray-500">Loading chats...</p>
    </div>
  );

  if (error) return (
    <div className="flex items-center justify-center h-[calc(100vh-4rem)] mt-16">
      <p className="text-red-500">{error}</p>
    </div>
  );

  return (
    <div className="flex h-[calc(100vh-4rem)] mt-16">
      {/* Rest of the component remains the same */}
      {/* Sidebar with chat list */}
      <div className="w-64 border-r bg-gray-50 flex flex-col">
        <div className="p-4 border-b">
          <h2 className="font-semibold mb-4">Chats</h2>
          <div className="space-y-2">
            <input
              type="text"
              placeholder="New participants..."
              className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              value={newParticipants}
              onChange={(e) => setNewParticipants(e.target.value)}
            />
            <button 
              onClick={handleCreateChat}
              className="w-full px-4 py-2 text-white bg-blue-500 rounded-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 flex items-center justify-center gap-2"
            >
              <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
                <path fillRule="evenodd" d="M10 3a1 1 0 011 1v5h5a1 1 0 110 2h-5v5a1 1 0 11-2 0v-5H4a1 1 0 110-2h5V4a1 1 0 011-1z" clipRule="evenodd" />
              </svg>
              New Chat
            </button>
          </div>
        </div>
        <div className="flex-1 overflow-y-auto p-2">
          {chats.length === 0 ? (
            <p className="text-gray-500 text-sm text-center p-4">No chats available</p>
          ) : (
            <div className="space-y-2">
              {chats.map((chat) => (
                <button
                  key={chat.chatId}
                  onClick={() => setSelectedChatId(chat.chatId)}
                  className={`w-full px-3 py-2 rounded-md text-left transition-colors ${
                    selectedChatId === chat.chatId
                      ? 'bg-blue-100 text-blue-800'
                      : 'hover:bg-gray-100'
                  }`}
                >
                  <div className="font-medium">Chat {chat.chatId}</div>
                  <div className="text-xs text-gray-500 truncate">
                    {chat.participants.join(", ")}
                  </div>
                </button>
              ))}
            </div>
          )}
        </div>
      </div>

      {/* Main chat area */}
      <div className="flex-1 flex flex-col">
        {selectedChatId ? (
          <>
            <div className="p-4 border-b bg-white">
              <div className="flex items-center">
                <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-2" viewBox="0 0 20 20" fill="currentColor">
                  <path fillRule="evenodd" d="M18 10c0 3.866-3.582 7-8 7a8.841 8.841 0 01-4.083-.98L2 17l1.338-3.123C2.493 12.767 2 11.434 2 10c0-3.866 3.582-7 8-7s8 3.134 8 7zM7 9H5v2h2V9zm8 0h-2v2h2V9zM9 9h2v2H9V9z" clipRule="evenodd" />
                </svg>
                <h2 className="font-semibold">Chat {selectedChatId}</h2>
              </div>
              <div className="flex items-center text-sm text-gray-500 mt-1">
                <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4 mr-1" viewBox="0 0 20 20" fill="currentColor">
                  <path d="M9 6a3 3 0 11-6 0 3 3 0 016 0zM17 6a3 3 0 11-6 0 3 3 0 016 0zM12.93 17c.046-.327.07-.66.07-1a6.97 6.97 0 00-1.5-4.33A5 5 0 0119 16v1h-6.07zM6 11a5 5 0 015 5v1H1v-1a5 5 0 015-5z" />
                </svg>
                {chats.find(c => c.chatId === selectedChatId)?.participants.join(", ")}
              </div>
            </div>
            <div className="flex-1 overflow-y-auto p-4 bg-gray-50">
              {messages.map((msg) => (
                <div
                  key={msg.id}
                  className={`mb-4 flex ${msg.sender === userId ? 'justify-end' : 'justify-start'}`}
                >
                  <div
                    className={`max-w-[70%] rounded-lg px-4 py-2 ${
                      msg.sender === userId
                        ? 'bg-blue-500 text-white'
                        : 'bg-white text-gray-900 shadow'
                    }`}
                  >
                    {msg.text}
                  </div>
                </div>
              ))}
            </div>
            <form onSubmit={handleAddMessage} className="p-4 border-t bg-white">
              <div className="flex gap-2">
                <input
                  type="text"
                  placeholder="Type a message..."
                  className="flex-1 px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  value={newMessage}
                  onChange={(e) => setNewMessage(e.target.value)}
                />
                <button 
                  type="submit"
                  className="px-4 py-2 text-white bg-blue-500 rounded-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
                    <path d="M10.894 2.553a1 1 0 00-1.788 0l-7 14a1 1 0 001.169 1.409l5-1.429A1 1 0 009 15.571V11a1 1 0 112 0v4.571a1 1 0 00.725.962l5 1.428a1 1 0 001.17-1.408l-7-14z" />
                  </svg>
                </button>
              </div>
            </form>
          </>
        ) : (
          <div className="flex-1 flex items-center justify-center text-gray-500 bg-gray-50">
            Select a chat to start messaging
          </div>
        )}
      </div>
    </div>
  );
};

export default ChatComponent;
