<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tạo mã giảm giá - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
    <style>
        .select2-container {
            width: 100% !important;
        }
        .select2-container--default .select2-selection--multiple {
            min-height: 38px;
            border: 1px solid #ddd;
            border-radius: 6px;
        }
    </style>
</head>
<body>
    <div id="header-placeholder"></div>

    <main class="main-content">
        <div class="form-container">
            <h2><i class="fas fa-plus-circle"></i> Tạo mã giảm giá mới</h2>
            
            <c:if test="${requestScope.error != null}">
                <div class="alert alert-danger">
                    <i class="fas fa-exclamation-circle"></i>
                    <span>${requestScope.error}</span>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/admin/create-coupon" method="POST" class="admin-form">
                <div class="form-section">
                    <h3>Thông tin cơ bản</h3>
                    <div class="form-row">
                        <label for="code">Mã coupon: <span class="required">*</span></label>
                        <input type="text" id="code" name="code" value="${param.code}" required 
                               pattern="[A-Z0-9]+" title="Chỉ chấp nhận chữ in hoa và số">
                    </div>

                    <div class="form-row">
                        <label for="name">Tên: <span class="required">*</span></label>
                        <input type="text" id="name" name="name" value="${param.name}" required>
                    </div>

                    <div class="form-row">
                        <label for="description">Mô tả:</label>
                        <textarea id="description" name="description" rows="3">${param.description}</textarea>
                    </div>

                    <div class="form-row">
                        <label for="status">Trạng thái: <span class="required">*</span></label>
                        <select id="status" name="status" required>
                            <option value="1" ${param.status == '1' ? 'selected' : 'selected'}>Đang hoạt động</option>
                            <option value="2" ${param.status == '2' ? 'selected' : ''}>Không hoạt động</option>
                        </select>
                    </div>
                </div>

                <div class="form-section">
                    <h3>Thông tin giảm giá</h3>
                    <div class="form-row">
                        <label for="discount_type">Loại giảm giá: <span class="required">*</span></label>
                        <select id="discount_type" name="discount_type" required onchange="toggleDiscountValue()">
                            <option value="1" ${param.discount_type == '1' ? 'selected' : 'selected'}>Phần trăm</option>
                            <option value="2" ${param.discount_type == '2' ? 'selected' : ''}>Cố định</option>
                        </select>
                    </div>

                    <div class="form-row">
                        <label for="discount_value">Giá trị giảm: <span class="required">*</span></label>
                        <input type="number" id="discount_value" name="discount_value" 
                               value="${param.discount_value}" step="0.01" min="0" required>
                        <small id="discount_hint" class="form-hint"></small>
                    </div>

                    <div class="form-row">
                        <label for="min_order_amount">Đơn hàng tối thiểu: <span class="required">*</span></label>
                        <input type="number" id="min_order_amount" name="min_order_amount" 
                               value="${param.min_order_amount != null ? param.min_order_amount : '0'}" step="1000" min="0" required>
                    </div>

                    <div class="form-row">
                        <label for="max_discount_amount">Giảm tối đa:</label>
                        <input type="number" id="max_discount_amount" name="max_discount_amount" 
                               value="${param.max_discount_amount}" step="1000" min="0">
                        <small class="form-hint">Để trống nếu không giới hạn</small>
                    </div>
                </div>

                <div class="form-section">
                    <h3>Thời gian hiệu lực</h3>
                    <div class="form-row">
                        <label for="start_date">Ngày bắt đầu: <span class="required">*</span></label>
                        <input type="datetime-local" id="start_date" name="start_date" 
                               value="${param.start_date}" required>
                    </div>

                    <div class="form-row">
                        <label for="end_date">Ngày kết thúc: <span class="required">*</span></label>
                        <input type="datetime-local" id="end_date" name="end_date" 
                               value="${param.end_date}" required>
                    </div>
                </div>

                <div class="form-section">
                    <h3>Giới hạn sử dụng</h3>
                    <div class="form-row">
                        <label for="usage_limit_per_user">Lượt sử dụng/người: <span class="required">*</span></label>
                        <input type="number" id="usage_limit_per_user" name="usage_limit_per_user" 
                               value="${param.usage_limit_per_user != null ? param.usage_limit_per_user : '1'}" min="1" required>
                    </div>

                    <div class="form-row">
                        <label for="total_usage_limit">Tổng lượt sử dụng:</label>
                        <input type="number" id="total_usage_limit" name="total_usage_limit" 
                               value="${param.total_usage_limit}" min="1">
                        <small class="form-hint">Để trống nếu không giới hạn</small>
                    </div>
                </div>

                <div class="form-section">
                    <h3>Áp dụng</h3>
                    <div class="form-row">
                        <label for="applicable_product_ids">Sản phẩm áp dụng:</label>
                        <select id="applicable_product_ids" name="applicable_product_ids" multiple="multiple" class="select2-multi">
                            <option value="">Tất cả sản phẩm</option>
                            <c:forEach var="product" items="${products}">
                                <option value="${product.id}">${product.name} - <fmt:formatNumber value="${product.denomination}" type="currency" currencySymbol="₫" groupingUsed="true" maxFractionDigits="0"/></option>
                            </c:forEach>
                        </select>
                        <small class="form-hint">Để trống nếu áp dụng cho tất cả sản phẩm</small>
                    </div>

                    <div class="form-row">
                        <label for="applicable_provider_ids">Nhà cung cấp áp dụng:</label>
                        <select id="applicable_provider_ids" name="applicable_provider_ids" multiple="multiple" class="select2-multi">
                            <option value="">Tất cả nhà cung cấp</option>
                            <c:forEach var="provider" items="${providers}">
                                <option value="${provider.id}">${provider.name} (${provider.code})</option>
                            </c:forEach>
                        </select>
                        <small class="form-hint">Để trống nếu áp dụng cho tất cả nhà cung cấp</small>
                    </div>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-save"></i> Tạo mã giảm giá
                    </button>
                    <a href="${pageContext.request.contextPath}/admin/coupons" class="btn btn-secondary">
                        <i class="fas fa-times"></i> Hủy
                    </a>
                </div>
            </form>
        </div>
    </main>

    <div id="footer-placeholder"></div>

    <script>
        window.userData = {
            id: ${sessionScope.user.id},
            username: '<c:out value="${sessionScope.user.username}" escapeXml="true"/>',
            email: '<c:out value="${sessionScope.user.email}" escapeXml="true"/>',
            role: '<c:out value="${sessionScope.user.role}" escapeXml="true"/>',
            balance: ${sessionScope.user.balance}
        };

        function toggleDiscountValue() {
            const discountType = document.getElementById('discount_type').value;
            const discountValue = document.getElementById('discount_value');
            const discountHint = document.getElementById('discount_hint');
            
            if (discountType === '1') {
                discountValue.max = '100';
                discountValue.step = '0.01';
                discountHint.textContent = 'Nhập giá trị từ 0 đến 100 (%)';
            } else {
                discountValue.removeAttribute('max');
                discountValue.step = '1000';
                discountHint.textContent = 'Nhập số tiền giảm (VND)';
            }
        }

        document.addEventListener('DOMContentLoaded', function() {
            toggleDiscountValue();
            
            const form = document.querySelector('.admin-form');
            form.addEventListener('submit', function(e) {
                const startDate = new Date(document.getElementById('start_date').value);
                const endDate = new Date(document.getElementById('end_date').value);
                
                if (endDate <= startDate) {
                    e.preventDefault();
                    alert('Ngày kết thúc phải sau ngày bắt đầu');
                    return false;
                }
            });
        });
    </script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
    <script>
        $(document).ready(function() {
            // Initialize Select2
            $('.select2-multi').select2({
                placeholder: 'Chọn...',
                allowClear: true,
                width: '100%'
            });
            
            // Create hidden inputs to send comma-separated values
            $('form').on('submit', function(e) {
                // Remove old hidden inputs if any
                $('input[name="applicable_product_ids_hidden"]').remove();
                $('input[name="applicable_provider_ids_hidden"]').remove();
                
                const productValues = $('#applicable_product_ids').val();
                const providerValues = $('#applicable_provider_ids').val();
                
                // Disable the original selects
                $('#applicable_product_ids').prop('disabled', true);
                $('#applicable_provider_ids').prop('disabled', true);
                
                // Create hidden inputs with the comma-separated values
                if (productValues && productValues.length > 0) {
                    $('<input>').attr({
                        type: 'hidden',
                        name: 'applicable_product_ids',
                        value: productValues.join(',')
                    }).appendTo(this);
                } else {
                    $('<input>').attr({
                        type: 'hidden',
                        name: 'applicable_product_ids',
                        value: ''
                    }).appendTo(this);
                }
                
                if (providerValues && providerValues.length > 0) {
                    $('<input>').attr({
                        type: 'hidden',
                        name: 'applicable_provider_ids',
                        value: providerValues.join(',')
                    }).appendTo(this);
                } else {
                    $('<input>').attr({
                        type: 'hidden',
                        name: 'applicable_provider_ids',
                        value: ''
                    }).appendTo(this);
                }
            });
        });
    </script>
    <script src="${pageContext.request.contextPath}/js/layout.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script src="${pageContext.request.contextPath}/js/toast.js"></script>
</body>
</html>

