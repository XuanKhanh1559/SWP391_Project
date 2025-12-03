// Admin coupons management
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
}

const mockCoupons = [
    { id: 1, code: 'WELCOME10', name: 'Chào mừng 10%', discount_type: 'percentage', discount_value: 10, min_order_amount: 50000, status: 'active' },
    { id: 2, code: 'SAVE20K', name: 'Tiết kiệm 20K', discount_type: 'fixed_amount', discount_value: 20000, min_order_amount: 100000, status: 'active' }
];

function loadCoupons() {
    const user = JSON.parse(localStorage.getItem('currentUser') || '{}');
    if (!user.id || user.role !== 'admin') {
        window.location.href = '../guest/login.html';
        return;
    }
    
    renderCoupons();
    
    document.getElementById('addCouponBtn')?.addEventListener('click', function() {
        alert('Tính năng thêm coupon đang được phát triển');
    });
}

function renderCoupons() {
    const tbody = document.getElementById('couponsTableBody');
    if (!tbody) return;
    
    tbody.innerHTML = mockCoupons.map(coupon => `
        <tr>
            <td>${coupon.code}</td>
            <td>${coupon.name}</td>
            <td>${coupon.discount_type === 'percentage' ? 'Phần trăm' : 'Số tiền cố định'}</td>
            <td>${coupon.discount_type === 'percentage' ? `${coupon.discount_value}%` : formatCurrency(coupon.discount_value)}</td>
            <td>${formatCurrency(coupon.min_order_amount)}</td>
            <td><span class="order-status ${coupon.status}">${coupon.status === 'active' ? 'Hoạt động' : 'Tạm dừng'}</span></td>
            <td>
                <button class="btn-action btn-edit">Sửa</button>
                <button class="btn-action btn-delete">Xóa</button>
            </td>
        </tr>
    `).join('');
}

document.addEventListener('DOMContentLoaded', loadCoupons);


