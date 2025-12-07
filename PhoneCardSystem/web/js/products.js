function addToCart(productId) {
    const products = window.productsData || [];
    const product = products.find(p => p.id === productId);
    if (!product) {
        alert('Không tìm thấy sản phẩm!');
        return;
    }
    
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

document.addEventListener('DOMContentLoaded', function() {
    const filterForm = document.getElementById('filterForm');
    if (filterForm) {
        const filterProviderEl = document.getElementById('filterProvider');
        const filterTypeEl = document.getElementById('filterType');
        const searchProductEl = document.getElementById('searchProduct');
        
        if (filterProviderEl) {
            filterProviderEl.addEventListener('change', function() {
                filterForm.submit();
            });
        }
        
        if (filterTypeEl) {
            filterTypeEl.addEventListener('change', function() {
                filterForm.submit();
            });
        }
        
        if (searchProductEl) {
            let searchTimeout;
            searchProductEl.addEventListener('input', function() {
                clearTimeout(searchTimeout);
                searchTimeout = setTimeout(function() {
                    filterForm.submit();
                }, 500);
            });
        }
    }
});
