<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết đơn hàng - Hệ Thống Bán Thẻ Điện Thoại</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div id="header-placeholder"></div>

    <main class="main-content">
        <div class="container">
            <div class="page-header">
                <a href="${pageContext.request.contextPath}/user/orders" class="btn btn-outline">
                    <i class="fas fa-arrow-left"></i> Quay lại
                </a>
                <h2><i class="fas fa-receipt"></i> Chi tiết đơn hàng ${order.order_code}</h2>
            </div>

            <div class="order-detail-container">
                <div class="detail-section">
                    <h3><i class="fas fa-info-circle"></i> Thông tin đơn hàng</h3>
                    <div class="detail-grid">
                        <div class="detail-item">
                            <span class="detail-label">Mã đơn hàng:</span>
                            <span class="detail-value">${order.order_code}</span>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">Ngày đặt:</span>
                            <span class="detail-value">
                                <fmt:formatDate value="${order.created_at}" pattern="dd/MM/yyyy HH:mm:ss"/>
                            </span>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">Trạng thái:</span>
                            <span class="detail-value">
                                <c:choose>
                                    <c:when test="${order.status == 0}">
                                        <span class="badge badge-warning"><i class="fas fa-clock"></i> Đang xử lý</span>
                                    </c:when>
                                    <c:when test="${order.status == 1}">
                                        <span class="badge badge-success"><i class="fas fa-check-circle"></i> Hoàn thành</span>
                                    </c:when>
                                    <c:when test="${order.status == 2}">
                                        <span class="badge badge-danger"><i class="fas fa-times-circle"></i> Đã hủy</span>
                                    </c:when>
                                </c:choose>
                            </span>
                        </div>
                    </div>
                </div>

                <div class="detail-section">
                    <h3><i class="fas fa-box"></i> Sản phẩm</h3>
                    <div class="order-items-table">
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
                                        <td data-label="STT">${status.index + 1}</td>
                                        <td data-label="Sản phẩm">${item.product_name_snapshot}</td>
                                        <td data-label="Mã thẻ">
                                            <c:choose>
                                                <c:when test="${not empty item.card_code}">
                                                    <span style="font-weight: bold; color: #2196F3;">${item.card_code}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span style="color: #999;">-</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td data-label="Serial">
                                            <c:choose>
                                                <c:when test="${not empty item.card_serial}">
                                                    <span style="font-weight: bold; color: #2196F3;">${item.card_serial}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span style="color: #999;">-</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td data-label="Số lượng">${item.quantity}</td>
                                        <td data-label="Đơn giá">
                                            <fmt:formatNumber value="${item.unit_price}" type="currency" currencyCode="VND" pattern="#,##0 đ"/>
                                        </td>
                                        <td data-label="Thành tiền">
                                            <fmt:formatNumber value="${item.total_price}" type="currency" currencyCode="VND" pattern="#,##0 đ"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div class="detail-section">
                    <h3><i class="fas fa-receipt"></i> Tổng kết đơn hàng</h3>
                    <div class="order-summary-box">
                        <div class="order-summary-item">
                            <span>Tạm tính:</span>
                            <span><fmt:formatNumber value="${order.subtotal_amount}" type="currency" currencyCode="VND" pattern="#,##0 đ"/></span>
                        </div>
                        <c:if test="${order.discount_amount > 0}">
                            <div class="order-summary-item" style="color: #4CAF50;">
                                <span>Giảm giá:</span>
                                <span>- <fmt:formatNumber value="${order.discount_amount}" type="currency" currencyCode="VND" pattern="#,##0 đ"/></span>
                            </div>
                        </c:if>
                        <div class="order-summary-item order-total">
                            <span>Tổng cộng:</span>
                            <span style="color: #ff6b6b; font-size: 20px; font-weight: bold;">
                                <fmt:formatNumber value="${order.total_amount}" type="currency" currencyCode="VND" pattern="#,##0 đ"/>
                            </span>
                        </div>
                    </div>
                </div>

                <c:if test="${not empty order.notes}">
                    <div class="detail-section">
                        <h3><i class="fas fa-sticky-note"></i> Ghi chú</h3>
                        <p>${order.notes}</p>
                    </div>
                </c:if>
            </div>
        </div>
    </main>

    <div id="footer-placeholder"></div>

    <script>
        const contextPath = '${pageContext.request.contextPath}';
        
        window.userData = {
            id: ${sessionScope.user.id},
            username: '<c:out value="${sessionScope.user.username}" escapeXml="true"/>',
            email: '<c:out value="${sessionScope.user.email}" escapeXml="true"/>',
            role: '<c:out value="${sessionScope.user.role}" escapeXml="true"/>',
            balance: ${sessionScope.user.balance}
        };
    </script>
    <script src="${pageContext.request.contextPath}/js/layout.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
