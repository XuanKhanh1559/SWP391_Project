// Order detail page functionality
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

function loadOrderDetail() {
    const user = JSON.parse(localStorage.getItem('currentUser') || '{}');
    if (!user.id) {
        window.location.href = '../guest/login.html';
        return;
    }
    
    const urlParams = new URLSearchParams(window.location.search);
    const orderId = parseInt(urlParams.get('id'));
    
    const orders = JSON.parse(localStorage.getItem('orders') || '[]');
    const order = orders.find(o => o.id === orderId);
    
    if (!order) {
        document.getElementById('orderDetailContainer').innerHTML = '<p>Không tìm thấy đơn hàng</p>';
        return;
    }
    
    renderOrderDetail(order);
}

function renderOrderDetail(order) {
    const container = document.getElementById('orderDetailContainer');
    container.innerHTML = `
        <h2>Chi tiết đơn hàng</h2>
        <div class="order-detail-info">
            <p><strong>Mã đơn hàng:</strong> ${order.order_code}</p>
            <p><strong>Ngày đặt:</strong> ${new Date(order.created_at).toLocaleString('vi-VN')}</p>
            <p><strong>Trạng thái:</strong> <span class="order-status ${order.status}">${getStatusText(order.status)}</span></p>
            <p><strong>Tổng tiền:</strong> ${formatCurrency(order.total_amount)}</p>
        </div>
    `;
}

document.addEventListener('DOMContentLoaded', loadOrderDetail);


