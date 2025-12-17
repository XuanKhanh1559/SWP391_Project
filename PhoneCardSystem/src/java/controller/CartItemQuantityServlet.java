package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dal.CartDao;
import model.User;

public class CartItemQuantityServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false, \"quantity\": 0}");
            out.flush();
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        String productIdParam = request.getParameter("productId");
        if (productIdParam == null || productIdParam.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false, \"quantity\": 0}");
            out.flush();
            return;
        }
        
        int productId;
        try {
            productId = Integer.parseInt(productIdParam);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false, \"quantity\": 0}");
            out.flush();
            return;
        }
        
        CartDao cartDao = new CartDao();
        int quantity = cartDao.getCartItemQuantity(user.getId(), productId);
        
        PrintWriter out = response.getWriter();
        out.print("{\"success\": true, \"quantity\": " + quantity + "}");
        out.flush();
    }

    @Override
    public String getServletInfo() {
        return "Cart Item Quantity Servlet";
    }
}

