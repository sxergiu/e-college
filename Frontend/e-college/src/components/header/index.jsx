import React from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../../auth_context'
import { doSignOut } from '../../firebase/auth'
import { Home, LayoutDashboard, MessageCircle, Bell } from 'lucide-react'

const Header = () => {
    const navigate = useNavigate()
    const { userLoggedIn } = useAuth()

    return (
        <nav className='flex flex-row justify-between w-full z-20 fixed top-0 left-0 h-12 border-b bg-gray-200 px-4'>
            {/* Left section */}
            <div className='flex items-center gap-x-4'>
                {userLoggedIn && (
                    <>
                        <Link 
                            to="/" 
                            className='flex items-center gap-x-1 text-sm text-gray-700 hover:text-blue-600'
                        >
                            <Home size={18} />
                            Home
                        </Link>
                        <Link 
                            to="/dashboard" 
                            className='flex items-center gap-x-1 text-sm text-gray-700 hover:text-blue-600'
                        >
                            <LayoutDashboard size={18} />
                            Dashboard
                        </Link>
                    </>
                )}
            </div>

            {/* Center section - can be used for logo or title */}
            <div className='flex items-center'>
            </div>

            {/* Right section */}
            <div className='flex items-center gap-x-4'>
                {userLoggedIn ? (
                    <>
                        <Link 
                            to="/messages" 
                            className='text-gray-700 hover:text-blue-600'
                        >
                            <MessageCircle size={20} />
                        </Link>
                        <Link 
                            to="/notifications" 
                            className='text-gray-700 hover:text-blue-600'
                        >
                            <Bell size={20} />
                        </Link>
                        <button 
                            onClick={() => { 
                                doSignOut().then(() => { 
                                    navigate('/login') 
                                }) 
                            }} 
                            className='text-sm text-blue-600 hover:text-blue-800'
                        >
                            Logout
                        </button>
                    </>
                ) : (
                    <>
                        <Link 
                            className='text-sm text-blue-600 hover:text-blue-800' 
                            to='/login'
                        >
                            Login
                        </Link>
                        <Link 
                            className='text-sm text-blue-600 hover:text-blue-800' 
                            to='/register'
                        >
                            Register
                        </Link>
                    </>
                )}
            </div>
        </nav>
    )
}

export default Header