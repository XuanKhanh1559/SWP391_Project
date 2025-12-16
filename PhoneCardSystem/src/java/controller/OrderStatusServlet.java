package controller;

import dal.OrderIntentDao;
import model.OrderIntent;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class OrderStatusServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<>();

        try {
            String intentIdParam = request.getParameter("intentId");
            if (intentIdParam == null || intentIdParam.isEmpty()) {
                result.put("success", false);
                result.put("error", "Missing intentId");
                out.print(gson.toJson(result));
                return;
            }

            int intentId = Integer.parseInt(intentIdParam);
            OrderIntentDao intentDao = new OrderIntentDao();
            OrderIntent intent = intentDao.getIntentById(intentId);

            if (intent == null) {
                result.put("success", false);
                result.put("error", "Order intent not found");
                out.print(gson.toJson(result));
                return;
            }

            result.put("success", true);
            result.put("status", intent.getStatus());
            result.put("orderId", intent.getOrderId());
            result.put("errorMessage", intent.getErrorMessage());

        } catch (Exception e) {
            result.put("success", false);
            result.put("error", "Error: " + e.getMessage());
            e.printStackTrace();
        }

        out.print(gson.toJson(result));
    }
}

