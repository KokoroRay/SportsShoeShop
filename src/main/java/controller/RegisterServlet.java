package controller;

import dao.UserDAO;
import model.User;
import utils.EmailUtil;
import utils.PasswordUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

// Định nghĩa servlet xử lý yêu cầu đăng ký tài khoản người dùng
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    // Khởi tạo đối tượng UserDAO để tương tác với cơ sở dữ liệu người dùng
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy dữ liệu đăng ký từ form gửi lên
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String dob = request.getParameter("dob");

        try {
            // Kiểm tra xem tên đăng nhập đã tồn tại chưa
            if (userDAO.getUserByUsername(username) != null) {
                // Nếu tồn tại, đặt thông báo lỗi và chuyển hướng về trang đăng ký
                request.setAttribute("errorMessage", "Username already exists. Please use another one.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            // Kiểm tra xem email đã được đăng ký chưa
            if (userDAO.getUserByEmail(email) != null) {
                // Nếu email đã tồn tại, đặt thông báo lỗi và chuyển hướng về trang đăng ký
                request.setAttribute("errorMessage", "Email already registered. Please use another email.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
            String hashedPassword = PasswordUtils.hashPassword(password);

            // Tạo đối tượng User mới và gán các thông tin đăng ký
            User user = new User();
            user.setUserName(username);
            user.setEmail(email);
            user.setPassword(hashedPassword);
            user.setPhone(phone);
            user.setAddress(address);
            user.setDob(dob);
            user.setRole("customer"); // Phân quyền mặc định là "customer" cho khách hàng

            // Thêm người dùng mới vào cơ sở dữ liệu
            if (userDAO.insertUser(user)) {
                // Nếu đăng ký thành công, gửi email chào mừng tới người dùng
                EmailUtil.sendEmail(email, "Welcome to Our Shoe Store", "Thank you for registering with us!");

                // Chuyển hướng người dùng tới trang đăng nhập sau khi đăng ký thành công
                response.sendRedirect("login.jsp");
            } else {
                // Nếu không thể thêm người dùng vào cơ sở dữ liệu, đặt thông báo lỗi và chuyển về trang đăng ký
                request.setAttribute("errorMessage", "An error occurred. Please try again.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            // Bắt ngoại lệ và in lỗi ra console để kiểm tra
            e.printStackTrace();
            // Đặt thông báo lỗi chung và chuyển hướng về trang đăng ký
            request.setAttribute("errorMessage", "An unexpected error occurred. Please try again.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}
