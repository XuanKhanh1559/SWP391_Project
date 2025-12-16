package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dal.OrderDao;
import model.Order;
import model.User;

public class AdminOrderServlet extends HttpServlet {

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
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        String statusParam = request.getParameter("status");
        String dateFilterParam = request.getParameter("dateFilter");
        String searchParam = request.getParameter("search");
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
        
        OrderDao orderDao = new OrderDao();
        List<Order> orders = orderDao.getAllOrders(searchParam, statusParam, dateFilterParam, page, pageSize);
        int totalOrders = orderDao.countOrders(searchParam, statusParam, dateFilterParam);
        int totalPages = (int) Math.ceil((double) totalOrders / pageSize);
        
        Map<Integer, String> usernames = new HashMap<>();
        for (Order order : orders) {
            String username = orderDao.getUsernameByOrderId(order.getId());
            usernames.put(order.getId(), username);
        }
        
        request.setAttribute("orders", orders);
        request.setAttribute("usernames", usernames);
        request.setAttribute("selectedStatus", statusParam);
        request.setAttribute("selectedDateFilter", dateFilterParam);
        request.setAttribute("searchTerm", searchParam);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalOrders", totalOrders);
        
        request.getRequestDispatcher("/admin/orders-management.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Admin Order Management Servlet";
    }
}

