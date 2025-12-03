// Admin statistics
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
}

function loadStatistics() {
    const user = JSON.parse(localStorage.getItem('currentUser') || '{}');
    if (!user.id || user.role !== 'admin') {
        window.location.href = '../guest/login.jsp';
        return;
    }
    
    // Mock statistics
    const orders = JSON.parse(localStorage.getItem('orders') || '[]');
    const totalRevenue = orders.reduce((sum, o) => sum + o.total_amount, 0);
    
    document.getElementById('revenue').textContent = formatCurrency(totalRevenue);
    document.getElementById('ordersCount').textContent = orders.length;
    document.getElementById('newUsers').textContent = '5';
    document.getElementById('topProduct').textContent = 'Thẻ nạp Viettel 50.000đ';
    
    var filterPeriodEl = document.getElementById('filterPeriod');
    if (filterPeriodEl) {
        filterPeriodEl.addEventListener('change', function() {
            // Filter statistics by period
            alert('Lọc theo kỳ: ' + this.value);
        });
    }
}

document.addEventListener('DOMContentLoaded', loadStatistics);
