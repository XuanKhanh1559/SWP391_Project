<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Navigation -->
<nav class="navbar">
    <div class="nav-container">
        <div class="nav-logo">
            <i class="fas fa-mobile-alt"></i>
            <span id="navLogoText">Thẻ Điện Thoại</span>
        </div>
        
        <ul class="nav-menu" id="navMenu">
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/index.jsp" class="nav-link" data-page="home">
                    <i class="fas fa-home"></i>
                    <span class="nav-text">Trang chủ</span>
                </a>
            </li>
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/products" class="nav-link" data-page="products">
                    <i class="fas fa-box"></i>
                    <span class="nav-text">Sản phẩm</span>
                </a>
            </li>
            <li id="userMenu" class="nav-item-group" style="display: none;">
                <a href="${pageContext.request.contextPath}/user/dashboard" class="nav-link" data-page="dashboard">
                    <i class="fas fa-chart-line"></i>
                    <span class="nav-text">Dashboard</span>
                </a>
                <a href="${pageContext.request.contextPath}/cart" class="nav-link" data-page="cart">
                    <i class="fas fa-shopping-cart"></i>
                    <span class="nav-text">Giỏ hàng</span>
                    <span class="nav-badge cart-count" id="cartCount">0</span>
                </a>
                <a href="${pageContext.request.contextPath}/user/orders" class="nav-link" data-page="orders">
                    <i class="fas fa-receipt"></i>
                    <span class="nav-text">Đơn hàng</span>
                </a>
                <a href="${pageContext.request.contextPath}/user/coupons" class="nav-link" data-page="coupons">
                    <i class="fas fa-ticket-alt"></i>
                    <span class="nav-text">Mã giảm giá</span>
                </a>
                <!-- <a href="${pageContext.request.contextPath}/user/notifications.jsp" class="nav-link" data-page="notifications">
                    <i class="fas fa-bell"></i>
                    <span class="nav-text">Thông báo</span>
                </a> -->
            </li>
            <li id="adminMenu" class="nav-item" style="display: none;">
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link" data-page="admin-dashboard">
                    <i class="fas fa-cog"></i>
                    <span class="nav-text">Quản trị</span>
                </a>
            </li>
        </ul>
        
        <div class="nav-actions">
            <div class="nav-auth" id="navAuth">
                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline">
                    <i class="fas fa-sign-in-alt"></i>
                    <span>Đăng nhập</span>
                </a>
                <a href="${pageContext.request.contextPath}/register" class="btn btn-primary">
                    <i class="fas fa-user-plus"></i>
                    <span>Đăng ký</span>
                </a>
            </div>
            <div class="nav-user" id="navUser" style="display: none;">
                <a href="#" class="nav-link" id="userMenuToggle"><i class="fas fa-user"></i> <span id="userName">Tài khoản</span></a>
                <div class="user-dropdown" id="userDropdown">
                    <a href="${pageContext.request.contextPath}/profile">Hồ sơ</a>
                    <a href="${pageContext.request.contextPath}/user/deposit.jsp">Nạp tiền</a>
                    <a href="${pageContext.request.contextPath}/logout" id="logoutBtn">Đăng xuất</a>
                </div>
            </div>
        </div>
        
        <button class="nav-toggle" id="navToggle" type="button" aria-label="Toggle menu">
            <span class="hamburger-line"></span>
            <span class="hamburger-line"></span>
            <span class="hamburger-line"></span>
        </button>
    </div>
</nav>


