<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Giỏ hàng - Hệ Thống Bán Thẻ Điện Thoại</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div id="header-placeholder"></div>

    <main class="main-content">
        <div class="page-header">
            <h2>Giỏ hàng</h2>
        </div>
        <div class="cart-container">
            <div class="cart-items" id="cartItems">
                <c:choose>
                    <c:when test="${cartItems != null && !cartItems.isEmpty()}">
                        <c:forEach var="item" items="${cartItems}">
                            <div class="cart-item" data-cart-item-id="${item.id}">
                                <div class="cart-item-info">
                                    <h4>${item.product.name}</h4>
                                    <p><fmt:formatNumber value="${item.unit_price}" type="currency" currencyCode="VND" pattern="#,##0 đ"/> x ${item.quantity}</p>
                                </div>
                                <div class="cart-item-actions">
                                    <button class="btn btn-outline" onclick="updateCartQuantity(${item.id}, -1)">-</button>
                                    <span>${item.quantity}</span>
                                    <button class="btn btn-outline" onclick="updateCartQuantity(${item.id}, 1)">+</button>
                                    <button class="btn btn-outline" onclick="removeFromCart(${item.id})">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p>Giỏ hàng trống</p>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="cart-summary">
                <h3>Tổng kết</h3>
                <div class="summary-row total">
                    <span>Tổng cộng:</span>
                    <span id="cartTotal">
                        <c:set var="subtotal" value="0"/>
                        <c:forEach var="item" items="${cartItems}">
                            <c:set var="subtotal" value="${subtotal + (item.unit_price * item.quantity)}"/>
                        </c:forEach>
                        <fmt:formatNumber value="${subtotal}" type="currency" currencyCode="VND" pattern="#,##0 đ"/>
                    </span>
                </div>
                <a href="${pageContext.request.contextPath}/user/checkout" class="btn btn-primary btn-block" style="padding: 0.5rem 0.5rem;">Thanh toán</a>
            </div>
        </div>
    </main>

    <div id="footer-placeholder"></div>

    <script>
        const contextPath = '${pageContext.request.contextPath}';
        
        function updateCartQuantity(cartItemId, delta) {
            fetch(contextPath + '/update-cart', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'cartItemId=' + cartItemId + '&quantity=' + delta
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    location.reload();
                } else {
                    if (window.showToast) {
                        showToast(data.message, 'error');
                    } else {
                        alert(data.message);
                    }
                }
            })
            .catch(error => {
                console.error('Error:', error);
                if (window.showToast) {
                    showToast('Có lỗi xảy ra. Vui lòng thử lại.', 'error');
                } else {
                    alert('Có lỗi xảy ra. Vui lòng thử lại.');
                }
            });
        }
        
        function removeFromCart(cartItemId) {
            if (!confirm('Bạn có chắc muốn xóa sản phẩm này khỏi giỏ hàng?')) {
                return;
            }
            
            fetch(contextPath + '/remove-cart-item', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'cartItemId=' + cartItemId
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    location.reload();
                } else {
                    if (window.showToast) {
                        showToast(data.message, 'error');
                    } else {
                        alert(data.message);
                    }
                }
            })
            .catch(error => {
                console.error('Error:', error);
                if (window.showToast) {
                    showToast('Có lỗi xảy ra. Vui lòng thử lại.', 'error');
                } else {
                    alert('Có lỗi xảy ra. Vui lòng thử lại.');
                }
            });
        }
    </script>
    <script>
        window.userData = {
            id: ${sessionScope.user.id},
            username: '<c:out value="${sessionScope.user.username}" escapeXml="true"/>',
            email: '<c:out value="${sessionScope.user.email}" escapeXml="true"/>',
            role: '<c:out value="${sessionScope.user.role}" escapeXml="true"/>',
            balance: ${sessionScope.user.balance}
        };
    </script>
    <script src="${pageContext.request.contextPath}/js/toast.js"></script>
    <script src="${pageContext.request.contextPath}/js/layout.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>

