package controller;

import dao.UserDAO;
import utils.PasswordUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

// Định nghĩa servlet xử lý yêu cầu đặt lại mật khẩu
@WebServlet("/ResetPasswordServlet")
public class ResetPasswordServlet extends HttpServlet {
    // Khởi tạo đối tượng UserDAO để tương tác với cơ sở dữ liệu người dùng
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy phiên (session) hiện tại của người dùng
        HttpSession session = request.getSession();
        // Lấy email và mã xác nhận đã lưu trong session từ quá trình reset mật khẩu
        String email = (String) session.getAttribute("email");
        String verificationCode = (String) session.getAttribute("verificationCode");

        // Lấy mã xác nhận do người dùng nhập và mật khẩu mới từ form
        String enteredCode = request.getParameter("code");
        String newPassword = request.getParameter("newPassword");

        // Nếu email hoặc mã xác nhận không có trong session, chuyển hướng về trang quên mật khẩu
        if (email == null || verificationCode == null) {
            response.sendRedirect("forgot-password.jsp");
            return;
        }

        try {
            // Kiểm tra xem mã xác nhận nhập vào có khớp với mã lưu trong session không
            if (!verificationCode.equals(enteredCode)) {
                // Nếu không khớp, đặt thông báo lỗi và chuyển tiếp về trang reset mật khẩu để người dùng thử lại
                request.setAttribute("errorMessage", "Invalid verification code.");
                request.getRequestDispatcher("reset-password.jsp").forward(request, response);
                return;
            }

            // Cập nhật mật khẩu mới vào cơ sở dữ liệu
            // Mã hóa mật khẩu mới trước khi lưu để đảm bảo an toàn
            userDAO.updatePassword(email, PasswordUtils.hashPassword(newPassword));

            // Xóa các thuộc tính trong session liên quan đến quá trình đặt lại mật khẩu
            session.removeAttribute("email");
            session.removeAttribute("verificationCode");

            // Chuyển hướng người dùng về trang đăng nhập kèm theo thông báo thành công
            response.sendRedirect("login.jsp?successMessage=Password reset successfully. Please log in.");
        } catch (Exception e) {
            // Nếu xảy ra lỗi, in thông báo lỗi ra console để kiểm tra
            e.printStackTrace();
            // Đặt thông báo lỗi và chuyển tiếp về trang reset mật khẩu để người dùng thử lại
            request.setAttribute("errorMessage", "Email already registered, please login!");
            request.getRequestDispatcher("reset-password.jsp").forward(request, response);
        }
    }
}
