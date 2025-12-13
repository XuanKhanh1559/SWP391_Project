const contextPath = window.location.pathname.split('/')[1] ? '/' + window.location.pathname.split('/')[1] : '';

function editProduct(productId) {
    showToast('Tính năng đang phát triển', 'info');
}

function deleteProduct(productId) {
    if (confirm('Bạn có chắc chắn muốn xóa sản phẩm này?')) {
        showToast('Tính năng đang phát triển', 'info');
    }
}

document.addEventListener('DOMContentLoaded', function() {
    const filterForm = document.getElementById('filterForm');
    if (filterForm) {
        const filterProviderEl = document.getElementById('filterProvider');
        const filterTypeEl = document.getElementById('filterType');
        const filterStatusEl = document.getElementById('filterStatus');
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
        
        if (filterStatusEl) {
            filterStatusEl.addEventListener('change', function() {
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
    
    const addProductBtn = document.getElementById('addProductBtn');
    if (addProductBtn) {
        addProductBtn.addEventListener('click', function() {
            showToast('Tính năng thêm sản phẩm đang được phát triển', 'info');
        });
    }
});
