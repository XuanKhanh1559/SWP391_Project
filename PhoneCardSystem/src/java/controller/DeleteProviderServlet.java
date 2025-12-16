package controller;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dal.ProviderDao;
import model.User;

public class DeleteProviderServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        JsonObject result = new JsonObject();
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            result.addProperty("success", false);
            result.addProperty("message", "Vui lòng đăng nhập");
            out.print(result.toString());
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (!"admin".equalsIgnoreCase(user.getRole())) {
            result.addProperty("success", false);
            result.addProperty("message", "Bạn không có quyền thực hiện thao tác này");
            out.print(result.toString());
            return;
        }
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            result.addProperty("success", false);
            result.addProperty("message", "ID nhà cung cấp không hợp lệ");
            out.print(result.toString());
            return;
        }
        
        int id;
        try {
            id = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            result.addProperty("success", false);
            result.addProperty("message", "ID nhà cung cấp không hợp lệ");
            out.print(result.toString());
            return;
        }
        
        ProviderDao providerDao = new ProviderDao();
        
        if (providerDao.deleteProvider(id)) {
            result.addProperty("success", true);
            result.addProperty("message", "Xóa nhà cung cấp thành công");
        } else {
            result.addProperty("success", false);
            result.addProperty("message", "Có lỗi xảy ra khi xóa nhà cung cấp");
        }
        
        out.print(result.toString());
    }

    @Override
    public String getServletInfo() {
        return "Delete Provider Servlet";
    }
}

