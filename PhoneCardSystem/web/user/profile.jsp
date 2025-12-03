<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hồ sơ - Hệ Thống Bán Thẻ Điện Thoại</title>
    <link rel="stylesheet" href="../css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <!-- Header Placeholder -->
    <div id="header-placeholder"></div>

    <!-- Main Content -->
    <main class="main-content">
        <div class="profile-container">
            <div class="profile-sidebar">
                <div class="profile-avatar">
                    <i class="fas fa-user-circle"></i>
                </div>
                <h3 id="profileUserName">Username</h3>
                <div class="profile-balance">
                    <span>Số dư:</span>
                    <span class="balance-amount" id="profileBalance">0 đ</span>
                </div>
                <nav class="profile-nav">
                    <a href="#profileInfo" class="active">Thông tin cá nhân</a>
                    <a href="#transactions">Giao dịch</a>
                </nav>
            </div>
            <div class="profile-content">
                <div id="profileInfo" class="profile-section active">
                    <h2>Thông tin cá nhân</h2>
                    <form id="profileForm">
                        <div class="form-group">
                            <label>Username</label>
                            <input type="text" id="profileUsername" readonly>
                        </div>
                        <div class="form-group">
                            <label>Email</label>
                            <input type="email" id="profileEmail">
                        </div>
                        <div class="form-group">
                            <label>Số điện thoại</label>
                            <input type="tel" id="profilePhone">
                        </div>
                        <button type="submit" class="btn btn-primary">Cập nhật</button>
                    </form>
                </div>
                <div id="transactions" class="profile-section">
                    <h2>Lịch sử giao dịch</h2>
                    <div class="transactions-list" id="transactionsList">
                        <!-- Transactions will be loaded here -->
                    </div>
                </div>
            </div>
        </div>
    </main>

    <!-- Footer Placeholder -->
    <div id="footer-placeholder"></div>

    <script src="../js/layout.js"></script>
    <script src="../js/app.js"></script>
    <script src="../js/profile.js"></script>
</body>
</html>

