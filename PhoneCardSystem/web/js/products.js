// Products page functionality
const mockProducts = [
    { id: 1, provider_id: 1, name: 'Thẻ nạp Viettel 10.000đ', type: 'topup', denomination: 10000, price: 10000, description: 'Thẻ nạp tiền điện thoại Viettel mệnh giá 10.000đ', status: 'active' },
    { id: 2, provider_id: 1, name: 'Thẻ nạp Viettel 20.000đ', type: 'topup', denomination: 20000, price: 20000, description: 'Thẻ nạp tiền điện thoại Viettel mệnh giá 20.000đ', status: 'active' },
    { id: 3, provider_id: 1, name: 'Thẻ nạp Viettel 50.000đ', type: 'topup', denomination: 50000, price: 50000, description: 'Thẻ nạp tiền điện thoại Viettel mệnh giá 50.000đ', status: 'active' },
    { id: 4, provider_id: 1, name: 'Gói data Viettel 1GB/30 ngày', type: 'data_package', denomination: 0, price: 50000, description: 'Gói data 1GB sử dụng trong 30 ngày', status: 'active' },
    { id: 5, provider_id: 2, name: 'Thẻ nạp Vinaphone 10.000đ', type: 'topup', denomination: 10000, price: 10000, description: 'Thẻ nạp tiền điện thoại Vinaphone mệnh giá 10.000đ', status: 'active' },
    { id: 6, provider_id: 2, name: 'Thẻ nạp Vinaphone 20.000đ', type: 'topup', denomination: 20000, price: 20000, description: 'Thẻ nạp tiền điện thoại Vinaphone mệnh giá 20.000đ', status: 'active' },
    { id: 7, provider_id: 3, name: 'Thẻ nạp Mobifone 10.000đ', type: 'topup', denomination: 10000, price: 10000, description: 'Thẻ nạp tiền điện thoại Mobifone mệnh giá 10.000đ', status: 'active' }
];

function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
}

function renderProducts(products) {
    const grid = document.getElementById('productsGrid');
    if (!grid) return;
    
    grid.innerHTML = products.map(product => `
        <div class="product-card">
            <div class="product-image">
                <i class="fas fa-mobile-alt"></i>
            </div>
            <div class="product-info">
                <h3>${product.name}</h3>
                <p>${product.description}</p>
                <div class="product-price">${formatCurrency(product.price)}</div>
                <div class="product-actions">
                    <button class="btn btn-primary btn-add-cart" onclick="addToCart(${product.id})">
                        <i class="fas fa-cart-plus"></i> Thêm vào giỏ
                    </button>
                </div>
            </div>
        </div>
    `).join('');
}

function filterProducts() {
    const providerFilter = document.getElementById('filterProvider')?.value || '';
    const typeFilter = document.getElementById('filterType')?.value || '';
    const searchTerm = (document.getElementById('searchProduct')?.value || '').toLowerCase();
    
    let filtered = mockProducts.filter(product => {
        const matchProvider = !providerFilter || product.provider_id == providerFilter;
        const matchType = !typeFilter || product.type === typeFilter;
        const matchSearch = !searchTerm || product.name.toLowerCase().includes(searchTerm);
        return matchProvider && matchType && matchSearch;
    });
    
    renderProducts(filtered);
}

function addToCart(productId) {
    const user = JSON.parse(localStorage.getItem('currentUser') || '{}');
    if (!user.id) {
        alert('Vui lòng đăng nhập để mua hàng!');
        window.location.href = 'login.html';
        return;
    }
    
    const product = mockProducts.find(p => p.id === productId);
    if (!product) return;
    
    let cart = JSON.parse(localStorage.getItem('cart') || '[]');
    const existingItem = cart.find(item => item.productId === productId);
    
    if (existingItem) {
        existingItem.quantity += 1;
    } else {
        cart.push({
            productId: productId,
            product: product,
            quantity: 1
        });
    }
    
    localStorage.setItem('cart', JSON.stringify(cart));
    alert('Đã thêm vào giỏ hàng!');
}

// Initialize
document.addEventListener('DOMContentLoaded', function() {
    renderProducts(mockProducts);
    
    document.getElementById('filterProvider')?.addEventListener('change', filterProducts);
    document.getElementById('filterType')?.addEventListener('change', filterProducts);
    document.getElementById('searchProduct')?.addEventListener('input', filterProducts);
});


