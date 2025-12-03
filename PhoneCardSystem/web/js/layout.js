// Layout loader - Load header and footer dynamically
function loadLayout() {
    // Determine base path based on current location
    const currentPath = window.location.pathname;
    const currentDir = window.location.pathname.split('/').slice(-2, -1)[0];
    let basePath = '';
    
    if (currentDir === 'guest' || currentDir === 'user' || currentDir === 'admin') {
        basePath = '../';
    } else {
        basePath = '';
    }
    
    // Load header
    fetch(`${basePath}layouts/header.jsp`)
        .then(response => response.text())
        .then(data => {
            const headerPlaceholder = document.getElementById('header-placeholder');
            if (headerPlaceholder) {
                headerPlaceholder.innerHTML = data;
                initHeader();
            }
        })
        .catch(error => console.error('Error loading header:', error));
    
    // Load footer
    fetch(`${basePath}layouts/footer.jsp`)
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
    // Update navigation based on user role
    const user = JSON.parse(localStorage.getItem('currentUser') || '{}');
    const navAuth = document.getElementById('navAuth');
    const navUser = document.getElementById('navUser');
    const userMenu = document.getElementById('userMenu');
    const adminMenu = document.getElementById('adminMenu');
    const userName = document.getElementById('userName');
    
    if (user.id) {
        if (navAuth) navAuth.style.display = 'none';
        if (navUser) {
            navUser.style.display = 'block';
            if (userName) userName.textContent = user.username || 'Tài khoản';
        }
        
        if (user.role === 'admin') {
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
    
    // Logout handler
    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', function(e) {
            e.preventDefault();
            localStorage.removeItem('currentUser');
            window.location.href = '../index.jsp';
        });
    }
    
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

// Load layout when DOM is ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', loadLayout);
} else {
    loadLayout();
}
