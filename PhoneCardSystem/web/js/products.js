function addToCart(productId) {
    
    const contextPath = window.location.pathname.split('/')[1] ? '/' + window.location.pathname.split('/')[1] : '';
    
    fetch(contextPath + '/add-to-cart', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
            'Accept': 'application/json; charset=UTF-8'
        },
        body: 'productId=' + productId + '&quantity=1'
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => {
                try {
                    return JSON.parse(text);
                } catch (e) {
                    throw new Error('Invalid response');
                }
            });
        }
        return response.json();
    })
    .then(data => {
        console.log('Add to cart response:', data);
        if (data.success) {
            console.log('Calling showToast with:', data.message, 'success');
            if (typeof showToast === 'function') {
                showToast(data.message, 'success');
            } else {
                console.error('showToast is not a function. Type:', typeof showToast, 'Available:', Object.keys(window).filter(k => k.includes('toast')));
                alert(data.message);
            }
            if (window.updateCartCount) {
                window.updateCartCount();
            }
        } else {
            console.log('Calling showToast with error:', data.message);
            if (typeof showToast === 'function') {
                showToast(data.message, 'error');
            } else {
                console.error('showToast is not a function for error');
                alert(data.message);
            }
        }
    })
    .catch(error => {
        console.error('Error:', error);
        if (typeof showToast === 'function') {
            showToast('Có lỗi xảy ra. Vui lòng thử lại.', 'error');
        } else {
            alert('Có lỗi xảy ra. Vui lòng thử lại.');
        }
    });
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
