// Common app functionality
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
}


function updateCartCount() {
    const contextPath = window.location.pathname.split('/')[1] ? '/' + window.location.pathname.split('/')[1] : '';
    
    fetch(`${contextPath}/cart-count`)
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
    
    fetch(`${contextPath}/api/balance`)
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                const balanceElements = document.querySelectorAll('.user-balance, .balance-amount, [data-balance]');
                balanceElements.forEach(el => {
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
