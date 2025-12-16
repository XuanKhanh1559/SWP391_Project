<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quên mật khẩu - Hệ Thống Bán Thẻ Điện Thoại</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div id="header-placeholder"></div>

    <main class="main-content">
        <div class="auth-container">
            <div class="auth-card">
                <h2>Quên mật khẩu</h2>
                <% if (request.getAttribute("error") != null) { %>
                    <div class="alert alert-error" style="color: red; margin-bottom: 1rem; padding: 0.75rem; background-color: #fee; border: 1px solid #fcc; border-radius: 4px;">
                        <%= request.getAttribute("error") %>
                    </div>
                <% } %>
                <p style="margin-bottom: 1.5rem; color: #666;">Nhập email của bạn để nhận mã xác nhận đặt lại mật khẩu</p>
                <form id="forgotPasswordForm" action="${pageContext.request.contextPath}/forgot-password" method="POST">
                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" id="forgotEmail" name="email" 
                               value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>" required>
                    </div>
                    <button type="submit" class="btn btn-primary btn-block" style="padding: 0.5rem 0.5rem;">Gửi mã xác nhận</button>
                    <p class="auth-link">
                        <a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a>
                    </p>
                </form>
            </div>
        </div>
    </main>

    <div id="footer-placeholder"></div>

    <script src="${pageContext.request.contextPath}/js/layout.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>


