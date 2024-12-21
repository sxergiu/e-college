import React, { useState } from 'react';
import { Navigate, Link } from 'react-router-dom';
import { doSignInWithEmailAndPassword, doSignInWithGoogle } from '../../../firebase/auth';
import { useAuth } from '../../../auth_context';

const Login = () => {
    const { userLoggedIn } = useAuth();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [isSigningIn, setIsSigningIn] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (isSigningIn) return; // Prevent multiple submissions

        setIsSigningIn(true);
        setErrorMessage(''); // Clear previous errors

        try {
            await doSignInWithEmailAndPassword(email, password);
        } catch (error) {
            setIsSigningIn(false);
            if (error.code === 'auth/wrong-password' || error.code === 'auth/user-not-found') {
                setErrorMessage('Invalid email or password.');
            } else if (error.code === 'auth/too-many-requests'){
                setErrorMessage('Too many attempts. Please try again later')
            }
             else {
                setErrorMessage('An error occurred during sign-in.');
                console.error("Firebase Sign In Error:", error); // Log the full error for debugging
            }
        } finally {
            setIsSigningIn(false); // Ensure isSigningIn is always reset
        }
    };

    const handleGoogleSignIn = async (e) => {
        e.preventDefault();
        if (isSigningIn) return;

        setIsSigningIn(true);
        setErrorMessage('');

        try {
            await doSignInWithGoogle();
        } catch (error) {
            setIsSigningIn(false);
            setErrorMessage('An error occurred during Google sign-in.');
            console.error("Firebase Google Sign In Error:", error);
        } finally {
            setIsSigningIn(false);
        }
    };

    return (
        <div>
            {userLoggedIn && <Navigate to="/home" replace />}

            <main className="w-full h-screen flex justify-center items-center">
                <div className="w-96 text-gray-600 space-y-5 p-4 shadow-xl border rounded-xl">
                    <div className="text-center">
                        <h3 className="text-gray-800 text-xl font-semibold sm:text-2xl mt-2">Welcome Back</h3>
                    </div>
                    <form onSubmit={handleSubmit} className="space-y-5">
                        <div>
                            <label className="text-sm text-gray-600 font-bold" htmlFor="email">Email</label>
                            <input
                                type="email"
                                id="email"
                                autoComplete="email"
                                required
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                className="w-full mt-2 px-3 py-2 text-gray-500 bg-transparent outline-none border focus:border-indigo-600 shadow-sm rounded-lg transition duration-300"
                            />
                        </div>
                        <div>
                            <label className="text-sm text-gray-600 font-bold" htmlFor="password">Password</label>
                            <input
                                type="password"
                                id="password"
                                autoComplete="current-password"
                                required
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                className="w-full mt-2 px-3 py-2 text-gray-500 bg-transparent outline-none border focus:border-indigo-600 shadow-sm rounded-lg transition duration-300"
                            />
                        </div>

                        {errorMessage && <span className="text-red-600 font-bold">{errorMessage}</span>}

                        <button
                            type="submit"
                            disabled={isSigningIn}
                            className={`w-full px-4 py-2 text-white font-medium rounded-lg ${
                                isSigningIn ? 'bg-gray-300 cursor-not-allowed' : 'bg-indigo-600 hover:bg-indigo-700 hover:shadow-xl transition duration-300'
                            }`}
                        >
                            {isSigningIn ? 'Signing In...' : 'Sign In'}
                        </button>
                    </form>
                    <p className="text-center text-sm">
                        Don't have an account? <Link to="/register" className="hover:underline font-bold">Sign up</Link>
                    </p>
                    <div className="flex items-center">
                        <div className="border-t border-gray-300 flex-grow mr-2"></div>
                        <span className="text-sm font-bold">OR</span>
                        <div className="border-t border-gray-300 flex-grow ml-2"></div>
                    </div>
                    <button
                        disabled={isSigningIn}
                        onClick={handleGoogleSignIn}
                        className={`w-full flex items-center justify-center gap-x-3 py-2.5 border rounded-lg text-sm font-medium ${
                            isSigningIn ? 'cursor-not-allowed' : 'hover:bg-gray-100 transition duration-300 active:bg-gray-100'
                        }`}
                    >
                        <svg className="w-5 h-5" viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
                            {/* ... (SVG content remains unchanged) */}
                        </svg>
                        {isSigningIn ? 'Signing In...' : 'Continue with Google'}
                    </button>
                </div>
            </main>
        </div>
    );
};

export default Login;