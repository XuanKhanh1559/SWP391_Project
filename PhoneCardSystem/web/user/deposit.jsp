<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nạp tiền - Hệ Thống Bán Thẻ Điện Thoại</title>
    <link rel="stylesheet" href="../css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <!-- Header Placeholder -->
    <div id="header-placeholder"></div>

    <!-- Main Content -->
    <main class="main-content">
        <div class="page-header">
            <h2>Nạp tiền</h2>
        </div>
        <div class="deposit-container">
            <div class="deposit-card">
                <h3>Nạp tiền vào tài khoản</h3>
                <form id="depositForm">
                    <div class="form-group">
                        <label>Số tiền nạp</label>
                        <input type="number" id="depositAmount" min="10000" step="10000" required>
                    </div>
                    <div class="form-group">
                        <label>Phương thức thanh toán</label>
                        <select id="depositMethod" class="form-control">
                            <option value="vnpay">VNPay</option>
                            <option value="momo">MoMo</option>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-primary btn-block">Nạp tiền</button>
                </form>
                <div class="deposit-info">
                    <p><strong>Lưu ý:</strong></p>
                    <ul>
                        <li>Số tiền nạp tối thiểu: 10.000đ</li>
                        <li>Tiền sẽ được cộng vào tài khoản ngay sau khi thanh toán thành công</li>
                    </ul>
                </div>
            </div>
        </div>
    </main>

    <!-- Footer Placeholder -->
    <div id="footer-placeholder"></div>

    <%@ page import="model.User" %>
    <%
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
    %>
    <script>
        window.userData = {
            id: <%= user.getId() %>,
            username: '<%= user.getUsername().replace("'", "\\'") %>',
            email: '<%= user.getEmail().replace("'", "\\'") %>',
            role: '<%= user.getRole() != null ? user.getRole().replace("'", "\\'") : "user" %>',
            balance: <%= user.getBalance() %>
        };
    </script>
    <script src="../js/layout.js"></script>
    <script src="../js/app.js"></script>
    <script>
        document.getElementById('depositForm').addEventListener('submit', function(e) {
            e.preventDefault();
            const amount = parseFloat(document.getElementById('depositAmount').value);
            const method = document.getElementById('depositMethod').value;
            
            if (amount < 10000) {
                alert('Số tiền nạp tối thiểu 10.000đ!');
                return;
            }
            
            alert(`Đang chuyển hướng đến ${method} để thanh toán ${new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount)}`);
        });
    </script>
</body>
</html>

