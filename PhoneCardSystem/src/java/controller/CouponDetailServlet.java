package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import model.Coupon;
import dal.CouponDao;

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
            
            request.setAttribute("coupon", coupon);
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

