package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import dal.UserDao;
import model.User;

public class BanUserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\": false, \"message\": \"Vui lòng đăng nhập\"}");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (!"admin".equalsIgnoreCase(user.getRole())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"success\": false, \"message\": \"Bạn không có quyền thực hiện thao tác này\"}");
            return;
        }
        
        String userIdParam = request.getParameter("id");
        String action = request.getParameter("action");
        
        if (userIdParam == null || userIdParam.trim().isEmpty()) {
            response.getWriter().write("{\"success\": false, \"message\": \"ID người dùng không hợp lệ\"}");
            return;
        }
        
        try {
            int userId = Integer.parseInt(userIdParam);
            
            if (userId == user.getId()) {
                response.getWriter().write("{\"success\": false, \"message\": \"Không thể khóa/mở khóa chính mình\"}");
                return;
            }
            
            UserDao userDao = new UserDao();
            boolean success = false;
            String message = "";
            
            if ("unban".equals(action)) {
                success = userDao.unbanUser(userId);
                message = success ? "Đã mở khóa người dùng thành công" : "Mở khóa người dùng thất bại";
            } else {
                success = userDao.banUser(userId);
                message = success ? "Đã khóa người dùng thành công" : "Khóa người dùng thất bại";
            }
            
            if (!success && userDao.getLastError() != null) {
                message = userDao.getLastError();
            }
            
            response.getWriter().write("{\"success\": " + success + ", \"message\": \"" + message + "\"}");
        } catch (NumberFormatException e) {
            response.getWriter().write("{\"success\": false, \"message\": \"ID người dùng không hợp lệ\"}");
        }
    }

    @Override
    public String getServletInfo() {
        return "Ban User Servlet";
    }
}

