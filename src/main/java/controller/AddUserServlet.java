package controller;
import dao.UserDAO;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/AddUserServlet")
public class AddUserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Khởi tạo đối tượng UserDAO để tương tác với cơ sở dữ liệu người dùng
        UserDAO userDAO = new UserDAO();

        // Lấy các tham số từ form gửi lên từ client
        String username = request.getParameter("username"); // Tên đăng nhập
        String email = request.getParameter("email");       // Địa chỉ email
        String password = request.getParameter("password"); // Mật khẩu (cần xem xét việc mã hóa)
        String phone = request.getParameter("phone");       // Số điện thoại
        String address = request.getParameter("address");   // Địa chỉ
        String dob = request.getParameter("dob");           // Ngày sinh
        String role = request.getParameter("role");         // Quyền (role)

        // Tạo đối tượng User mới và thiết lập các thuộc tính với dữ liệu lấy từ form
        User newUser = new User();
        newUser.setUserName(username); // Gán tên người dùng
        newUser.setEmail(email);       // Gán email
        newUser.setPassword(password); // Gán mật khẩu (có thể mã hóa mật khẩu trước khi lưu)
        newUser.setPhone(phone);       // Gán số điện thoại
        newUser.setAddress(address);   // Gán địa chỉ
        newUser.setDob(dob);           // Gán ngày sinh
        newUser.setRole(role);         // Gán quyền

        try {
            // Kiểm tra xem tên người dùng đã tồn tại hay chưa
            if (userDAO.usernameExists(username)) {
                request.setAttribute("errorMessage", "A user with this username already exists.");
            } 
            // Kiểm tra xem email đã tồn tại hay chưa
            else if (userDAO.emailExists(email)) {
                request.setAttribute("errorMessage", "A user with this email already exists.");
            } else {
                // Nếu không có trùng lặp, tiến hành chèn người dùng mới vào cơ sở dữ liệu
                boolean isUserAdded = userDAO.insertUser(newUser);

                if (isUserAdded) {
                    // Nếu thêm thành công, đặt thông báo thành công
                    request.setAttribute("successMessage", "User added successfully.");
                } else {
                    // Nếu thêm không thành công, đặt thông báo lỗi
                    request.setAttribute("errorMessage", "Failed to add user. Please try again.");
                }
            }
        } catch (Exception e) {
            // Ghi lại ngoại lệ ra console và đặt thông báo lỗi chung
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while adding the user. Please try again later.");
        }

        // Chuyển tiếp yêu cầu về trang add-user-for-admin.jsp kèm theo thông báo thành công hoặc lỗi
        request.getRequestDispatcher("add-user-for-admin.jsp").forward(request, response);
    }
}
