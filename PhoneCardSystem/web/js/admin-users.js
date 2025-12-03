// Admin users management
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
}

function loadUsers() {
    const user = JSON.parse(localStorage.getItem('currentUser') || '{}');
    if (!user.id || user.role !== 'admin') {
        window.location.href = '../guest/login.jsp';
        return;
    }
    
    const mockUsers = [
        { id: 1, username: 'user1', email: 'user1@example.com', balance: 500000, status: 'active', created_at: '2024-01-01' },
        { id: 2, username: 'user2', email: 'user2@example.com', balance: 200000, status: 'active', created_at: '2024-01-02' }
    ];
    
    renderUsers(mockUsers);
    
    document.getElementById('searchUser')?.addEventListener('input', function() {
        const searchTerm = this.value.toLowerCase();
        const filtered = mockUsers.filter(u => 
            u.username.toLowerCase().includes(searchTerm) || 
            u.email.toLowerCase().includes(searchTerm)
        );
        renderUsers(filtered);
    });
}

function renderUsers(users) {
    const tbody = document.getElementById('usersTableBody');
    if (!tbody) return;
    
    tbody.innerHTML = users.map(u => `
        <tr>
            <td>${u.id}</td>
            <td>${u.username}</td>
            <td>${u.email}</td>
            <td>${formatCurrency(u.balance)}</td>
            <td><span class="order-status ${u.status}">${u.status === 'active' ? 'Hoạt động' : 'Tạm khóa'}</span></td>
            <td>${new Date(u.created_at).toLocaleDateString('vi-VN')}</td>
            <td>
                <button class="btn-action btn-view">Xem</button>
                <button class="btn-action btn-edit">Sửa</button>
            </td>
        </tr>
    `).join('');
}

document.addEventListener('DOMContentLoaded', loadUsers);


