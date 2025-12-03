<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Hệ Thống Bán Thẻ Điện Thoại</title>
    <link rel="stylesheet" href="../css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <!-- Header Placeholder -->
    <div id="header-placeholder"></div>

    <!-- Main Content -->
    <main class="main-content">
        <div class="dashboard-container">
            <h2>Dashboard</h2>
            <div class="dashboard-stats">
                <div class="stat-card">
                    <i class="fas fa-wallet"></i>
                    <h3>Số dư</h3>
                    <p class="stat-value" id="userBalance">0 đ</p>
                </div>
                <div class="stat-card">
                    <i class="fas fa-shopping-cart"></i>
                    <h3>Đơn hàng</h3>
                    <p class="stat-value" id="totalOrders">0</p>
                </div>
                <div class="stat-card">
                    <i class="fas fa-ticket-alt"></i>
                    <h3>Coupon</h3>
                    <p class="stat-value" id="totalCoupons">0</p>
                </div>
                <div class="stat-card">
                    <i class="fas fa-bell"></i>
                    <h3>Thông báo</h3>
                    <p class="stat-value" id="unreadNotifications">0</p>
                </div>
            </div>
            <div class="dashboard-actions">
                <a href="../guest/products.jsp" class="btn btn-primary">Mua sản phẩm</a>
                <a href="deposit.jsp" class="btn btn-outline">Nạp tiền</a>
                <a href="orders.jsp" class="btn btn-outline">Xem đơn hàng</a>
            </div>
        </div>
    </main>

    <!-- Footer Placeholder -->
    <div id="footer-placeholder"></div>

    <script src="../js/layout.js"></script>
    <script src="../js/app.js"></script>
    <script>
        const user = JSON.parse(localStorage.getItem('currentUser') || '{}');
        if (!user.id) {
            window.location.href = '../guest/login.jsp';
        }
        
        document.getElementById('userBalance').textContent = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(user.balance || 0);
    </script>
</body>
</html>

