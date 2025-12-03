// Orders page functionality
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
}

function getStatusText(status) {
    const statusMap = {
        'pending': 'Đang xử lý',
        'processing': 'Đang xử lý',
        'completed': 'Hoàn thành',
        'cancelled': 'Đã hủy',
        'refunded': 'Đã hoàn tiền'
    };
    return statusMap[status] || status;
}

function loadOrders() {
    const user = JSON.parse(localStorage.getItem('currentUser') || '{}');
    if (!user.id) {
        window.location.href = '../guest/login.jsp';
        return;
    }
    
    const orders = JSON.parse(localStorage.getItem('orders') || '[]');
    renderOrders(orders);
}

function renderOrders(orders) {
    const container = document.getElementById('ordersList');
    if (!container) return;
    
    if (orders.length === 0) {
        container.innerHTML = '<p>Chưa có đơn hàng nào</p>';
        return;
    }
    
    container.innerHTML = orders.map(order => `
        <div class="order-card" onclick="window.location.href='order-detail.jsp?id=${order.id}'">
            <div class="order-header">
                <div>
                    <h3>${order.order_code}</h3>
                    <p>Ngày đặt: ${new Date(order.created_at).toLocaleDateString('vi-VN')}</p>
                </div>
                <div>
                    <span class="order-status ${order.status}">${getStatusText(order.status)}</span>
                    <p style="margin-top: 0.5rem; font-weight: bold;">${formatCurrency(order.total_amount)}</p>
                </div>
            </div>
        </div>
    `).join('');
}

document.addEventListener('DOMContentLoaded', loadOrders);


