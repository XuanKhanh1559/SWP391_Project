const contextPath = window.location.pathname.split('/')[1] ? '/' + window.location.pathname.split('/')[1] : '';

function viewUserDetail(userId) {
    window.location.href = contextPath + '/admin/user-detail?id=' + userId;
}

function banUser(userId) {
    showConfirm(
        'Bạn có chắc chắn muốn khóa người dùng này?',
        function() {
            showToast('Tính năng khóa người dùng đang được phát triển', 'info');
        }
    );
}

function unbanUser(userId) {
    showConfirm(
        'Bạn có chắc chắn muốn mở khóa người dùng này?',
        function() {
            showToast('Tính năng mở khóa người dùng đang được phát triển', 'info');
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
