package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dal.UserDao;
import model.User;

public class ChangePasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        response.setContentType("text/html;charset=UTF-8");
        request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User currentUser = (User) session.getAttribute("user");
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        
        if (currentPassword == null || newPassword == null || confirmPassword == null ||
            currentPassword.trim().isEmpty() || newPassword.trim().isEmpty() || confirmPassword.trim().isEmpty()) {
            request.setAttribute("changePasswordError", "Vui lòng nhập đầy đủ thông tin");
            request.setAttribute("activeTab", "changePassword");
            request.setAttribute("user", currentUser);
            request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("changePasswordError", "Mật khẩu xác nhận không khớp");
            request.setAttribute("activeTab", "changePassword");
            request.setAttribute("user", currentUser);
            request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
            return;
        }
        
        UserDao userDao = new UserDao();
        
        if (!userDao.verifyCurrentPassword(currentUser.getId(), currentPassword)) {
            request.setAttribute("changePasswordError", "Mật khẩu hiện tại không đúng");
            request.setAttribute("activeTab", "changePassword");
            request.setAttribute("user", currentUser);
            request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
            return;
        }
        
        boolean updated = userDao.updatePasswordByUserId(currentUser.getId(), newPassword);
        if (updated) {
            request.setAttribute("changePasswordSuccess", "Đổi mật khẩu thành công");
            request.setAttribute("activeTab", "changePassword");
        } else {
            String err = userDao.getLastError();
            request.setAttribute("changePasswordError", err != null ? err : "Đổi mật khẩu thất bại");
            request.setAttribute("activeTab", "changePassword");
        }
        request.setAttribute("user", currentUser);
        request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Change Password Servlet";
    }
}

