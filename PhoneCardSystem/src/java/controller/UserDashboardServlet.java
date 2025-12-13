package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

public class UserDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        int totalOrders = 0;
        int availableCoupons = 0;
        int unreadNotifications = 0;
        
        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("availableCoupons", availableCoupons);
        request.setAttribute("unreadNotifications", unreadNotifications);
        
        request.getRequestDispatcher("/user/dashboard.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "User Dashboard Servlet";
    }
}

