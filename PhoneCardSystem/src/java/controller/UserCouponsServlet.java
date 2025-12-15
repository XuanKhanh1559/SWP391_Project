package controller;

import dal.CouponDao;
import model.User;
import model.Coupon;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class UserCouponsServlet extends HttpServlet {
    
    private static final int COUPONS_PER_PAGE = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        
        if ("getAvailable".equals(action)) {
            CouponDao couponDao = new CouponDao();
            List<Coupon> coupons = couponDao.getAvailableCouponsForUser(user.getId());
            
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            Gson gson = new Gson();
            out.print(gson.toJson(coupons));
            return;
        }

        request.getRequestDispatcher("/user/coupons.jsp").forward(request, response);
    }
}

