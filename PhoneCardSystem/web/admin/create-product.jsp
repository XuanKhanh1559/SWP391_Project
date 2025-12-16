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
    <title>Thêm sản phẩm mới - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div id="header-placeholder"></div>

    <main class="main-content">
        <div class="page-header">
            <h2>Thêm sản phẩm mới</h2>
        </div>

        <div class="form-container">
            <c:if test="${error != null}">
                <div class="alert alert-error">
                    <i class="fas fa-exclamation-circle"></i>
                    ${error}
                </div>
            </c:if>

            <form method="POST" action="${pageContext.request.contextPath}/admin/create-product" class="admin-form">
                <div class="form-group">
                    <label for="providerId">Nhà mạng <span class="required">*</span></label>
                    <select id="providerId" name="providerId" required>
                        <option value="">Chọn nhà mạng</option>
                        <c:forEach var="provider" items="${providers}">
                            <option value="${provider.id}">${provider.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="name">Tên sản phẩm <span class="required">*</span></label>
                    <input type="text" id="name" name="name" required placeholder="Ví dụ: Thẻ nạp Viettel 50.000đ">
                </div>

                <div class="form-group">
                    <label for="type">Loại sản phẩm <span class="required">*</span></label>
                    <select id="type" name="type" required>
                        <option value="">Chọn loại sản phẩm</option>
                        <option value="1">Nạp tiền</option>
                        <option value="2">Gói data</option>
                    </select>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="denomination">Mệnh giá <span class="required">*</span></label>
                        <input type="number" id="denomination" name="denomination" step="0.01" min="0" required placeholder="50000">
                    </div>

                    <div class="form-group">
                        <label for="price">Giá bán <span class="required">*</span></label>
                        <input type="number" id="price" name="price" step="0.01" min="0" required placeholder="50000">
                    </div>
                </div>

                <div class="form-group">
                    <label for="status">Trạng thái <span class="required">*</span></label>
                    <select id="status" name="status" required>
                        <option value="active" selected>Hoạt động</option>
                        <option value="inactive">Tạm dừng</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="description">Mô tả</label>
                    <textarea id="description" name="description" rows="4" placeholder="Nhập mô tả sản phẩm (tùy chọn)"></textarea>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-save"></i> Tạo sản phẩm
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

