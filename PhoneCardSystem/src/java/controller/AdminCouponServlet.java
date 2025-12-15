package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;
import model.Coupon;
import dal.CouponDao;
import java.util.List;

public class AdminCouponServlet extends HttpServlet {

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
        
        String search = request.getParameter("search");
        String statusParam = request.getParameter("status");
        String pageParam = request.getParameter("page");
        
        Integer status = null;
        if (statusParam != null && !statusParam.trim().isEmpty()) {
            try {
                status = Integer.parseInt(statusParam);
            } catch (NumberFormatException e) {
                status = null;
            }
        }
        
        int page = 1;
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        
        int limit = 15;
        int offset = (page - 1) * limit;
        
        CouponDao couponDao = new CouponDao();
        List<Coupon> coupons = couponDao.getAllCoupons(limit, offset, search, status);
        int totalCoupons = couponDao.countCoupons(search, status);
        int totalPages = (int) Math.ceil((double) totalCoupons / limit);
        
        request.setAttribute("coupons", coupons);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalCoupons", totalCoupons);
        request.setAttribute("search", search);
        request.setAttribute("status", status);
        
        request.getRequestDispatcher("/admin/coupons-management.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Admin Coupon Management Servlet";
    }
}

