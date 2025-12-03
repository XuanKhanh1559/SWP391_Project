<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thống kê - Admin</title>
    <link rel="stylesheet" href="../css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <!-- Header Placeholder -->
    <div id="header-placeholder"></div>

    <!-- Main Content -->
    <main class="main-content">
        <div class="page-header">
            <h2>Thống kê</h2>
            <div class="filter-bar">
                <select id="filterPeriod" class="filter-select">
                    <option value="today">Hôm nay</option>
                    <option value="week">Tuần này</option>
                    <option value="month">Tháng này</option>
                    <option value="year">Năm này</option>
                </select>
            </div>
        </div>
        <div class="statistics-container">
            <div class="stat-card">
                <h3>Doanh thu</h3>
                <p class="stat-value" id="revenue">0 đ</p>
            </div>
            <div class="stat-card">
                <h3>Số đơn hàng</h3>
                <p class="stat-value" id="ordersCount">0</p>
            </div>
            <div class="stat-card">
                <h3>Người dùng mới</h3>
                <p class="stat-value" id="newUsers">0</p>
            </div>
            <div class="stat-card">
                <h3>Sản phẩm bán chạy</h3>
                <p class="stat-value" id="topProduct">-</p>
            </div>
        </div>
    </main>

    <!-- Footer Placeholder -->
    <div id="footer-placeholder"></div>

    <script src="../js/layout.js"></script>
    <script src="../js/app.js"></script>
    <script src="../js/admin-statistics.js"></script>
</body>
</html>

