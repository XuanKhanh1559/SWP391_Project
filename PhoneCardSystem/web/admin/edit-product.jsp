<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <title>Chỉnh sửa sản phẩm - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div id="header-placeholder"></div>

    <main class="main-content">
        <div class="page-header">
            <h2>Chỉnh sửa sản phẩm</h2>
        </div>

        <div class="form-container">
            <c:if test="${error != null}">
                <div class="alert alert-error">
                    <i class="fas fa-exclamation-circle"></i>
                    ${error}
                </div>
            </c:if>

            <form method="POST" action="${pageContext.request.contextPath}/admin/edit-product" class="admin-form" accept-charset="UTF-8">
                <input type="hidden" name="id" value="${product.id}">
                
                <div class="form-group">
                    <label for="name">Tên sản phẩm <span class="required">*</span></label>
                    <input type="text" id="name" name="name" value="${product.name}" required>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="denomination">Mệnh giá <span class="required">*</span></label>
                        <input type="number" id="denomination" name="denomination" step="0.01" value="${product.denomination}" required>
                    </div>

                    <div class="form-group">
                        <label for="price">Giá bán <span class="required">*</span></label>
                        <input type="number" id="price" name="price" step="0.01" value="${product.price}" required>
                    </div>
                </div>

                <div class="form-group">
                    <label for="status">Trạng thái <span class="required">*</span></label>
                    <select id="status" name="status" required>
                        <option value="active" ${product.status == 1 ? 'selected' : ''}>Hoạt động</option>
                        <option value="inactive" ${product.status != 1 ? 'selected' : ''}>Tạm dừng</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="description">Mô tả</label>
                    <textarea id="description" name="description" rows="4">${product.description}</textarea>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-save"></i> Lưu thay đổi
                    </button>
                    <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-secondary">
                        <i class="fas fa-times"></i> Hủy
                    </a>
                </div>
            </form>
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
</body>
</html>

