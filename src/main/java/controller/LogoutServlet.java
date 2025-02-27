package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Hủy session hiện tại
        HttpSession session = request.getSession(false);  // Lấy session hiện tại (không tạo mới)
        if (session != null) {
            session.invalidate();  // Hủy session
        }

        // Chuyển hướng về trang chủ sau khi đăng xuất
        response.sendRedirect("home.jsp");
    }
}