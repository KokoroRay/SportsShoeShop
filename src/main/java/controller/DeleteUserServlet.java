package controller;

import dao.UserDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/DeleteUserServlet")
public class DeleteUserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Lấy tham số userId từ request và chuyển đổi sang kiểu số nguyên
            int userId = Integer.parseInt(request.getParameter("userId"));
            
            // Tạo đối tượng UserDAO để thao tác với dữ liệu người dùng trong cơ sở dữ liệu
            UserDAO userDAO = new UserDAO();
            // Gọi phương thức deleteUser để xóa người dùng theo userId
            userDAO.deleteUser(userId);
            
            // Chuyển hướng về trang quản lý người dùng sau khi xóa thành công
            response.sendRedirect("manage-users.jsp");
        } catch (SQLException ex) {
            // Ghi log lỗi nếu có SQLException xảy ra
            Logger.getLogger(DeleteUserServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
