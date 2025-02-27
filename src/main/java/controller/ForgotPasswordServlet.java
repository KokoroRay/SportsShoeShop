package controller;

import dao.UserDAO;
import model.User;
import utils.EmailUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Random;

// Định nghĩa servlet xử lý yêu cầu quên mật khẩu
@WebServlet("/ForgotPasswordServlet")
public class ForgotPasswordServlet extends HttpServlet {
    // Khởi tạo đối tượng UserDAO để tương tác với cơ sở dữ liệu người dùng
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy email người dùng gửi lên từ form
        String email = request.getParameter("email");

        try {
            // Lấy thông tin người dùng từ cơ sở dữ liệu dựa trên email để lấy tên người dùng
            User user = userDAO.getUserByEmail(email);
            if (user == null) {
                // Nếu không tìm thấy người dùng, đặt thông báo lỗi và chuyển tiếp về trang quên mật khẩu
                request.setAttribute("errorMessage", "Email not found. Please try again.");
                request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
                return;
            }

            // Lấy tên người dùng từ đối tượng User
            String username = user.getUserName();

            // Tạo mã xác nhận ngẫu nhiên 6 chữ số
            String verificationCode = String.format("%06d", new Random().nextInt(999999));
            // Lưu mã xác nhận và email vào session để sử dụng sau này
            request.getSession().setAttribute("verificationCode", verificationCode);
            request.getSession().setAttribute("email", email);

            // Soạn nội dung email gồm tên người dùng và mã xác nhận
            String emailContent = "Hello " + username + ",\n\n" +
                    "You requested to reset your password.\n" +
                    "Your username is: " + username + "\n" +
                    "Your password reset verification code is: " + verificationCode + "\n\n" +
                    "If you did not request this reset, please ignore this email.\n\n" +
                    "Thank you.";

            // Gửi email chứa mã xác nhận đến địa chỉ email người dùng
            EmailUtil.sendEmail(email, "Password Reset Code", emailContent);

            // Đặt thông báo thành công và chuyển tiếp về trang nhập mã xác nhận để reset mật khẩu
            request.setAttribute("successMessage", "A reset code has been sent to your email.");
            request.getRequestDispatcher("reset-password.jsp").forward(request, response);
        } catch (Exception e) {
            // In lỗi ra console và đặt thông báo lỗi, chuyển tiếp về trang quên mật khẩu
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred. Please try again.");
            request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
        }
    }
}
