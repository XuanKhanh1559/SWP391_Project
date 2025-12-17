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
                    <h3><i class="fas fa-shopping-bag"></i> Lịch sử đơn hàng 
                        <span class="badge badge-info">${totalOrders}</span>
                    </h3>
                    
                    <c:choose>
                        <c:when test="${empty orders}">
                            <p class="placeholder-text">Người dùng này chưa có đơn hàng nào</p>
                        </c:when>
                        <c:otherwise>
                            <div class="table-container">
                                <table class="data-table">
                                    <thead>
                                        <tr>
                                            <th>Mã đơn</th>
                                            <th>Ngày đặt</th>
                                            <th>Tổng tiền</th>
                                            <th>Trạng thái</th>
                                            <th>Thao tác</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="order" items="${orders}">
                                            <tr>
                                                <td>${order.order_code}</td>
                                                <td>
                                                    <fmt:formatDate value="${order.created_at}" pattern="dd/MM/yyyy HH:mm"/>
                                                </td>
                                                <td>
                                                    <strong style="color: #ff6b6b;">
                                                        <fmt:formatNumber value="${order.total_amount}" type="currency" currencyCode="VND" pattern="#,##0 đ"/>
                                                    </strong>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${order.status == 0}">
                                                            <span class="badge badge-warning">Đang xử lý</span>
                                                        </c:when>
                                                        <c:when test="${order.status == 1}">
                                                            <span class="badge badge-success">Hoàn thành</span>
                                                        </c:when>
                                                        <c:when test="${order.status == 2}">
                                                            <span class="badge badge-danger">Đã hủy</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge">N/A</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/admin/order-detail?id=${order.id}" 
                                                       class="btn btn-sm btn-primary">
                                                        <i class="fas fa-eye"></i>
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            
                            <c:if test="${totalOrderPages > 1}">
                                <div class="pagination">
                                    <c:if test="${orderPage > 1}">
                                        <a href="${pageContext.request.contextPath}/admin/user-detail?id=${viewUser.id}&orderPage=${orderPage - 1}&transactionPage=${transactionPage}" 
                                           class="pagination-btn">
                                            <i class="fas fa-chevron-left"></i> Trước
                                        </a>
                                    </c:if>
                                    
                                    <span class="pagination-info">
                                        Trang ${orderPage} / ${totalOrderPages}
                                    </span>
                                    
                                    <c:if test="${orderPage < totalOrderPages}">
                                        <a href="${pageContext.request.contextPath}/admin/user-detail?id=${viewUser.id}&orderPage=${orderPage + 1}&transactionPage=${transactionPage}" 
                                           class="pagination-btn">
                                            Sau <i class="fas fa-chevron-right"></i>
                                        </a>
                                    </c:if>
                                </div>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="activity-card">
                    <h3><i class="fas fa-history"></i> Lịch sử giao dịch 
                        <span class="badge badge-info">${totalTransactions}</span>
                    </h3>
                    
                    <c:choose>
                        <c:when test="${empty transactions}">
                            <p class="placeholder-text">Người dùng này chưa có giao dịch nào</p>
                        </c:when>
                        <c:otherwise>
                            <div class="table-container">
                                <table class="data-table">
                                    <thead>
                                        <tr>
                                            <th>Mã giao dịch</th>
                                            <th>Loại</th>
                                            <th>Số tiền</th>
                                            <th>Số dư trước</th>
                                            <th>Số dư sau</th>
                                            <th>Trạng thái</th>
                                            <th>Ngày giao dịch</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="transaction" items="${transactions}">
                                            <tr>
                                                <td>${transaction.transaction_code}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${transaction.type == 1}">
                                                            <span class="badge badge-success">
                                                                <i class="fas fa-arrow-down"></i> Nạp tiền
                                                            </span>
                                                        </c:when>
                                                        <c:when test="${transaction.type == 2}">
                                                            <span class="badge badge-danger">
                                                                <i class="fas fa-arrow-up"></i> Mua hàng
                                                            </span>
                                                        </c:when>
                                                        <c:when test="${transaction.type == 3}">
                                                            <span class="badge badge-info">
                                                                <i class="fas fa-undo"></i> Hoàn tiền
                                                            </span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge">Khác</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <strong style="color: ${transaction.type == 1 ? '#4CAF50' : '#ff6b6b'};">
                                                        <c:if test="${transaction.type == 2}">-</c:if>
                                                        <fmt:formatNumber value="${transaction.amount}" type="currency" currencyCode="VND" pattern="#,##0 đ"/>
                                                    </strong>
                                                </td>
                                                <td>
                                                    <fmt:formatNumber value="${transaction.balance_before}" type="currency" currencyCode="VND" pattern="#,##0 đ"/>
                                                </td>
                                                <td>
                                                    <strong>
                                                        <fmt:formatNumber value="${transaction.balance_after}" type="currency" currencyCode="VND" pattern="#,##0 đ"/>
                                                    </strong>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${transaction.status == 1}">
                                                            <span class="badge badge-success">Hoàn thành</span>
                                                        </c:when>
                                                        <c:when test="${transaction.status == 2}">
                                                            <span class="badge badge-danger">Thất bại</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge badge-warning">Đang xử lý</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <fmt:formatDate value="${transaction.created_at}" pattern="dd/MM/yyyy HH:mm"/>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            
                            <c:if test="${totalTransactionPages > 1}">
                                <div class="pagination">
                                    <c:if test="${transactionPage > 1}">
                                        <a href="${pageContext.request.contextPath}/admin/user-detail?id=${viewUser.id}&orderPage=${orderPage}&transactionPage=${transactionPage - 1}" 
                                           class="pagination-btn">
                                            <i class="fas fa-chevron-left"></i> Trước
                                        </a>
                                    </c:if>
                                    
                                    <span class="pagination-info">
                                        Trang ${transactionPage} / ${totalTransactionPages}
                                    </span>
                                    
                                    <c:if test="${transactionPage < totalTransactionPages}">
                                        <a href="${pageContext.request.contextPath}/admin/user-detail?id=${viewUser.id}&orderPage=${orderPage}&transactionPage=${transactionPage + 1}" 
                                           class="pagination-btn">
                                            Sau <i class="fas fa-chevron-right"></i>
                                        </a>
                                    </c:if>
                                </div>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
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

        const contextPath = window.location.pathname.split('/')[1] ? '/' + window.location.pathname.split('/')[1] : '';

        function banUser(userId) {
            showConfirm(
                'Bạn có chắc chắn muốn khóa tài khoản người dùng này?',
                function() {
                    fetch(contextPath + '/admin/ban-user', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: 'id=' + userId
                    })
                    .then(response => response.json())
                    .then(data => {
                        if (data.success) {
                            showToast(data.message, 'success');
                            setTimeout(() => {
                                window.location.reload();
                            }, 1500);
                        } else {
                            showToast(data.message, 'error');
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        showToast('Có lỗi xảy ra. Vui lòng thử lại.', 'error');
                    });
                }
            );
        }

        function unbanUser(userId) {
            showConfirm(
                'Bạn có chắc chắn muốn mở khóa tài khoản người dùng này?',
                function() {
                    fetch(contextPath + '/admin/ban-user', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: 'id=' + userId + '&action=unban'
                    })
                    .then(response => response.json())
                    .then(data => {
                        if (data.success) {
                            showToast(data.message, 'success');
                            setTimeout(() => {
                                window.location.reload();
                            }, 1500);
                        } else {
                            showToast(data.message, 'error');
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        showToast('Có lỗi xảy ra. Vui lòng thử lại.', 'error');
                    });
                }
            );
        }
    </script>
    <script src="${pageContext.request.contextPath}/js/layout.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script src="${pageContext.request.contextPath}/js/toast.js"></script>
</body>
</html>

