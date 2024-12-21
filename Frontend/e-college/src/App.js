import Login from "./components/auth/login";
import Register from "./components/auth/register";
import Header from "./components/header";
import Home from "./components/home";
import Dashboard from "./components/dashboard"; 
import MyItems from "./components/myItems";
import { AuthProvider } from "./auth_context";
import { useRoutes } from "react-router-dom";

function App() {
  const routesArray = [
    {
      path: "/login",
      element: <Login />,
    },
    {
      path: "/register",
      element: <Register />,
    },
    {
      path: "/home",
      element: <Home />,
    },
    {
      path: "/dashboard",
      element: <Dashboard />,
    },
    {
      path: "/my-items",
      element: <MyItems />,
    },
    {
      path: "*", // Catch-all route for undefined paths
      element: <Login />,
    },
  ];

  let routesElement = useRoutes(routesArray);

  return (
    <AuthProvider>
      <Header />
      <div className="w-full h-screen flex flex-col">{routesElement}</div>
    </AuthProvider>
  );
}

export default App;
