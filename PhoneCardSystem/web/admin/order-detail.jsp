<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết đơn hàng - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div id="header-placeholder"></div>

    <main class="main-content">
        <div class="page-header">
            <h2><i class="fas fa-file-invoice"></i> Chi tiết đơn hàng</h2>
            <a href="${pageContext.request.contextPath}/admin/orders" class="btn btn-secondary">
                <i class="fas fa-arrow-left"></i> Quay lại
            </a>
        </div>

        <div class="order-detail-container">
            <div class="detail-section">
                <h3><i class="fas fa-info-circle"></i> Thông tin đơn hàng</h3>
                <div class="info-grid">
                    <div class="info-item">
                        <label>Mã đơn hàng:</label>
                        <span><strong>${order.order_code}</strong></span>
                    </div>
                    <div class="info-item">
                        <label>Người dùng:</label>
                        <span><i class="fas fa-user"></i> ${username}</span>
                    </div>
                    <div class="info-item">
                        <label>Trạng thái:</label>
                        <span>
                            <c:choose>
                                <c:when test="${order.status == 0}">
                                    <span class="badge badge-warning">
                                        <i class="fas fa-clock"></i> Đang xử lý
                                    </span>
                                </c:when>
                                <c:when test="${order.status == 1}">
                                    <span class="badge badge-success">
                                        <i class="fas fa-check-circle"></i> Hoàn thành
                                    </span>
                                </c:when>
                                <c:when test="${order.status == 2}">
                                    <span class="badge badge-danger">
                                        <i class="fas fa-times-circle"></i> Đã hủy
                                    </span>
                                </c:when>
                            </c:choose>
                        </span>
                    </div>
                    <div class="info-item">
                        <label>Ngày tạo:</label>
                        <span><fmt:formatDate value="${order.created_at}" pattern="dd/MM/yyyy HH:mm:ss"/></span>
                    </div>
                    <div class="info-item">
                        <label>Cập nhật lần cuối:</label>
                        <span><fmt:formatDate value="${order.updated_at}" pattern="dd/MM/yyyy HH:mm:ss"/></span>
                    </div>
                </div>
            </div>

            <div class="detail-section">
                <h3><i class="fas fa-box"></i> Danh sách sản phẩm</h3>
                <div class="table-responsive order-items-table">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>STT</th>
                                <th>Sản phẩm</th>
                                <th>Mã thẻ</th>
                                <th>Serial</th>
                                <th>Số lượng</th>
                                <th>Đơn giá</th>
                                <th>Thành tiền</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${orderItems}" varStatus="status">
                                <tr>
                                    <td>${status.index + 1}</td>
                                    <td>
                                        <strong>${item.product_name_snapshot}</strong>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty item.card_code}">
                                                <span style="font-weight: bold; color: #2196F3;">${item.card_code}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span style="color: #999;">-</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty item.card_serial}">
                                                <span style="font-weight: bold; color: #2196F3;">${item.card_serial}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span style="color: #999;">-</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${item.quantity}</td>
                                    <td>
                                        <fmt:formatNumber value="${item.unit_price}" type="currency" currencyCode="VND" pattern="#,##0 đ"/>
                                    </td>
                                    <td>
                                        <strong>
                                            <fmt:formatNumber value="${item.total_price}" type="currency" currencyCode="VND" pattern="#,##0 đ"/>
                                        </strong>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="detail-section">
                <h3><i class="fas fa-calculator"></i> Tổng kết thanh toán</h3>
                <div class="order-summary-box">
                    <div class="summary-row">
                        <span>Tạm tính:</span>
                        <span><fmt:formatNumber value="${order.subtotal_amount}" type="currency" currencyCode="VND" pattern="#,##0 đ"/></span>
                    </div>
                    <c:if test="${order.discount_amount > 0}">
                        <div class="summary-row" style="color: #4CAF50;">
                            <span>Giảm giá:</span>
                            <span>-<fmt:formatNumber value="${order.discount_amount}" type="currency" currencyCode="VND" pattern="#,##0 đ"/></span>
                        </div>
                    </c:if>
                    <div class="summary-row total">
                        <span>Tổng cộng:</span>
                        <span style="color: #ff6b6b; font-weight: bold;">
                            <fmt:formatNumber value="${order.total_amount}" type="currency" currencyCode="VND" pattern="#,##0 đ"/>
                        </span>
                    </div>
                </div>
                
                <c:if test="${order.notes != null && !order.notes.trim().isEmpty()}">
                    <div class="order-notes">
                        <h4><i class="fas fa-sticky-note"></i> Ghi chú:</h4>
                        <p>${order.notes}</p>
                    </div>
                </c:if>
            </div>
        </div>
    </main>

    <div id="footer-placeholder"></div>

    <script>
        window.userData = {
            id: <%= user.getId() %>,
            username: '<%= user.getUsername().replace("'", "\\'") %>',
            email: '<%= user.getEmail().replace("'", "\\'") %>',
            role: '<%= user.getRole() %>',
            balance: <%= user.getBalance() %>
        };
    </script>
    <script src="${pageContext.request.contextPath}/js/layout.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>

