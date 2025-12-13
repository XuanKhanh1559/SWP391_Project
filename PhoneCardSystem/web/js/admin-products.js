const contextPath = window.location.pathname.split('/')[1] ? '/' + window.location.pathname.split('/')[1] : '';

function editProduct(productId) {
    window.location.href = contextPath + '/admin/edit-product?id=' + productId;
}

function deleteProduct(productId) {
    showConfirm(
        'Bạn có chắc chắn muốn xóa sản phẩm này?',
        function() {
            fetch(contextPath + '/admin/delete-product', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'id=' + productId
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    showToast(data.message, 'success');
                    setTimeout(() => {
                        window.location.reload();
                    }, 1500);
                } else {
                    showToast(data.message, 'error');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showToast('Có lỗi xảy ra. Vui lòng thử lại.', 'error');
            });
        }
    );
}

function restoreProduct(productId) {
    showConfirm(
        'Bạn có chắc chắn muốn khôi phục sản phẩm này?',
        function() {
            fetch(contextPath + '/admin/delete-product', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'id=' + productId + '&action=restore'
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    showToast(data.message, 'success');
                    setTimeout(() => {
                        window.location.reload();
                    }, 1500);
                } else {
                    showToast(data.message, 'error');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showToast('Có lỗi xảy ra. Vui lòng thử lại.', 'error');
            });
        }
    );
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
