package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dal.UserDao;
import model.User;

public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User currentUser = (User) session.getAttribute("user");
        UserDao userDao = new UserDao();
        User user = userDao.getUserById(currentUser.getId());
        
        if (user != null) {
            request.setAttribute("user", user);
            session.setAttribute("user", user);
            session.setAttribute("balance", user.getBalance());
            session.setAttribute("role", user.getRole());
        }
        
        response.setContentType("text/html;charset=UTF-8");
        request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User currentUser = (User) session.getAttribute("user");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("profileError", "Email không được để trống");
            request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
            return;
        }
        
        UserDao userDao = new UserDao();
        
        User existingUser = userDao.getUserByEmail(email);
        if (existingUser != null && existingUser.getId() != currentUser.getId()) {
            request.setAttribute("profileError", "Email đã được sử dụng bởi tài khoản khác");
            request.setAttribute("user", currentUser);
            request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
            return;
        }
        
        boolean updated = userDao.updateProfile(currentUser.getId(), email, phone != null ? phone : "");
        if (updated) {
            User updatedUser = userDao.getUserById(currentUser.getId());
            if (updatedUser != null) {
                session.setAttribute("user", updatedUser);
                session.setAttribute("email", updatedUser.getEmail());
                session.setAttribute("role", updatedUser.getRole());
                request.setAttribute("user", updatedUser);
                request.setAttribute("profileSuccess", "Cập nhật thông tin thành công");
            }
        } else {
            String err = userDao.getLastError();
            request.setAttribute("profileError", err != null ? err : "Cập nhật thông tin thất bại");
            request.setAttribute("user", currentUser);
        }
        
        request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Profile Servlet";
    }
}

