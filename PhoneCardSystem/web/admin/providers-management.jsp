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
    <title>Quản lý nhà cung cấp - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div id="header-placeholder"></div>

    <main class="main-content">
        <div class="page-header">
            <h2><i class="fas fa-building"></i> Quản lý nhà cung cấp</h2>
            <a href="${pageContext.request.contextPath}/admin/create-provider" class="btn btn-primary">
                <i class="fas fa-plus"></i> Thêm nhà cung cấp
            </a>
        </div>
        
        <c:if test="${sessionScope.successMessage != null}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle"></i>
                ${sessionScope.successMessage}
            </div>
            <c:remove var="successMessage" scope="session"/>
        </c:if>
        
        <form method="GET" action="${pageContext.request.contextPath}/admin/providers" id="filterForm">
            <div class="filter-bar">
                <select name="status" id="filterStatus" class="filter-select">
                    <option value="">Tất cả trạng thái</option>
                    <option value="1" ${selectedStatus == '1' ? 'selected' : ''}>Hoạt động</option>
                    <option value="0" ${selectedStatus == '0' ? 'selected' : ''}>Tạm dừng</option>
                </select>
                <select name="deleted" id="filterDeleted" class="filter-select">
                    <option value="">Đang hoạt động</option>
                    <option value="1" ${selectedDeleted == '1' ? 'selected' : ''}>Đã xóa</option>
                </select>
                <input type="text" name="search" id="searchProvider" class="search-input" placeholder="Tìm kiếm nhà cung cấp..." value="${searchTerm != null ? searchTerm : ''}">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-search"></i> Tìm kiếm
                </button>
            </div>
        </form>

        <div class="admin-table-container">
            <c:choose>
                <c:when test="${providers != null && !providers.isEmpty()}">
                    <table class="admin-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Mã</th>
                                <th>Tên nhà cung cấp</th>
                                <th>Mô tả</th>
                                <th>Trạng thái</th>
                                <th>Ngày tạo</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="provider" items="${providers}">
                                <tr id="provider-${provider.id}">
                                    <td>${provider.id}</td>
                                    <td><strong>${provider.code}</strong></td>
                                    <td>${provider.name}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${provider.description != null && !provider.description.trim().isEmpty()}">
                                                ${provider.description.length() > 50 ? provider.description.substring(0, 50) + '...' : provider.description}
                                            </c:when>
                                            <c:otherwise>
                                                <span style="color: #999;">-</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${provider.status == 1}">
                                                <span class="badge badge-success">
                                                    <i class="fas fa-check-circle"></i> Hoạt động
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-secondary">
                                                    <i class="fas fa-pause-circle"></i> Tạm dừng
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${provider.created_at}" pattern="dd/MM/yyyy HH:mm"/>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${provider.deleted == 0}">
                                                <a href="${pageContext.request.contextPath}/admin/edit-provider?id=${provider.id}" class="btn btn-sm btn-primary" title="Chỉnh sửa">
                                                    <i class="fas fa-edit"></i>
                                                </a>
                                                <button onclick="deleteProvider(${provider.id})" class="btn btn-sm btn-danger" title="Xóa">
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                            </c:when>
                                            <c:otherwise>
                                                <button onclick="restoreProvider(${provider.id})" class="btn btn-sm btn-success" title="Khôi phục">
                                                    <i class="fas fa-undo"></i> Khôi phục
                                                </button>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    
                    <c:if test="${totalPages > 1}">
                        <div class="pagination">
                            <c:if test="${currentPage > 1}">
                                <a href="?page=${currentPage - 1}&status=${selectedStatus != null ? selectedStatus : ''}&deleted=${selectedDeleted != null ? selectedDeleted : ''}&search=${searchTerm != null ? searchTerm : ''}" class="pagination-btn">
                                    <i class="fas fa-chevron-left"></i> Trước
                                </a>
                            </c:if>
                            
                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <c:choose>
                                    <c:when test="${i == currentPage}">
                                        <span class="pagination-btn active">${i}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="?page=${i}&status=${selectedStatus != null ? selectedStatus : ''}&deleted=${selectedDeleted != null ? selectedDeleted : ''}&search=${searchTerm != null ? searchTerm : ''}" class="pagination-btn">${i}</a>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                            
                            <c:if test="${currentPage < totalPages}">
                                <a href="?page=${currentPage + 1}&status=${selectedStatus != null ? selectedStatus : ''}&deleted=${selectedDeleted != null ? selectedDeleted : ''}&search=${searchTerm != null ? searchTerm : ''}" class="pagination-btn">
                                    Sau <i class="fas fa-chevron-right"></i>
                                </a>
                            </c:if>
                        </div>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <i class="fas fa-building" style="font-size: 64px; color: #ddd;"></i>
                        <h3>Không tìm thấy nhà cung cấp</h3>
                        <p>Không có nhà cung cấp nào phù hợp với bộ lọc của bạn</p>
                        <a href="${pageContext.request.contextPath}/admin/create-provider" class="btn btn-primary">
                            <i class="fas fa-plus"></i> Thêm nhà cung cấp mới
                        </a>
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
    <script src="${pageContext.request.contextPath}/js/toast.js"></script>
    <script>
        const contextPath = '${pageContext.request.contextPath}';
        
        document.getElementById('filterStatus').addEventListener('change', function() {
            document.getElementById('filterForm').submit();
        });
        
        document.getElementById('filterDeleted').addEventListener('change', function() {
            document.getElementById('filterForm').submit();
        });

        function deleteProvider(id) {
            showConfirm(
                'Bạn có chắc chắn muốn xóa nhà cung cấp này? Các sản phẩm của nhà cung cấp này sẽ bị ẩn và không thể mua.',
                function() {
                    fetch(contextPath + '/admin/delete-provider', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: 'id=' + id
                    })
                    .then(response => response.json())
                    .then(data => {
                        if (data.success) {
                            showToast(data.message, 'success');
                            setTimeout(() => location.reload(), 1500);
                        } else {
                            showToast(data.message, 'error');
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        showToast('Có lỗi xảy ra khi xóa nhà cung cấp', 'error');
                    });
                },
                null,
                'Xác nhận xóa'
            );
        }

        function restoreProvider(id) {
            showConfirm(
                'Bạn có chắc chắn muốn khôi phục nhà cung cấp này? Các sản phẩm của nhà cung cấp này sẽ hiển thị trở lại.',
                function() {
                    fetch(contextPath + '/admin/restore-provider', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: 'id=' + id
                    })
                    .then(response => response.json())
                    .then(data => {
                        if (data.success) {
                            showToast(data.message, 'success');
                            setTimeout(() => location.reload(), 1500);
                        } else {
                            showToast(data.message, 'error');
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        showToast('Có lỗi xảy ra khi khôi phục nhà cung cấp', 'error');
                    });
                },
                null,
                'Xác nhận khôi phục'
            );
        }
    </script>
</body>
</html>

