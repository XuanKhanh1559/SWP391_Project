// Layout loader - Load header and footer dynamically
function loadLayout() {
    // Determine context path from current location
    const currentPath = window.location.pathname;
    // Extract context path (e.g., /phone_card_system from /phone_card_system/login)
    const pathParts = currentPath.split('/').filter(p => p);
    const contextPath = pathParts.length > 0 ? '/' + pathParts[0] : '';
    
    // Load header with credentials to include session
    fetch(`${contextPath}/layouts/header.jsp`, {
        credentials: 'same-origin'
    })
        .then(response => response.text())
        .then(data => {
            const headerPlaceholder = document.getElementById('header-placeholder');
            if (headerPlaceholder) {
                // Replace ${pageContext.request.contextPath} with actual context path
                const processedData = data.replace(/\$\{pageContext\.request\.contextPath\}/g, contextPath);
                headerPlaceholder.innerHTML = processedData;
                // Use setTimeout to ensure DOM is ready
                setTimeout(() => {
                    initHeader();
                }, 10);
            }
        })
        .catch(error => console.error('Error loading header:', error));
    
    // Load footer
    fetch(`${contextPath}/layouts/footer.jsp`)
        .then(response => response.text())
        .then(data => {
            const footerPlaceholder = document.getElementById('footer-placeholder');
            if (footerPlaceholder) {
                footerPlaceholder.innerHTML = data;
                initFooter();
            }
        })
        .catch(error => console.error('Error loading footer:', error));
}

function initHeader() {
    // Check if user is logged in via session (will be set by JSP)
    const navAuth = document.getElementById('navAuth');
    const navUser = document.getElementById('navUser');
    const userMenu = document.getElementById('userMenu');
    const adminMenu = document.getElementById('adminMenu');
    const userName = document.getElementById('userName');
    
    // Check if user data is available from JSP (set via script tag)
    const userLoggedIn = typeof window.userData !== 'undefined' && window.userData !== null && window.userData.id;
    
    // Respect server-side rendering first, then update if needed
    if (userLoggedIn) {
        // User is logged in - ensure correct display
        if (navAuth) navAuth.style.display = 'none';
        if (navUser) {
            navUser.style.display = 'block';
            if (userName && window.userData.username) {
                userName.textContent = window.userData.username;
            }
        }
        
        const isAdmin = window.userData.role === 'admin';
        if (isAdmin) {
            if (adminMenu) adminMenu.style.display = 'block';
            if (userMenu) userMenu.style.display = 'none';
            const logoText = document.getElementById('navLogoText');
            if (logoText) logoText.textContent = 'Admin Panel';
        } else {
            if (userMenu) userMenu.style.display = 'block';
            if (adminMenu) adminMenu.style.display = 'none';
        }
    } else {
        // User is NOT logged in - force hide user menus
        if (navAuth) {
            navAuth.style.display = 'flex';
        }
        if (navUser) {
            navUser.style.display = 'none';
        }
        if (userMenu) {
            // Use both class and inline style to ensure hiding
            userMenu.classList.add('hidden');
            userMenu.style.display = 'none';
            userMenu.style.setProperty('display', 'none', 'important');
        }
        if (adminMenu) {
            adminMenu.classList.add('hidden');
            adminMenu.style.display = 'none';
            adminMenu.style.setProperty('display', 'none', 'important');
        }
    }
    
    // Update cart count
    updateCartCount();
    
    // Mobile menu toggle with hamburger animation
    const navToggle = document.getElementById('navToggle');
    const navMenu = document.getElementById('navMenu');
    
    if (navToggle && navMenu) {
        navToggle.addEventListener('click', () => {
            navMenu.classList.toggle('active');
            navToggle.classList.toggle('active');
            
            // Close user dropdown when opening mobile menu
            const navUser = document.getElementById('navUser');
            if (navUser) {
                navUser.classList.remove('active');
            }
        });
        
        // Close mobile menu when clicking outside
        document.addEventListener('click', (e) => {
            if (!navToggle.contains(e.target) && !navMenu.contains(e.target)) {
                navMenu.classList.remove('active');
                navToggle.classList.remove('active');
            }
        });
    }
    
    // User dropdown toggle - CLICK ONLY, NO HOVER
    setupUserDropdown();
    
    // Set active nav link based on current page - only one active at a time
    const currentPath = window.location.pathname;
    const currentPage = currentPath.split('/').pop().replace('.jsp', '').replace('.html', '');
    
    // Remove all active classes first
    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.remove('active');
    });
    
    // Find and activate the matching link
    document.querySelectorAll('.nav-link').forEach(link => {
        const href = link.getAttribute('href');
        const dataPage = link.getAttribute('data-page');
        
        if (!href) return;
        
        // Normalize paths for comparison
        const linkPath = href.split('?')[0]; // Remove query params
        const normalizedCurrentPath = currentPath.split('?')[0];
        
        // Check if current path matches the link
        let isActive = false;
        
        // Exact match
        if (normalizedCurrentPath === linkPath) {
            isActive = true;
        }
        // Match by data-page attribute
        else if (dataPage) {
            if (dataPage === 'home' && (currentPage === '' || currentPage === 'index' || normalizedCurrentPath.endsWith('/index.jsp'))) {
                isActive = true;
            } else if (dataPage === 'products' && (currentPage === 'products' || normalizedCurrentPath.includes('/products'))) {
                isActive = true;
            } else if (dataPage === 'dashboard' && normalizedCurrentPath.includes('/user/dashboard')) {
                isActive = true;
            } else if (dataPage === 'cart' && (currentPage === 'cart' || normalizedCurrentPath.includes('/cart'))) {
                isActive = true;
            } else if (dataPage === 'orders' && normalizedCurrentPath.includes('/user/orders')) {
                isActive = true;
            } else if (dataPage === 'coupons' && normalizedCurrentPath.includes('/user/coupons')) {
                isActive = true;
            } else if (dataPage === 'admin-dashboard' && normalizedCurrentPath.includes('/admin/dashboard')) {
                isActive = true;
            }
        }
        // Fallback: check if href is included in current path
        else if (normalizedCurrentPath.includes(linkPath) && linkPath !== '/') {
            isActive = true;
        }
        
        if (isActive) {
            link.classList.add('active');
        }
    });
}

function initFooter() {
    // Footer is simple, no links needed
}

function updateCartCount() {
    const contextPath = window.location.pathname.split('/')[1] ? '/' + window.location.pathname.split('/')[1] : '';
    
    fetch(`${contextPath}/cart-count`, {
        headers: {
            'Accept': 'application/json; charset=UTF-8'
        }
    })
        .then(response => response.json())
        .then(data => {
            const cartCountEl = document.getElementById('cartCount');
            if (cartCountEl) {
                cartCountEl.textContent = data.count || 0;
            }
        })
        .catch(error => {
            console.error('Error updating cart count:', error);
            const cartCountEl = document.getElementById('cartCount');
            if (cartCountEl) {
                cartCountEl.textContent = '0';
            }
        });
}

window.updateCartCount = updateCartCount;

function setupUserDropdown() {
    const userMenuToggle = document.getElementById('userMenuToggle');
    const navUser = document.getElementById('navUser');
    
    if (!userMenuToggle || !navUser) {
        return;
    }
    
    // Remove any existing listeners
    const newToggle = userMenuToggle.cloneNode(true);
    userMenuToggle.parentNode.replaceChild(newToggle, userMenuToggle);
    
    // Add click event to toggle dropdown
    newToggle.addEventListener('click', function(e) {
        e.preventDefault();
        e.stopPropagation();
        
        const isActive = navUser.classList.contains('active');
        // Close mobile menu if open
        const navMenu = document.getElementById('navMenu');
        const navToggle = document.getElementById('navToggle');
        if (navMenu && navMenu.classList.contains('active')) {
            navMenu.classList.remove('active');
            if (navToggle) navToggle.classList.remove('active');
        }
        
        // Close all other dropdowns first
        document.querySelectorAll('.nav-user.active').forEach(el => {
            if (el !== navUser) el.classList.remove('active');
        });
        // Toggle current dropdown
        if (isActive) {
            navUser.classList.remove('active');
        } else {
            navUser.classList.add('active');
        }
    });
    
    // Close dropdown when clicking outside (only add once globally)
    if (!window.userDropdownClickHandlerAdded) {
        window.userDropdownClickHandlerAdded = true;
        document.addEventListener('click', function(e) {
            const allNavUsers = document.querySelectorAll('.nav-user');
            allNavUsers.forEach(navUserEl => {
                if (navUserEl && !navUserEl.contains(e.target)) {
                    navUserEl.classList.remove('active');
                }
            });
        });
    }
}

// Load layout when DOM is ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', loadLayout);
} else {
    loadLayout();
}
