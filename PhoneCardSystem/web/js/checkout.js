// Checkout page functionality
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
}

function loadCheckout() {
    const user = JSON.parse(localStorage.getItem('currentUser') || '{}');
    if (!user.id) {
        window.location.href = '../guest/login.jsp';
        return;
    }
    
    const cart = JSON.parse(localStorage.getItem('cart') || '[]');
    if (cart.length === 0) {
        alert('Giỏ hàng trống!');
        window.location.href = 'cart.jsp';
        return;
    }
    
    renderCheckout(cart);
    
    var placeOrderBtn = document.getElementById('placeOrderBtn');
    if (placeOrderBtn) {
        placeOrderBtn.addEventListener('click', placeOrder);
    }
}

function renderCheckout(cart) {
    const itemsContainer = document.getElementById('checkoutItems');
    const subtotal = cart.reduce((sum, item) => sum + (item.product.price * item.quantity), 0);
    const discount = 0;
    const total = subtotal - discount;
    
    itemsContainer.innerHTML = cart.map(item => `
        <div class="cart-item">
            <div class="cart-item-info">
                <h4>${item.product.name}</h4>
                <p>${formatCurrency(item.product.price)} x ${item.quantity}</p>
            </div>
        </div>
    `).join('');
    
    document.getElementById('checkoutSubtotal').textContent = formatCurrency(subtotal);
    document.getElementById('checkoutDiscount').textContent = formatCurrency(discount);
    document.getElementById('checkoutTotal').textContent = formatCurrency(total);
}

function placeOrder() {
    const user = JSON.parse(localStorage.getItem('currentUser') || '{}');
    const cart = JSON.parse(localStorage.getItem('cart') || '[]');
    const paymentMethod = document.querySelector('input[name="paymentMethod"]:checked').value;
    const subtotal = cart.reduce((sum, item) => sum + (item.product.price * item.quantity), 0);
    const total = subtotal;
    
    if (paymentMethod === 'balance' && user.balance < total) {
        alert('Số dư không đủ!');
        return;
    }
    
    // Mock order creation
    const order = {
        id: Date.now(),
        order_code: `ORD${Date.now()}`,
        total_amount: total,
        status: 'completed',
        created_at: new Date().toISOString()
    };
    
    let orders = JSON.parse(localStorage.getItem('orders') || '[]');
    orders.push(order);
    localStorage.setItem('orders', JSON.stringify(orders));
    
    if (paymentMethod === 'balance') {
        user.balance -= total;
        localStorage.setItem('currentUser', JSON.stringify(user));
    }
    
    localStorage.removeItem('cart');
    alert('Đặt hàng thành công!');
    window.location.href = 'orders.jsp';
}

document.addEventListener('DOMContentLoaded', loadCheckout);
