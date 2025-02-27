package controller;

import dao.UserDAO;
import model.User;
import utils.PasswordUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy username và password từ form đăng nhập
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            // Kiểm tra người dùng trong cơ sở dữ liệu bằng username
            User user = userDAO.getUserByUsername(username);

            // Nếu thông tin đăng nhập đúng
            if (user != null && user.getPassword().equals(PasswordUtils.hashPassword(password))) {
                // Tạo session cho người dùng
                HttpSession session = request.getSession();
                session.setAttribute("user", user);  // Lưu đối tượng User vào session

                // Điều hướng đến trang admin hoặc khách hàng
                if ("admin".equals(user.getRole())) {
                    response.sendRedirect("dashboard.jsp");
                } else {
                    response.sendRedirect("home.jsp");
                }
            } else {
                // Sai thông tin đăng nhập, trả về trang đăng nhập với lỗi
                request.setAttribute("errorMessage", "Invalid username or password.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred. Please try again.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }
}
