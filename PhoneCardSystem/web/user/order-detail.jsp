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
    <style>
        .copy-icon {
            margin-left: 8px;
            cursor: pointer;
            color: #666;
            font-size: 14px;
            transition: all 0.3s ease;
            padding: 4px;
        }
        .copy-icon:hover {
            color: #4CAF50;
            transform: scale(1.1);
        }
        .card-value {
            display: inline-flex;
            align-items: center;
        }
    </style>
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
                    <div class="order-items-container">
                        <%@ page import="java.util.*" %>
                        <%
                            // Group items by product
                            Map<String, List<model.OrderItem>> groupedItems = new LinkedHashMap<>();
                            List<model.OrderItem> orderItems = (List<model.OrderItem>) request.getAttribute("orderItems");
                            if (orderItems != null) {
                                for (model.OrderItem item : orderItems) {
                                    String key = item.getProduct_name_snapshot();
                                    if (!groupedItems.containsKey(key)) {
                                        groupedItems.put(key, new ArrayList<>());
                                    }
                                    groupedItems.get(key).add(item);
                                }
                            }
                            request.setAttribute("groupedItems", groupedItems);
                        %>
                        
                        <c:forEach var="entry" items="${groupedItems}" varStatus="status">
                            <c:set var="productName" value="${entry.key}" />
                            <c:set var="items" value="${entry.value}" />
                            <c:set var="itemCount" value="${items.size()}" />
                            <c:set var="firstItem" value="${items[0]}" />
                            <c:set var="totalPrice" value="0" />
                            <c:forEach var="item" items="${items}">
                                <c:set var="totalPrice" value="${totalPrice + item.total_price}" />
                            </c:forEach>
                            
                            <div class="product-group">
                                <div class="product-header" onclick="toggleCardList(${status.index})">
                                    <div class="product-info">
                                        <div class="product-name">
                                            <span class="product-number">${status.index + 1}.</span>
                                            <strong>${productName}</strong>
                                        </div>
                                        <div class="product-details">
                                            <span class="product-quantity">Số lượng: <strong>${itemCount}</strong></span>
                                            <span class="product-unit-price">Đơn giá: <strong><fmt:formatNumber value="${firstItem.unit_price}" type="currency" currencyCode="VND" pattern="#,##0 đ"/></strong></span>
                                            <span class="product-total">Thành tiền: <strong style="color: #ff6b6b;"><fmt:formatNumber value="${totalPrice}" type="currency" currencyCode="VND" pattern="#,##0 đ"/></strong></span>
                                        </div>
                                    </div>
                                    <div class="toggle-icon">
                                        <i class="fas fa-chevron-down" id="icon-${status.index}"></i>
                                    </div>
                                </div>
                                
                                <div class="card-list" id="cardList-${status.index}" style="display: none;">
                                    <div class="card-list-header">
                                        <i class="fas fa-ticket-alt"></i> Thông tin thẻ đã mua
                                    </div>
                                    <c:forEach var="item" items="${items}" varStatus="cardStatus">
                                        <div class="card-item">
                                            <div class="card-number">#${cardStatus.index + 1}</div>
                                            <div class="card-info-grid">
                                                <div class="card-info-item">
                                                    <span class="card-label"><i class="fas fa-barcode"></i> Mã thẻ:</span>
                                                    <span class="card-value">${item.card_code}</span>
                                                </div>
                                                <div class="card-info-item">
                                                    <span class="card-label"><i class="fas fa-hashtag"></i> Serial:</span>
                                                    <span class="card-value">
                                                        ${item.card_serial}
                                                        <i class="fas fa-copy copy-icon" onclick="copySerial('${item.card_serial}', this)" title="Sao chép serial"></i>
                                                    </span>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </c:forEach>
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
        
        function toggleCardList(index) {
            const cardList = document.getElementById('cardList-' + index);
            const icon = document.getElementById('icon-' + index);
            
            if (cardList.style.display === 'none') {
                cardList.style.display = 'block';
                icon.classList.remove('fa-chevron-down');
                icon.classList.add('fa-chevron-up');
            } else {
                cardList.style.display = 'none';
                icon.classList.remove('fa-chevron-up');
                icon.classList.add('fa-chevron-down');
            }
        }
        
        function copySerial(serial, iconElement) {
            navigator.clipboard.writeText(serial).then(function() {
                // Visual feedback
                const originalClass = iconElement.className;
                iconElement.classList.remove('fa-copy');
                iconElement.classList.add('fa-check');
                iconElement.style.color = '#4CAF50';
                
                // Show tooltip
                const originalTitle = iconElement.title;
                iconElement.title = 'Đã sao chép!';
                
                setTimeout(function() {
                    iconElement.className = originalClass;
                    iconElement.style.color = '';
                    iconElement.title = originalTitle;
                }, 2000);
            }).catch(function(err) {
                console.error('Lỗi khi sao chép:', err);
                alert('Không thể sao chép serial. Vui lòng thử lại.');
            });
        }
    </script>
    <script src="${pageContext.request.contextPath}/js/layout.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
