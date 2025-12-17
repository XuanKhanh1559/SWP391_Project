package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dal.CartDao;
import model.User;

public class RemoveCartItemServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false, \"message\": \"Vui lòng đăng nhập\"}");
            out.flush();
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        String cartItemIdParam = request.getParameter("cartItemId");
        
        if (cartItemIdParam == null || cartItemIdParam.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false, \"message\": \"Cart item không hợp lệ\"}");
            out.flush();
            return;
        }
        
        int cartItemId;
        try {
            cartItemId = Integer.parseInt(cartItemIdParam);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("{\"success\": false, \"message\": \"Cart item không hợp lệ\"}");
            out.flush();
            return;
        }
        
        CartDao cartDao = new CartDao();
        boolean success = cartDao.removeCartItem(cartItemId);
        if (!success) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = response.getWriter();
            String error = cartDao.getLastError();
            // Clean up error message - remove technical SQL errors for user
            String errorMessage = (error != null ? error : "Lỗi khi xóa khỏi giỏ hàng");
            if (errorMessage.contains("Duplicate entry") || errorMessage.contains("unique_user_product")) {
                errorMessage = "Lỗi khi xóa khỏi giỏ hàng. Vui lòng thử lại.";
            }
            errorMessage = errorMessage.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
            out.print("{\"success\": false, \"message\": \"" + errorMessage + "\"}");
            out.flush();
            return;
        }
        
        PrintWriter out = response.getWriter();
        out.print("{\"success\": true, \"message\": \"Đã xóa khỏi giỏ hàng\"}");
        out.flush();
    }

    @Override
    public String getServletInfo() {
        return "Remove Cart Item Servlet";
    }
}


