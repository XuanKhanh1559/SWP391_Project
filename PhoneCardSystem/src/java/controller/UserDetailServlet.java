package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dal.UserDao;
import model.User;

public class UserDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User currentUser = (User) session.getAttribute("user");
        if (!"admin".equalsIgnoreCase(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }
        
        String userIdParam = request.getParameter("id");
        if (userIdParam == null || userIdParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/users");
            return;
        }
        
        try {
            int userId = Integer.parseInt(userIdParam);
            UserDao userDao = new UserDao();
            User viewUser = userDao.getUserById(userId);
            
            if (viewUser == null) {
                request.setAttribute("error", "Người dùng không tồn tại");
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;
            }
            
            request.setAttribute("viewUser", viewUser);
            request.getRequestDispatcher("/admin/user-detail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/users");
        }
    }

    @Override
    public String getServletInfo() {
        return "User Detail Servlet";
    }
}

