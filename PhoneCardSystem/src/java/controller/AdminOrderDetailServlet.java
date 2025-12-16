package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dal.OrderDao;
import model.Order;
import model.OrderItem;
import model.User;

public class AdminOrderDetailServlet extends HttpServlet {

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
        
        String orderIdParam = request.getParameter("id");
        if (orderIdParam == null || orderIdParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/orders");
            return;
        }
        
        int orderId;
        try {
            orderId = Integer.parseInt(orderIdParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/orders");
            return;
        }
        
        OrderDao orderDao = new OrderDao();
        Order order = orderDao.getOrderById(orderId);
        
        if (order == null) {
            response.sendRedirect(request.getContextPath() + "/admin/orders");
            return;
        }
        
        List<OrderItem> orderItems = orderDao.getOrderItemsByOrderId(orderId);
        String username = orderDao.getUsernameByOrderId(orderId);
        
        request.setAttribute("order", order);
        request.setAttribute("orderItems", orderItems);
        request.setAttribute("username", username);
        
        request.getRequestDispatcher("/admin/order-detail.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Admin Order Detail Servlet";
    }
}

