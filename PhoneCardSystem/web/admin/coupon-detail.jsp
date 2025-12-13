<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết mã giảm giá - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div id="header-placeholder"></div>

    <main class="main-content">
        <div class="detail-container">
            <div class="detail-header">
                <h2><i class="fas fa-ticket-alt"></i> Chi tiết mã giảm giá</h2>
                <div class="detail-actions">
                    <a href="${pageContext.request.contextPath}/admin/edit-coupon?id=${coupon.id}" class="btn btn-primary">
                        <i class="fas fa-edit"></i> Chỉnh sửa
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/coupons" class="btn btn-secondary">
                        <i class="fas fa-arrow-left"></i> Quay lại
                    </a>
                </div>
            </div>

            <div class="detail-content">
                <div class="detail-section">
                    <h3 class="section-title">Thông tin cơ bản</h3>
                    <div class="detail-grid">
                        <div class="detail-item">
                            <label class="detail-label">ID:</label>
                            <span class="detail-value">${coupon.id}</span>
                        </div>
                        <div class="detail-item">
                            <label class="detail-label">Mã coupon:</label>
                            <span class="detail-value coupon-code-display">${coupon.code}</span>
                        </div>
                        <div class="detail-item full-width">
                            <label class="detail-label">Tên:</label>
                            <span class="detail-value">${coupon.name}</span>
                        </div>
                        <div class="detail-item full-width">
                            <label class="detail-label">Mô tả:</label>
                            <span class="detail-value">${coupon.description != null ? coupon.description : 'Không có mô tả'}</span>
                        </div>
                        <div class="detail-item">
                            <label class="detail-label">Trạng thái:</label>
                            <span class="detail-value">
                                <c:choose>
                                    <c:when test="${coupon.status == 1}">
                                        <span class="badge badge-success">Đang hoạt động</span>
                                    </c:when>
                                    <c:when test="${coupon.status == 2}">
                                        <span class="badge badge-secondary">Không hoạt động</span>
                                    </c:when>
                                    <c:when test="${coupon.status == 3}">
                                        <span class="badge badge-warning">Hết hạn</span>
                                    </c:when>
                                    <c:when test="${coupon.status == 4}">
                                        <span class="badge badge-danger">Đã hết lượt</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-secondary">N/A</span>
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                        <div class="detail-item">
                            <label class="detail-label">Campaign ID:</label>
                            <span class="detail-value">${coupon.campaign_id != null ? coupon.campaign_id : 'N/A'}</span>
                        </div>
                    </div>
                </div>

                <div class="detail-section">
                    <h3 class="section-title">Thông tin giảm giá</h3>
                    <div class="detail-grid">
                        <div class="detail-item">
                            <label class="detail-label">Loại giảm giá:</label>
                            <span class="detail-value">
                                <c:choose>
                                    <c:when test="${coupon.discount_type == 1}">
                                        <span class="badge badge-info">Phần trăm</span>
                                    </c:when>
                                    <c:when test="${coupon.discount_type == 2}">
                                        <span class="badge badge-info">Cố định</span>
                                    </c:when>
                                    <c:otherwise>N/A</c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                        <div class="detail-item">
                            <label class="detail-label">Giá trị giảm:</label>
                            <span class="detail-value highlight-value">
                                <c:choose>
                                    <c:when test="${coupon.discount_type == 1}">
                                        ${coupon.discount_value}%
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber value="${coupon.discount_value}" type="currency" currencySymbol="₫" groupingUsed="true" maxFractionDigits="0"/>
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                        <div class="detail-item">
                            <label class="detail-label">Đơn hàng tối thiểu:</label>
                            <span class="detail-value">
                                <fmt:formatNumber value="${coupon.min_order_amount}" type="currency" currencySymbol="₫" groupingUsed="true" maxFractionDigits="0"/>
                            </span>
                        </div>
                        <div class="detail-item">
                            <label class="detail-label">Giảm tối đa:</label>
                            <span class="detail-value">
                                <c:choose>
                                    <c:when test="${coupon.max_discount_amount != null}">
                                        <fmt:formatNumber value="${coupon.max_discount_amount}" type="currency" currencySymbol="₫" groupingUsed="true" maxFractionDigits="0"/>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-success">Không giới hạn</span>
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                    </div>
                </div>

                <div class="detail-section">
                    <h3 class="section-title">Thời gian hiệu lực</h3>
                    <div class="detail-grid">
                        <div class="detail-item">
                            <label class="detail-label">Ngày bắt đầu:</label>
                            <span class="detail-value">
                                <i class="fas fa-calendar-alt"></i>
                                <fmt:formatDate value="${coupon.start_date}" pattern="dd/MM/yyyy HH:mm:ss" />
                            </span>
                        </div>
                        <div class="detail-item">
                            <label class="detail-label">Ngày kết thúc:</label>
                            <span class="detail-value">
                                <i class="fas fa-calendar-alt"></i>
                                <fmt:formatDate value="${coupon.end_date}" pattern="dd/MM/yyyy HH:mm:ss" />
                            </span>
                        </div>
                    </div>
                </div>

                <div class="detail-section">
                    <h3 class="section-title">Giới hạn sử dụng</h3>
                    <div class="detail-grid">
                        <div class="detail-item">
                            <label class="detail-label">Lượt sử dụng/người:</label>
                            <span class="detail-value highlight-number">${coupon.usage_limit_per_user}</span>
                        </div>
                        <div class="detail-item">
                            <label class="detail-label">Tổng lượt sử dụng:</label>
                            <span class="detail-value highlight-number">
                                <c:choose>
                                    <c:when test="${coupon.total_usage_limit != null}">
                                        ${coupon.total_usage_limit}
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-success">∞ (Không giới hạn)</span>
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                        <div class="detail-item">
                            <label class="detail-label">Đã sử dụng:</label>
                            <span class="detail-value highlight-number usage-count">${coupon.current_usage_count}</span>
                        </div>
                        <div class="detail-item">
                            <label class="detail-label">Còn lại:</label>
                            <span class="detail-value highlight-number">
                                <c:choose>
                                    <c:when test="${coupon.total_usage_limit != null}">
                                        ${coupon.total_usage_limit - coupon.current_usage_count}
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-success">∞</span>
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                    </div>
                </div>

                <div class="detail-section">
                    <h3 class="section-title">Áp dụng</h3>
                    <div class="detail-grid">
                        <div class="detail-item full-width">
                            <label class="detail-label">Sản phẩm áp dụng:</label>
                            <span class="detail-value">
                                <c:choose>
                                    <c:when test="${coupon.applicable_product_ids != null && !coupon.applicable_product_ids.trim().isEmpty()}">
                                        ${coupon.applicable_product_ids}
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted">Áp dụng cho tất cả sản phẩm</span>
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                        <div class="detail-item full-width">
                            <label class="detail-label">Nhà cung cấp áp dụng:</label>
                            <span class="detail-value">
                                <c:choose>
                                    <c:when test="${coupon.applicable_provider_ids != null && !coupon.applicable_provider_ids.trim().isEmpty()}">
                                        ${coupon.applicable_provider_ids}
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted">Áp dụng cho tất cả nhà cung cấp</span>
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                    </div>
                </div>

                <div class="detail-section">
                    <h3 class="section-title">Thông tin hệ thống</h3>
                    <div class="detail-grid">
                        <div class="detail-item">
                            <label class="detail-label">Ngày tạo:</label>
                            <span class="detail-value">
                                <fmt:formatDate value="${coupon.created_at}" pattern="dd/MM/yyyy HH:mm:ss" />
                            </span>
                        </div>
                        <div class="detail-item">
                            <label class="detail-label">Cập nhật lần cuối:</label>
                            <span class="detail-value">
                                <fmt:formatDate value="${coupon.updated_at}" pattern="dd/MM/yyyy HH:mm:ss" />
                            </span>
                        </div>
                        <div class="detail-item">
                            <label class="detail-label">Trạng thái xóa:</label>
                            <span class="detail-value">
                                <c:choose>
                                    <c:when test="${coupon.deleted == 0}">
                                        <span class="badge badge-success">Đang hoạt động</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-danger">Đã xóa</span>
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                    </div>
                </div>
            </div>
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
    </script>
    <script src="${pageContext.request.contextPath}/js/layout.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script src="${pageContext.request.contextPath}/js/toast.js"></script>
</body>
</html>

