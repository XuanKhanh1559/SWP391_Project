<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đơn hàng - Hệ Thống Bán Thẻ Điện Thoại</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div id="header-placeholder"></div>

    <main class="main-content">
        <div class="container">
            <div class="page-header">
                <h2><i class="fas fa-shopping-bag"></i> Đơn hàng của tôi</h2>
            </div>

            <c:if test="${empty orders}">
                <div class="empty-state">
                    <i class="fas fa-shopping-bag" style="font-size: 64px; color: #ddd;"></i>
                    <h3>Chưa có đơn hàng nào</h3>
                    <p>Bạn chưa có đơn hàng nào. Hãy mua sắm ngay!</p>
                    <a href="${pageContext.request.contextPath}/guest/home" class="btn btn-primary">
                        <i class="fas fa-shopping-cart"></i> Mua sắm ngay
                    </a>
                </div>
            </c:if>

            <c:if test="${not empty orders}">
                <div class="orders-list">
                    <c:forEach var="order" items="${orders}">
                        <div class="order-card">
                            <div class="order-header">
                                <div class="order-info">
                                    <h3><i class="fas fa-receipt"></i> ${order.order_code}</h3>
                                    <span class="order-date">
                                        <i class="fas fa-calendar"></i>
                                        <fmt:formatDate value="${order.created_at}" pattern="dd/MM/yyyy HH:mm"/>
                                    </span>
                                </div>
                                <div class="order-status">
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
                                </div>
                            </div>

                            <div class="order-body">
                                <div class="order-summary">
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
                                        <span style="color: #ff6b6b; font-size: 18px; font-weight: bold;">
                                            <fmt:formatNumber value="${order.total_amount}" type="currency" currencyCode="VND" pattern="#,##0 đ"/>
                                        </span>
                                    </div>
                                </div>
                            </div>

                            <div class="order-footer">
                                <a href="${pageContext.request.contextPath}/user/order-detail?id=${order.id}" class="btn btn-outline">
                                    <i class="fas fa-eye"></i> Xem chi tiết
                                </a>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <c:if test="${totalPages > 1}">
                    <div class="pagination">
                        <c:if test="${currentPage > 1}">
                            <a href="?page=${currentPage - 1}" class="pagination-btn">
                                <i class="fas fa-chevron-left"></i> Trước
                            </a>
                        </c:if>

                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <c:choose>
                                <c:when test="${i == currentPage}">
                                    <span class="pagination-btn active">${i}</span>
                                </c:when>
                                <c:otherwise>
                                    <a href="?page=${i}" class="pagination-btn">${i}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>

                        <c:if test="${currentPage < totalPages}">
                            <a href="?page=${currentPage + 1}" class="pagination-btn">
                                Sau <i class="fas fa-chevron-right"></i>
                            </a>
                        </c:if>
                    </div>
                </c:if>
            </c:if>
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
