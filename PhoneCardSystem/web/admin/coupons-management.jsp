<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý mã giảm giá - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div id="header-placeholder"></div>

    <main class="main-content">
        <div class="admin-container">
            <div class="page-header">
                <h2>Quản lý mã giảm giá</h2>
                <a href="${pageContext.request.contextPath}/admin/create-coupon" class="btn btn-primary">
                    <i class="fas fa-plus"></i> Thêm mã giảm giá
                </a>
            </div>

            <div class="filter-section">
                <form action="${pageContext.request.contextPath}/admin/coupons" method="GET" class="filter-form">
                    <div class="filter-group">
                        <input type="text" name="search" placeholder="Tìm kiếm theo mã, tên..." 
                               value="${param.search}" class="filter-input">
                    </div>
                    <div class="filter-group">
                        <select name="status" class="filter-select">
                            <option value="">Tất cả trạng thái</option>
                            <option value="1" ${param.status == '1' ? 'selected' : ''}>Đang hoạt động</option>
                            <option value="2" ${param.status == '2' ? 'selected' : ''}>Không hoạt động</option>
                            <option value="3" ${param.status == '3' ? 'selected' : ''}>Hết hạn</option>
                            <option value="4" ${param.status == '4' ? 'selected' : ''}>Đã hết lượt</option>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-secondary">
                        <i class="fas fa-search"></i> Tìm kiếm
                    </button>
                </form>
            </div>

            <div class="admin-table-container">
                <c:choose>
                    <c:when test="${not empty coupons}">
                        <div class="table-responsive">
                            <table class="admin-table coupon-table">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Mã coupon</th>
                                        <th>Tên</th>
                                        <th>Mô tả</th>
                                        <th>Loại giảm</th>
                                        <th>Giá trị giảm</th>
                                        <th>Đơn tối thiểu</th>
                                        <th>Giảm tối đa</th>
                                        <th>Ngày bắt đầu</th>
                                        <th>Ngày kết thúc</th>
                                        <th>Lượt/người</th>
                                        <th>Tổng lượt</th>
                                        <th>Đã dùng</th>
                                        <th>Trạng thái</th>
                                        <th>Hành động</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="coupon" items="${coupons}">
                                        <tr>
                                            <td>${coupon.id}</td>
                                            <td><strong class="coupon-code">${coupon.code}</strong></td>
                                            <td class="coupon-name">${coupon.name}</td>
                                            <td class="coupon-desc">${coupon.description}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${coupon.discount_type == 1}">
                                                        <span class="badge badge-info">Phần trăm</span>
                                                    </c:when>
                                                    <c:when test="${coupon.discount_type == 2}">
                                                        <span class="badge badge-info">Cố định</span>
                                                    </c:when>
                                                    <c:otherwise>N/A</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="coupon-value">
                                                <c:choose>
                                                    <c:when test="${coupon.discount_type == 1}">
                                                        <strong>${coupon.discount_value}%</strong>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <strong><fmt:formatNumber value="${coupon.discount_value}" type="currency" currencySymbol="₫" groupingUsed="true" maxFractionDigits="0"/></strong>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="coupon-min">
                                                <fmt:formatNumber value="${coupon.min_order_amount}" type="currency" currencySymbol="₫" groupingUsed="true" maxFractionDigits="0"/>
                                            </td>
                                            <td class="coupon-max">
                                                <c:choose>
                                                    <c:when test="${coupon.max_discount_amount != null}">
                                                        <fmt:formatNumber value="${coupon.max_discount_amount}" type="currency" currencySymbol="₫" groupingUsed="true" maxFractionDigits="0"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-muted">Không giới hạn</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="coupon-date">
                                                <fmt:formatDate value="${coupon.start_date}" pattern="dd/MM/yyyy" />
                                            </td>
                                            <td class="coupon-date">
                                                <fmt:formatDate value="${coupon.end_date}" pattern="dd/MM/yyyy" />
                                            </td>
                                            <td class="text-center">${coupon.usage_limit_per_user}</td>
                                            <td class="text-center">
                                                <c:choose>
                                                    <c:when test="${coupon.total_usage_limit != null}">
                                                        ${coupon.total_usage_limit}
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-success">∞</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="text-center">
                                                <strong>${coupon.current_usage_count}</strong>
                                            </td>
                                            <td>
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
                                            </td>
                                            <td class="action-buttons">
                                                <button onclick="viewCoupon(${coupon.id})" class="btn-action btn-view" title="Xem">
                                                    <i class="fas fa-eye"></i>
                                                </button>
                                                <button onclick="editCoupon(${coupon.id})" class="btn-action btn-edit" title="Sửa">
                                                    <i class="fas fa-edit"></i>
                                                </button>
                                                <c:choose>
                                                    <c:when test="${coupon.deleted == 0}">
                                                        <button onclick="deleteCoupon(${coupon.id})" class="btn-action btn-delete" title="Xóa">
                                                            <i class="fas fa-trash"></i>
                                                        </button>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <button onclick="restoreCoupon(${coupon.id})" class="btn-action btn-restore" title="Khôi phục">
                                                            <i class="fas fa-undo"></i>
                                                        </button>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>

                        <div class="pagination">
                            <div class="pagination-info">
                                Hiển thị ${(currentPage - 1) * 15 + 1} - 
                                ${currentPage * 15 > totalCoupons ? totalCoupons : currentPage * 15} 
                                trong tổng số ${totalCoupons} mã giảm giá
                            </div>
                            <c:if test="${totalPages > 1}">
                                <div class="pagination-controls">
                                    <c:if test="${currentPage > 1}">
                                        <a href="?page=${currentPage - 1}&search=${param.search}&status=${param.status}" 
                                           class="pagination-btn">Trước</a>
                                    </c:if>
                                    
                                    <c:forEach begin="1" end="${totalPages}" var="i">
                                        <c:if test="${i == 1 || i == totalPages || (i >= currentPage - 2 && i <= currentPage + 2)}">
                                            <a href="?page=${i}&search=${param.search}&status=${param.status}" 
                                               class="pagination-btn ${i == currentPage ? 'active' : ''}">${i}</a>
                                        </c:if>
                                        <c:if test="${i == currentPage - 3 && currentPage > 4}">
                                            <span class="pagination-ellipsis">...</span>
                                        </c:if>
                                        <c:if test="${i == currentPage + 3 && currentPage < totalPages - 3}">
                                            <span class="pagination-ellipsis">...</span>
                                        </c:if>
                                    </c:forEach>
                                    
                                    <c:if test="${currentPage < totalPages}">
                                        <a href="?page=${currentPage + 1}&search=${param.search}&status=${param.status}" 
                                           class="pagination-btn">Sau</a>
                                    </c:if>
                                </div>
                            </c:if>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-state">
                            <i class="fas fa-ticket-alt"></i>
                            <p>Không tìm thấy mã giảm giá nào</p>
                        </div>
                    </c:otherwise>
                </c:choose>
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
    <script src="${pageContext.request.contextPath}/js/admin-coupons.js"></script>
</body>
</html>
