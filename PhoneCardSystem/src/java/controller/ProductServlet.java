package controller;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import dal.ProductDao;
import model.Product;
import model.User;

public class ProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession(false);
        boolean isAdmin = false;
        
        if (session != null) {
            String role = (String) session.getAttribute("role");
            isAdmin = "admin".equalsIgnoreCase(role);
        }
        
        String providerParam = request.getParameter("provider");
        String typeParam = request.getParameter("type");
        String searchParam = request.getParameter("search");
        String pageParam = request.getParameter("page");
        
        Integer providerId = null;
        if (providerParam != null && !providerParam.trim().isEmpty()) {
            try {
                providerId = Integer.parseInt(providerParam);
            } catch (NumberFormatException e) {
            }
        }
        
        Integer type = null;
        if (typeParam != null && !typeParam.trim().isEmpty()) {
            if ("topup".equalsIgnoreCase(typeParam)) {
                type = 1;
            } else if ("data_package".equalsIgnoreCase(typeParam)) {
                type = 2;
            }
        }
        
        int page = 1;
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) {
                    page = 1;
                }
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        
        int pageSize = 15;
        
        ProductDao productDao = new ProductDao();
        List<Product> products = productDao.getAllProducts(providerId, type, searchParam, isAdmin, page, pageSize);
        int totalProducts = productDao.countProducts(providerId, type, searchParam, isAdmin);
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        
        request.setAttribute("products", products);
        request.setAttribute("selectedProvider", providerId);
        request.setAttribute("selectedType", typeParam);
        request.setAttribute("searchTerm", searchParam);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalProducts", totalProducts);
        
        request.getRequestDispatcher("/guest/products.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Product Servlet";
    }
}

