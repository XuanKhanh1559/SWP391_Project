<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Navigation -->
<nav class="navbar">
    <div class="nav-container">
        <div class="nav-logo">
            <i class="fas fa-mobile-alt"></i>
            <span id="navLogoText">Thẻ Điện Thoại</span>
        </div>
        <ul class="nav-menu" id="navMenu">
            <li><a href="${pageContext.request.contextPath}/index.jsp" class="nav-link" data-page="home">Trang chủ</a></li>
            <li><a href="${pageContext.request.contextPath}/products" class="nav-link" data-page="products">Sản phẩm</a></li>
            <li id="userMenu" style="display: none;">
                <a href="${pageContext.request.contextPath}/user/dashboard" class="nav-link" data-page="dashboard">Dashboard</a>
                <a href="${pageContext.request.contextPath}/cart" class="nav-link" data-page="cart">Giỏ hàng <span class="cart-count" id="cartCount">0</span></a>
                <a href="${pageContext.request.contextPath}/user/orders.jsp" class="nav-link" data-page="orders">Đơn hàng</a>
                <a href="${pageContext.request.contextPath}/user/coupons.jsp" class="nav-link" data-page="coupons">Mã giảm giá</a>
                <a href="${pageContext.request.contextPath}/user/notifications.jsp" class="nav-link" data-page="notifications">Thông báo</a>
            </li>
            <li id="adminMenu" style="display: none;">
                <a href="${pageContext.request.contextPath}/admin/dashboard.jsp" class="nav-link" data-page="admin-dashboard">Quản trị</a>
            </li>
        </ul>
        <div class="nav-auth" id="navAuth">
            <a href="${pageContext.request.contextPath}/login" class="btn btn-outline">Đăng nhập</a>
            <a href="${pageContext.request.contextPath}/register" class="btn btn-primary">Đăng ký</a>
        </div>
        <div class="nav-user" id="navUser" style="display: none;">
            <a href="#" class="nav-link" id="userMenuToggle"><i class="fas fa-user"></i> <span id="userName">Tài khoản</span></a>
            <div class="user-dropdown" id="userDropdown">
                <a href="${pageContext.request.contextPath}/profile">Hồ sơ</a>
                <a href="${pageContext.request.contextPath}/user/deposit.jsp">Nạp tiền</a>
                <a href="${pageContext.request.contextPath}/logout" id="logoutBtn">Đăng xuất</a>
            </div>
        </div>
        <div class="nav-toggle" id="navToggle">
            <span></span>
            <span></span>
            <span></span>
        </div>
    </div>
</nav>


