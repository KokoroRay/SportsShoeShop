package controller;

import dao.UserDAO;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

// Định nghĩa servlet xử lý yêu cầu chỉnh sửa thông tin người dùng
@WebServlet("/EditUserServlet")
public class EditUserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy userId từ tham số request và chuyển đổi sang số nguyên
        int userId = Integer.parseInt(request.getParameter("userId"));
        // Lấy các thông tin khác của người dùng từ form gửi lên
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String role = request.getParameter("role");

        // Tạo đối tượng User mới và thiết lập các thuộc tính dựa trên dữ liệu nhận được
        User user = new User();
        user.setUserId(userId);
        user.setUserName(username);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRole(role);

        // Tạo đối tượng UserDAO để thao tác với cơ sở dữ liệu người dùng
        UserDAO userDAO = new UserDAO();
        
        try {
            // Gọi phương thức cập nhật thông tin người dùng trong cơ sở dữ liệu
            userDAO.updateUser(user);  // Xử lý ngoại lệ SQLException nếu có lỗi xảy ra trong quá trình cập nhật
            // Nếu cập nhật thành công, chuyển hướng về trang quản lý người dùng
            response.sendRedirect("manage-users.jsp");
        } catch (SQLException e) {
            // In ra lỗi trên console để kiểm tra
            e.printStackTrace();
            // Thiết lập thông báo lỗi và chuyển hướng về trang chỉnh sửa người dùng để hiển thị thông báo lỗi
            request.setAttribute("errorMessage", "Error updating user. Please try again.");
            request.getRequestDispatcher("edit-user.jsp").forward(request, response);
        }
    }
}
