package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dal.UserDao;

public class ResetPasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String email = request.getParameter("email");
        String token = request.getParameter("token");
        request.setAttribute("email", email);
        request.setAttribute("token", token);
        request.setAttribute("sent", request.getParameter("sent"));
        request.getRequestDispatcher("/guest/reset-password.jsp").forward(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String email = request.getParameter("email");
        String token = request.getParameter("resetToken");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Trim and normalize inputs
        if (email != null) {
            email = email.trim().toLowerCase(); // Email is case-insensitive
        }
        if (token != null) {
            token = token.trim();
            // Normalize token to 6 digits with leading zeros
            try {
                int tokenInt = Integer.parseInt(token);
                token = String.format("%06d", tokenInt);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Mã xác nhận phải là số 6 chữ số");
                request.setAttribute("email", email);
                request.setAttribute("token", token);
                request.getRequestDispatcher("/guest/reset-password.jsp").forward(request, response);
                return;
            }
        }
        if (password != null) {
            password = password.trim();
        }
        if (confirmPassword != null) {
            confirmPassword = confirmPassword.trim();
        }
        
        if (email == null || token == null || password == null || confirmPassword == null ||
            email.isEmpty() || token.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin");
            request.setAttribute("email", email);
            request.setAttribute("token", token);
            request.getRequestDispatcher("/guest/reset-password.jsp").forward(request, response);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp");
            request.setAttribute("email", email);
            request.setAttribute("token", token);
            request.getRequestDispatcher("/guest/reset-password.jsp").forward(request, response);
            return;
        }
        
        UserDao userDao = new UserDao();
        
        if (!userDao.verifyPasswordResetToken(email, token)) {
            String errorMsg = userDao.getLastError();
            request.setAttribute("error", errorMsg != null && !errorMsg.isEmpty() ? errorMsg : "Mã xác nhận không hợp lệ hoặc đã hết hạn");
            request.setAttribute("email", email);
            request.setAttribute("token", token);
            request.getRequestDispatcher("/guest/reset-password.jsp").forward(request, response);
            return;
        }
        
        boolean updated = userDao.updatePasswordByEmail(email, password);
        if (updated) {
            userDao.markTokenAsUsed(email, token);
            response.sendRedirect(request.getContextPath() + "/login?reset=success");
        } else {
            String err = userDao.getLastError();
            request.setAttribute("error", err != null ? err : "Đặt lại mật khẩu thất bại");
            request.setAttribute("email", email);
            request.setAttribute("token", token);
            request.getRequestDispatcher("/guest/reset-password.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Reset Password Servlet";
    }
}

