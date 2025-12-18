package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;
import dal.CouponDao;
import com.google.gson.JsonObject;

public class DeleteCouponServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        
        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        JsonObject jsonResponse = new JsonObject();
        
        if (session == null || session.getAttribute("user") == null) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Vui lòng đăng nhập");
            out.print(jsonResponse.toString());
            out.flush();
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (!"admin".equalsIgnoreCase(user.getRole())) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Bạn không có quyền thực hiện thao tác này");
            out.print(jsonResponse.toString());
            out.flush();
            return;
        }
        
        String idParam = request.getParameter("id");
        String action = request.getParameter("action");
        
        if (idParam == null || idParam.trim().isEmpty()) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "ID mã giảm giá không hợp lệ");
            out.print(jsonResponse.toString());
            out.flush();
            return;
        }
        
        try {
            int id = Integer.parseInt(idParam);
            CouponDao couponDao = new CouponDao();
            boolean success;
            
            if ("restore".equals(action)) {
                success = couponDao.restoreCoupon(id);
                if (success) {
                    jsonResponse.addProperty("success", true);
                    jsonResponse.addProperty("message", "Khôi phục mã giảm giá thành công");
                } else {
                    jsonResponse.addProperty("success", false);
                    jsonResponse.addProperty("message", "Khôi phục mã giảm giá thất bại");
                }
            } else {
                success = couponDao.deleteCoupon(id);
                if (success) {
                    jsonResponse.addProperty("success", true);
                    jsonResponse.addProperty("message", "Xóa mã giảm giá thành công");
                } else {
                    jsonResponse.addProperty("success", false);
                    jsonResponse.addProperty("message", "Xóa mã giảm giá thất bại");
                }
            }
            
            out.print(jsonResponse.toString());
            out.flush();
        } catch (NumberFormatException e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "ID mã giảm giá không hợp lệ");
            out.print(jsonResponse.toString());
            out.flush();
        }
    }

    @Override
    public String getServletInfo() {
        return "Delete Coupon Servlet";
    }
}

