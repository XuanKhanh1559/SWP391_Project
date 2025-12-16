<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mã giảm giá - Hệ Thống Bán Thẻ Điện Thoại</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .coupons-container {
            max-width: 1200px;
            margin: 20px auto;
            padding: 20px;
        }
        .coupon-card {
            background: white;
            border-radius: 10px;
            padding: 10px;
            margin-bottom: 20px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-left: 5px solid #ff6b6b;
        }
        .coupon-info {
            flex: 1;
        }
        .coupon-code {
            font-size: 24px;
            font-weight: bold;
            color: #ff6b6b;
            margin-bottom: 10px;
        }
        .coupon-name {
            font-size: 18px;
            margin-bottom: 8px;
        }
        .coupon-desc {
            color: #666;
            font-size: 14px;
            margin-bottom: 10px;
        }
        .coupon-details {
            display: flex;
            gap: 20px;
            flex-wrap: wrap;
            margin-top: 10px;
        }
        .coupon-detail-item {
            font-size: 13px;
            color: #555;
        }
        .coupon-detail-item i {
            margin-right: 5px;
            color: #ff6b6b;
        }
        .coupon-actions {
            display: flex;
            flex-direction: column;
            gap: 10px;
            align-items: flex-end;
        }
        .coupon-value {
            font-size: 20px;
            font-weight: bold;
            color: #ff6b6b;
            margin-bottom: 10px;
        }
        .btn-copy {
            padding: 10px 5px;
            background: #ff6b6b;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
        }
        .btn-copy:hover {
            background: #ff5252;
        }
        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #999;
        }
        .empty-state i {
            font-size: 64px;
            margin-bottom: 20px;
        }
        .loading {
            text-align: center;
            padding: 40px;
            font-size: 18px;
            color: #666;
        }
    </style>
</head>
<body>
    <div id="header-placeholder"></div>

    <main class="main-content">
        <div class="page-header">
            <h2><i class="fas fa-tag"></i> Mã giảm giá khả dụng</h2>
        </div>
        <div class="coupons-container">
            <div class="coupons-list" id="couponsList">
                <div class="loading"><i class="fas fa-spinner fa-spin"></i> Đang tải...</div>
            </div>
        </div>
    </main>

    <div id="footer-placeholder"></div>

    <script>
        window.userData = {
            id: ${sessionScope.user.id},
            username: '<c:out value="${sessionScope.user.username}" escapeXml="true"/>',
            email: '<c:out value="${sessionScope.user.email}" escapeXml="true"/>',
            role: '<c:out value="${sessionScope.user.role}" escapeXml="true"/>',
            balance: ${sessionScope.user.balance}
        };
    </script>
    <script src="${pageContext.request.contextPath}/js/layout.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script src="${pageContext.request.contextPath}/js/toast.js"></script>
    <script>
        const contextPath = '${pageContext.request.contextPath}';
        
        function loadCoupons() {
            fetch(contextPath + '/user/coupons?action=getAvailable')
                .then(response => response.json())
                .then(coupons => {
                    displayCoupons(coupons);
                })
                .catch(error => {
                    console.error('Error loading coupons:', error);
                    document.getElementById('couponsList').innerHTML = '<div class="empty-state"><p>Có lỗi xảy ra khi tải mã giảm giá</p></div>';
                });
        }
        
        function displayCoupons(coupons) {
            const container = document.getElementById('couponsList');
            
            if (!coupons || coupons.length === 0) {
                container.innerHTML = '<div class="empty-state"><i class="fas fa-ticket-alt"></i><p>Hiện tại không có mã giảm giá khả dụng</p></div>';
                return;
            }
            
            let html = '';
            coupons.forEach(coupon => {
                const discountValue = coupon.discount_type == 1 
                    ? '-' + coupon.discount_value + '%' 
                    : '-' + formatCurrency(coupon.discount_value);
                
                let maxDiscountHtml = '';
                if (coupon.max_discount_amount) {
                    maxDiscountHtml = '<div class="coupon-detail-item"><i class="fas fa-coins"></i>Giảm tối đa: ' + formatCurrency(coupon.max_discount_amount) + '</div>';
                }
                
                html += '<div class="coupon-card">' +
                    '<div class="coupon-info">' +
                    '<div class="coupon-code">' + coupon.code + '</div>' +
                    '<div class="coupon-name">' + coupon.name + '</div>' +
                    '<div class="coupon-desc">' + (coupon.description || '') + '</div>' +
                    '<div class="coupon-details">' +
                    '<div class="coupon-detail-item"><i class="fas fa-calendar"></i>HSD: ' + formatDate(coupon.end_date) + '</div>' +
                    '<div class="coupon-detail-item"><i class="fas fa-shopping-cart"></i>Đơn tối thiểu: ' + formatCurrency(coupon.min_order_amount) + '</div>' +
                    '<div class="coupon-detail-item"><i class="fas fa-ticket-alt"></i>Còn lại: ' + coupon.usage_limit_per_user + ' lượt/người</div>' +
                    maxDiscountHtml +
                    '</div></div>' +
                    '<div class="coupon-actions">' +
                    '<div class="coupon-value">' + discountValue + '</div>' +
                    '<button class="btn-copy" onclick="copyCouponCode(\'' + coupon.code + '\')"><i class="fas fa-copy"></i> Sao chép mã</button>' +
                    '</div></div>';
            });
            
            container.innerHTML = html;
        }
        
        function copyCouponCode(code) {
            navigator.clipboard.writeText(code).then(() => {
                showToast('Đã sao chép mã: ' + code, 'success');
            }).catch(err => {
                console.error('Failed to copy:', err);
                showToast('Không thể sao chép mã', 'error');
            });
        }
        
        function formatCurrency(value) {
            return new Intl.NumberFormat('vi-VN', {
                style: 'currency',
                currency: 'VND'
            }).format(value);
        }
        
        function formatDate(dateStr) {
            const date = new Date(dateStr);
            return date.toLocaleDateString('vi-VN');
        }
        
        // Load coupons on page load
        document.addEventListener('DOMContentLoaded', function() {
            loadCoupons();
        });
    </script>
</body>
</html>
