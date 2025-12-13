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
    <title>Quản lý sản phẩm - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div id="header-placeholder"></div>

    <main class="main-content">
        <div class="page-header">
            <h2>Quản lý sản phẩm</h2>
            <button class="btn btn-primary" id="addProductBtn">
                <i class="fas fa-plus"></i> Thêm sản phẩm
            </button>
        </div>
        
        <c:if test="${sessionScope.successMessage != null}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle"></i>
                ${sessionScope.successMessage}
            </div>
            <c:remove var="successMessage" scope="session"/>
        </c:if>
        
        <form method="GET" action="${pageContext.request.contextPath}/admin/products" id="filterForm">
            <div class="filter-bar">
                <select name="provider" id="filterProvider" class="filter-select">
                    <option value="">Tất cả nhà mạng</option>
                    <option value="1" ${selectedProvider == 1 ? 'selected' : ''}>Viettel</option>
                    <option value="2" ${selectedProvider == 2 ? 'selected' : ''}>Vinaphone</option>
                    <option value="3" ${selectedProvider == 3 ? 'selected' : ''}>Mobifone</option>
                </select>
                <select name="type" id="filterType" class="filter-select">
                    <option value="">Tất cả loại</option>
                    <option value="topup" ${selectedType == 'topup' ? 'selected' : ''}>Nạp tiền</option>
                    <option value="data_package" ${selectedType == 'data_package' ? 'selected' : ''}>Gói data</option>
                </select>
                <select name="status" id="filterStatus" class="filter-select">
                    <option value="">Tất cả trạng thái</option>
                    <option value="active" ${selectedStatus == 'active' ? 'selected' : ''}>Hoạt động</option>
                    <option value="inactive" ${selectedStatus == 'inactive' ? 'selected' : ''}>Tạm dừng</option>
                </select>
                <input type="text" name="search" id="searchProduct" class="search-input" placeholder="Tìm kiếm sản phẩm..." value="${searchTerm != null ? searchTerm : ''}">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-search"></i> Tìm kiếm
                </button>
            </div>
        </form>

        <div class="admin-table-container">
            <c:choose>
                <c:when test="${products != null && !products.isEmpty()}">
                    <table class="admin-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Tên sản phẩm</th>
                                <th>Nhà mạng</th>
                                <th>Loại</th>
                                <th>Mệnh giá</th>
                                <th>Giá bán</th>
                                <th>Trạng thái</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="product" items="${products}">
                                <tr>
                                    <td>${product.id}</td>
                                    <td>${product.name}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${product.provider_id == 1}">Viettel</c:when>
                                            <c:when test="${product.provider_id == 2}">Vinaphone</c:when>
                                            <c:when test="${product.provider_id == 3}">Mobifone</c:when>
                                            <c:otherwise>N/A</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${product.type == 1}">Nạp tiền</c:when>
                                            <c:when test="${product.type == 2}">Gói data</c:when>
                                            <c:otherwise>N/A</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <fmt:formatNumber value="${product.denomination}" type="currency" currencyCode="VND" pattern="#,##0 đ"/>
                                    </td>
                                    <td>
                                        <fmt:formatNumber value="${product.price}" type="currency" currencyCode="VND" pattern="#,##0 đ"/>
                                    </td>
                                    <td>
                                        <span class="order-status ${product.status == 1 ? 'completed' : 'cancelled'}">
                                            ${product.status == 1 ? 'Hoạt động' : 'Tạm dừng'}
                                        </span>
                                    </td>
                                    <td>
                                        <button class="btn-action btn-edit" onclick="editProduct(${product.id})">
                                            <i class="fas fa-edit"></i> Sửa
                                        </button>
                                        <button class="btn-action btn-delete" onclick="deleteProduct(${product.id})">
                                            <i class="fas fa-trash"></i> Xóa
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    
                    <c:if test="${totalPages > 1}">
                        <div class="pagination">
                            <c:if test="${currentPage > 1}">
                                <a href="${pageContext.request.contextPath}/admin/products?page=${currentPage - 1}&provider=${selectedProvider != null ? selectedProvider : ''}&type=${selectedType != null ? selectedType : ''}&status=${selectedStatus != null ? selectedStatus : ''}&search=${searchTerm != null ? searchTerm : ''}" class="pagination-link">
                                    <i class="fas fa-chevron-left"></i> Trước
                                </a>
                            </c:if>
                            
                            <div class="pagination-info">
                                Hiển thị ${(currentPage - 1) * 15 + 1} - ${currentPage * 15 > totalProducts ? totalProducts : currentPage * 15} trong tổng số ${totalProducts} sản phẩm
                            </div>
                            
                            <c:if test="${currentPage < totalPages}">
                                <a href="${pageContext.request.contextPath}/admin/products?page=${currentPage + 1}&provider=${selectedProvider != null ? selectedProvider : ''}&type=${selectedType != null ? selectedType : ''}&status=${selectedStatus != null ? selectedStatus : ''}&search=${searchTerm != null ? searchTerm : ''}" class="pagination-link">
                                    Sau <i class="fas fa-chevron-right"></i>
                                </a>
                            </c:if>
                        </div>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <i class="fas fa-box-open"></i>
                        <p>Không tìm thấy sản phẩm nào</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </main>

    <div id="footer-placeholder"></div>

    <script>
        window.userData = {
            id: <%= user.getId() %>,
            username: '<c:out value="${user.username}" escapeXml="true"/>',
            email: '<c:out value="${user.email}" escapeXml="true"/>',
            role: '<c:out value="${user.role}" escapeXml="true"/>'
        };
    </script>
    <script src="${pageContext.request.contextPath}/js/layout.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script src="${pageContext.request.contextPath}/js/toast.js"></script>
    <script src="${pageContext.request.contextPath}/js/admin-products.js"></script>
</body>
</html>
