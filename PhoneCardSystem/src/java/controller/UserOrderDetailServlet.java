package controller;

import dal.OrderDao;
import model.Order;
import model.OrderItem;
import model.User;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class UserOrderDetailServlet extends HttpServlet {
    
    private OrderDao orderDao;
    
    @Override
    public void init() throws ServletException {
        orderDao = new OrderDao();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        String orderIdParam = request.getParameter("id");
        if (orderIdParam == null || orderIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/user/orders");
            return;
        }
        
        int orderId;
        try {
            orderId = Integer.parseInt(orderIdParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/user/orders");
            return;
        }
        
        Order order = orderDao.getOrderById(orderId);
        
        if (order == null || order.getUser_id() != user.getId()) {
            response.sendRedirect(request.getContextPath() + "/user/orders");
            return;
        }
        
        List<OrderItem> orderItems = orderDao.getOrderItemsByOrderId(orderId);
        
        request.setAttribute("order", order);
        request.setAttribute("orderItems", orderItems);
        
        request.getRequestDispatcher("/user/order-detail.jsp").forward(request, response);
    }
}

