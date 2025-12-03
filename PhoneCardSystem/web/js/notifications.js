// Notifications page functionality
const mockNotifications = [
    { id: 1, type: 'order', title: 'Đơn hàng đã được xử lý', content: 'Đơn hàng ORD20240101001 của bạn đã được hoàn thành', is_read: false, created_at: '2024-01-01' },
    { id: 2, type: 'coupon', title: 'Mã giảm giá mới', content: 'Bạn có mã giảm giá WELCOME10 mới', is_read: false, created_at: '2024-01-02' }
];

function loadNotifications() {
    const user = JSON.parse(localStorage.getItem('currentUser') || '{}');
    if (!user.id) {
        window.location.href = '../guest/login.html';
        return;
    }
    
    renderNotifications();
    
    document.getElementById('markAllReadBtn')?.addEventListener('click', markAllRead);
}

function renderNotifications() {
    const container = document.getElementById('notificationsList');
    if (!container) return;
    
    if (mockNotifications.length === 0) {
        container.innerHTML = '<p>Không có thông báo nào</p>';
        return;
    }
    
    container.innerHTML = mockNotifications.map(notif => `
        <div class="notification-item ${notif.is_read ? '' : 'unread'}" onclick="markAsRead(${notif.id})">
            <div class="notification-title">${notif.title}</div>
            <div class="notification-content">${notif.content}</div>
            <div class="notification-time">${new Date(notif.created_at).toLocaleString('vi-VN')}</div>
        </div>
    `).join('');
}

function markAsRead(notifId) {
    const notif = mockNotifications.find(n => n.id === notifId);
    if (notif) {
        notif.is_read = true;
        renderNotifications();
    }
}

function markAllRead() {
    mockNotifications.forEach(notif => notif.is_read = true);
    renderNotifications();
    alert('Đã đánh dấu tất cả đã đọc!');
}

document.addEventListener('DOMContentLoaded', loadNotifications);


