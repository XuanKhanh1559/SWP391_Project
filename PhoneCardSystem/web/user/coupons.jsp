<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mã giảm giá - Hệ Thống Bán Thẻ Điện Thoại</title>
    <link rel="stylesheet" href="../css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <!-- Header Placeholder -->
    <div id="header-placeholder"></div>

    <!-- Main Content -->
    <main class="main-content">
        <div class="page-header">
            <h2>Mã giảm giá</h2>
        </div>
        <div class="coupons-container">
            <div class="coupons-list" id="couponsList">
                <!-- Coupons will be loaded here -->
            </div>
        </div>
    </main>

    <!-- Footer Placeholder -->
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
    <script src="../js/layout.js"></script>
    <script src="../js/app.js"></script>
    <script src="../js/coupons.js"></script>
</body>
</html>


