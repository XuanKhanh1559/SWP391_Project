package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import dal.ProductDao;
import model.Product;
import model.User;

public class DeleteProductServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\": false, \"message\": \"Vui lòng đăng nhập\"}");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (!"admin".equalsIgnoreCase(user.getRole())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"success\": false, \"message\": \"Bạn không có quyền thực hiện thao tác này\"}");
            return;
        }
        
        String productIdParam = request.getParameter("id");
        String action = request.getParameter("action");
        
        if (productIdParam == null || productIdParam.trim().isEmpty()) {
            response.getWriter().write("{\"success\": false, \"message\": \"ID sản phẩm không hợp lệ\"}");
            return;
        }
        
        try {
            int productId = Integer.parseInt(productIdParam);
            ProductDao productDao = new ProductDao();
            boolean success = false;
            String message = "";
            
            if ("restore".equals(action)) {
                success = productDao.restoreProduct(productId);
                message = success ? "Đã khôi phục sản phẩm thành công" : "Khôi phục sản phẩm thất bại";
            } else {
                success = productDao.deleteProduct(productId);
                message = success ? "Đã xóa sản phẩm thành công" : "Xóa sản phẩm thất bại";
            }
            
            if (!success && productDao.getLastError() != null) {
                message = productDao.getLastError();
            }
            
            response.getWriter().write("{\"success\": " + success + ", \"message\": \"" + message + "\"}");
        } catch (NumberFormatException e) {
            response.getWriter().write("{\"success\": false, \"message\": \"ID sản phẩm không hợp lệ\"}");
        }
    }

    @Override
    public String getServletInfo() {
        return "Delete Product Servlet";
    }
}

