<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thanh toán - Hệ Thống Bán Thẻ Điện Thoại</title>
    <link rel="stylesheet" href="../css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <!-- Header Placeholder -->
    <div id="header-placeholder"></div>

    <!-- Main Content -->
    <main class="main-content">
        <div class="checkout-container">
            <div class="checkout-form">
                <h2>Thanh toán</h2>
                <div class="checkout-section">
                    <h3>Thông tin đơn hàng</h3>
                    <div id="checkoutItems"></div>
                </div>
                <div class="checkout-section">
                    <h3>Phương thức thanh toán</h3>
                    <div class="payment-methods">
                        <label class="payment-method">
                            <input type="radio" name="paymentMethod" value="balance" checked>
                            <span>Số dư tài khoản</span>
                        </label>
                        <label class="payment-method">
                            <input type="radio" name="paymentMethod" value="vnpay">
                            <span>VNPay</span>
                        </label>
                        <label class="payment-method">
                            <input type="radio" name="paymentMethod" value="momo">
                            <span>MoMo</span>
                        </label>
                    </div>
                </div>
                <div class="checkout-summary">
                    <div class="summary-row">
                        <span>Tạm tính:</span>
                        <span id="checkoutSubtotal">0 đ</span>
                    </div>
                    <div class="summary-row">
                        <span>Giảm giá:</span>
                        <span id="checkoutDiscount">0 đ</span>
                    </div>
                    <div class="summary-row total">
                        <span>Tổng cộng:</span>
                        <span id="checkoutTotal">0 đ</span>
                    </div>
                    <button class="btn btn-primary btn-block" id="placeOrderBtn">Đặt hàng</button>
                </div>
            </div>
        </div>
    </main>

    <!-- Footer Placeholder -->
    <div id="footer-placeholder"></div>

    <script src="../js/layout.js"></script>
    <script src="../js/app.js"></script>
    <script src="../js/checkout.js"></script>
</body>
</html>


