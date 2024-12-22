import React, { useState } from 'react';
import axios from 'axios';

const SearchBar = () => {
  const [query, setQuery] = useState('');   // State for the search query
  const [results, setResults] = useState([]); // State for the search results
  const [loading, setLoading] = useState(false); // State to show loading status
  const [error, setError] = useState('');  // State to show error message

  // Function to handle input change
  const handleChange = (e) => {
    setQuery(e.target.value);
  };

  // Function to call the API when the user presses Enter or clicks the Search button
  const handleSearch = async () => {
    if (!query) return;  // Don't search if query is empty
    
    setLoading(true);  // Set loading state to true before making the request
    setError('');  // Reset previous error

    try {
      // Replace with your actual endpoint
      const response = await axios.get(`http://localhost:8080/item/search?q=${query}`);
      setResults(response.data); // Set the response data into state
    } catch (err) {
      setError('Error fetching data. Please try again later.');
      console.error('Error fetching data: ', err);
    } finally {
      setLoading(false); // Set loading state back to false after the request is done
    }
  };

  // Optional: Trigger the search when the user presses the Enter key
  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleSearch();
    }
  };

  return (
    <div className="search-bar">
      <input
        type="text"
        value={query}
        onChange={handleChange}
        onKeyPress={handleKeyPress}
        placeholder="Search..."
      />
      <button onClick={handleSearch}>Search</button>

      {loading && <p>Loading...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}

      <div className="results">
        {results.length > 0 ? (
          <ul>
            {results.map((result, index) => (
              <li key={index}>{result.name}</li> // Change `result.name` according to your response structure
            ))}
          </ul>
        ) : (
          <p>No results found</p>
        )}
      </div>
    </div>
  );
};

export default SearchBar;
