// Admin orders management
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
}

function getStatusText(status) {
    const statusMap = {
        'pending': 'Đang xử lý',
        'completed': 'Hoàn thành',
        'cancelled': 'Đã hủy'
    };
    return statusMap[status] || status;
}

function loadOrders() {
    const user = JSON.parse(localStorage.getItem('currentUser') || '{}');
    if (!user.id || user.role !== 'admin') {
        window.location.href = '../guest/login.html';
        return;
    }
    
    const orders = JSON.parse(localStorage.getItem('orders') || '[]');
    renderOrders(orders);
    
    document.getElementById('filterStatus')?.addEventListener('change', function() {
        const status = this.value;
        const allOrders = JSON.parse(localStorage.getItem('orders') || '[]');
        const filtered = status ? allOrders.filter(o => o.status === status) : allOrders;
        renderOrders(filtered);
    });
}

function renderOrders(orders) {
    const tbody = document.getElementById('ordersTableBody');
    if (!tbody) return;
    
    if (orders.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6">Không có đơn hàng nào</td></tr>';
        return;
    }
    
    tbody.innerHTML = orders.map(order => `
        <tr>
            <td>${order.order_code}</td>
            <td>User #${order.user_id || 1}</td>
            <td>${formatCurrency(order.total_amount)}</td>
            <td><span class="order-status ${order.status}">${getStatusText(order.status)}</span></td>
            <td>${new Date(order.created_at).toLocaleDateString('vi-VN')}</td>
            <td>
                <button class="btn-action btn-view">Xem</button>
                <button class="btn-action btn-edit">Sửa</button>
            </td>
        </tr>
    `).join('');
}

document.addEventListener('DOMContentLoaded', loadOrders);


