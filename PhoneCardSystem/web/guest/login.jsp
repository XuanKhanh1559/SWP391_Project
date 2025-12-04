<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập - Hệ Thống Bán Thẻ Điện Thoại</title>
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
                <h2>Đăng nhập</h2>
                <% if (request.getAttribute("error") != null) { %>
                    <div class="alert alert-error" style="color: red; margin-bottom: 1rem; padding: 0.75rem; background-color: #fee; border: 1px solid #fcc; border-radius: 4px;">
                        <%= request.getAttribute("error") %>
                    </div>
                <% } %>
                <form id="loginForm" action="${pageContext.request.contextPath}/login" method="POST">
                    <div class="form-group">
                        <label>Email hoặc Username</label>
                        <input type="text" id="loginEmail" name="emailOrUsername" required>
                    </div>
                    <div class="form-group">
                        <label>Mật khẩu</label>
                        <div style="position: relative;">
                            <input type="password" id="loginPassword" name="password" required style="padding-right: 40px;">
                            <i class="fas fa-eye" id="toggleLoginPassword" style="position: absolute; right: 12px; top: 50%; transform: translateY(-50%); cursor: pointer; color: #666;"></i>
                        </div>
                    </div>
                    <button type="submit" class="btn btn-primary btn-block">Đăng nhập</button>
                    <p class="auth-link">Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Đăng ký ngay</a></p>
                </form>
            </div>
        </div>
    </main>

    <!-- Footer Placeholder -->
    <div id="footer-placeholder"></div>

    <script src="${pageContext.request.contextPath}/js/layout.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script>
        const toggleLoginPassword = document.getElementById('toggleLoginPassword');
        const loginPassword = document.getElementById('loginPassword');
        
        toggleLoginPassword.addEventListener('click', function() {
            const type = loginPassword.getAttribute('type') === 'password' ? 'text' : 'password';
            loginPassword.setAttribute('type', type);
            toggleLoginPassword.classList.toggle('fa-eye');
            toggleLoginPassword.classList.toggle('fa-eye-slash');
        });
    </script>
</body>
</html>

