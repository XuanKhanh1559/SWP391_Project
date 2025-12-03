// Profile page functionality
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
}

function loadProfile() {
    const user = JSON.parse(localStorage.getItem('currentUser') || '{}');
    if (!user.id) {
        window.location.href = '../guest/login.jsp';
        return;
    }
    
    document.getElementById('profileUserName').textContent = user.username || 'User';
    document.getElementById('profileBalance').textContent = formatCurrency(user.balance || 0);
    document.getElementById('profileUsername').value = user.username || '';
    document.getElementById('profileEmail').value = user.email || '';
    document.getElementById('profilePhone').value = user.phone || '';
    
    // Profile navigation
    document.querySelectorAll('.profile-nav a').forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const section = link.getAttribute('href').substring(1);
            showProfileSection(section);
        });
    });
    
    document.getElementById('profileForm')?.addEventListener('submit', updateProfile);
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

function updateProfile(e) {
    e.preventDefault();
    const user = JSON.parse(localStorage.getItem('currentUser') || '{}');
    user.email = document.getElementById('profileEmail').value;
    user.phone = document.getElementById('profilePhone').value;
    localStorage.setItem('currentUser', JSON.stringify(user));
    alert('Cập nhật thông tin thành công!');
}

function loadTransactions() {
    const container = document.getElementById('transactionsList');
    const transactions = [
        { id: 1, type: 'deposit', amount: 500000, status: 'completed', created_at: '2024-01-01' },
        { id: 2, type: 'purchase', amount: -50000, status: 'completed', created_at: '2024-01-02' }
    ];
    
    container.innerHTML = transactions.map(t => `
        <div class="transaction-item">
            <div>
                <strong>${getTransactionTypeText(t.type)}</strong>
                <p>${new Date(t.created_at).toLocaleString('vi-VN')}</p>
            </div>
            <div style="color: ${t.amount > 0 ? 'green' : 'red'}; font-weight: bold;">
                ${t.amount > 0 ? '+' : ''}${formatCurrency(t.amount)}
            </div>
        </div>
    `).join('');
}

function getTransactionTypeText(type) {
    const typeMap = {
        'deposit': 'Nạp tiền',
        'withdraw': 'Rút tiền',
        'purchase': 'Mua hàng',
        'refund': 'Hoàn tiền'
    };
    return typeMap[type] || type;
}

document.addEventListener('DOMContentLoaded', loadProfile);


