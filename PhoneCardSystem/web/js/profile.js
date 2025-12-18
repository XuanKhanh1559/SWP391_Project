function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
}

function loadProfile() {
    if (!window.userData) {
        return;
    }
    
    const profileNav = document.querySelectorAll('.profile-nav a');
    if (profileNav.length > 0) {
        profileNav.forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                const section = link.getAttribute('href').substring(1);
                showProfileSection(section);
            });
        });
    }
}

function showProfileSection(section) {
    document.querySelectorAll('.profile-section').forEach(s => s.classList.remove('active'));
    document.getElementById(section).classList.add('active');
    
    document.querySelectorAll('.profile-nav a').forEach(link => {
        link.classList.remove('active');
        if (link.getAttribute('href') === `#${section}`) {
            link.classList.add('active');
        }
    });
    
    if (section === 'transactions') {
        loadTransactions();
    }
}


function loadTransactions(page = 1) {
    const container = document.getElementById('transactionsList');
    if (!container) return;
    
    // Get text labels from data attributes to avoid encoding issues
    const texts = {
        maGd: container.getAttribute('data-text-ma-gd') || 'Mã GD:',
        soDu: container.getAttribute('data-text-so-du') || 'Số dư:',
        muaHang: container.getAttribute('data-text-mua-hang') || 'Mua hàng',
        napTien: container.getAttribute('data-text-nap-tien') || 'Nạp tiền',
        hoanTien: container.getAttribute('data-text-hoan-tien') || 'Hoàn tiền',
        thanhCong: container.getAttribute('data-text-thanh-cong') || 'Thành công',
        dangXuLy: container.getAttribute('data-text-dang-xu-ly') || 'Đang xử lý',
        thatBai: container.getAttribute('data-text-that-bai') || 'Thất bại',
        khac: container.getAttribute('data-text-khac') || 'Khác',
        khongXacDinh: container.getAttribute('data-text-khong-xac-dinh') || 'Không xác định'
    };
    
    container.innerHTML = '<div style="text-align: center; padding: 2rem;"><i class="fas fa-spinner fa-spin"></i> Đang tải...</div>';
    
    const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf('/', 1));
    
    fetch(contextPath + '/profile?action=getTransactions&page=' + page, {
        headers: {
            'Accept': 'application/json;charset=UTF-8'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            // Ensure response is treated as UTF-8
            return response.text().then(text => {
                try {
                    return JSON.parse(text);
                } catch (e) {
                    console.error('JSON parse error:', e);
                    throw e;
                }
            });
        })
        .then(data => {
            if (data.success) {
                if (data.transactions.length === 0) {
                    container.innerHTML = '<div style="text-align: center; padding: 2rem; color: #999;"><i class="fas fa-receipt" style="font-size: 48px; margin-bottom: 1rem;"></i><p>Chưa có giao dịch nào</p></div>';
                    return;
                }
                
                let html = '<div class="transactions-wrapper">';
                data.transactions.forEach(t => {
                    const typeText = getTransactionTypeText(t.type, texts);
                    const statusText = getTransactionStatusText(t.status, texts);
                    const statusClass = t.status === 1 ? 'success' : (t.status === 2 ? 'danger' : 'warning');
                    
                    // Purchase (type=2) là chi tiêu → hiển thị âm và màu đỏ
                    const isPurchase = t.type === 2;
                    const displayAmount = isPurchase ? -t.amount : t.amount;
                    const amountColor = displayAmount > 0 ? 'green' : 'red';
                    const amountPrefix = displayAmount > 0 ? '+' : '';
                    
                    html += '<div class="transaction-item">' +
                        '<div class="transaction-info">' +
                        '<div class="transaction-header">' +
                        '<strong>' + escapeHtml(typeText) + '</strong>' +
                        '<span class="badge badge-' + statusClass + '">' + escapeHtml(statusText) + '</span>' +
                        '</div>' +
                        '<p class="transaction-code">' + escapeHtml(texts.maGd) + ' ' + escapeHtml(t.transaction_code) + '</p>' +
                        '<p class="transaction-desc">' + escapeHtml(t.description || '') + '</p>' +
                        '<p class="transaction-time"><i class="fas fa-clock"></i> ' + new Date(t.created_at).toLocaleString('vi-VN') + '</p>' +
                        '</div>' +
                        '<div class="transaction-amount" style="color: ' + amountColor + '; font-weight: bold; text-align: right;">' +
                        amountPrefix + formatCurrency(displayAmount) +
                        '<p style="font-size: 0.85rem; color: #666; margin-top: 0.5rem;">' + escapeHtml(texts.soDu) + ' ' + formatCurrency(t.balance_after) + '</p>' +
                        '</div>' +
                        '</div>';
                });
                html += '</div>';
                
                if (data.totalPages > 1) {
                    html += '<div class="pagination">';
                    if (data.currentPage > 1) {
                        html += '<a href="#" class="pagination-btn" onclick="loadTransactions(' + (data.currentPage - 1) + '); return false;"><i class="fas fa-chevron-left"></i> Trước</a>';
                    }
                    
                    for (let i = 1; i <= data.totalPages; i++) {
                        if (i === data.currentPage) {
                            html += '<span class="pagination-btn active">' + i + '</span>';
                        } else {
                            html += '<a href="#" class="pagination-btn" onclick="loadTransactions(' + i + '); return false;">' + i + '</a>';
                        }
                    }
                    
                    if (data.currentPage < data.totalPages) {
                        html += '<a href="#" class="pagination-btn" onclick="loadTransactions(' + (data.currentPage + 1) + '); return false;">Sau <i class="fas fa-chevron-right"></i></a>';
                    }
                    html += '</div>';
                }
                
                container.innerHTML = html;
            } else {
                container.innerHTML = '<div style="text-align: center; padding: 2rem; color: red;">Có lỗi xảy ra khi tải giao dịch</div>';
            }
        })
        .catch(error => {
            console.error('Error loading transactions:', error);
            container.innerHTML = '<div style="text-align: center; padding: 2rem; color: red;">Có lỗi xảy ra khi tải giao dịch</div>';
        });
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

function getTransactionTypeText(type, texts) {
    const typeMap = {
        1: texts.napTien,
        2: texts.muaHang,
        3: texts.hoanTien,
        'deposit': texts.napTien,
        'withdraw': 'Rút tiền',
        'purchase': texts.muaHang,
        'refund': texts.hoanTien
    };
    return typeMap[type] || texts.khac;
}

function getTransactionStatusText(status, texts) {
    const statusMap = {
        0: texts.dangXuLy,
        1: texts.thanhCong,
        2: texts.thatBai,
        'pending': texts.dangXuLy,
        'completed': texts.thanhCong,
        'failed': texts.thatBai
    };
    return statusMap[status] || texts.khongXacDinh;
}

document.addEventListener('DOMContentLoaded', loadProfile);
