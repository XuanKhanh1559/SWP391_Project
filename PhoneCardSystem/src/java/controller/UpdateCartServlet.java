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
        response.setCharacterEncoding("UTF-8");
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
        
        CartItem cartItem = null;
        for (CartItem item : cartDao.getCartByUserId(user.getId())) {
            if (item.getId() == cartItemId) {
                cartItem = item;
                break;
            }
        }
        
        if (cartItem == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false, \"message\": \"Cart item không tồn tại\"}");
            out.flush();
            return;
        }
        
        int currentQuantity = cartItem.getQuantity();
        int newQuantity = currentQuantity + delta;
        
        if (newQuantity <= 0) {
            // Remove item if quantity becomes 0 or negative
            boolean success = cartDao.removeCartItem(cartItemId);
            if (success) {
                PrintWriter out = response.getWriter();
                out.print("{\"success\": true, \"message\": \"Đã xóa khỏi giỏ hàng\"}");
                out.flush();
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                PrintWriter out = response.getWriter();
                String error = cartDao.getLastError();
                String errorMessage = (error != null ? error : "Lỗi khi xóa khỏi giỏ hàng").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
                out.print("{\"success\": false, \"message\": \"" + errorMessage + "\"}");
                out.flush();
            }
            return;
        }
        
        // Check stock availability
        dal.ProductDao productDao = new dal.ProductDao();
        model.Product product = productDao.getProductById(cartItem.getProduct_id(), false);
        if (product == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false, \"message\": \"Sản phẩm không tồn tại\"}");
            out.flush();
            return;
        }
        
        int availableStock = product.getStock();
        if (availableStock <= 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false, \"message\": \"Sản phẩm đã hết hàng\"}");
            out.flush();
            return;
        }
        
        if (newQuantity > availableStock) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false, \"message\": \"Số lượng vượt quá hàng có sẵn. Số lượng tối đa: " + availableStock + "\"}");
            out.flush();
            return;
        }
        
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


