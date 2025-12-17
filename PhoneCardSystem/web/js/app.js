// Common app functionality
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
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

function updateBalance() {
    const contextPath = window.location.pathname.split('/')[1] ? '/' + window.location.pathname.split('/')[1] : '';
    
    if (!window.userData || !window.userData.id) {
        return;
    }
    
    fetch(`${contextPath}/api/balance`, {
        headers: {
            'Accept': 'application/json; charset=UTF-8'
        }
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // Only update balance of current logged-in user, NOT view-user balance
                // Exclude elements with class 'view-user-balance' to prevent overwriting other user's balance
                const balanceElements = document.querySelectorAll('.user-balance, .balance-amount, [data-balance]');
                balanceElements.forEach(el => {
                    // Skip if it's a view-user balance - don't overwrite other user's balance
                    if (el.classList.contains('view-user-balance')) {
                        return;
                    }
                    const formattedBalance = new Intl.NumberFormat('vi-VN', { 
                        style: 'currency', 
                        currency: 'VND' 
                    }).format(data.balance);
                    el.textContent = formattedBalance;
                });
                
                if (window.userData) {
                    window.userData.balance = data.balance;
                }
            }
        })
        .catch(error => {
            console.error('Error updating balance:', error);
        });
}

window.updateCartCount = updateCartCount;
window.updateBalance = updateBalance;

function initApp() {
    updateCartCount();
    
    if (window.userData && window.userData.id) {
        updateBalance();
        setInterval(updateBalance, 10000);
        
        window.addEventListener('focus', function() {
            updateBalance();
        });
    }
    
    const navToggle = document.getElementById('navToggle');
    const navMenu = document.getElementById('navMenu');
    
    if (navToggle && navMenu) {
        navToggle.addEventListener('click', () => {
            navMenu.classList.toggle('active');
        });
    }
}

// Initialize when DOM is ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initApp);
} else {
    initApp();
}
