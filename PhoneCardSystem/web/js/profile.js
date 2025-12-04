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
