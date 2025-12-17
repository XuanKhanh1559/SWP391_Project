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
    <title>Chỉnh sửa nhà cung cấp - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div id="header-placeholder"></div>

    <main class="main-content">
        <div class="page-header">
            <h2><i class="fas fa-edit"></i> Chỉnh sửa nhà cung cấp</h2>
            <a href="${pageContext.request.contextPath}/admin/providers" class="btn btn-secondary">
                <i class="fas fa-arrow-left"></i> Quay lại
            </a>
        </div>

        <div class="form-container">
            <c:if test="${error != null}">
                <div class="alert alert-error">
                    <i class="fas fa-exclamation-circle"></i>
                    ${error}
                </div>
            </c:if>

            <form method="POST" action="${pageContext.request.contextPath}/admin/edit-provider" class="admin-form" accept-charset="UTF-8">
                <input type="hidden" name="id" value="${provider.id}">

                <div class="form-group">
                    <label for="code">Mã nhà cung cấp <span class="required">*</span></label>
                    <input type="text" id="code" name="code" class="form-control" 
                           placeholder="VD: VIETTEL" required value="${provider.code}">
                    <small class="form-text">Mã viết hoa, không dấu, không khoảng trắng</small>
                </div>

                <div class="form-group">
                    <label for="name">Tên nhà cung cấp <span class="required">*</span></label>
                    <input type="text" id="name" name="name" class="form-control" 
                           placeholder="VD: Viettel" required value="${provider.name}">
                </div>

                <div class="form-group">
                    <label for="description">Mô tả</label>
                    <textarea id="description" name="description" class="form-control" rows="4" 
                              placeholder="Nhập mô tả về nhà cung cấp...">${provider.description}</textarea>
                </div>

                <div class="form-group">
                    <label for="status">Trạng thái</label>
                    <select id="status" name="status" class="form-control">
                        <option value="1" ${provider.status == 1 ? 'selected' : ''}>Hoạt động</option>
                        <option value="0" ${provider.status == 0 ? 'selected' : ''}>Tạm dừng</option>
                    </select>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-save"></i> Cập nhật
                    </button>
                    <a href="${pageContext.request.contextPath}/admin/providers" class="btn btn-secondary">
                        <i class="fas fa-times"></i> Hủy
                    </a>
                </div>
            </form>
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
        document.getElementById('code').addEventListener('input', function(e) {
            this.value = this.value.toUpperCase().replace(/[^A-Z0-9]/g, '');
        });
    </script>
</body>
</html>

