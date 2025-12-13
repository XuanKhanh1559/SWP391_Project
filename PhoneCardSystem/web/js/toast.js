function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

function showToast(message, type = 'info', title = null) {
    console.log('showToast called:', { message, type, title });
    
    if (!message) {
        console.error('showToast: message is required');
        return;
    }
    
    let container = document.getElementById('toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        container.className = 'toast-container';
        document.body.appendChild(container);
    }
    
    const backdrop = document.createElement('div');
    backdrop.className = 'toast-backdrop';
    backdrop.style.cssText = 'position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0, 0, 0, 0.5); z-index: 10001; pointer-events: auto;';
    
    const toast = document.createElement('div');
    toast.className = 'toast ' + type;
    
    // Apply all necessary inline styles
    const baseStyles = 'position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); z-index: 10002; pointer-events: auto;';
    const layoutStyles = 'display: flex; align-items: center; gap: 1.5rem;';
    const visualStyles = 'background: white; border-radius: 12px; padding: 2rem 2.5rem; min-width: 350px; max-width: 500px;';
    const shadowStyles = 'box-shadow: 0 5px 20px rgba(0,0,0,0.15);';
    
    let borderColor = '#4a90e2'; // default info color
    if (type === 'success') borderColor = '#50c878';
    else if (type === 'error') borderColor = '#e74c3c';
    else if (type === 'warning') borderColor = '#f39c12';
    
    const borderStyles = 'border-left: 4px solid ' + borderColor + ';';
    
    toast.style.cssText = baseStyles + layoutStyles + visualStyles + shadowStyles + borderStyles;
    
    const icons = {
        success: 'fa-check-circle',
        error: 'fa-exclamation-circle',
        warning: 'fa-exclamation-triangle',
        info: 'fa-info-circle'
    };
    
    const titles = {
        success: 'Thành công',
        error: 'Lỗi',
        warning: 'Cảnh báo',
        info: 'Thông tin'
    };
    
    const displayTitle = escapeHtml(title || titles[type] || 'Thông báo');
    const escapedMessage = escapeHtml(message);
    const iconClass = icons[type] || icons.info;
    
    let iconColor = '#4a90e2'; // default info color
    if (type === 'success') iconColor = '#50c878';
    else if (type === 'error') iconColor = '#e74c3c';
    else if (type === 'warning') iconColor = '#f39c12';
    
    toast.innerHTML = 
        '<i class="fas ' + iconClass + '" style="font-size: 1.5rem; flex-shrink: 0; color: ' + iconColor + ';"></i>' +
        '<div style="flex: 1;">' +
            '<div style="font-weight: 600; margin-bottom: 0.25rem; color: #2c3e50;">' + displayTitle + '</div>' +
            '<div style="font-size: 0.9rem; color: #333333;">' + escapedMessage + '</div>' +
        '</div>' +
        '<button class="toast-close" onclick="closeToast(this.closest(\'.toast\'), this.closest(\'.toast-container\').querySelector(\'.toast-backdrop\'))" style="background: none; border: none; font-size: 1.2rem; cursor: pointer; color: #333333; opacity: 0.5; padding: 0; width: 24px; height: 24px; display: flex; align-items: center; justify-content: center;">' +
            '<i class="fas fa-times"></i>' +
        '</button>';
    
    backdrop.onclick = function() {
        closeToast(toast, backdrop);
    };
    
    // Append backdrop first, then toast (toast should be on top)
    container.appendChild(backdrop);
    container.appendChild(toast);
    
    // Force reflow to ensure animations work
    void container.offsetHeight;
    
    console.log('Toast elements added to DOM', {
        container: container,
        backdrop: backdrop,
        toast: toast,
        toastStyles: window.getComputedStyle(toast),
        backdropStyles: window.getComputedStyle(backdrop),
        toastVisible: toast.offsetHeight > 0,
        backdropVisible: backdrop.offsetHeight > 0
    });
    
    setTimeout(() => {
        closeToast(toast, backdrop);
    }, 3000);
}

function closeToast(toast, backdrop) {
    if (toast) {
        toast.classList.add('fade-out');
    }
    if (backdrop) {
        backdrop.style.animation = 'fadeOut 0.3s ease-out forwards';
    }
    
    setTimeout(() => {
        if (toast && toast.parentElement) {
            toast.remove();
        }
        if (backdrop && backdrop.parentElement) {
            backdrop.remove();
        }
    }, 300);
}

function showConfirm(message, onConfirm, onCancel = null, title = 'Xác nhận') {
    let container = document.getElementById('toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        container.className = 'toast-container';
        document.body.appendChild(container);
    }
    
    const backdrop = document.createElement('div');
    backdrop.className = 'toast-backdrop';
    backdrop.style.cssText = 'position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0, 0, 0, 0.5); z-index: 10001; pointer-events: auto;';
    
    const confirmDialog = document.createElement('div');
    confirmDialog.className = 'toast warning';
    
    const baseStyles = 'position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); z-index: 10002; pointer-events: auto;';
    const layoutStyles = 'display: flex; flex-direction: column; gap: 1.5rem;';
    const visualStyles = 'background: white; border-radius: 12px; padding: 2rem 2.5rem; min-width: 400px; max-width: 500px;';
    const shadowStyles = 'box-shadow: 0 5px 20px rgba(0,0,0,0.15);';
    const borderStyles = 'border-left: 4px solid #f39c12;';
    
    confirmDialog.style.cssText = baseStyles + layoutStyles + visualStyles + shadowStyles + borderStyles;
    
    const escapedTitle = escapeHtml(title);
    const escapedMessage = escapeHtml(message);
    
    confirmDialog.innerHTML = 
        '<div style="display: flex; align-items: center; gap: 1rem;">' +
            '<i class="fas fa-exclamation-triangle" style="font-size: 1.5rem; flex-shrink: 0; color: #f39c12;"></i>' +
            '<div style="flex: 1;">' +
                '<div style="font-weight: 600; margin-bottom: 0.25rem; color: #2c3e50;">' + escapedTitle + '</div>' +
                '<div style="font-size: 0.9rem; color: #333333;">' + escapedMessage + '</div>' +
            '</div>' +
        '</div>' +
        '<div style="display: flex; gap: 1rem; justify-content: flex-end;">' +
            '<button id="confirm-cancel-btn" style="padding: 0.6rem 1.5rem; border: 1px solid #ddd; background: #ecf0f1; color: #333; border-radius: 8px; cursor: pointer; font-size: 0.9rem; font-weight: 500;">' +
                '<i class="fas fa-times"></i> Hủy' +
            '</button>' +
            '<button id="confirm-ok-btn" style="padding: 0.6rem 1.5rem; border: none; background: #e74c3c; color: white; border-radius: 8px; cursor: pointer; font-size: 0.9rem; font-weight: 500;">' +
                '<i class="fas fa-check"></i> Xác nhận' +
            '</button>' +
        '</div>';
    
    const handleClose = () => {
        closeToast(confirmDialog, backdrop);
    };
    
    backdrop.onclick = function() {
        if (onCancel) onCancel();
        handleClose();
    };
    
    container.appendChild(backdrop);
    container.appendChild(confirmDialog);
    
    setTimeout(() => {
        const cancelBtn = document.getElementById('confirm-cancel-btn');
        const okBtn = document.getElementById('confirm-ok-btn');
        
        if (cancelBtn) {
            cancelBtn.onclick = function() {
                if (onCancel) onCancel();
                handleClose();
            };
        }
        
        if (okBtn) {
            okBtn.onclick = function() {
                handleClose();
                if (onConfirm) onConfirm();
            };
        }
    }, 0);
}

window.showToast = showToast;
window.closeToast = closeToast;
window.showConfirm = showConfirm;

console.log('Toast.js loaded successfully');

