<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Hệ Thống Bán Thẻ Điện Thoại</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div id="header-placeholder"></div>

    <main class="main-content">
        <div class="dashboard-container">
            <h2 class="dashboard-title">Dashboard</h2>
            
            <div class="dashboard-stats">
                <div class="stat-card">
                    <div class="stat-icon balance-icon">
                        <i class="fas fa-wallet"></i>
                    </div>
                    <div class="stat-info">
                        <h3 class="stat-label">Số dư tài khoản</h3>
                        <p class="stat-value">
                            <fmt:formatNumber value="${sessionScope.user.balance}" type="currency" currencySymbol="₫" groupingUsed="true" maxFractionDigits="0"/>
                        </p>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon orders-icon">
                        <i class="fas fa-shopping-bag"></i>
                    </div>
                    <div class="stat-info">
                        <h3 class="stat-label">Tổng đơn hàng</h3>
                        <p class="stat-value">${requestScope.totalOrders}</p>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon coupons-icon">
                        <i class="fas fa-ticket-alt"></i>
                    </div>
                    <div class="stat-info">
                        <h3 class="stat-label">Mã giảm giá khả dụng</h3>
                        <p class="stat-value">${requestScope.availableCoupons}</p>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon notifications-icon">
                        <i class="fas fa-bell"></i>
                    </div>
                    <div class="stat-info">
                        <h3 class="stat-label">Thông báo chưa đọc</h3>
                        <p class="stat-value">${requestScope.unreadNotifications}</p>
                    </div>
                </div>
            </div>

            <div class="dashboard-actions">
                <h3 class="actions-title">Hành động nhanh</h3>
                <div class="actions-grid">
                    <a href="${pageContext.request.contextPath}/products" class="action-btn buy-products">
                        <i class="fas fa-shopping-cart"></i>
                        <span>Mua sản phẩm</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/deposit" class="action-btn deposit">
                        <i class="fas fa-plus-circle"></i>
                        <span>Nạp tiền</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/user/orders" class="action-btn view-orders">
                        <i class="fas fa-list"></i>
                        <span>Xem đơn hàng</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/profile" class="action-btn view-profile">
                        <i class="fas fa-user"></i>
                        <span>Xem hồ sơ</span>
                    </a>
                </div>
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
</body>
</html>
