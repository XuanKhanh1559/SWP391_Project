package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dal.UserDao;
import model.User;

public class BanUserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        
        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print("{\"success\": false, \"message\": \"Vui lòng đăng nhập\"}");
            out.flush();
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (!"admin".equalsIgnoreCase(user.getRole())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            out.print("{\"success\": false, \"message\": \"Bạn không có quyền thực hiện thao tác này\"}");
            out.flush();
            return;
        }
        
        String userIdParam = request.getParameter("id");
        String action = request.getParameter("action");
        
        if (userIdParam == null || userIdParam.trim().isEmpty()) {
            out.print("{\"success\": false, \"message\": \"ID người dùng không hợp lệ\"}");
            out.flush();
            return;
        }
        
        try {
            int userId = Integer.parseInt(userIdParam);
            
            if (userId == user.getId()) {
                out.print("{\"success\": false, \"message\": \"Không thể khóa/mở khóa chính mình\"}");
                out.flush();
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
            
            // Escape message for JSON
            String escapedMessage = message.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
            out.print("{\"success\": " + success + ", \"message\": \"" + escapedMessage + "\"}");
            out.flush();
        } catch (NumberFormatException e) {
            out.print("{\"success\": false, \"message\": \"ID người dùng không hợp lệ\"}");
            out.flush();
        }
    }

    @Override
    public String getServletInfo() {
        return "Ban User Servlet";
    }
}

