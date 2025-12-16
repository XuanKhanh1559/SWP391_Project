package controller;

import dal.CartDao;
import dal.ProductDao;
import model.User;
import model.CartItem;
import model.Product;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class CheckoutPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        List<CartItem> cartItems = null;
        String directParam = request.getParameter("direct");
        
        if ("true".equals(directParam)) {
            String productIdStr = request.getParameter("productId");
            String quantityStr = request.getParameter("quantity");
            
            if (productIdStr != null && quantityStr != null) {
                try {
                    int productId = Integer.parseInt(productIdStr);
                    int quantity = Integer.parseInt(quantityStr);
                    
                    ProductDao productDao = new ProductDao();
                    Product product = productDao.getProductById(productId, false);
                    
                    if (product != null) {
                        CartItem directItem = new CartItem();
                        directItem.setProduct_id(productId);
                        directItem.setProduct(product);
                        directItem.setQuantity(quantity);
                        directItem.setUnit_price(product.getPrice());
                        
                        cartItems = new ArrayList<>();
                        cartItems.add(directItem);
                        
                        request.setAttribute("directPurchase", true);
                    }
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath() + "/products");
                    return;
                }
            }
        } else {
            CartDao cartDao = new CartDao();
            cartItems = cartDao.getCartByUserId(user.getId());
        }

        if (cartItems == null || cartItems.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/user/cart");
            return;
        }

        double totalAmount = 0;
        for (CartItem item : cartItems) {
            totalAmount += item.getUnit_price() * item.getQuantity();
        }

        if (user.getBalance() < totalAmount) {
            request.setAttribute("insufficientBalance", true);
            request.setAttribute("totalAmount", totalAmount);
            request.setAttribute("userBalance", user.getBalance());
            request.setAttribute("cartItems", cartItems);
            request.getRequestDispatcher("/user/checkout.jsp").forward(request, response);
            return;
        }

        request.setAttribute("cartItems", cartItems);
        request.getRequestDispatcher("/user/checkout.jsp").forward(request, response);
    }
}

