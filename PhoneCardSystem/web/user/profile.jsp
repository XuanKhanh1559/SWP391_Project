<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hồ sơ - Hệ Thống Bán Thẻ Điện Thoại</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <%
        User user = null;
        if (request.getAttribute("user") != null) {
            user = (User) request.getAttribute("user");
        } else {
            HttpSession userSession = request.getSession(false);
            if (userSession != null && userSession.getAttribute("user") != null) {
                user = (User) userSession.getAttribute("user");
            }
        }
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
    %>
    <div id="header-placeholder"></div>

    <!-- Main Content -->
    <main class="main-content">
        <div class="profile-container">
            <div class="profile-sidebar">
                <div class="profile-avatar">
                    <i class="fas fa-user-circle"></i>
                </div>
                <h3 id="profileUserName"><%= user.getUsername() %></h3>
                <div class="profile-balance">
                    <span>Số dư:</span>
                    <span class="balance-amount" id="profileBalance">
                        <%= new java.text.DecimalFormat("#,##0.00").format(user.getBalance()) %> đ
                    </span>
                </div>
                <nav class="profile-nav">
                    <a href="#profileInfo" id="navProfileInfo">Thông tin cá nhân</a>
                    <a href="#changePassword" id="navChangePassword">Đổi mật khẩu</a>
                    <a href="#transactions" id="navTransactions">Giao dịch</a>
                </nav>
            </div>
            <div class="profile-content">
                <%
                    String activeTab = (String) request.getAttribute("activeTab");
                    if (activeTab == null) {
                        activeTab = "profileInfo";
                    }
                %>
                <div id="profileInfo" class="profile-section <%= "profileInfo".equals(activeTab) ? "active" : "" %>">
                    <h2>Thông tin cá nhân</h2>
                    <% if (request.getAttribute("profileError") != null) { %>
                        <div class="alert alert-error" style="color: red; margin-bottom: 1rem; padding: 0.75rem; background-color: #fee; border: 1px solid #fcc; border-radius: 4px;">
                            <%= request.getAttribute("profileError") %>
                        </div>
                    <% } %>
                    <% if (request.getAttribute("profileSuccess") != null) { %>
                        <div class="alert alert-success" style="color: green; margin-bottom: 1rem; padding: 0.75rem; background-color: #efe; border: 1px solid #cfc; border-radius: 4px;">
                            <%= request.getAttribute("profileSuccess") %>
                        </div>
                    <% } %>
                    <form id="profileForm" action="${pageContext.request.contextPath}/profile" method="POST">
                        <div class="form-group">
                            <label>Username</label>
                            <input type="text" id="profileUsername" name="username" 
                                   value="<%= user.getUsername() %>" 
                                   readonly>
                        </div>
                        <div class="form-group">
                            <label>Email</label>
                            <input type="email" id="profileEmail" name="email" 
                                   value="<%= user.getEmail() != null ? user.getEmail() : "" %>" 
                                   required>
                        </div>
                        <div class="form-group">
                            <label>Số điện thoại</label>
                            <input type="tel" id="profilePhone" name="phone" 
                                   value="<%= user.getPhone() != null ? user.getPhone() : "" %>"
                                   pattern="^0[0-9]{9,10}$"
                                   title="Số điện thoại phải bắt đầu bằng 0 và có 10-11 chữ số"
                                   placeholder="0xxxxxxxxx">
                            <small style="color: #666; font-size: 0.85rem;">Số điện thoại phải bắt đầu bằng 0 (10-11 chữ số)</small>
                        </div>
                        <button type="submit" class="btn btn-primary">Cập nhật</button>
                    </form>
                </div>
                <div id="changePassword" class="profile-section <%= "changePassword".equals(activeTab) ? "active" : "" %>">
                    <h2>Đổi mật khẩu</h2>
                    <% if (request.getAttribute("changePasswordError") != null) { %>
                        <div class="alert alert-error" style="color: red; margin-bottom: 1rem; padding: 0.75rem; background-color: #fee; border: 1px solid #fcc; border-radius: 4px;">
                            <%= request.getAttribute("changePasswordError") %>
                        </div>
                    <% } %>
                    <% if (request.getAttribute("changePasswordSuccess") != null) { %>
                        <div class="alert alert-success" style="color: green; margin-bottom: 1rem; padding: 0.75rem; background-color: #efe; border: 1px solid #cfc; border-radius: 4px;">
                            <%= request.getAttribute("changePasswordSuccess") %>
                        </div>
                    <% } %>
                    <form id="changePasswordForm" action="${pageContext.request.contextPath}/change-password" method="POST">
                        <div class="form-group">
                            <label>Mật khẩu hiện tại</label>
                            <div style="position: relative;">
                                <input type="password" id="currentPassword" name="currentPassword" required style="padding-right: 40px;">
                                <i class="fas fa-eye" id="toggleCurrentPassword" style="position: absolute; right: 12px; top: 50%; transform: translateY(-50%); cursor: pointer; color: #666;"></i>
                            </div>
                        </div>
                        <div class="form-group">
                            <label>Mật khẩu mới</label>
                            <div style="position: relative;">
                                <input type="password" id="newPassword" name="newPassword" required style="padding-right: 40px;">
                                <i class="fas fa-eye" id="toggleNewPassword" style="position: absolute; right: 12px; top: 50%; transform: translateY(-50%); cursor: pointer; color: #666;"></i>
                            </div>
                        </div>
                        <div class="form-group">
                            <label>Xác nhận mật khẩu mới</label>
                            <div style="position: relative;">
                                <input type="password" id="confirmPassword" name="confirmPassword" required style="padding-right: 40px;">
                                <i class="fas fa-eye" id="toggleConfirmPassword" style="position: absolute; right: 12px; top: 50%; transform: translateY(-50%); cursor: pointer; color: #666;"></i>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary">Đổi mật khẩu</button>
                    </form>
                </div>
                <div id="transactions" class="profile-section <%= "transactions".equals(activeTab) ? "active" : "" %>">
                    <h2>Lịch sử giao dịch</h2>
                    <div class="transactions-list" id="transactionsList">
                    </div>
                </div>
            </div>
        </div>
    </main>

    <!-- Footer Placeholder -->
    <div id="footer-placeholder"></div>

    <script>
        window.userData = null;
        <% if (user != null) { %>
            window.userData = {
                id: <%= user.getId() %>,
                username: '<%= user.getUsername().replace("'", "\\'") %>',
                email: '<%= user.getEmail().replace("'", "\\'") %>',
                role: '<%= user.getRole() != null ? user.getRole().replace("'", "\\'") : "user" %>',
                phone: '<%= user.getPhone() != null ? user.getPhone().replace("'", "\\'") : "" %>',
                balance: <%= user.getBalance() %>
            };
        <% } %>
        
        document.addEventListener('DOMContentLoaded', function() {
            const activeTab = '<%= activeTab != null ? activeTab : "profileInfo" %>';
            const navLinks = {
                'profileInfo': document.getElementById('navProfileInfo'),
                'changePassword': document.getElementById('navChangePassword'),
                'transactions': document.getElementById('navTransactions')
            };
            
            Object.keys(navLinks).forEach(tab => {
                if (navLinks[tab]) {
                    if (tab === activeTab) {
                        navLinks[tab].classList.add('active');
                    } else {
                        navLinks[tab].classList.remove('active');
                    }
                }
            });
        });
    </script>
    <script src="${pageContext.request.contextPath}/js/layout.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script src="${pageContext.request.contextPath}/js/profile.js"></script>
    <script>
        const toggleCurrentPassword = document.getElementById('toggleCurrentPassword');
        const currentPassword = document.getElementById('currentPassword');
        const toggleNewPassword = document.getElementById('toggleNewPassword');
        const newPassword = document.getElementById('newPassword');
        const toggleConfirmPassword = document.getElementById('toggleConfirmPassword');
        const confirmPassword = document.getElementById('confirmPassword');
        
        if (toggleCurrentPassword && currentPassword) {
            toggleCurrentPassword.addEventListener('click', function() {
                const type = currentPassword.getAttribute('type') === 'password' ? 'text' : 'password';
                currentPassword.setAttribute('type', type);
                toggleCurrentPassword.classList.toggle('fa-eye');
                toggleCurrentPassword.classList.toggle('fa-eye-slash');
            });
        }
        
        if (toggleNewPassword && newPassword) {
            toggleNewPassword.addEventListener('click', function() {
                const type = newPassword.getAttribute('type') === 'password' ? 'text' : 'password';
                newPassword.setAttribute('type', type);
                toggleNewPassword.classList.toggle('fa-eye');
                toggleNewPassword.classList.toggle('fa-eye-slash');
            });
        }
        
        if (toggleConfirmPassword && confirmPassword) {
            toggleConfirmPassword.addEventListener('click', function() {
                const type = confirmPassword.getAttribute('type') === 'password' ? 'text' : 'password';
                confirmPassword.setAttribute('type', type);
                toggleConfirmPassword.classList.toggle('fa-eye');
                toggleConfirmPassword.classList.toggle('fa-eye-slash');
            });
        }
        
        // Validate phone number
        const profileForm = document.getElementById('profileForm');
        const profilePhone = document.getElementById('profilePhone');
        
        if (profileForm && profilePhone) {
            profileForm.addEventListener('submit', function(e) {
                const phone = profilePhone.value.trim();
                if (phone && !phone.startsWith('0')) {
                    e.preventDefault();
                    if (window.showToast) {
                        showToast('Số điện thoại phải bắt đầu bằng 0', 'error');
                    } else {
                        alert('Số điện thoại phải bắt đầu bằng 0');
                    }
                    profilePhone.focus();
                    return false;
                }
                if (phone && !/^0[0-9]{9,10}$/.test(phone)) {
                    e.preventDefault();
                    if (window.showToast) {
                        showToast('Số điện thoại không hợp lệ. Phải bắt đầu bằng 0 và có 10-11 chữ số', 'error');
                    } else {
                        alert('Số điện thoại không hợp lệ. Phải bắt đầu bằng 0 và có 10-11 chữ số');
                    }
                    profilePhone.focus();
                    return false;
                }
            });
        }
    </script>
    <script src="${pageContext.request.contextPath}/js/toast.js"></script>
</body>
</html>

