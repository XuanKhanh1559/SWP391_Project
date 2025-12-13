<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nạp tiền - Hệ Thống Bán Thẻ Điện Thoại</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div id="header-placeholder"></div>

    <main class="main-content">
        <div class="page-header">
            <h2>Nạp tiền vào ví</h2>
        </div>

        <div class="form-container">
            <c:if test="${param.error == 'invalid_amount'}">
                <div class="alert alert-error">
                    <i class="fas fa-exclamation-circle"></i>
                    Số tiền không hợp lệ
                </div>
            </c:if>
            
            <c:if test="${param.error == 'min_amount'}">
                <div class="alert alert-error">
                    <i class="fas fa-exclamation-circle"></i>
                    Số tiền nạp tối thiểu là 10.000đ
                </div>
            </c:if>

            <div class="balance-display">
                <i class="fas fa-wallet"></i>
                <div>
                    <p>Số dư hiện tại</p>
                    <h3 class="balance-amount">
                        <fmt:formatNumber value="${user.balance}" type="currency" currencyCode="VND" pattern="#,##0 đ"/>
                    </h3>
                </div>
            </div>

            <form method="POST" action="${pageContext.request.contextPath}/vnpay" class="admin-form">
                <div class="form-group">
                    <label for="amount">Số tiền nạp <span class="required">*</span></label>
                    <input type="number" id="amount" name="amount" step="1000" min="10000" required placeholder="Nhập số tiền (tối thiểu 10.000đ)">
                    <small style="color: #666; margin-top: 0.5rem; display: block;">
                        Số tiền tối thiểu: 10.000đ
                    </small>
                </div>

                <div class="quick-amount-buttons">
                    <button type="button" class="quick-amount-btn" onclick="setAmount(50000)">50.000đ</button>
                    <button type="button" class="quick-amount-btn" onclick="setAmount(100000)">100.000đ</button>
                    <button type="button" class="quick-amount-btn" onclick="setAmount(200000)">200.000đ</button>
                    <button type="button" class="quick-amount-btn" onclick="setAmount(500000)">500.000đ</button>
                    <button type="button" class="quick-amount-btn" onclick="setAmount(1000000)">1.000.000đ</button>
                </div>

                <div class="payment-method-info">
                    <i class="fas fa-credit-card"></i>
                    <span>Thanh toán qua VNPay</span>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-money-bill-wave"></i> Nạp tiền ngay
                    </button>
                    <a href="${pageContext.request.contextPath}/profile" class="btn btn-secondary">
                        <i class="fas fa-times"></i> Hủy
                    </a>
                </div>
            </form>
        </div>
    </main>

    <div id="footer-placeholder"></div>

    <script>
        window.userData = {
            id: ${user.id},
            username: '${user.username}',
            email: '${user.email}',
            role: '${user.role}'
        };

        function setAmount(value) {
            document.getElementById('amount').value = value;
        }
    </script>
    <script src="${pageContext.request.contextPath}/js/layout.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script src="${pageContext.request.contextPath}/js/toast.js"></script>
</body>
</html>
