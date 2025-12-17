package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dal.ProductDao;
import model.Product;
import model.User;

public class DeleteProductServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        
        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print("{\"success\": false, \"message\": \"Vui lòng đăng nhập\"}");
            out.flush();
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (!"admin".equalsIgnoreCase(user.getRole())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            out.print("{\"success\": false, \"message\": \"Bạn không có quyền thực hiện thao tác này\"}");
            out.flush();
            return;
        }
        
        String productIdParam = request.getParameter("id");
        String action = request.getParameter("action");
        
        if (productIdParam == null || productIdParam.trim().isEmpty()) {
            out.print("{\"success\": false, \"message\": \"ID sản phẩm không hợp lệ\"}");
            out.flush();
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
            
            // Escape message for JSON
            String escapedMessage = message.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
            out.print("{\"success\": " + success + ", \"message\": \"" + escapedMessage + "\"}");
            out.flush();
        } catch (NumberFormatException e) {
            out.print("{\"success\": false, \"message\": \"ID sản phẩm không hợp lệ\"}");
            out.flush();
        }
    }

    @Override
    public String getServletInfo() {
        return "Delete Product Servlet";
    }
}

