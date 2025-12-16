<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý đơn hàng - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div id="header-placeholder"></div>

    <main class="main-content">
        <div class="page-header">
            <h2><i class="fas fa-shopping-bag"></i> Quản lý đơn hàng</h2>
            <div class="page-stats">
                <span><strong>${totalOrders}</strong> đơn hàng</span>
            </div>
        </div>
        
        <c:if test="${sessionScope.successMessage != null}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle"></i>
                ${sessionScope.successMessage}
            </div>
            <c:remove var="successMessage" scope="session"/>
        </c:if>
        
        <form method="GET" action="${pageContext.request.contextPath}/admin/orders" id="filterForm">
            <div class="filter-bar">
                <select name="status" id="filterStatus" class="filter-select">
                    <option value="">Tất cả trạng thái</option>
                    <option value="pending" ${selectedStatus == 'pending' ? 'selected' : ''}>Đang xử lý</option>
                    <option value="completed" ${selectedStatus == 'completed' ? 'selected' : ''}>Hoàn thành</option>
                    <option value="cancelled" ${selectedStatus == 'cancelled' ? 'selected' : ''}>Đã hủy</option>
                </select>
                <select name="dateFilter" id="filterDate" class="filter-select">
                    <option value="">Tất cả thời gian</option>
                    <option value="today" ${selectedDateFilter == 'today' ? 'selected' : ''}>Hôm nay</option>
                    <option value="week" ${selectedDateFilter == 'week' ? 'selected' : ''}>Tuần này</option>
                    <option value="month" ${selectedDateFilter == 'month' ? 'selected' : ''}>Tháng này</option>
                </select>
                <input type="text" name="search" id="searchOrder" class="search-input" placeholder="Tìm kiếm đơn hàng, người dùng..." value="${searchTerm != null ? searchTerm : ''}">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-search"></i> Tìm kiếm
                </button>
            </div>
        </form>

        <div class="admin-table-container">
            <c:choose>
                <c:when test="${orders != null && !orders.isEmpty()}">
                    <table class="admin-table">
                        <thead>
                            <tr>
                                <th>Mã đơn</th>
                                <th>Người dùng</th>
                                <th>Tổng tiền</th>
                                <th>Giảm giá</th>
                                <th>Thanh toán</th>
                                <th>Trạng thái</th>
                                <th>Ngày tạo</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="order" items="${orders}">
                                <tr>
                                    <td>
                                        <strong>${order.order_code}</strong>
                                    </td>
                                    <td>
                                        <i class="fas fa-user"></i> ${usernames[order.id]}
                                    </td>
                                    <td>
                                        <fmt:formatNumber value="${order.subtotal_amount}" type="currency" currencyCode="VND" pattern="#,##0 đ"/>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${order.discount_amount > 0}">
                                                <span style="color: #4CAF50;">
                                                    -<fmt:formatNumber value="${order.discount_amount}" type="currency" currencyCode="VND" pattern="#,##0 đ"/>
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span style="color: #999;">-</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <strong style="color: #ff6b6b;">
                                            <fmt:formatNumber value="${order.total_amount}" type="currency" currencyCode="VND" pattern="#,##0 đ"/>
                                        </strong>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${order.status == 0}">
                                                <span class="badge badge-warning">
                                                    <i class="fas fa-clock"></i> Đang xử lý
                                                </span>
                                            </c:when>
                                            <c:when test="${order.status == 1}">
                                                <span class="badge badge-success">
                                                    <i class="fas fa-check-circle"></i> Hoàn thành
                                                </span>
                                            </c:when>
                                            <c:when test="${order.status == 2}">
                                                <span class="badge badge-danger">
                                                    <i class="fas fa-times-circle"></i> Đã hủy
                                                </span>
                                            </c:when>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${order.created_at}" pattern="dd/MM/yyyy HH:mm"/>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/admin/order-detail?id=${order.id}" class="btn btn-sm btn-primary" title="Xem chi tiết">
                                            <i class="fas fa-eye"></i>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    
                    <c:if test="${totalPages > 1}">
                        <div class="pagination">
                            <c:if test="${currentPage > 1}">
                                <a href="?page=${currentPage - 1}&status=${selectedStatus != null ? selectedStatus : ''}&dateFilter=${selectedDateFilter != null ? selectedDateFilter : ''}&search=${searchTerm != null ? searchTerm : ''}" class="pagination-btn">
                                    <i class="fas fa-chevron-left"></i> Trước
                                </a>
                            </c:if>
                            
                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <c:choose>
                                    <c:when test="${i == currentPage}">
                                        <span class="pagination-btn active">${i}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="?page=${i}&status=${selectedStatus != null ? selectedStatus : ''}&dateFilter=${selectedDateFilter != null ? selectedDateFilter : ''}&search=${searchTerm != null ? searchTerm : ''}" class="pagination-btn">${i}</a>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                            
                            <c:if test="${currentPage < totalPages}">
                                <a href="?page=${currentPage + 1}&status=${selectedStatus != null ? selectedStatus : ''}&dateFilter=${selectedDateFilter != null ? selectedDateFilter : ''}&search=${searchTerm != null ? searchTerm : ''}" class="pagination-btn">
                                    Sau <i class="fas fa-chevron-right"></i>
                                </a>
                            </c:if>
                        </div>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <i class="fas fa-shopping-bag" style="font-size: 64px; color: #ddd;"></i>
                        <h3>Không tìm thấy đơn hàng</h3>
                        <p>Không có đơn hàng nào phù hợp với bộ lọc của bạn</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </main>

    <div id="footer-placeholder"></div>

    <script>
        window.userData = {
            id: <%= user.getId() %>,
            username: '<%= user.getUsername().replace("'", "\\'") %>',
            email: '<%= user.getEmail().replace("'", "\\'") %>',
            role: '<%= user.getRole() %>',
            balance: <%= user.getBalance() %>
        };
    </script>
    <script src="${pageContext.request.contextPath}/js/layout.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script>
        document.getElementById('filterStatus').addEventListener('change', function() {
            document.getElementById('filterForm').submit();
        });
        
        document.getElementById('filterDate').addEventListener('change', function() {
            document.getElementById('filterForm').submit();
        });
    </script>
</body>
</html>
