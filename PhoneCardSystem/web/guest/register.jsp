<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký - Hệ Thống Bán Thẻ Điện Thoại</title>
    <link rel="stylesheet" href="../css/styles.css">
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
                <form id="registerForm">
                    <div class="form-group">
                        <label>Username</label>
                        <input type="text" id="registerUsername" required>
                    </div>
                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" id="registerEmail" required>
                    </div>
                    <div class="form-group">
                        <label>Số điện thoại</label>
                        <input type="tel" id="registerPhone">
                    </div>
                    <div class="form-group">
                        <label>Mật khẩu</label>
                        <input type="password" id="registerPassword" required>
                    </div>
                    <div class="form-group">
                        <label>Xác nhận mật khẩu</label>
                        <input type="password" id="registerConfirmPassword" required>
                    </div>
                    <button type="submit" class="btn btn-primary btn-block">Đăng ký</button>
                    <p class="auth-link">Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập</a></p>
                </form>
            </div>
        </div>
    </main>

    <!-- Footer Placeholder -->
    <div id="footer-placeholder"></div>

    <script src="../js/layout.js"></script>
    <script src="../js/app.js"></script>
    <script>
        document.getElementById('registerForm').addEventListener('submit', function(e) {
            e.preventDefault();
            const password = document.getElementById('registerPassword').value;
            const confirmPassword = document.getElementById('registerConfirmPassword').value;
            
            if (password !== confirmPassword) {
                alert('Mật khẩu xác nhận không khớp!');
                return;
            }
            
            const user = {
                id: Date.now(),
                username: document.getElementById('registerUsername').value,
                email: document.getElementById('registerEmail').value,
                phone: document.getElementById('registerPhone').value,
                role: 'user',
                balance: 0
            };
            
            localStorage.setItem('currentUser', JSON.stringify(user));
            window.location.href = '../user/dashboard.jsp';
        });
    </script>
</body>
</html>

