package controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;
import model.Coupon;
import model.Product;
import model.Provider;
import dal.CouponDao;
import dal.ProductDao;
import dal.ProviderDao;

public class EditCouponServlet extends HttpServlet {

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
            List<Product> products = productDao.getAllActiveProducts();
            List<Provider> providers = providerDao.getAllActiveProviders();
            
            request.setAttribute("coupon", coupon);
            request.setAttribute("products", products);
            request.setAttribute("providers", providers);
            request.getRequestDispatcher("/admin/edit-coupon.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/coupons");
        }
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
            int id = Integer.parseInt(request.getParameter("id"));
            String code = request.getParameter("code");
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
            coupon.setId(id);
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
            
            CouponDao couponDao = new CouponDao();
            boolean success = couponDao.updateCoupon(coupon);
            
            if (success) {
                request.setAttribute("success", "Cập nhật mã giảm giá thành công");
                response.sendRedirect(request.getContextPath() + "/admin/coupon-detail?id=" + id);
            } else {
                request.setAttribute("error", "Cập nhật mã giảm giá thất bại");
                request.setAttribute("coupon", coupon);
                request.getRequestDispatcher("/admin/edit-coupon.jsp").forward(request, response);
            }
        } catch (NumberFormatException | ParseException e) {
            request.setAttribute("error", "Dữ liệu không hợp lệ: " + e.getMessage());
            request.getRequestDispatcher("/admin/edit-coupon.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Edit Coupon Servlet";
    }
}

