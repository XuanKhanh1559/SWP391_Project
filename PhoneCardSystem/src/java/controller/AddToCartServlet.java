package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import dal.CartDao;
import dal.ProductDao;
import model.Product;
import model.User;

public class AddToCartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false, \"message\": \"Vui lòng đăng nhập để mua hàng\"}");
            out.flush();
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        String productIdParam = request.getParameter("productId");
        String quantityParam = request.getParameter("quantity");
        
        if (productIdParam == null || productIdParam.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false, \"message\": \"Vui lòng chọn sản phẩm\"}");
            out.flush();
            return;
        }
        
        int productId;
        try {
            productId = Integer.parseInt(productIdParam);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false, \"message\": \"Sản phẩm không hợp lệ\"}");
            out.flush();
            return;
        }
        
        int quantity = 1;
        if (quantityParam != null && !quantityParam.trim().isEmpty()) {
            try {
                quantity = Integer.parseInt(quantityParam);
                if (quantity < 1) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    PrintWriter out = response.getWriter();
                    out.print("{\"success\": false, \"message\": \"Số lượng phải lớn hơn 0\"}");
                    out.flush();
                    return;
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                PrintWriter out = response.getWriter();
                out.print("{\"success\": false, \"message\": \"Số lượng không hợp lệ\"}");
                out.flush();
                return;
            }
        }
        
        CartDao cartDao = new CartDao();
        if (!cartDao.validateProductForCart(productId)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            String error = cartDao.getLastError();
            String errorMessage = (error != null ? error : "Sản phẩm không khả dụng").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
            out.print("{\"success\": false, \"message\": \"" + errorMessage + "\"}");
            out.flush();
            return;
        }
        
        ProductDao productDao = new ProductDao();
        Product product = productDao.getProductById(productId, false);
        if (product == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false, \"message\": \"Sản phẩm không tồn tại\"}");
            out.flush();
            return;
        }
        
        boolean success = cartDao.addToCart(user.getId(), productId, quantity, product.getPrice());
        if (!success) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = response.getWriter();
            String error = cartDao.getLastError();
            String errorMessage = (error != null ? error : "Lỗi khi thêm vào giỏ hàng").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
            out.print("{\"success\": false, \"message\": \"" + errorMessage + "\"}");
            out.flush();
            return;
        }
        
        int totalItems = cartDao.getCartCount(user.getId());
        
        PrintWriter out = response.getWriter();
        out.print("{\"success\": true, \"message\": \"Đã thêm vào giỏ hàng\", \"cartCount\": " + totalItems + "}");
        out.flush();
    }

    @Override
    public String getServletInfo() {
        return "Add To Cart Servlet";
    }
}

