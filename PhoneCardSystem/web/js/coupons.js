// Coupons page functionality
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
}

const mockCoupons = [
    { id: 1, code: 'WELCOME10', name: 'Chào mừng 10%', discount_type: 'percentage', discount_value: 10, min_order_amount: 50000, max_discount_amount: 50000, status: 'active' },
    { id: 2, code: 'SAVE20K', name: 'Tiết kiệm 20K', discount_type: 'fixed_amount', discount_value: 20000, min_order_amount: 100000, status: 'active' },
    { id: 3, code: 'VIP50', name: 'VIP 50%', discount_type: 'percentage', discount_value: 50, min_order_amount: 200000, max_discount_amount: 100000, status: 'active' }
];

function loadCoupons() {
    const user = JSON.parse(localStorage.getItem('currentUser') || '{}');
    if (!user.id) {
        window.location.href = '../guest/login.jsp';
        return;
    }
    
    renderCoupons();
}

function renderCoupons() {
    const container = document.getElementById('couponsList');
    if (!container) return;
    
    container.innerHTML = mockCoupons.map(coupon => `
        <div class="coupon-card">
            <div class="coupon-code">${coupon.code}</div>
            <div class="coupon-name">${coupon.name}</div>
            <div class="coupon-discount">
                ${coupon.discount_type === 'percentage' ? `${coupon.discount_value}%` : formatCurrency(coupon.discount_value)}
            </div>
            <div class="coupon-conditions">
                Áp dụng cho đơn hàng từ ${formatCurrency(coupon.min_order_amount)}
            </div>
        </div>
    `).join('');
}

document.addEventListener('DOMContentLoaded', loadCoupons);
