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
    <title>Kết quả nạp tiền - Hệ Thống Bán Thẻ Điện Thoại</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div id="header-placeholder"></div>

    <main class="main-content">
        <div class="form-container">
            <c:choose>
                <c:when test="${success == true}">
                    <div class="result-success">
                        <i class="fas fa-check-circle"></i>
                        <h2>Nạp tiền thành công!</h2>
                        <p class="result-message">${message}</p>
                        <div class="result-amount">
                            <fmt:formatNumber value="${amount}" type="currency" currencyCode="VND" pattern="#,##0 đ"/>
                        </div>
                        <div class="result-balance">
                            <p>Số dư mới:</p>
                            <h3>
                                <fmt:formatNumber value="${user.balance}" type="currency" currencyCode="VND" pattern="#,##0 đ"/>
                            </h3>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="result-error">
                        <i class="fas fa-times-circle"></i>
                        <h2>Nạp tiền thất bại</h2>
                        <p class="result-message">${error}</p>
                    </div>
                </c:otherwise>
            </c:choose>

            <div class="form-actions" style="justify-content: center; margin-top: 2rem;">
                <c:choose>
                    <c:when test="${success == true && not empty sessionScope.depositReturnUrl}">
                        <a href="${sessionScope.depositReturnUrl}" class="btn btn-primary">
                            <i class="fas fa-shopping-cart"></i> Tiếp tục thanh toán
                        </a>
                        <a href="${pageContext.request.contextPath}/profile" class="btn btn-secondary">
                            <i class="fas fa-user"></i> Về trang cá nhân
                        </a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/deposit" class="btn btn-primary">
                            <i class="fas fa-redo"></i> Nạp tiền lại
                        </a>
                        <a href="${pageContext.request.contextPath}/profile" class="btn btn-secondary">
                            <i class="fas fa-user"></i> Về trang cá nhân
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>
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
    </script>
    <script src="${pageContext.request.contextPath}/js/layout.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <c:if test="${success == true && not empty sessionScope.depositReturnUrl}">
        <script>
            let countdown = 3;
            const resultMessage = document.querySelector('.result-message');
            
            function updateCountdown() {
                resultMessage.innerHTML = 'Đang chuyển hướng đến trang thanh toán trong ' + countdown + ' giây...';
                countdown--;
                
                if (countdown < 0) {
                    window.location.href = '${sessionScope.depositReturnUrl}';
                    <% session.removeAttribute("depositReturnUrl"); %>
                }
            }
            
            updateCountdown();
            setInterval(updateCountdown, 1000);
        </script>
    </c:if>
</body>
</html>

