import React from "react";
import ReactDOM from "react-dom/client"; // Use `react-dom/client` for React 18
import App from "./App";

// Get the root element
const rootElement = document.getElementById("root");

// Create the root using `createRoot`
const root = ReactDOM.createRoot(rootElement);

// Render the app using the new API
root.render(<App />);
