package controller;

import dao.UserDAO;
import model.User;
import utils.PasswordUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/ChangePasswordServlet")
public class ChangePasswordServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Nếu người dùng chưa đăng nhập, chuyển hướng đến trang đăng nhập
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Lấy mật khẩu hiện tại và mật khẩu mới từ form
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");

        try {
            // Kiểm tra mật khẩu hiện tại có khớp không
            String hashedCurrentPassword = PasswordUtils.hashPassword(currentPassword);
            if (!hashedCurrentPassword.equals(user.getPassword())) {
                // Nếu mật khẩu hiện tại không khớp
                request.setAttribute("errorMessage", "Current password is incorrect.");
                request.getRequestDispatcher("profile.jsp").forward(request, response);
                return;
            }

            // Cập nhật mật khẩu mới vào cơ sở dữ liệu
            String hashedNewPassword = PasswordUtils.hashPassword(newPassword);
            if (userDAO.updatePassword(user.getEmail(), hashedNewPassword)) {
                // Cập nhật mật khẩu mới trong session
                user.setPassword(hashedNewPassword);
                session.setAttribute("user", user);

                request.setAttribute("successMessage", "Password changed successfully.");
            } else {
                request.setAttribute("errorMessage", "Failed to change password. Please try again.");
            }
            request.getRequestDispatcher("profile.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred. Please try again.");
            request.getRequestDispatcher("profile.jsp").forward(request, response);
        }
    }
}
