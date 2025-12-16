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
    <title>Thống kê hệ thống - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.0/dist/chart.umd.min.js"></script>
    <style>
        .stats-tabs {
            display: flex;
            gap: 1rem;
            margin-bottom: 2rem;
            border-bottom: 2px solid #e0e0e0;
        }
        
        .stats-tab {
            padding: 1rem 2rem;
            cursor: pointer;
            border: none;
            background: none;
            font-size: 1rem;
            font-weight: 500;
            color: #666;
            position: relative;
            transition: all 0.3s;
        }
        
        .stats-tab:hover {
            color: #4a90e2;
        }
        
        .stats-tab.active {
            color: #4a90e2;
        }
        
        .stats-tab.active::after {
            content: '';
            position: absolute;
            bottom: -2px;
            left: 0;
            right: 0;
            height: 2px;
            background: #4a90e2;
        }
        
        .stats-content {
            display: none;
        }
        
        .stats-content.active {
            display: block;
        }
        
        .stats-filters {
            display: flex;
            gap: 1rem;
            margin-bottom: 2rem;
            flex-wrap: wrap;
            align-items: center;
        }
        
        .stats-filters select, .stats-filters input {
            padding: 0.5rem 1rem;
            border: 1px solid #ddd;
            border-radius: 8px;
            font-size: 0.9rem;
        }
        
        .stats-summary {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1.5rem;
            margin-bottom: 2rem;
        }
        
        .summary-card {
            background: white;
            padding: 0.2px 0.5rem;
            border-radius: 12px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            border-left: 4px solid #4a90e2;
        }
        
        .summary-card h3 {
            font-size: 0.9rem;
            color: #666;
            margin: 0 0 0.5rem 0;
        }
        
        .summary-card .value {
            font-size: 20px;
            font-weight: bold;
            color: #2c3e50;
        }
        
        .chart-container {
            background: white;
            padding: 2rem;
            border-radius: 12px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            margin-bottom: 2rem;
        }
        
        .chart-wrapper {
            position: relative;
            height: 400px;
        }
    </style>
</head>
<body>
    <div id="header-placeholder"></div>

    <main class="main-content">
        <div class="page-header">
            <h2><i class="fas fa-chart-line"></i> Thống kê hệ thống</h2>
        </div>

        <div class="stats-tabs">
            <button class="stats-tab active" onclick="switchTab('revenue')">
                <i class="fas fa-money-bill-wave"></i> Doanh thu
            </button>
            <button class="stats-tab" onclick="switchTab('users')">
                <i class="fas fa-users"></i> Người dùng mới
            </button>
        </div>

        <!-- Revenue Statistics -->
        <div id="revenue-tab" class="stats-content active">
            <div class="stats-filters">
                <select id="revenue-period" onchange="loadRevenueStats()">
                    <option value="day">Theo ngày (trong tháng)</option>
                    <option value="week">Theo tuần (3 tháng gần nhất)</option>
                    <option value="month" selected>Theo tháng (trong năm)</option>
                </select>
                
                <select id="revenue-year" onchange="loadRevenueStats()">
                    <option value="2024">2024</option>
                    <option value="2025" selected>2025</option>
                    <option value="2026">2026</option>
                </select>
                
                <select id="revenue-month" onchange="loadRevenueStats()" style="display:none;">
                    <option value="1">Tháng 1</option>
                    <option value="2">Tháng 2</option>
                    <option value="3">Tháng 3</option>
                    <option value="4">Tháng 4</option>
                    <option value="5">Tháng 5</option>
                    <option value="6">Tháng 6</option>
                    <option value="7">Tháng 7</option>
                    <option value="8">Tháng 8</option>
                    <option value="9">Tháng 9</option>
                    <option value="10">Tháng 10</option>
                    <option value="11">Tháng 11</option>
                    <option value="12">Tháng 12</option>
                </select>
            </div>

            <div class="stats-summary">
                <div class="summary-card">
                    <h3>Tổng doanh thu</h3>
                    <div class="value" id="total-revenue">0 đ</div>
                </div>
            </div>

            <div class="chart-container">
                <h3 style="margin-bottom: 1.5rem;">Biểu đồ doanh thu</h3>
                <div class="chart-wrapper">
                    <canvas id="revenue-chart"></canvas>
                </div>
            </div>
        </div>

        <!-- User Statistics -->
        <div id="users-tab" class="stats-content">
            <div class="stats-filters">
                <select id="user-period" onchange="loadUserStats()">
                    <option value="day">Theo ngày (trong tháng)</option>
                    <option value="week">Theo tuần (3 tháng gần nhất)</option>
                    <option value="month" selected>Theo tháng (trong năm)</option>
                </select>
                
                <select id="user-year" onchange="loadUserStats()">
                    <option value="2024">2024</option>
                    <option value="2025" selected>2025</option>
                    <option value="2026">2026</option>
                </select>
                
                <select id="user-month" onchange="loadUserStats()" style="display:none;">
                    <option value="1">Tháng 1</option>
                    <option value="2">Tháng 2</option>
                    <option value="3">Tháng 3</option>
                    <option value="4">Tháng 4</option>
                    <option value="5">Tháng 5</option>
                    <option value="6">Tháng 6</option>
                    <option value="7">Tháng 7</option>
                    <option value="8">Tháng 8</option>
                    <option value="9">Tháng 9</option>
                    <option value="10">Tháng 10</option>
                    <option value="11">Tháng 11</option>
                    <option value="12">Tháng 12</option>
                </select>
            </div>

            <div class="stats-summary">
                <div class="summary-card">
                    <h3>Người dùng mới trong kỳ</h3>
                    <div class="value" id="new-users">0</div>
                </div>
            </div>

            <div class="chart-container">
                <h3 style="margin-bottom: 1.5rem;">Biểu đồ người dùng mới</h3>
                <div class="chart-wrapper">
                    <canvas id="user-chart"></canvas>
                </div>
            </div>
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
        
        const contextPath = '${pageContext.request.contextPath}';
        let revenueChart = null;
        let userChart = null;
        
        function switchTab(tabName) {
            document.querySelectorAll('.stats-tab').forEach(tab => tab.classList.remove('active'));
            document.querySelectorAll('.stats-content').forEach(content => content.classList.remove('active'));
            
            event.target.closest('.stats-tab').classList.add('active');
            document.getElementById(tabName + '-tab').classList.add('active');
            
            if (tabName === 'revenue') {
                loadRevenueStats();
            } else {
                loadUserStats();
            }
        }
        
        document.getElementById('revenue-period').addEventListener('change', function() {
            const monthSelect = document.getElementById('revenue-month');
            if (this.value === 'day') {
                monthSelect.style.display = 'block';
            } else if (this.value === 'week') {
                monthSelect.style.display = 'none';
            } else {
                monthSelect.style.display = 'none';
            }
        });
        
        document.getElementById('user-period').addEventListener('change', function() {
            const monthSelect = document.getElementById('user-month');
            if (this.value === 'day') {
                monthSelect.style.display = 'block';
            } else if (this.value === 'week') {
                monthSelect.style.display = 'none';
            } else {
                monthSelect.style.display = 'none';
            }
        });
        
        function loadRevenueStats() {
            const period = document.getElementById('revenue-period').value;
            const year = document.getElementById('revenue-year').value;
            const month = document.getElementById('revenue-month').value;
            
            const url = contextPath + '/admin/statistics?action=getRevenue&period=' + period + 
                       '&year=' + year + '&month=' + month;
            
            fetch(url)
                .then(response => response.json())
                .then(data => {
                    if (data.error) {
                        console.error('Error:', data.error);
                        return;
                    }
                    
                    document.getElementById('total-revenue').textContent = 
                        new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(data.total);
                    
                    if (revenueChart) {
                        revenueChart.destroy();
                    }
                    
                    const ctx = document.getElementById('revenue-chart').getContext('2d');
                    revenueChart = new Chart(ctx, {
                        type: 'bar',
                        data: {
                            labels: data.labels,
                            datasets: [{
                                label: 'Doanh thu (VNĐ)',
                                data: data.data,
                                backgroundColor: 'rgba(74, 144, 226, 0.8)',
                                borderColor: 'rgba(74, 144, 226, 1)',
                                borderWidth: 1
                            }]
                        },
                        options: {
                            responsive: true,
                            maintainAspectRatio: false,
                            plugins: {
                                legend: {
                                    display: false
                                },
                                tooltip: {
                                    callbacks: {
                                        label: function(context) {
                                            return new Intl.NumberFormat('vi-VN', { 
                                                style: 'currency', 
                                                currency: 'VND' 
                                            }).format(context.parsed.y);
                                        }
                                    }
                                }
                            },
                            scales: {
                                y: {
                                    beginAtZero: true,
                                    ticks: {
                                        callback: function(value) {
                                            return new Intl.NumberFormat('vi-VN').format(value) + ' đ';
                                        }
                                    }
                                }
                            }
                        }
                    });
                })
                .catch(error => console.error('Error:', error));
        }
        
        function loadUserStats() {
            const period = document.getElementById('user-period').value;
            const year = document.getElementById('user-year').value;
            const month = document.getElementById('user-month').value;
            
            const url = contextPath + '/admin/statistics?action=getUsers&period=' + period + 
                       '&year=' + year + '&month=' + month;
            
            fetch(url)
                .then(response => response.json())
                .then(data => {
                    if (data.error) {
                        console.error('Error:', data.error);
                        return;
                    }
                    
                    document.getElementById('new-users').textContent = data.total;
                    
                    if (userChart) {
                        userChart.destroy();
                    }
                    
                    const ctx = document.getElementById('user-chart').getContext('2d');
                    userChart = new Chart(ctx, {
                        type: 'bar',
                        data: {
                            labels: data.labels,
                            datasets: [{
                                label: 'Người dùng mới',
                                data: data.data,
                                backgroundColor: 'rgba(80, 200, 120, 0.8)',
                                borderColor: 'rgba(80, 200, 120, 1)',
                                borderWidth: 1
                            }]
                        },
                        options: {
                            responsive: true,
                            maintainAspectRatio: false,
                            plugins: {
                                legend: {
                                    display: false
                                }
                            },
                            scales: {
                                y: {
                                    beginAtZero: true,
                                    ticks: {
                                        stepSize: 1
                                    }
                                }
                            }
                        }
                    });
                })
                .catch(error => console.error('Error:', error));
        }
        
        // Load initial data
        document.addEventListener('DOMContentLoaded', function() {
            const now = new Date();
            document.getElementById('revenue-month').value = now.getMonth() + 1;
            document.getElementById('user-month').value = now.getMonth() + 1;
            loadRevenueStats();
        });
    </script>
    <script src="${pageContext.request.contextPath}/js/layout.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
