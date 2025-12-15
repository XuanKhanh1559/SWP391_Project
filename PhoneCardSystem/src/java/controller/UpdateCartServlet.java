package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dal.CartDao;
import model.CartItem;
import model.User;

public class UpdateCartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false, \"message\": \"Vui lòng đăng nhập\"}");
            out.flush();
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        String cartItemIdParam = request.getParameter("cartItemId");
        String quantityParam = request.getParameter("quantity");
        
        if (cartItemIdParam == null || cartItemIdParam.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false, \"message\": \"Cart item không hợp lệ\"}");
            out.flush();
            return;
        }
        
        int cartItemId;
        try {
            cartItemId = Integer.parseInt(cartItemIdParam);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false, \"message\": \"Cart item không hợp lệ\"}");
            out.flush();
            return;
        }
        
        int delta = 0;
        if (quantityParam != null && !quantityParam.trim().isEmpty()) {
            try {
                delta = Integer.parseInt(quantityParam);
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                PrintWriter out = response.getWriter();
                out.print("{\"success\": false, \"message\": \"Số lượng không hợp lệ\"}");
                out.flush();
                return;
            }
        }
        
        CartDao cartDao = new CartDao();
        
        int currentQuantity = 0;
        for (CartItem item : cartDao.getCartByUserId(user.getId())) {
            if (item.getId() == cartItemId) {
                currentQuantity = item.getQuantity();
                break;
            }
        }
        
        int newQuantity = currentQuantity + delta;
        
        boolean success = cartDao.updateCartItemQuantity(cartItemId, newQuantity);
        if (!success) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = response.getWriter();
            String error = cartDao.getLastError();
            String errorMessage = (error != null ? error : "Lỗi khi cập nhật giỏ hàng").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
            out.print("{\"success\": false, \"message\": \"" + errorMessage + "\"}");
            out.flush();
            return;
        }
        
        PrintWriter out = response.getWriter();
        out.print("{\"success\": true, \"message\": \"Đã cập nhật giỏ hàng\"}");
        out.flush();
    }

    @Override
    public String getServletInfo() {
        return "Update Cart Servlet";
    }
}


