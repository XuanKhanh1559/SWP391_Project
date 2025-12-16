package controller;

import dal.*;
import model.*;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<>();

        try {
            CartDao cartDao = new CartDao();
            ProductDao productDao = new ProductDao();
            CouponDao couponDao = new CouponDao();

            List<CartItem> cartItems;
            String directParam = request.getParameter("direct");
            
            if ("true".equals(directParam)) {
                String productIdStr = request.getParameter("productId");
                String quantityStr = request.getParameter("quantity");
                
                if (productIdStr == null || quantityStr == null) {
                    result.put("success", false);
                    result.put("error", "Thông tin sản phẩm không hợp lệ");
                    out.print(gson.toJson(result));
                    return;
                }
                
                int productId = Integer.parseInt(productIdStr);
                int quantity = Integer.parseInt(quantityStr);
                
                Product product = productDao.getProductById(productId, false);
                if (product == null) {
                    result.put("success", false);
                    result.put("error", "Sản phẩm không tồn tại");
                    out.print(gson.toJson(result));
                    return;
                }
                
                CartItem directItem = new CartItem();
                directItem.setProduct_id(productId);
                directItem.setProduct(product);
                directItem.setQuantity(quantity);
                directItem.setUnit_price(product.getPrice());
                
                cartItems = new ArrayList<>();
                cartItems.add(directItem);
            } else {
                cartItems = cartDao.getCartByUserId(user.getId());
                
                if (cartItems == null || cartItems.isEmpty()) {
                    result.put("success", false);
                    result.put("error", "Giỏ hàng trống");
                    out.print(gson.toJson(result));
                    return;
                }
            }

            double subtotal = 0;
            List<CreateOrderPayload.CartItemData> payloadItems = new ArrayList<>();

            for (CartItem item : cartItems) {
                Product product = productDao.getProductById(item.getProduct_id(), false);
                if (product == null) {
                    result.put("success", false);
                    result.put("error", "Sản phẩm không tồn tại");
                    out.print(gson.toJson(result));
                    return;
                }

                if (product.getStock() < item.getQuantity()) {
                    result.put("success", false);
                    result.put("error", "Sản phẩm " + product.getName() + " không đủ số lượng (còn: " + product.getStock() + ")");
                    out.print(gson.toJson(result));
                    return;
                }

                double itemTotal = product.getPrice() * item.getQuantity();
                subtotal += itemTotal;

                CreateOrderPayload.CartItemData payloadItem = new CreateOrderPayload.CartItemData();
                payloadItem.setProductId(product.getId());
                payloadItem.setQuantity(item.getQuantity());
                payloadItem.setPrice(product.getPrice());
                payloadItems.add(payloadItem);
            }

            String couponCode = request.getParameter("coupon_code");
            double discountAmount = 0;
            Coupon coupon = null;

            if (couponCode != null && !couponCode.trim().isEmpty()) {
                coupon = couponDao.getCouponByCode(couponCode);
                if (coupon == null) {
                    result.put("success", false);
                    result.put("error", "Mã giảm giá không hợp lệ");
                    out.print(gson.toJson(result));
                    return;
                }

                if (!couponDao.canUserUseCoupon(user.getId(), coupon.getId())) {
                    result.put("success", false);
                    result.put("error", "Bạn đã sử dụng hết lượt dùng mã này");
                    out.print(gson.toJson(result));
                    return;
                }

                if (subtotal < coupon.getMin_order_amount()) {
                    result.put("success", false);
                    result.put("error", "Đơn hàng chưa đủ giá trị tối thiểu");
                    out.print(gson.toJson(result));
                    return;
                }

                if (coupon.getDiscount_type() == 1) {
                    discountAmount = subtotal * (coupon.getDiscount_value() / 100.0);
                    if (coupon.getMax_discount_amount() != null && discountAmount > coupon.getMax_discount_amount()) {
                        discountAmount = coupon.getMax_discount_amount();
                    }
                } else {
                    discountAmount = coupon.getDiscount_value();
                }
            }

            double totalAmount = subtotal - discountAmount;

            CreateOrderPayload payload = new CreateOrderPayload();
            payload.setUserId(user.getId());
            payload.setCartItems(payloadItems);
            payload.setCouponCode(couponCode);
            payload.setSubtotalAmount(subtotal);
            payload.setDiscountAmount(discountAmount);
            payload.setTotalAmount(totalAmount);

            Map<String, Object> jobData = new HashMap<>();
            jobData.put("type", "CREATE_ORDER");
            jobData.put("data", gson.toJson(payload));
            String jobPayload = gson.toJson(jobData);

            JobDao jobDao = new JobDao();
            int jobId = jobDao.dispatchJob("default", jobPayload);

            if (jobId > 0) {
                OrderIntent intent = new OrderIntent();
                intent.setUserId(user.getId());
                intent.setJobId(jobId);
                intent.setStatus("PENDING");
                intent.setCartItems(gson.toJson(payloadItems));
                intent.setCouponCode(couponCode);
                intent.setSubtotalAmount(subtotal);
                intent.setDiscountAmount(discountAmount);
                intent.setTotalAmount(totalAmount);

                OrderIntentDao intentDao = new OrderIntentDao();
                int intentId = intentDao.createOrderIntent(intent);

                if (intentId > 0) {
                    result.put("success", true);
                    result.put("intentId", intentId);
                    result.put("message", "Đơn hàng đang được xử lý");
                } else {
                    result.put("success", false);
                    result.put("error", "Không thể tạo order intent");
                }
            } else {
                result.put("success", false);
                result.put("error", "Không thể dispatch job");
            }

        } catch (Exception e) {
            result.put("success", false);
            result.put("error", "Lỗi: " + e.getMessage());
            e.printStackTrace();
        }

        out.print(gson.toJson(result));
    }
}

