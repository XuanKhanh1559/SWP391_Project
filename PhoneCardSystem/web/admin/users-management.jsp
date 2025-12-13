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
    <title>Quản lý người dùng - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div id="header-placeholder"></div>

    <main class="main-content">
        <div class="page-header">
            <h2>Quản lý người dùng</h2>
        </div>
        
        <c:if test="${sessionScope.successMessage != null}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle"></i>
                ${sessionScope.successMessage}
            </div>
            <c:remove var="successMessage" scope="session"/>
        </c:if>
        
        <form method="GET" action="${pageContext.request.contextPath}/admin/users" id="filterForm">
            <div class="filter-bar">
                <select name="status" id="filterStatus" class="filter-select">
                    <option value="">Tất cả trạng thái</option>
                    <option value="active" ${selectedStatus == 'active' ? 'selected' : ''}>Hoạt động</option>
                    <option value="inactive" ${selectedStatus == 'inactive' ? 'selected' : ''}>Không hoạt động</option>
                    <option value="banned" ${selectedStatus == 'banned' ? 'selected' : ''}>Bị khóa</option>
                </select>
                <input type="text" name="search" id="searchUser" class="search-input" placeholder="Tìm kiếm theo username hoặc email..." value="${searchTerm != null ? searchTerm : ''}">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-search"></i> Tìm kiếm
                </button>
            </div>
        </form>

        <div class="admin-table-container">
            <c:choose>
                <c:when test="${users != null && !users.isEmpty()}">
                    <table class="admin-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Username</th>
                                <th>Email</th>
                                <th>Số điện thoại</th>
                                <th>Số dư</th>
                                <th>Trạng thái</th>
                                <th>Ngày tạo</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="u" items="${users}">
                                <tr>
                                    <td>${u.id}</td>
                                    <td>${u.username}</td>
                                    <td>${u.email}</td>
                                    <td>${u.phone != null ? u.phone : 'N/A'}</td>
                                    <td>
                                        <fmt:formatNumber value="${u.balance}" type="currency" currencyCode="VND" pattern="#,##0 đ"/>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${u.status == 1}">
                                                <span class="order-status completed">Hoạt động</span>
                                            </c:when>
                                            <c:when test="${u.status == 2}">
                                                <span class="order-status pending">Không hoạt động</span>
                                            </c:when>
                                            <c:when test="${u.status == 3}">
                                                <span class="order-status cancelled">Bị khóa</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="order-status">N/A</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${u.created_at}" pattern="dd/MM/yyyy HH:mm"/>
                                    </td>
                                    <td>
                                        <button class="btn-action btn-edit" onclick="viewUserDetail(${u.id})">
                                            <i class="fas fa-eye"></i> Xem
                                        </button>
                                        <c:if test="${u.status == 1}">
                                            <button class="btn-action btn-delete" onclick="banUser(${u.id})">
                                                <i class="fas fa-ban"></i> Khóa
                                            </button>
                                        </c:if>
                                        <c:if test="${u.status == 3}">
                                            <button class="btn-action btn-restore" onclick="unbanUser(${u.id})">
                                                <i class="fas fa-unlock"></i> Mở khóa
                                            </button>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    
                    <c:if test="${totalPages > 1}">
                        <div class="pagination">
                            <c:if test="${currentPage > 1}">
                                <a href="${pageContext.request.contextPath}/admin/users?page=${currentPage - 1}&status=${selectedStatus != null ? selectedStatus : ''}&search=${searchTerm != null ? searchTerm : ''}" class="pagination-link">
                                    <i class="fas fa-chevron-left"></i> Trước
                                </a>
                            </c:if>
                            
                            <div class="pagination-info">
                                Hiển thị ${(currentPage - 1) * 15 + 1} - ${currentPage * 15 > totalUsers ? totalUsers : currentPage * 15} trong tổng số ${totalUsers} người dùng
                            </div>
                            
                            <c:if test="${currentPage < totalPages}">
                                <a href="${pageContext.request.contextPath}/admin/users?page=${currentPage + 1}&status=${selectedStatus != null ? selectedStatus : ''}&search=${searchTerm != null ? searchTerm : ''}" class="pagination-link">
                                    Sau <i class="fas fa-chevron-right"></i>
                                </a>
                            </c:if>
                        </div>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <i class="fas fa-users"></i>
                        <p>Không tìm thấy người dùng nào</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </main>

    <div id="footer-placeholder"></div>

    <script>
        window.userData = {
            id: ${user.id},
            username: '${user.username}',
            email: '${user.email}',
            role: '${user.role}'
        };
    </script>
    <script src="${pageContext.request.contextPath}/js/layout.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script src="${pageContext.request.contextPath}/js/toast.js"></script>
    <script src="${pageContext.request.contextPath}/js/admin-users.js"></script>
</body>
</html>
