const contextPath = window.location.pathname.split('/')[1] ? '/' + window.location.pathname.split('/')[1] : '';

function viewCoupon(id) {
    window.location.href = `${contextPath}/admin/coupon-detail?id=${id}`;
}

function editCoupon(id) {
    window.location.href = `${contextPath}/admin/edit-coupon?id=${id}`;
}

function deleteCoupon(id) {
    showConfirm(
        'Bạn có chắc chắn muốn xóa mã giảm giá này?',
        () => {
            fetch(`${contextPath}/admin/delete-coupon`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `id=${id}&action=delete`
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
                showToast('Có lỗi xảy ra khi xóa mã giảm giá', 'error');
            });
        },
        () => {
            console.log('Hủy xóa mã giảm giá');
        },
        'Xác nhận xóa',
        'Xóa',
        'Hủy'
    );
}

function restoreCoupon(id) {
    showConfirm(
        'Bạn có chắc chắn muốn khôi phục mã giảm giá này?',
        () => {
            fetch(`${contextPath}/admin/delete-coupon`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `id=${id}&action=restore`
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
                showToast('Có lỗi xảy ra khi khôi phục mã giảm giá', 'error');
            });
        },
        () => {
            console.log('Hủy khôi phục mã giảm giá');
        },
        'Xác nhận khôi phục',
        'Khôi phục',
        'Hủy'
    );
}
