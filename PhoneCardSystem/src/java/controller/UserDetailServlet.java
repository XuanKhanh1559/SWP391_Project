package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dal.UserDao;
import dal.OrderDao;
import dal.PaymentTransactionDao;
import model.User;
import java.util.List;

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
            
            // Refresh balance from database to ensure we have the latest value
            userDao.refreshUserBalance(viewUser);
            
            // Pagination for orders
            int orderPage = 1;
            int orderPageSize = 10;
            String orderPageParam = request.getParameter("orderPage");
            if (orderPageParam != null && !orderPageParam.trim().isEmpty()) {
                try {
                    orderPage = Integer.parseInt(orderPageParam);
                    if (orderPage < 1) orderPage = 1;
                } catch (NumberFormatException e) {
                    orderPage = 1;
                }
            }
            
            // Pagination for transactions
            int transactionPage = 1;
            int transactionPageSize = 10;
            String transactionPageParam = request.getParameter("transactionPage");
            if (transactionPageParam != null && !transactionPageParam.trim().isEmpty()) {
                try {
                    transactionPage = Integer.parseInt(transactionPageParam);
                    if (transactionPage < 1) transactionPage = 1;
                } catch (NumberFormatException e) {
                    transactionPage = 1;
                }
            }
            
            // Fetch orders
            OrderDao orderDao = new OrderDao();
            List<model.Order> orders = orderDao.getOrdersByUserId(userId, orderPageSize, (orderPage - 1) * orderPageSize);
            int totalOrders = orderDao.countOrdersByUserId(userId);
            int totalOrderPages = (int) Math.ceil((double) totalOrders / orderPageSize);
            
            // Fetch transactions
            PaymentTransactionDao transactionDao = new PaymentTransactionDao();
            List<model.PaymentTransaction> transactions = transactionDao.getTransactionsByUserId(userId, transactionPageSize, (transactionPage - 1) * transactionPageSize);
            int totalTransactions = transactionDao.countTransactionsByUserId(userId);
            int totalTransactionPages = (int) Math.ceil((double) totalTransactions / transactionPageSize);
            
            request.setAttribute("viewUser", viewUser);
            request.setAttribute("orders", orders);
            request.setAttribute("totalOrders", totalOrders);
            request.setAttribute("orderPage", orderPage);
            request.setAttribute("totalOrderPages", totalOrderPages);
            request.setAttribute("transactions", transactions);
            request.setAttribute("totalTransactions", totalTransactions);
            request.setAttribute("transactionPage", transactionPage);
            request.setAttribute("totalTransactionPages", totalTransactionPages);
            
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

