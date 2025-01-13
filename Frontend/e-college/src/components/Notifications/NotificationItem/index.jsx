import React from 'react';
import { CheckCircle } from 'lucide-react';

// Helper function to format raw Firestore timestamp
const formatTimestamp = (timestamp) => {
  if (!timestamp || !timestamp.seconds) return ''; // Check if timestamp is valid
  const date = new Date(timestamp.seconds * 1000); // Convert seconds to milliseconds
  return date.toLocaleString(); // You can customize the format as needed
};

const NotificationItem = ({ notification, fetchNotifications }) => {
  const markAsRead = async () => {
    try {
      await fetch(`http://localhost:8080/notifications/${notification.id}/read`, {
        method: 'PUT',
      }); // Replace with your actual endpoint
      fetchNotifications();
    } catch (error) {
      console.error('Error marking notification as read:', error);
    }
  };

  return (
    <div
  className={`p-3 border-b flex justify-between items-start ${notification.isRead ? 'bg-gray-200' : 'bg-gray-100'}`}
>
  {/* Notification Content */}
  <div className="flex-grow">
    <h4 className="font-semibold text-gray-800">{notification.title}</h4>
    <p className="text-gray-600">{notification.message}</p>
    <p className="text-sm text-gray-500">{formatTimestamp(notification.timestamp)}</p>
  </div>

  {/* Mark as Read Button */}
  <button
    onClick={markAsRead}
    disabled={notification.isRead}
    className={`ml-4 p-2 rounded-md ${notification.isRead ? 'text-gray-400' : 'text-indigo-600'} ${notification.isRead ? 'bg-gray-300' : 'hover:bg-indigo-600 hover:text-white'} transition-colors duration-300`}
  >
    <CheckCircle size={20} />
  </button>
</div>
  );
};

export default NotificationItem;
