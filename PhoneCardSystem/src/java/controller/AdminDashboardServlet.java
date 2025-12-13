package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import dal.StatisticsDao;

public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (!"admin".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/user/dashboard");
            return;
        }
        
        StatisticsDao statsDao = new StatisticsDao();
        
        int totalUsers = statsDao.getTotalUsers();
        int totalActiveUsers = statsDao.getTotalActiveUsers();
        int totalProducts = statsDao.getTotalProducts();
        int totalActiveProducts = statsDao.getTotalActiveProducts();
        int totalOrders = statsDao.getTotalOrders();
        int totalPendingOrders = statsDao.getTotalPendingOrders();
        int totalCompletedOrders = statsDao.getTotalCompletedOrders();
        double totalRevenue = statsDao.getTotalRevenue();
        double totalUserBalance = statsDao.getTotalUserBalance();
        
        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("totalActiveUsers", totalActiveUsers);
        request.setAttribute("totalProducts", totalProducts);
        request.setAttribute("totalActiveProducts", totalActiveProducts);
        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("totalPendingOrders", totalPendingOrders);
        request.setAttribute("totalCompletedOrders", totalCompletedOrders);
        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("totalUserBalance", totalUserBalance);
        
        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Admin Dashboard Servlet";
    }
}

