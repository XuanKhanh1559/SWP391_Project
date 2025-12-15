<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết sản phẩm - Hệ Thống Bán Thẻ Điện Thoại</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div id="header-placeholder"></div>

    <main class="main-content">
        <c:choose>
            <c:when test="${product != null}">
                <div class="product-detail-container">
                    <div class="product-detail-header">
                        <div class="product-detail-image">
                            <i class="fas fa-mobile-alt"></i>
                        </div>
                        <div class="product-detail-info">
                            <h1>${product.name}</h1>
                            <c:if test="${product.description != null && !product.description.isEmpty()}">
                                <p class="product-description">${product.description}</p>
                            </c:if>
                            <div class="product-detail-price">
                                <fmt:formatNumber value="${product.price}" type="currency" currencyCode="VND" pattern="#,##0 đ"/>
                            </div>
                            <div class="product-detail-meta">
                                <div class="meta-item">
                                    <strong>Nhà mạng:</strong>
                                    <span>
                                        <c:choose>
                                            <c:when test="${product.provider_id == 1}">Viettel</c:when>
                                            <c:when test="${product.provider_id == 2}">Vinaphone</c:when>
                                            <c:when test="${product.provider_id == 3}">Mobifone</c:when>
                                            <c:otherwise>Không xác định</c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>
                                <div class="meta-item">
                                    <strong>Loại:</strong>
                                    <span>
                                        <c:choose>
                                            <c:when test="${product.type == 1}">Nạp tiền</c:when>
                                            <c:when test="${product.type == 2}">Gói data</c:when>
                                            <c:otherwise>Không xác định</c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>
                                <c:if test="${product.denomination > 0}">
                                    <div class="meta-item">
                                        <strong>Mệnh giá:</strong>
                                        <span><fmt:formatNumber value="${product.denomination}" type="currency" currencyCode="VND" pattern="#,##0 đ"/></span>
                                    </div>
                                </c:if>
                                <div class="meta-item">
                                    <strong>Trạng thái:</strong>
                                    <span>
                                        <c:choose>
                                            <c:when test="${product.status == 1}">Đang bán</c:when>
                                            <c:when test="${product.status == 2}">Ngừng bán</c:when>
                                            <c:when test="${product.status == 3}">Hết hàng</c:when>
                                            <c:otherwise>Không xác định</c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>
                            </div>
                            <c:if test="${sessionScope.user != null && product.status == 1}">
                                <div class="quantity-selector">
                                    <label for="quantity">Số lượng:</label>
                                    <input type="number" id="quantity" name="quantity" min="1" value="1" class="quantity-input">
                                </div>
                                <div class="product-detail-actions" style="display: flex; gap: 10px;">
                                    <button class="btn btn-primary btn-large" onclick="addToCartFromDetail(${product.id})" style="flex: 1;">
                                        <i class="fas fa-cart-plus"></i> Thêm vào giỏ hàng
                                    </button>
                                    <button class="btn btn-success btn-large" onclick="buyNow(${product.id})" style="flex: 1; background-color: #ff6b6b;">
                                        <i class="fas fa-shopping-bag"></i> Mua ngay
                                    </button>
                                </div>
                            </c:if>
                            <c:if test="${sessionScope.user == null}">
                                <div class="product-detail-actions">
                                    <a href="${pageContext.request.contextPath}/login" class="btn btn-primary btn-large">
                                        <i class="fas fa-sign-in-alt"></i> Đăng nhập để mua hàng
                                    </a>
                                </div>
                            </c:if>
                        </div>
                    </div>
                    <div class="product-detail-footer">
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-outline">
                            <i class="fas fa-arrow-left"></i> Quay lại danh sách sản phẩm
                        </a>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <p>Sản phẩm không tồn tại</p>
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">Quay lại danh sách sản phẩm</a>
                </div>
            </c:otherwise>
        </c:choose>
    </main>

    <div id="footer-placeholder"></div>

    <script>
        window.userData = <c:choose>
            <c:when test="${sessionScope.user != null}">
            {
                id: ${sessionScope.user.id},
                username: "<c:out value="${sessionScope.user.username}" escapeXml="true"/>",
                email: "<c:out value="${sessionScope.user.email}" escapeXml="true"/>",
                role: "<c:out value="${sessionScope.user.role != null ? sessionScope.user.role : 'user'}" escapeXml="true"/>",
                balance: ${sessionScope.user.balance}
            }
            </c:when>
            <c:otherwise>null</c:otherwise>
        </c:choose>;
        
        window.productData = {
            id: ${product != null ? product.id : 0},
            name: "<c:out value="${product != null ? product.name : ''}" escapeXml="true"/>",
            price: ${product != null ? product.price : 0}
        };
    </script>
    <script src="${pageContext.request.contextPath}/js/layout.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script src="${pageContext.request.contextPath}/js/toast.js"></script>
    <script>
        function addToCartFromDetail(productId) {
            const quantity = parseInt(document.getElementById('quantity').value) || 1;
            if (quantity < 1) {
                showToast('Số lượng phải lớn hơn 0', 'warning');
                return;
            }
            
            const contextPath = '${pageContext.request.contextPath}';
            
            fetch(contextPath + '/add-to-cart', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'productId=' + productId + '&quantity=' + quantity
            })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        try {
                            return JSON.parse(text);
                        } catch (e) {
                            throw new Error('Invalid response');
                        }
                    });
                }
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    showToast(data.message, 'success');
                    if (window.updateCartCount) {
                        window.updateCartCount();
                    }
                } else {
                    showToast(data.message, 'error');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showToast('Có lỗi xảy ra. Vui lòng thử lại.', 'error');
            });
        }
        
        function buyNow(productId) {
            const quantity = parseInt(document.getElementById('quantity').value) || 1;
            if (quantity < 1) {
                showToast('Số lượng phải lớn hơn 0', 'warning');
                return;
            }
            
            const contextPath = '${pageContext.request.contextPath}';
            window.location.href = contextPath + '/user/checkout?direct=true&productId=' + productId + '&quantity=' + quantity;
        }
    </script>
</body>
</html>

