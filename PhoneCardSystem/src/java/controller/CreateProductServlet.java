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
import model.Provider;
import model.User;

public class CreateProductServlet extends HttpServlet {

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
        
        ProviderDao providerDao = new ProviderDao();
        List<Provider> providers = providerDao.getAllActiveProviders();
        request.setAttribute("providers", providers);
        
        request.getRequestDispatcher("/admin/create-product.jsp").forward(request, response);
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
        
        String providerIdParam = request.getParameter("providerId");
        String name = request.getParameter("name");
        String typeParam = request.getParameter("type");
        String denominationParam = request.getParameter("denomination");
        String priceParam = request.getParameter("price");
        String description = request.getParameter("description");
        String status = request.getParameter("status");
        
        if (providerIdParam == null || name == null || typeParam == null || 
            denominationParam == null || priceParam == null || status == null) {
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
            int providerId = Integer.parseInt(providerIdParam);
            int type = Integer.parseInt(typeParam);
            double denomination = Double.parseDouble(denominationParam);
            double price = Double.parseDouble(priceParam);
            
            ProductDao productDao = new ProductDao();
            boolean created = productDao.createProduct(providerId, name, type, denomination, price, description, status);
            
            if (created) {
                session.setAttribute("successMessage", "Đã tạo sản phẩm mới thành công");
                response.sendRedirect(request.getContextPath() + "/admin/products");
            } else {
                String error = productDao.getLastError();
                request.setAttribute("error", error != null ? error : "Tạo sản phẩm thất bại");
                doGet(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Giá trị nhập vào không hợp lệ");
            doGet(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Create Product Servlet";
    }
}

