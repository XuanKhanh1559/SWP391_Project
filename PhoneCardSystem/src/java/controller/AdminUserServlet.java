package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dal.UserDao;
import model.User;

public class AdminUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (!"admin".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }
        
        String searchParam = request.getParameter("search");
        String statusParam = request.getParameter("status");
        String pageParam = request.getParameter("page");
        
        int page = 1;
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) {
                    page = 1;
                }
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        
        int pageSize = 15;
        
        UserDao userDao = new UserDao();
        List<User> users = userDao.getAllUsers(searchParam, statusParam, page, pageSize);
        int totalUsers = userDao.countUsers(searchParam, statusParam);
        int totalPages = (int) Math.ceil((double) totalUsers / pageSize);
        
        request.setAttribute("users", users);
        request.setAttribute("selectedStatus", statusParam);
        request.setAttribute("searchTerm", searchParam);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalUsers", totalUsers);
        
        request.getRequestDispatcher("/admin/users-management.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Admin User Management Servlet";
    }
}

