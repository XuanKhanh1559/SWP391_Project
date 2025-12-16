package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dal.CartDao;
import model.User;

public class ViewCartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        CartDao cartDao = new CartDao();
        
        request.setAttribute("cartItems", cartDao.getCartByUserId(user.getId()));
        
        request.getRequestDispatcher("/user/cart.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "View Cart Servlet";
    }
}


