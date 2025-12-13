package controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

public class CreateCouponServlet extends HttpServlet {

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
        
        ProductDao productDao = new ProductDao();
        ProviderDao providerDao = new ProviderDao();
        List<Product> products = productDao.getAllActiveProducts();
        List<Provider> providers = providerDao.getAllActiveProviders();
        
        request.setAttribute("products", products);
        request.setAttribute("providers", providers);
        request.getRequestDispatcher("/admin/create-coupon.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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
        
        try {
            String code = request.getParameter("code");
            
            CouponDao couponDao = new CouponDao();
            if (couponDao.checkCouponCodeExists(code)) {
                request.setAttribute("error", "Mã coupon đã tồn tại. Vui lòng chọn mã khác.");
                ProductDao productDao = new ProductDao();
                ProviderDao providerDao = new ProviderDao();
                request.setAttribute("products", productDao.getAllActiveProducts());
                request.setAttribute("providers", providerDao.getAllActiveProviders());
                request.getRequestDispatcher("/admin/create-coupon.jsp").forward(request, response);
                return;
            }
            
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            int discountType = Integer.parseInt(request.getParameter("discount_type"));
            double discountValue = Double.parseDouble(request.getParameter("discount_value"));
            double minOrderAmount = Double.parseDouble(request.getParameter("min_order_amount"));
            
            String maxDiscountAmountParam = request.getParameter("max_discount_amount");
            Double maxDiscountAmount = null;
            if (maxDiscountAmountParam != null && !maxDiscountAmountParam.trim().isEmpty()) {
                maxDiscountAmount = Double.parseDouble(maxDiscountAmountParam);
            }
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date startDate = sdf.parse(request.getParameter("start_date"));
            Date endDate = sdf.parse(request.getParameter("end_date"));
            
            int usageLimitPerUser = Integer.parseInt(request.getParameter("usage_limit_per_user"));
            
            String totalUsageLimitParam = request.getParameter("total_usage_limit");
            Integer totalUsageLimit = null;
            if (totalUsageLimitParam != null && !totalUsageLimitParam.trim().isEmpty()) {
                totalUsageLimit = Integer.parseInt(totalUsageLimitParam);
            }
            
            int status = Integer.parseInt(request.getParameter("status"));
            String applicableProductIds = request.getParameter("applicable_product_ids");
            String applicableProviderIds = request.getParameter("applicable_provider_ids");
            
            Coupon coupon = new Coupon();
            coupon.setCode(code);
            coupon.setName(name);
            coupon.setDescription(description);
            coupon.setDiscount_type(discountType);
            coupon.setDiscount_value(discountValue);
            coupon.setMin_order_amount(minOrderAmount);
            coupon.setMax_discount_amount(maxDiscountAmount);
            coupon.setStart_date(startDate);
            coupon.setEnd_date(endDate);
            coupon.setUsage_limit_per_user(usageLimitPerUser);
            coupon.setTotal_usage_limit(totalUsageLimit);
            coupon.setStatus(status);
            coupon.setApplicable_product_ids(applicableProductIds);
            coupon.setApplicable_provider_ids(applicableProviderIds);
            
            boolean success = couponDao.createCoupon(coupon);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/admin/coupons");
            } else {
                request.setAttribute("error", "Tạo mã giảm giá thất bại");
                ProductDao productDao = new ProductDao();
                ProviderDao providerDao = new ProviderDao();
                request.setAttribute("products", productDao.getAllActiveProducts());
                request.setAttribute("providers", providerDao.getAllActiveProviders());
                request.setAttribute("coupon", coupon);
                request.getRequestDispatcher("/admin/create-coupon.jsp").forward(request, response);
            }
        } catch (NumberFormatException | ParseException e) {
            request.setAttribute("error", "Dữ liệu không hợp lệ: " + e.getMessage());
            ProductDao productDao = new ProductDao();
            ProviderDao providerDao = new ProviderDao();
            request.setAttribute("products", productDao.getAllActiveProducts());
            request.setAttribute("providers", providerDao.getAllActiveProviders());
            request.getRequestDispatcher("/admin/create-coupon.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Create Coupon Servlet";
    }
}

