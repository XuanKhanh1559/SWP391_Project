<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thanh toán - Hệ Thống Bán Thẻ Điện Thoại</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .checkout-container {
            max-width: 800px;
            margin: 20px auto;
            padding: 20px;
        }
        .checkout-section {
            background: white;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 20px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        .order-summary-item {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid #eee;
        }
        .order-total {
            font-size: 20px;
            font-weight: bold;
            color: #ff6b6b;
            margin-top: 15px;
        }
        .processing-overlay {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,0.8);
            z-index: 9999;
            justify-content: center;
            align-items: center;
        }
        .processing-content {
            background: white;
            padding: 40px;
            border-radius: 10px;
            text-align: center;
            max-width: 400px;
        }
        .spinner {
            border: 4px solid #f3f3f3;
            border-top: 4px solid #ff6b6b;
            border-radius: 50%;
            width: 50px;
            height: 50px;
            animation: spin 1s linear infinite;
            margin: 20px auto;
        }
        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
        .success-icon {
            font-size: 50px;
            color: #4caf50;
            margin-bottom: 20px;
        }
        .error-icon {
            font-size: 50px;
            color: #f44336;
            margin-bottom: 20px;
        }
        .insufficient-balance-modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,0.8);
            z-index: 10000;
            justify-content: center;
            align-items: center;
        }
        .insufficient-balance-content {
            background: white;
            padding: 40px;
            border-radius: 10px;
            text-align: center;
            max-width: 450px;
        }
        .warning-icon {
            font-size: 60px;
            color: #ff9800;
            margin-bottom: 20px;
        }
        .balance-info {
            background: #fff3cd;
            border: 1px solid #ffc107;
            border-radius: 5px;
            padding: 15px;
            margin: 20px 0;
        }
        .balance-info p {
            margin: 5px 0;
            font-size: 16px;
        }
        .modal-buttons {
            display: flex;
            gap: 10px;
            justify-content: center;
            margin-top: 20px;
        }
        .btn-deposit {
            background: #4caf50;
            color: white;
            padding: 12px 30px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }
        .btn-deposit:hover {
            background: #45a049;
        }
        .btn-cancel {
            background: #f44336;
            color: white;
            padding: 12px 30px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }
        .btn-cancel:hover {
            background: #da190b;
        }
        .coupon-modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,0.7);
            z-index: 10001;
            justify-content: center;
            align-items: center;
        }
        .coupon-modal-content {
            background: white;
            padding: 20px;
            border-radius: 10px;
            max-width: 700px;
            max-height: 85vh;
            width: 90%;
            display: flex;
            flex-direction: column;
        }
        #couponList {
            overflow-y: auto;
            max-height: calc(85vh - 100px);
            padding-right: 10px;
        }
        #couponList::-webkit-scrollbar {
            width: 8px;
        }
        #couponList::-webkit-scrollbar-track {
            background: #f1f1f1;
            border-radius: 4px;
        }
        #couponList::-webkit-scrollbar-thumb {
            background: #ff6b6b;
            border-radius: 4px;
        }
        #couponList::-webkit-scrollbar-thumb:hover {
            background: #ff5252;
        }
        .coupon-modal-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }
        .coupon-modal-close {
            cursor: pointer;
            font-size: 24px;
            color: #666;
        }
        .coupon-item {
            border: 2px solid #eee;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 15px;
            cursor: pointer;
            transition: all 0.3s;
        }
        .coupon-item:hover {
            border-color: #ff6b6b;
            background: #fff5f5;
        }
        .coupon-item-code {
            font-size: 18px;
            font-weight: bold;
            color: #ff6b6b;
            margin-bottom: 5px;
        }
        .coupon-item-value {
            font-size: 20px;
            font-weight: bold;
            color: #4CAF50;
            margin-bottom: 5px;
        }
        .coupon-item-desc {
            font-size: 14px;
            color: #666;
            margin-bottom: 8px;
        }
        .coupon-item-details {
            font-size: 12px;
            color: #999;
        }
        
        @keyframes progressFill {
            from {
                width: 0%;
            }
            to {
                width: 100%;
            }
        }
    </style>
</head>
<body>
    <div id="header-placeholder"></div>

    <main class="main-content">
        <div class="page-header">
            <h2>Thanh toán</h2>
        </div>
        
        <div class="checkout-container">
            <div class="checkout-section">
                <h3>Thông tin đơn hàng</h3>
                <c:forEach var="item" items="${cartItems}">
                    <div class="order-summary-item">
                        <span>${item.product.name} x ${item.quantity}</span>
                        <span><fmt:formatNumber value="${item.unit_price * item.quantity}" type="currency" currencyCode="VND" pattern="#,##0 đ"/></span>
                    </div>
                </c:forEach>
                
                <div class="order-summary-item" style="border-top: 1px dashed #ddd; padding-top: 10px; margin-top: 10px;">
                    <span>Tạm tính:</span>
                    <span id="subtotal">
                        <c:set var="total" value="0"/>
                        <c:forEach var="item" items="${cartItems}">
                            <c:set var="total" value="${total + (item.unit_price * item.quantity)}"/>
                        </c:forEach>
                        <fmt:formatNumber value="${total}" type="currency" currencyCode="VND" pattern="#,##0 đ"/>
                    </span>
                </div>
                
                <div class="order-summary-item" id="discountRow" style="color: #4CAF50; display: none;">
                    <span>Giảm giá:</span>
                    <span id="discountAmount">0 đ</span>
                </div>
                
                <div class="order-summary-item" style="font-size: 18px; font-weight: bold; border-top: 2px solid #333; padding-top: 10px; margin-top: 10px;">
                    <span>Tổng cộng:</span>
                    <span class="order-total" style="color: #ff6b6b;" id="finalTotal">
                        <fmt:formatNumber value="${total}" type="currency" currencyCode="VND" pattern="#,##0 đ"/>
                    </span>
                </div>
            </div>

            <div class="checkout-section">
                <h3>Phương thức thanh toán</h3>
                <div class="payment-method-option" style="padding: 15px; border: 2px solid #4CAF50; border-radius: 8px; background: #f1f8f4;">
                    <label style="display: flex; align-items: center; cursor: pointer; margin: 0;">
                        <input type="radio" name="payment_method" value="wallet" checked style="margin-right: 10px; width: 20px; height: 20px;">
                        <span style="font-size: 16px; font-weight: 500; color: #2c3e50;">
                            <i class="fas fa-wallet" style="color: #4CAF50; margin-right: 8px;"></i>
                            Ví điện tử (Số dư: <fmt:formatNumber value="${user.balance}" type="currency" currencyCode="VND" pattern="#,##0 đ"/>)
                        </span>
                    </label>
                </div>
            </div>

            <div class="checkout-section">
                <h3>Mã giảm giá (Không bắt buộc)</h3>
                <div class="form-group" style="display: flex; gap: 10px;">
                    <input type="text" id="couponCode" class="form-input" placeholder="Nhập mã giảm giá" style="flex: 1;">
                    <button class="btn btn-outline" onclick="showCouponModal()" style="white-space: nowrap;">
                        <i class="fas fa-list"></i> Chọn mã
                    </button>
                </div>
            </div>

            <button class="btn btn-primary btn-block" onclick="placeOrder()" style="padding: 0.5rem 0.5rem;">Đặt hàng</button>
        </div>
    </main>

    <div class="insufficient-balance-modal" id="insufficientBalanceModal">
        <div class="insufficient-balance-content">
            <div class="warning-icon"><i class="fas fa-exclamation-triangle"></i></div>
            <h3>Số dư không đủ!</h3>
            <div class="balance-info">
                <p><strong>Tổng đơn hàng:</strong> <span style="color: #ff6b6b;"><fmt:formatNumber value="${totalAmount}" type="currency" currencyCode="VND" pattern="#,##0 đ"/></span></p>
                <p><strong>Số dư hiện tại:</strong> <span style="color: #4caf50;"><fmt:formatNumber value="${userBalance}" type="currency" currencyCode="VND" pattern="#,##0 đ"/></span></p>
                <p><strong>Còn thiếu:</strong> <span style="color: #f44336;"><fmt:formatNumber value="${totalAmount - userBalance}" type="currency" currencyCode="VND" pattern="#,##0 đ"/></span></p>
            </div>
            <p>Vui lòng nạp thêm tiền để hoàn tất thanh toán</p>
            <div class="modal-buttons">
                <button class="btn-deposit" onclick="goToDeposit()"><i class="fas fa-wallet"></i> Nạp tiền ngay</button>
                <button class="btn-cancel" onclick="closeInsufficientModal()"><i class="fas fa-times"></i> Thoát</button>
            </div>
        </div>
    </div>

    <div class="coupon-modal" id="couponModal">
        <div class="coupon-modal-content">
            <div class="coupon-modal-header">
                <h3><i class="fas fa-tag"></i> Chọn mã giảm giá</h3>
                <span class="coupon-modal-close" onclick="closeCouponModal()">&times;</span>
            </div>
            <div id="couponList">
                <p style="text-align: center; padding: 20px;">Đang tải...</p>
            </div>
        </div>
    </div>

    <div class="processing-overlay" id="processingOverlay">
        <div class="processing-content">
            <div id="processingStatus">
                <div class="spinner"></div>
                <h3>Đang xử lý đơn hàng...</h3>
                <p>Vui lòng đợi trong giây lát</p>
            </div>
            <div id="successStatus" style="display: none;">
                <div class="success-icon"><i class="fas fa-check-circle"></i></div>
                <h3>Đặt hàng thành công!</h3>
                <p>Đang chuyển đến chi tiết đơn hàng...</p>
                <div class="progress-bar" style="width: 200px; height: 4px; background: #e0e0e0; border-radius: 2px; margin: 20px auto; overflow: hidden;">
                    <div class="progress-fill" style="width: 0%; height: 100%; background: #4CAF50; animation: progressFill 2s linear;"></div>
                </div>
            </div>
            <div id="errorStatus" style="display: none;">
                <div class="error-icon"><i class="fas fa-times-circle"></i></div>
                <h3>Đặt hàng thất bại!</h3>
                <p id="errorMessage"></p>
                <button class="btn btn-primary" onclick="closeOverlay()">Đóng</button>
            </div>
        </div>
    </div>

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
    <script>
        let pollInterval;
        let subtotalAmount = ${total};
        let selectedCouponData = null;
        let availableCoupons = [];
        
        window.onload = function() {
            <c:if test="${insufficientBalance == true}">
                document.getElementById('insufficientBalanceModal').style.display = 'flex';
            </c:if>
        };
        
        function goToDeposit() {
            const urlParams = new URLSearchParams(window.location.search);
            let checkoutUrl = '${pageContext.request.contextPath}/user/checkout';
            
            if (urlParams.get('direct') === 'true') {
                checkoutUrl += '?direct=true&productId=' + urlParams.get('productId') + '&quantity=' + urlParams.get('quantity');
            }
            
            const returnUrl = encodeURIComponent(checkoutUrl);
            window.location.href = '${pageContext.request.contextPath}/deposit?returnUrl=' + returnUrl;
        }
        
        function closeInsufficientModal() {
            document.getElementById('insufficientBalanceModal').style.display = 'none';
            window.location.href = '${pageContext.request.contextPath}/cart';
        }
        
        function placeOrder() {
            const couponCode = document.getElementById('couponCode').value.trim();
            const urlParams = new URLSearchParams(window.location.search);
            
            showProcessingOverlay();
            
            let body = 'coupon_code=' + encodeURIComponent(couponCode);
            
            if (urlParams.get('direct') === 'true') {
                body += '&direct=true';
                body += '&productId=' + encodeURIComponent(urlParams.get('productId'));
                body += '&quantity=' + encodeURIComponent(urlParams.get('quantity'));
            }
            
            fetch('${pageContext.request.contextPath}/checkout', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: body
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    startPolling(data.intentId);
                } else {
                    showError(data.error || 'Có lỗi xảy ra');
                }
            })
            .catch(error => {
                showError('Lỗi kết nối: ' + error.message);
            });
        }
        
        function startPolling(intentId) {
            pollInterval = setInterval(() => {
                fetch('${pageContext.request.contextPath}/order-status?intentId=' + intentId)
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        if (data.status === 'COMPLETED') {
                            clearInterval(pollInterval);
                            showSuccess(data.orderId);
                        } else if (data.status === 'FAILED') {
                            clearInterval(pollInterval);
                            showError(data.errorMessage || 'Đặt hàng thất bại');
                        }
                    }
                })
                .catch(error => {
                    console.error('Polling error:', error);
                });
            }, 1000);
            
            setTimeout(() => {
                if (pollInterval) {
                    clearInterval(pollInterval);
                    showError('Timeout: Đơn hàng mất quá nhiều thời gian xử lý');
                }
            }, 30000);
        }
        
        function showProcessingOverlay() {
            document.getElementById('processingOverlay').style.display = 'flex';
            document.getElementById('processingStatus').style.display = 'block';
            document.getElementById('successStatus').style.display = 'none';
            document.getElementById('errorStatus').style.display = 'none';
        }
        
        function showSuccess(orderId) {
            document.getElementById('processingStatus').style.display = 'none';
            document.getElementById('successStatus').style.display = 'block';
            
            if (typeof showToast === 'function') {
                showToast('Đặt hàng thành công! Đang chuyển đến chi tiết đơn hàng...', 'success');
            }
            
            setTimeout(() => {
                window.location.href = contextPath + '/user/order-detail?id=' + orderId;
            }, 2000);
        }
        
        function showError(message) {
            document.getElementById('processingStatus').style.display = 'none';
            document.getElementById('errorStatus').style.display = 'block';
            document.getElementById('errorMessage').textContent = message;
        }
        
        function closeOverlay() {
            document.getElementById('processingOverlay').style.display = 'none';
        }
        
        function showCouponModal() {
            document.getElementById('couponModal').style.display = 'flex';
            loadAvailableCoupons();
        }
        
        function closeCouponModal() {
            document.getElementById('couponModal').style.display = 'none';
        }
        
        function loadAvailableCoupons() {
            console.log('Loading coupons for modal...');
            fetch(contextPath + '/user/coupons?action=getAvailable')
                .then(response => {
                    console.log('Response status:', response.status);
                    return response.json();
                })
                .then(coupons => {
                    console.log('Loaded coupons:', coupons);
                    console.log('Number of coupons:', coupons.length);
                    availableCoupons = coupons;
                    displayCouponList(coupons);
                })
                .catch(error => {
                    console.error('Error loading coupons:', error);
                    document.getElementById('couponList').innerHTML = '<p style="text-align: center; color: red;">Có lỗi xảy ra khi tải mã giảm giá</p>';
                });
        }
        
        function displayCouponList(coupons) {
            const couponList = document.getElementById('couponList');
            
            // Filter coupons: Chỉ hiển thị coupon có min_order_amount <= subtotal
            const eligibleCoupons = coupons.filter(coupon => coupon.min_order_amount <= subtotalAmount);
            
            if (!eligibleCoupons || eligibleCoupons.length === 0) {
                couponList.innerHTML = '<p style="text-align: center; color: #999;">Không có mã giảm giá khả dụng cho đơn hàng này</p>';
                return;
            }
            
            let html = '';
            eligibleCoupons.forEach(coupon => {
                const discountValue = coupon.discount_type == 1 
                    ? '-' + coupon.discount_value + '%' 
                    : '-' + formatCurrency(coupon.discount_value);
                
                const maxDiscountText = coupon.max_discount_amount 
                    ? ' | Giảm tối đa: ' + formatCurrency(coupon.max_discount_amount) 
                    : '';
                
                html += '<div class="coupon-item" onclick="selectCoupon(\'' + coupon.code + '\')">' +
                    '<div class="coupon-item-code">' + coupon.code + '</div>' +
                    '<div class="coupon-item-value">' + discountValue + '</div>' +
                    '<div class="coupon-item-desc">' + (coupon.description || '') + '</div>' +
                    '<div class="coupon-item-details">' +
                    'Đơn tối thiểu: ' + formatCurrency(coupon.min_order_amount) + ' | ' +
                    'HSD: ' + formatDate(coupon.end_date) + ' | ' +
                    'Còn ' + coupon.usage_limit_per_user + ' lượt' +
                    maxDiscountText +
                    '</div></div>';
            });
            
            couponList.innerHTML = html;
        }
        
        function selectCoupon(code) {
            // Tìm coupon data
            selectedCouponData = availableCoupons.find(c => c.code === code);
            
            if (!selectedCouponData) {
                showToast('Không tìm thấy mã giảm giá', 'error');
                return;
            }
            
            // Tính discount
            let discountAmount = 0;
            if (selectedCouponData.discount_type == 1) {
                // Percentage
                discountAmount = subtotalAmount * (selectedCouponData.discount_value / 100);
                // Apply max discount if exists
                if (selectedCouponData.max_discount_amount && discountAmount > selectedCouponData.max_discount_amount) {
                    discountAmount = selectedCouponData.max_discount_amount;
                }
            } else {
                // Fixed amount
                discountAmount = selectedCouponData.discount_value;
            }
            
            // Update UI
            document.getElementById('couponCode').value = code;
            document.getElementById('discountRow').style.display = 'flex';
            document.getElementById('discountAmount').textContent = '- ' + formatCurrency(discountAmount);
            
            const finalTotal = subtotalAmount - discountAmount;
            document.getElementById('finalTotal').textContent = formatCurrency(finalTotal);
            
            closeCouponModal();
            showToast('Đã áp dụng mã: ' + code + ' (Giảm ' + formatCurrency(discountAmount) + ')', 'success');
        }
        
        function formatCurrency(value) {
            return new Intl.NumberFormat('vi-VN', {
                style: 'currency',
                currency: 'VND'
            }).format(value);
        }
        
        function formatDate(dateStr) {
            const date = new Date(dateStr);
            return date.toLocaleDateString('vi-VN');
        }
        
        // Reset discount when coupon code is cleared
        document.addEventListener('DOMContentLoaded', function() {
            const couponInput = document.getElementById('couponCode');
            if (couponInput) {
                couponInput.addEventListener('input', function() {
                    if (!this.value || this.value.trim() === '') {
                        selectedCouponData = null;
                        document.getElementById('discountRow').style.display = 'none';
                        document.getElementById('discountAmount').textContent = '0 đ';
                        document.getElementById('finalTotal').textContent = formatCurrency(subtotalAmount);
                    }
                });
            }
        });
    </script>
</body>
</html>
