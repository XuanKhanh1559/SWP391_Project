// Layout loader - Load header and footer dynamically
function loadLayout() {
    // Determine context path from current location
    const currentPath = window.location.pathname;
    // Extract context path (e.g., /phone_card_system from /phone_card_system/login)
    const pathParts = currentPath.split('/').filter(p => p);
    const contextPath = pathParts.length > 0 ? '/' + pathParts[0] : '';
    
    // Load header
    fetch(`${contextPath}/layouts/header.jsp`)
        .then(response => response.text())
        .then(data => {
            const headerPlaceholder = document.getElementById('header-placeholder');
            if (headerPlaceholder) {
                // Replace ${pageContext.request.contextPath} with actual context path
                const processedData = data.replace(/\$\{pageContext\.request\.contextPath\}/g, contextPath);
                headerPlaceholder.innerHTML = processedData;
                initHeader();
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
    // For now, we'll check if there's a user element in the page
    const navAuth = document.getElementById('navAuth');
    const navUser = document.getElementById('navUser');
    const userMenu = document.getElementById('userMenu');
    const adminMenu = document.getElementById('adminMenu');
    const userName = document.getElementById('userName');
    
    // Check if user data is available from JSP (set via script tag)
    const userLoggedIn = typeof window.userData !== 'undefined' && window.userData !== null;
    
    if (userLoggedIn && window.userData.id) {
        if (navAuth) navAuth.style.display = 'none';
        if (navUser) {
            navUser.style.display = 'block';
            if (userName) userName.textContent = window.userData.username || 'Tài khoản';
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
        if (navAuth) navAuth.style.display = 'flex';
        if (navUser) navUser.style.display = 'none';
        if (userMenu) userMenu.style.display = 'none';
        if (adminMenu) adminMenu.style.display = 'none';
    }
    
    // Update cart count
    updateCartCount();
    
    // Mobile menu toggle
    const navToggle = document.getElementById('navToggle');
    const navMenu = document.getElementById('navMenu');
    
    if (navToggle && navMenu) {
        navToggle.addEventListener('click', () => {
            navMenu.classList.toggle('active');
        });
    }
    
    // User dropdown toggle - CLICK ONLY, NO HOVER
    setupUserDropdown();
    
    // Set active nav link based on current page
    const currentPage = window.location.pathname.split('/').pop().replace('.jsp', '').replace('.html', '');
    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.remove('active');
        const href = link.getAttribute('href');
        if (link.getAttribute('data-page') === currentPage || 
            (href && href.includes(currentPage))) {
            link.classList.add('active');
        }
    });
}

function initFooter() {
    // Footer is simple, no links needed
}

function updateCartCount() {
    const cart = JSON.parse(localStorage.getItem('cart') || '[]');
    const count = cart.reduce((sum, item) => sum + item.quantity, 0);
    const cartCountEl = document.getElementById('cartCount');
    if (cartCountEl) {
        cartCountEl.textContent = count;
    }
}

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
