<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập - Hệ Thống Bán Thẻ Điện Thoại</title>
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
                <h2>Đăng nhập</h2>
                <form id="loginForm">
                    <div class="form-group">
                        <label>Email hoặc Username</label>
                        <input type="text" id="loginEmail" required>
                    </div>
                    <div class="form-group">
                        <label>Mật khẩu</label>
                        <input type="password" id="loginPassword" required>
                    </div>
                    <button type="submit" class="btn btn-primary btn-block">Đăng nhập</button>
                    <p class="auth-link">Chưa có tài khoản? <a href="register.jsp">Đăng ký ngay</a></p>
                </form>
            </div>
        </div>
    </main>

    <!-- Footer Placeholder -->
    <div id="footer-placeholder"></div>

    <script src="../js/layout.js"></script>
    <script src="../js/app.js"></script>
    <script>
        document.getElementById('loginForm').addEventListener('submit', function(e) {
            e.preventDefault();
            const email = document.getElementById('loginEmail').value;
            const username = email.split('@')[0];
            const password = document.getElementById('loginPassword').value;
            
            // Mock login - replace with actual API call
            let user;
            
            // Check if admin login
            if (email.toLowerCase() === 'admin@admin.com' || username.toLowerCase() === 'admin') {
                user = {
                    id: 999,
                    username: 'admin',
                    email: 'admin@admin.com',
                    role: 'admin',
                    balance: 10000000
                };
                localStorage.setItem('currentUser', JSON.stringify(user));
                window.location.href = '../admin/dashboard.jsp';
            } else {
                // Regular user login
                user = {
                    id: 1,
                    username: username,
                    email: email,
                    role: 'user',
                    balance: 500000
                };
                localStorage.setItem('currentUser', JSON.stringify(user));
                window.location.href = '../user/dashboard.jsp';
            }
        });
    </script>
</body>
</html>

