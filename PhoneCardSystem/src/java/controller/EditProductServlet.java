package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dal.ProductDao;
import dal.ProviderDao;
import model.Product;
import model.Provider;
import model.User;

public class EditProductServlet extends HttpServlet {

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
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }
        
        String productIdParam = request.getParameter("id");
        if (productIdParam == null || productIdParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/products");
            return;
        }
        
        try {
            int productId = Integer.parseInt(productIdParam);
            ProductDao productDao = new ProductDao();
            Product product = productDao.getProductById(productId, true);
            
            if (product == null) {
                request.setAttribute("error", "Sản phẩm không tồn tại");
                response.sendRedirect(request.getContextPath() + "/admin/products");
                return;
            }
            
            ProviderDao providerDao = new ProviderDao();
            List<Provider> providers = providerDao.getAllActiveProviders();
            
            request.setAttribute("product", product);
            request.setAttribute("providers", providers);
            request.getRequestDispatcher("/admin/edit-product.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/products");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (!"admin".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }
        
        String productIdParam = request.getParameter("id");
        String name = request.getParameter("name");
        String denominationParam = request.getParameter("denomination");
        String priceParam = request.getParameter("price");
        String description = request.getParameter("description");
        String status = request.getParameter("status");
        
        if (productIdParam == null || name == null || denominationParam == null || 
            priceParam == null || status == null) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin bắt buộc");
            doGet(request, response);
            return;
        }
        
        if (!"active".equalsIgnoreCase(status) && !"inactive".equalsIgnoreCase(status)) {
            request.setAttribute("error", "Trạng thái không hợp lệ");
            doGet(request, response);
            return;
        }
        
        try {
            int productId = Integer.parseInt(productIdParam);
            double denomination = Double.parseDouble(denominationParam);
            double price = Double.parseDouble(priceParam);
            
            ProductDao productDao = new ProductDao();
            boolean updated = productDao.updateProduct(productId, name, denomination, price, description, status);
            
            if (updated) {
                session.setAttribute("successMessage", "Đã cập nhật sản phẩm thành công");
                response.sendRedirect(request.getContextPath() + "/admin/products");
            } else {
                String error = productDao.getLastError();
                request.setAttribute("error", error != null ? error : "Cập nhật sản phẩm thất bại");
                doGet(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Giá trị nhập vào không hợp lệ");
            doGet(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Edit Product Servlet";
    }
}

