package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dal.ProductDao;
import model.Product;

public class ProductDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String productIdParam = request.getParameter("id");
        if (productIdParam == null || productIdParam.trim().isEmpty()) {
            request.setAttribute("error", "Sản phẩm không tồn tại");
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }
        
        int productId;
        try {
            productId = Integer.parseInt(productIdParam);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Sản phẩm không tồn tại");
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }
        
        HttpSession session = request.getSession(false);
        boolean isAdmin = false;
        
        if (session != null) {
            String role = (String) session.getAttribute("role");
            isAdmin = "admin".equalsIgnoreCase(role);
        }
        
        ProductDao productDao = new ProductDao();
        Product product = productDao.getProductById(productId, isAdmin);
        
        if (product == null) {
            request.setAttribute("error", "Sản phẩm không tồn tại");
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }
        
        request.setAttribute("product", product);
        
        request.getRequestDispatcher("/guest/product-detail.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Product Detail Servlet";
    }
}

