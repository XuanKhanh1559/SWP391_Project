/**
 * Helper function to ensure UTF-8 encoding for all fetch requests
 */
function fetchJson(url, options = {}) {
    // Set default headers for UTF-8
    const defaultHeaders = {
        'Accept': 'application/json; charset=UTF-8'
    };
    
    // Merge headers
    if (options.headers) {
        options.headers = {
            ...defaultHeaders,
            ...options.headers
        };
    } else {
        options.headers = defaultHeaders;
    }
    
    // Ensure Content-Type has charset if it's form-urlencoded
    if (options.headers['Content-Type'] && 
        options.headers['Content-Type'].includes('application/x-www-form-urlencoded') &&
        !options.headers['Content-Type'].includes('charset')) {
        options.headers['Content-Type'] += '; charset=UTF-8';
    }
    
    return fetch(url, options)
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    try {
                        return JSON.parse(text);
                    } catch (e) {
                        console.error('Failed to parse JSON:', text);
                        throw new Error('Invalid JSON response');
                    }
                });
            }
            return response.json();
        });
}

// Make it available globally
window.fetchJson = fetchJson;

