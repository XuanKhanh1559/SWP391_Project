package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import model.Coupon;
import model.Product;
import model.Provider;
import dal.CouponDao;
import dal.ProductDao;
import dal.ProviderDao;

public class CouponDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (!"admin".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/user/dashboard");
            return;
        }
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/coupons");
            return;
        }
        
        try {
            int id = Integer.parseInt(idParam);
            CouponDao couponDao = new CouponDao();
            Coupon coupon = couponDao.getCouponById(id);
            
            if (coupon == null) {
                request.setAttribute("error", "Không tìm thấy mã giảm giá");
                response.sendRedirect(request.getContextPath() + "/admin/coupons");
                return;
            }
            
            ProductDao productDao = new ProductDao();
            ProviderDao providerDao = new ProviderDao();
            
            List<String> applicableProductNames = new ArrayList<>();
            if (coupon.getApplicable_product_ids() != null && !coupon.getApplicable_product_ids().trim().isEmpty()) {
                String[] productIds = coupon.getApplicable_product_ids().split(",");
                List<Product> allProducts = productDao.getAllActiveProducts();
                for (String idStr : productIds) {
                    try {
                        int productId = Integer.parseInt(idStr.trim());
                        for (Product product : allProducts) {
                            if (product.getId() == productId) {
                                applicableProductNames.add(product.getName());
                                break;
                            }
                        }
                    } catch (NumberFormatException e) {
                        // Skip invalid IDs
                    }
                }
            }
            
            List<String> applicableProviderNames = new ArrayList<>();
            if (coupon.getApplicable_provider_ids() != null && !coupon.getApplicable_provider_ids().trim().isEmpty()) {
                String[] providerIds = coupon.getApplicable_provider_ids().split(",");
                List<Provider> allProviders = providerDao.getAllActiveProviders();
                for (String idStr : providerIds) {
                    try {
                        int providerId = Integer.parseInt(idStr.trim());
                        for (Provider provider : allProviders) {
                            if (provider.getId() == providerId) {
                                applicableProviderNames.add(provider.getName());
                                break;
                            }
                        }
                    } catch (NumberFormatException e) {
                        // Skip invalid IDs
                    }
                }
            }
            
            request.setAttribute("coupon", coupon);
            request.setAttribute("applicableProductNames", applicableProductNames);
            request.setAttribute("applicableProviderNames", applicableProviderNames);
            request.getRequestDispatcher("/admin/coupon-detail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/coupons");
        }
    }

    @Override
    public String getServletInfo() {
        return "Coupon Detail Servlet";
    }
}

