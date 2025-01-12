import React, { useState, useEffect } from 'react';
import NotificationItem from '../NotificationItem';
import { auth } from '../../../firebase/firebase';

const NotificationTab = () => {
  const currentUser = auth.currentUser;
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchNotifications();
  }, []);

  const fetchNotifications = async () => {
    try {
      const response = await fetch(`http://localhost:8080/notifications/${currentUser.uid}`); // Replace with your actual endpoint
      const data = await response.json();
      setNotifications(data);
    } catch (error) {
      console.error('Error fetching notifications:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed right-5 top-20 w-80 bg-white border border-gray-300 p-4 shadow-lg">
      {/* Header */}
      <div className="text-lg font-semibold text-gray-800 mb-4">
        Notifications
      </div>

      {/* Notification List */}
      <div className="mt-4 space-y-2 max-h-[400px] overflow-y-auto">
        {loading ? (
          <p>Loading notifications...</p>
        ) : notifications.length === 0 ? (
          <p>No notifications available</p>
        ) : (
          notifications.map(notification => (
            <NotificationItem
              key={notification.id}
              notification={notification}
              fetchNotifications={fetchNotifications}
            />
          ))
        )}
      </div>
    </div>
  );
};

export default NotificationTab;
