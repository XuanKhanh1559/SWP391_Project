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
    <title>Chi tiết người dùng - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div id="header-placeholder"></div>

    <main class="main-content">
        <div class="page-header">
            <h2>Chi tiết người dùng</h2>
            <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-secondary">
                <i class="fas fa-arrow-left"></i> Quay lại
            </a>
        </div>

        <div class="user-detail-container">
            <div class="user-info-card">
                <h3><i class="fas fa-user"></i> Thông tin cá nhân</h3>
                <div class="info-grid">
                    <div class="info-item">
                        <label>ID:</label>
                        <span>${viewUser.id}</span>
                    </div>
                    <div class="info-item">
                        <label>Username:</label>
                        <span>${viewUser.username}</span>
                    </div>
                    <div class="info-item">
                        <label>Email:</label>
                        <span>${viewUser.email}</span>
                    </div>
                    <div class="info-item">
                        <label>Số điện thoại:</label>
                        <span>${viewUser.phone != null ? viewUser.phone : 'Chưa cập nhật'}</span>
                    </div>
                    <div class="info-item">
                        <label>Số dư tài khoản:</label>
                        <span class="balance-amount">
                            <fmt:formatNumber value="${viewUser.balance}" type="currency" currencyCode="VND" pattern="#,##0 đ"/>
                        </span>
                    </div>
                    <div class="info-item">
                        <label>Vai trò:</label>
                        <span class="user-role">
                            <c:choose>
                                <c:when test="${viewUser.role == 'admin'}">
                                    <span class="badge badge-admin">Admin</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-user">User</span>
                                </c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                    <div class="info-item">
                        <label>Trạng thái:</label>
                        <span>
                            <c:choose>
                                <c:when test="${viewUser.status == 1}">
                                    <span class="order-status completed">Hoạt động</span>
                                </c:when>
                                <c:when test="${viewUser.status == 2}">
                                    <span class="order-status pending">Không hoạt động</span>
                                </c:when>
                                <c:when test="${viewUser.status == 3}">
                                    <span class="order-status cancelled">Bị khóa</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="order-status">N/A</span>
                                </c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                    <div class="info-item">
                        <label>Ngày tạo:</label>
                        <span>
                            <fmt:formatDate value="${viewUser.created_at}" pattern="dd/MM/yyyy HH:mm:ss"/>
                        </span>
                    </div>
                    <div class="info-item">
                        <label>Cập nhật lần cuối:</label>
                        <span>
                            <fmt:formatDate value="${viewUser.updated_at}" pattern="dd/MM/yyyy HH:mm:ss"/>
                        </span>
                    </div>
                </div>

                <div class="user-actions">
                    <c:if test="${viewUser.status == 1}">
                        <button class="btn btn-danger" onclick="banUser(${viewUser.id})">
                            <i class="fas fa-ban"></i> Khóa tài khoản
                        </button>
                    </c:if>
                    <c:if test="${viewUser.status == 3}">
                        <button class="btn btn-primary" onclick="unbanUser(${viewUser.id})">
                            <i class="fas fa-unlock"></i> Mở khóa tài khoản
                        </button>
                    </c:if>
                </div>
            </div>

            <div class="user-activity-section">
                <div class="activity-card">
                    <h3><i class="fas fa-shopping-bag"></i> Lịch sử đơn hàng</h3>
                    <p class="placeholder-text">Tính năng đang được phát triển</p>
                </div>

                <div class="activity-card">
                    <h3><i class="fas fa-history"></i> Lịch sử giao dịch</h3>
                    <p class="placeholder-text">Tính năng đang được phát triển</p>
                </div>
            </div>
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

        function banUser(userId) {
            showConfirm(
                'Bạn có chắc chắn muốn khóa tài khoản người dùng này?',
                function() {
                    showToast('Tính năng đang được phát triển', 'info');
                }
            );
        }

        function unbanUser(userId) {
            showConfirm(
                'Bạn có chắc chắn muốn mở khóa tài khoản người dùng này?',
                function() {
                    showToast('Tính năng đang được phát triển', 'info');
                }
            );
        }
    </script>
    <script src="${pageContext.request.contextPath}/js/layout.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script src="${pageContext.request.contextPath}/js/toast.js"></script>
</body>
</html>

