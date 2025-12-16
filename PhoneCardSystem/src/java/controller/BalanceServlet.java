package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dal.UserDao;
import model.User;

public class BalanceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\": false, \"message\": \"Vui lòng đăng nhập\"}");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        UserDao userDao = new UserDao();
        User updatedUser = userDao.getUserById(user.getId());
        
        if (updatedUser != null) {
            session.setAttribute("user", updatedUser);
            response.getWriter().write("{\"success\": true, \"balance\": " + updatedUser.getBalance() + "}");
        } else {
            response.getWriter().write("{\"success\": false, \"message\": \"Không thể lấy thông tin số dư\"}");
        }
    }

    @Override
    public String getServletInfo() {
        return "Balance API Servlet";
    }
}

