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
                            <div class="cart-item" data-cart-item-id="${item.id}" data-product-id="${item.product_id}" data-stock="${item.product.stock}">
                                <div class="cart-item-info">
                                    <h4>${item.product.name}</h4>
                                    <p><fmt:formatNumber value="${item.unit_price}" type="currency" currencyCode="VND" pattern="#,##0 đ"/> x ${item.quantity}</p>
                                </div>
                                <div class="cart-item-actions">
                                    <button class="btn btn-outline decrease-btn" 
                                            onclick="updateCartQuantity(${item.id}, -1)" 
                                            data-cart-item-id="${item.id}"
                                            ${item.quantity <= 1 ? 'disabled style="opacity: 0.5; cursor: not-allowed;"' : ''}>-</button>
                                    <span>${item.quantity}</span>
                                    <button class="btn btn-outline increase-btn" 
                                            onclick="updateCartQuantity(${item.id}, 1)" 
                                            data-cart-item-id="${item.id}"
                                            ${item.quantity >= item.product.stock ? 'disabled style="opacity: 0.5; cursor: not-allowed;"' : ''}>+</button>
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
                    'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                    'Accept': 'application/json; charset=UTF-8'
                },
                body: 'cartItemId=' + cartItemId + '&quantity=' + delta
            })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        try {
                            return JSON.parse(text);
                        } catch (e) {
                            throw new Error('Invalid JSON response');
                        }
                    });
                }
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    location.reload();
                } else {
                    if (window.showToast) {
                        showToast(data.message, 'error');
                    } else {
                        alert(data.message);
                    }
                    // Update button states after error
                    updateCartButtonStates();
                }
            })
            .catch(error => {
                console.error('Error:', error);
                if (window.showToast) {
                    showToast('Có lỗi xảy ra. Vui lòng thử lại.', 'error');
                } else {
                    alert('Có lỗi xảy ra. Vui lòng thử lại.');
                }
                // Update button states after error
                updateCartButtonStates();
            });
        }
        
        function updateCartButtonStates() {
            document.querySelectorAll('.cart-item').forEach(item => {
                const cartItemId = parseInt(item.dataset.cartItemId);
                const stock = parseInt(item.dataset.stock) || 0;
                const quantitySpan = item.querySelector('span');
                const decreaseBtn = item.querySelector('.decrease-btn');
                const increaseBtn = item.querySelector('.increase-btn');
                
                if (!quantitySpan) return;
                
                const currentQuantity = parseInt(quantitySpan.textContent) || 1;
                
                // Update decrease button
                if (decreaseBtn) {
                    if (currentQuantity <= 1) {
                        decreaseBtn.disabled = true;
                        decreaseBtn.style.opacity = '0.5';
                        decreaseBtn.style.cursor = 'not-allowed';
                    } else {
                        decreaseBtn.disabled = false;
                        decreaseBtn.style.opacity = '1';
                        decreaseBtn.style.cursor = 'pointer';
                    }
                }
                
                // Update increase button
                if (increaseBtn) {
                    if (currentQuantity >= stock) {
                        increaseBtn.disabled = true;
                        increaseBtn.style.opacity = '0.5';
                        increaseBtn.style.cursor = 'not-allowed';
                    } else {
                        increaseBtn.disabled = false;
                        increaseBtn.style.opacity = '1';
                        increaseBtn.style.cursor = 'pointer';
                    }
                }
            });
        }
        
        // Initialize button states on page load
        document.addEventListener('DOMContentLoaded', function() {
            updateCartButtonStates();
        });
        
        function removeFromCart(cartItemId) {
            if (window.showConfirm) {
                showConfirm(
                    'Bạn có chắc muốn xóa sản phẩm này khỏi giỏ hàng?',
                    function() {
                        // User confirmed, proceed with deletion
                        fetch(contextPath + '/remove-cart-item', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                                'Accept': 'application/json; charset=UTF-8'
                            },
                            body: 'cartItemId=' + cartItemId
                        })
                        .then(response => {
                            if (!response.ok) {
                                return response.text().then(text => {
                                    try {
                                        return JSON.parse(text);
                                    } catch (e) {
                                        throw new Error('Invalid JSON response');
                                    }
                                });
                            }
                            return response.json();
                        })
                        .then(data => {
                            if (data.success) {
                                if (window.showToast) {
                                    showToast('Đã xóa sản phẩm khỏi giỏ hàng', 'success');
                                }
                                setTimeout(() => {
                                    location.reload();
                                }, 500);
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
                    },
                    null, // onCancel - do nothing
                    'Xác nhận xóa'
                );
            } else {
                // Fallback to native confirm if showConfirm is not available
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

