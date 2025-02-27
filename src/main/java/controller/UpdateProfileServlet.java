package controller;

import dao.UserDAO;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/UpdateProfileServlet")
public class UpdateProfileServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy đối tượng User từ session
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Lấy thông tin cập nhật từ form
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String dob = request.getParameter("dob");

        // Cập nhật đối tượng user
        user.setPhone(phone);
        user.setAddress(address);
        user.setDob(dob);

        try {
            // Cập nhật thông tin trong cơ sở dữ liệu
            if (userDAO.updateUser(user)) {
                session.setAttribute("user", user);  // Cập nhật thông tin user trong session
                response.sendRedirect("profile.jsp");  // Điều hướng về profile.jsp
            } else {
                request.setAttribute("errorMessage", "Failed to update profile.");
                request.getRequestDispatcher("profile.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred. Please try again.");
            request.getRequestDispatcher("profile.jsp").forward(request, response);
        }
    }
}
