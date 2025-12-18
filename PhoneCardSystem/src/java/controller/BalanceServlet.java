package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dal.UserDao;
import java.io.PrintWriter;
import model.User;

public class BalanceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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
        UserDao userDao = new UserDao();
        User updatedUser = userDao.getUserById(user.getId());
        
        if (updatedUser != null) {
            session.setAttribute("user", updatedUser);
            out.print("{\"success\": true, \"balance\": " + updatedUser.getBalance() + "}");
            out.flush();
        } else {
            out.print("{\"success\": false, \"message\": \"Không thể lấy thông tin số dư\"}");
            out.flush();
        }
    }

    @Override
    public String getServletInfo() {
        return "Balance API Servlet";
    }
}

