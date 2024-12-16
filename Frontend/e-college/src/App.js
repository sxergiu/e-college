import Login from "./components/auth/login";
import Register from "./components/auth/register";

import Header from "./components/header";
import Home from "./components/home";

import { AuthProvider } from "./auth_context";
import { useRoutes } from "react-router-dom";

function App() {
  const routesArray = [
    {
      path: "*",
      element: <Login />,
    },
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
  ];
  let routesElement = useRoutes(routesArray);
  return (
    <AuthProvider>
      <Header />
      <div className="w-full h-screen flex flex-col">{routesElement}</div>
    </AuthProvider>
  );
}

const App = () => {
    const [users, setUsers] = useState([]);

    useEffect(() => {
        // Fetch all users on component mount
        async function fetchUsers() {
            const result = await getAllUsers();
            setUsers(result); // Update state with the fetched users
        }

        fetchUsers();
    }, []); // Empty dependency array means this runs once after the component mounts

    return (
        <div>
            <h1>All Users</h1>
            <ul>
                {users.length > 0 ? (
                    users.map((user) => (
                        <li key={user.id}>
                            <strong>{user.username}</strong>: {user.email}
                        </li>
                    ))
                ) : (
                    <p>No users found</p>
                )}
            </ul>
        </div>
    );
};

export default App;
