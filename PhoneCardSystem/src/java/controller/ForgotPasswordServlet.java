package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dal.UserDao;

public class ForgotPasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.getRequestDispatcher("/guest/forgot-password.jsp").forward(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String email = request.getParameter("email");
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập email");
            request.getRequestDispatcher("/guest/forgot-password.jsp").forward(request, response);
            return;
        }
        
        UserDao userDao = new UserDao();
        boolean ok = userDao.requestPasswordReset(email);
        
        if (ok) {
            response.sendRedirect(request.getContextPath() + "/reset-password?sent=1&email=" + 
                java.net.URLEncoder.encode(email, "UTF-8"));
        } else {
            String err = userDao.getLastError();
            request.setAttribute("error", err != null ? err : "Email không tồn tại hoặc có lỗi xảy ra");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/guest/forgot-password.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Forgot Password Servlet";
    }
}

