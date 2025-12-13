const contextPath = window.location.pathname.split('/')[1] ? '/' + window.location.pathname.split('/')[1] : '';

function viewUserDetail(userId) {
    window.location.href = contextPath + '/admin/user-detail?id=' + userId;
}

function banUser(userId) {
    showConfirm(
        'Bạn có chắc chắn muốn khóa người dùng này?',
        function() {
            fetch(contextPath + '/admin/ban-user', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'id=' + userId
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

function unbanUser(userId) {
    showConfirm(
        'Bạn có chắc chắn muốn mở khóa người dùng này?',
        function() {
            fetch(contextPath + '/admin/ban-user', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'id=' + userId + '&action=unban'
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
        const filterStatusEl = document.getElementById('filterStatus');
        const searchUserEl = document.getElementById('searchUser');
        
        if (filterStatusEl) {
            filterStatusEl.addEventListener('change', function() {
                filterForm.submit();
            });
        }
        
        if (searchUserEl) {
            let searchTimeout;
            searchUserEl.addEventListener('input', function() {
                clearTimeout(searchTimeout);
                searchTimeout = setTimeout(function() {
                    filterForm.submit();
                }, 500);
            });
        }
    }
});
