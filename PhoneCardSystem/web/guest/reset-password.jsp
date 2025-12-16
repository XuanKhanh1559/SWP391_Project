<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt lại mật khẩu - Hệ Thống Bán Thẻ Điện Thoại</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div id="header-placeholder"></div>

    <main class="main-content">
        <div class="auth-container">
            <div class="auth-card">
                <h2>Đặt lại mật khẩu</h2>
                <% if (request.getAttribute("sent") != null) { %>
                    <div class="alert alert-success" style="color: green; margin-bottom: 1rem; padding: 0.75rem; background-color: #efe; border: 1px solid #cfc; border-radius: 4px;">
                        Mã xác nhận đã được gửi đến email của bạn. Vui lòng kiểm tra hộp thư.
                    </div>
                <% } %>
                <% if (request.getAttribute("error") != null) { %>
                    <div class="alert alert-error" style="color: red; margin-bottom: 1rem; padding: 0.75rem; background-color: #fee; border: 1px solid #fcc; border-radius: 4px;">
                        <%= request.getAttribute("error") %>
                    </div>
                <% } %>
                <form id="resetPasswordForm" action="${pageContext.request.contextPath}/reset-password" method="POST">
                    <input type="hidden" name="email" value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : request.getParameter("email") != null ? request.getParameter("email") : "" %>">
                    <div class="form-group">
                        <label>Mã xác nhận</label>
                        <input type="text" id="resetToken" name="resetToken" 
                               value="<%= request.getParameter("token") != null ? request.getParameter("token") : "" %>" 
                               placeholder="Nhập mã 6 chữ số" required>
                    </div>
                    <div class="form-group">
                        <label>Mật khẩu mới</label>
                        <div style="position: relative;">
                            <input type="password" id="resetPassword" name="password" required style="padding-right: 40px;">
                            <i class="fas fa-eye" id="toggleResetPassword" style="position: absolute; right: 12px; top: 50%; transform: translateY(-50%); cursor: pointer; color: #666;"></i>
                        </div>
                    </div>
                    <div class="form-group">
                        <label>Xác nhận mật khẩu</label>
                        <div style="position: relative;">
                            <input type="password" id="resetConfirmPassword" name="confirmPassword" required style="padding-right: 40px;">
                            <i class="fas fa-eye" id="toggleResetConfirmPassword" style="position: absolute; right: 12px; top: 50%; transform: translateY(-50%); cursor: pointer; color: #666;"></i>
                        </div>
                    </div>
                    <button type="submit" class="btn btn-primary btn-block" style="padding: 0.5rem 0.5rem;">Đặt lại mật khẩu</button>
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
    <script>
        const toggleResetPassword = document.getElementById('toggleResetPassword');
        const resetPassword = document.getElementById('resetPassword');
        const toggleResetConfirmPassword = document.getElementById('toggleResetConfirmPassword');
        const resetConfirmPassword = document.getElementById('resetConfirmPassword');
        
        if (toggleResetPassword && resetPassword) {
            toggleResetPassword.addEventListener('click', function() {
                const type = resetPassword.getAttribute('type') === 'password' ? 'text' : 'password';
                resetPassword.setAttribute('type', type);
                toggleResetPassword.classList.toggle('fa-eye');
                toggleResetPassword.classList.toggle('fa-eye-slash');
            });
        }
        
        if (toggleResetConfirmPassword && resetConfirmPassword) {
            toggleResetConfirmPassword.addEventListener('click', function() {
                const type = resetConfirmPassword.getAttribute('type') === 'password' ? 'text' : 'password';
                resetConfirmPassword.setAttribute('type', type);
                toggleResetConfirmPassword.classList.toggle('fa-eye');
                toggleResetConfirmPassword.classList.toggle('fa-eye-slash');
            });
        }
    </script>
</body>
</html>

