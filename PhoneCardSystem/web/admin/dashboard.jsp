<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Hệ Thống Bán Thẻ Điện Thoại</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div id="header-placeholder"></div>

    <main class="main-content">
        <div class="admin-dashboard-container">
            <h2 class="dashboard-title">Admin Dashboard</h2>
            
            <div class="stats-section">
                <h3 class="section-title">Thống kê người dùng</h3>
                <div class="stats-grid">
                    <div class="stat-card-admin">
                        <div class="stat-icon users-icon">
                            <i class="fas fa-users"></i>
                        </div>
                        <div class="stat-info">
                            <h4 class="stat-label">Tổng người dùng</h4>
                            <p class="stat-value">${requestScope.totalUsers}</p>
                            <p class="stat-detail">Hoạt động: ${requestScope.totalActiveUsers}</p>
                        </div>
                    </div>

                    <div class="stat-card-admin">
                        <div class="stat-icon balance-icon">
                            <i class="fas fa-wallet"></i>
                        </div>
                        <div class="stat-info">
                            <h4 class="stat-label">Tổng số dư người dùng</h4>
                            <p class="stat-value">
                                <fmt:formatNumber value="${requestScope.totalUserBalance}" type="currency" currencySymbol="₫" groupingUsed="true" maxFractionDigits="0"/>
                            </p>
                        </div>
                    </div>
                </div>
            </div>

            <div class="stats-section">
                <h3 class="section-title">Thống kê sản phẩm</h3>
                <div class="stats-grid">
                    <div class="stat-card-admin">
                        <div class="stat-icon products-icon">
                            <i class="fas fa-box"></i>
                        </div>
                        <div class="stat-info">
                            <h4 class="stat-label">Tổng sản phẩm</h4>
                            <p class="stat-value">${requestScope.totalProducts}</p>
                            <p class="stat-detail">Đang bán: ${requestScope.totalActiveProducts}</p>
                        </div>
                    </div>
                </div>
            </div>

            <div class="stats-section">
                <h3 class="section-title">Thống kê đơn hàng</h3>
                <div class="stats-grid">
                    <div class="stat-card-admin">
                        <div class="stat-icon orders-icon">
                            <i class="fas fa-shopping-cart"></i>
                        </div>
                        <div class="stat-info">
                            <h4 class="stat-label">Tổng đơn hàng</h4>
                            <p class="stat-value">${requestScope.totalOrders}</p>
                            <p class="stat-detail">Đang xử lý: ${requestScope.totalPendingOrders} | Hoàn thành: ${requestScope.totalCompletedOrders}</p>
                        </div>
                    </div>

                    <div class="stat-card-admin">
                        <div class="stat-icon revenue-icon">
                            <i class="fas fa-dollar-sign"></i>
                        </div>
                        <div class="stat-info">
                            <h4 class="stat-label">Tổng doanh thu</h4>
                            <p class="stat-value">
                                <fmt:formatNumber value="${requestScope.totalRevenue}" type="currency" currencySymbol="₫" groupingUsed="true" maxFractionDigits="0"/>
                            </p>
                        </div>
                    </div>
                </div>
            </div>

            <div class="admin-actions-section">
                <h3 class="section-title">Quản lý nhanh</h3>
                <div class="admin-actions-grid">
                    <a href="${pageContext.request.contextPath}/admin/products" class="admin-action-btn products-btn">
                        <i class="fas fa-box"></i>
                        <span>Quản lý sản phẩm</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/orders" class="admin-action-btn orders-btn">
                        <i class="fas fa-shopping-bag"></i>
                        <span>Quản lý đơn hàng</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/users" class="admin-action-btn users-btn">
                        <i class="fas fa-users"></i>
                        <span>Quản lý người dùng</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/coupons" class="admin-action-btn coupons-btn">
                        <i class="fas fa-ticket-alt"></i>
                        <span>Quản lý mã giảm giá</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/providers" class="admin-action-btn providers-btn">
                        <i class="fas fa-building"></i>
                        <span>Quản lý nhà cung cấp</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/statistics" class="admin-action-btn statistics-btn">
                        <i class="fas fa-chart-line"></i>
                        <span>Thống kê chi tiết</span>
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
