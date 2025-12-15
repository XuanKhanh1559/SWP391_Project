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

public class CartCountServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        
        HttpSession session = request.getSession(false);
        int count = 0;
        
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                CartDao cartDao = new CartDao();
                count = cartDao.getCartCount(user.getId());
            }
        }
        
        PrintWriter out = response.getWriter();
        out.print("{\"count\": " + count + "}");
        out.flush();
    }

    @Override
    public String getServletInfo() {
        return "Cart Count Servlet";
    }
}

