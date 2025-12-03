// Cart page functionality
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
}

function loadCart() {
    const user = JSON.parse(localStorage.getItem('currentUser') || '{}');
    if (!user.id) {
        window.location.href = '../guest/login.jsp';
        return;
    }
    
    let cart = JSON.parse(localStorage.getItem('cart') || '[]');
    renderCart(cart);
    updateCartSummary(cart);
    updateCartCount(cart);
}

function renderCart(cart) {
    const container = document.getElementById('cartItems');
    if (!container) return;
    
    if (cart.length === 0) {
        container.innerHTML = '<p>Giỏ hàng trống</p>';
        return;
    }
    
    container.innerHTML = cart.map((item, index) => `
        <div class="cart-item">
            <div class="cart-item-info">
                <h4>${item.product.name}</h4>
                <p>${formatCurrency(item.product.price)} x ${item.quantity}</p>
            </div>
            <div class="cart-item-actions">
                <button class="btn btn-outline" onclick="updateCartQuantity(${index}, -1)">-</button>
                <span>${item.quantity}</span>
                <button class="btn btn-outline" onclick="updateCartQuantity(${index}, 1)">+</button>
                <button class="btn btn-outline" onclick="removeFromCart(${index})">
                    <i class="fas fa-trash"></i>
                </button>
            </div>
        </div>
    `).join('');
}

function updateCartQuantity(index, delta) {
    let cart = JSON.parse(localStorage.getItem('cart') || '[]');
    const item = cart[index];
    item.quantity += delta;
    if (item.quantity <= 0) {
        cart.splice(index, 1);
    }
    localStorage.setItem('cart', JSON.stringify(cart));
    loadCart();
}

function removeFromCart(index) {
    let cart = JSON.parse(localStorage.getItem('cart') || '[]');
    cart.splice(index, 1);
    localStorage.setItem('cart', JSON.stringify(cart));
    loadCart();
}

function updateCartSummary(cart) {
    const subtotal = cart.reduce((sum, item) => sum + (item.product.price * item.quantity), 0);
    const discount = 0; // TODO: Apply coupon discount
    const total = subtotal - discount;
    
    document.getElementById('cartSubtotal').textContent = formatCurrency(subtotal);
    document.getElementById('cartDiscount').textContent = formatCurrency(discount);
    document.getElementById('cartTotal').textContent = formatCurrency(total);
}

function updateCartCount(cart) {
    const count = cart.reduce((sum, item) => sum + item.quantity, 0);
    const cartCountEl = document.getElementById('cartCount');
    if (cartCountEl) {
        cartCountEl.textContent = count;
    }
}

document.addEventListener('DOMContentLoaded', function() {
    loadCart();
    
    document.getElementById('checkoutBtn')?.addEventListener('click', function() {
        const cart = JSON.parse(localStorage.getItem('cart') || '[]');
        if (cart.length === 0) {
            alert('Giỏ hàng trống!');
            return;
        }
        window.location.href = 'checkout.jsp';
    });
    
    document.getElementById('applyCouponBtn')?.addEventListener('click', function() {
        alert('Tính năng áp dụng coupon đang được phát triển');
    });
});


