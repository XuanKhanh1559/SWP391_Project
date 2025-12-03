// Admin products management
const mockProducts = [
    { id: 1, provider_id: 1, name: 'Thẻ nạp Viettel 10.000đ', type: 'topup', price: 10000, status: 'active' },
    { id: 2, provider_id: 1, name: 'Thẻ nạp Viettel 20.000đ', type: 'topup', price: 20000, status: 'active' },
    { id: 3, provider_id: 2, name: 'Thẻ nạp Vinaphone 10.000đ', type: 'topup', price: 10000, status: 'active' }
];

const providerMap = { 1: 'Viettel', 2: 'Vinaphone', 3: 'Mobifone' };

function loadProducts() {
    const user = JSON.parse(localStorage.getItem('currentUser') || '{}');
    if (!user.id || user.role !== 'admin') {
        window.location.href = '../guest/login.jsp';
        return;
    }
    
    renderProducts();
    
    var addProductBtn = document.getElementById('addProductBtn');
    if (addProductBtn) {
        addProductBtn.addEventListener('click', function() {
            alert('Tính năng thêm sản phẩm đang được phát triển');
        });
    }
}

function renderProducts() {
    const tbody = document.getElementById('productsTableBody');
    if (!tbody) return;
    
    tbody.innerHTML = mockProducts.map(product => `
        <tr>
            <td>${product.id}</td>
            <td>${product.name}</td>
            <td>${providerMap[product.provider_id] || 'N/A'}</td>
            <td>${product.type === 'topup' ? 'Nạp tiền' : 'Gói data'}</td>
            <td>${new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(product.price)}</td>
            <td><span class="order-status ${product.status}">${product.status === 'active' ? 'Hoạt động' : 'Tạm dừng'}</span></td>
            <td>
                <button class="btn-action btn-edit">Sửa</button>
                <button class="btn-action btn-delete">Xóa</button>
            </td>
        </tr>
    `).join('');
}

document.addEventListener('DOMContentLoaded', loadProducts);
