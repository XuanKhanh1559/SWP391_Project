<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký - Hệ Thống Bán Thẻ Điện Thoại</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <!-- Header Placeholder -->
    <div id="header-placeholder"></div>

    <!-- Main Content -->
    <main class="main-content">
        <div class="auth-container">
            <div class="auth-card">
                <h2>Đăng ký</h2>
                <% if (request.getAttribute("error") != null) { %>
                    <div class="alert alert-error" style="color: red; margin-bottom: 1rem; padding: 0.75rem; background-color: #fee; border: 1px solid #fcc; border-radius: 4px;">
                        <%= request.getAttribute("error") %>
                    </div>
                <% } %>
                <form id="registerForm" action="${pageContext.request.contextPath}/register" method="POST">
                    <div class="form-group">
                        <label>Username</label>
                        <input type="text" id="registerUsername" name="username" 
                               value="<%= request.getAttribute("username") != null ? request.getAttribute("username") : "" %>" required>
                    </div>
                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" id="registerEmail" name="email" 
                               value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>" required>
                    </div>
                    <div class="form-group">
                        <label>Số điện thoại</label>
                        <input type="tel" id="registerPhone" name="phone" 
                               value="<%= request.getAttribute("phone") != null ? request.getAttribute("phone") : "" %>">
                    </div>
                    <div class="form-group">
                        <label>Mật khẩu</label>
                        <div style="position: relative;">
                            <input type="password" id="registerPassword" name="password" required style="padding-right: 40px;">
                            <i class="fas fa-eye" id="toggleRegisterPassword" style="position: absolute; right: 12px; top: 50%; transform: translateY(-50%); cursor: pointer; color: #666;"></i>
                        </div>
                    </div>
                    <div class="form-group">
                        <label>Xác nhận mật khẩu</label>
                        <div style="position: relative;">
                            <input type="password" id="registerConfirmPassword" name="confirmPassword" required style="padding-right: 40px;">
                            <i class="fas fa-eye" id="toggleRegisterConfirmPassword" style="position: absolute; right: 12px; top: 50%; transform: translateY(-50%); cursor: pointer; color: #666;"></i>
                        </div>
                    </div>
                    <button type="submit" class="btn btn-primary btn-block">Đăng ký</button>
                    <p class="auth-link">Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập</a></p>
                </form>
            </div>
        </div>
    </main>

    <!-- Footer Placeholder -->
    <div id="footer-placeholder"></div>

    <script src="${pageContext.request.contextPath}/js/layout.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script>
        const toggleRegisterPassword = document.getElementById('toggleRegisterPassword');
        const registerPassword = document.getElementById('registerPassword');
        const toggleRegisterConfirmPassword = document.getElementById('toggleRegisterConfirmPassword');
        const registerConfirmPassword = document.getElementById('registerConfirmPassword');
        
        toggleRegisterPassword.addEventListener('click', function() {
            const type = registerPassword.getAttribute('type') === 'password' ? 'text' : 'password';
            registerPassword.setAttribute('type', type);
            toggleRegisterPassword.classList.toggle('fa-eye');
            toggleRegisterPassword.classList.toggle('fa-eye-slash');
        });
        
        toggleRegisterConfirmPassword.addEventListener('click', function() {
            const type = registerConfirmPassword.getAttribute('type') === 'password' ? 'text' : 'password';
            registerConfirmPassword.setAttribute('type', type);
            toggleRegisterConfirmPassword.classList.toggle('fa-eye');
            toggleRegisterConfirmPassword.classList.toggle('fa-eye-slash');
        });
    </script>
</body>
</html>

