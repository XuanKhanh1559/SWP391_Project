package util;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

public class ResponseUtil {
    
    /**
     * Set UTF-8 encoding for JSON response
     * Must be called before getting PrintWriter
     */
    public static void setJsonResponseEncoding(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
    }
    
    /**
     * Write JSON response with UTF-8 encoding
     */
    public static void writeJsonResponse(HttpServletResponse response, String json) throws IOException {
        setJsonResponseEncoding(response);
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
    
    /**
     * Escape string for JSON (handles Vietnamese characters)
     */
    public static String escapeJsonString(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("\\", "\\\\")
                 .replace("\"", "\\\"")
                 .replace("\n", "\\n")
                 .replace("\r", "\\r")
                 .replace("\t", "\\t");
    }
}

