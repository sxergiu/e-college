import { Home, LayoutDashboard, MessageCircle, Bell, Box, Heart } from 'lucide-react';
import React, { useState, useCallback } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../auth_context';
import { doSignOut } from '../../firebase/auth';
import NotificationTab from '../Notifications/NotificationTab';

const Header = () => {
    const navigate = useNavigate();
    const { userLoggedIn } = useAuth();
    const [isNotificationOpen, setIsNotificationOpen] = useState(false); // Toggle for notifications

    const handleLogout = useCallback(async () => {
        try {
            await doSignOut();
            setTimeout(() => {
                navigate('/login', { replace: true });
            }, 0);
        } catch (error) {
            console.error("Logout error:", error);
        }
    }, [navigate]);

    const toggleNotifications = () => {
        setIsNotificationOpen(prevState => !prevState); // Toggle notification tab
    };

    return (
        <nav className='flex flex-row justify-between w-full z-20 fixed top-0 left-0 h-12 border-b bg-gray-200 px-4'>
            {/* Left section */}
            <div className='flex items-center gap-x-4'>
                {userLoggedIn && (
                    <>
                        <Link 
                            to="/" 
                            className='flex items-center gap-x-1 text-sm text-gray-700 hover:text-indigo-600'
                        >
                            <Home size={18} />
                            Home
                        </Link>
                        <Link 
                            to="/dashboard" 
                            className='flex items-center gap-x-1 text-sm text-gray-700 hover:text-indigo-600'
                        >
                            <LayoutDashboard size={18} />
                            Dashboard
                        </Link>
                    </>
                )}
            </div>

            {/* Center section */}
            <div className='flex items-center'>
            </div>

            {/* Right section */}
            <div className='flex items-center gap-x-4'>
                {userLoggedIn ? (
                    <>
                        <Link 
                            to="/my-items" 
                            className='flex items-center gap-x-1 text-sm text-gray-700 hover:text-indigo-600'
                        >
                            <Box size={18} />
                            My Items
                        </Link>
                        <Link 
                            to="/wishlist" 
                            className='flex items-center gap-x-1 text-sm text-gray-700 hover:text-indigo-600'
                        >
                            <Heart size={18} />
                            Wishlist
                        </Link>
                        <Link 
                            to="/chat" 
                            className='text-gray-700 hover:text-indigo-600'
                        >
                            <MessageCircle size={20} />
                        </Link>

                        {/* Bell icon for notifications */}
                        <button 
                            onClick={toggleNotifications} 
                            className='text-gray-700 hover:text-indigo-600 relative'
                        >
                            <Bell size={20} />
                            {isNotificationOpen && <NotificationTab />} {/* Show NotificationTab when Bell icon is clicked */}
                        </button>

                        <button 
                            onClick={handleLogout}
                            className='text-sm text-indigo-600 hover:text-indigo-800'
                        >
                            Logout
                        </button>
                    </>
                ) : (
                    <>
                        <Link 
                            className='text-sm text-indigo-600 hover:text-indigo-800' 
                            to='/login'
                        >
                            Login
                        </Link>
                        <Link 
                            className='text-sm text-indigo-600 hover:text-indigo-800' 
                            to='/register'
                        >
                            Register
                        </Link>
                    </>
                )}
            </div>
        </nav>
    );
};

export default Header;
