package controller;

import dao.OrderDAO;
import model.Order;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

// Định nghĩa servlet xử lý yêu cầu hiển thị lịch sử mua hàng của người dùng
@WebServlet("/PurchaseHistoryServlet")
public class PurchaseHistoryServlet extends HttpServlet {

    // Phương thức xử lý yêu cầu GET từ phía client
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy phiên (session) hiện tại
        HttpSession session = request.getSession();
        // Lấy đối tượng người dùng đã đăng nhập từ session
        User loggedInUser = (User) session.getAttribute("user");

        // Nếu người dùng chưa đăng nhập, chuyển hướng đến trang đăng nhập
        if (loggedInUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Lấy ID của người dùng đã đăng nhập
        int userId = loggedInUser.getUserId();
        // Tạo đối tượng OrderDAO để thao tác với cơ sở dữ liệu đơn hàng
        OrderDAO orderDAO = new OrderDAO();
        // Lấy danh sách các đơn hàng dựa trên userId
        List<Order> userOrders = orderDAO.getOrdersByUserId(userId);

        // Đưa danh sách đơn hàng vào thuộc tính của request để gửi về trang hiển thị
        request.setAttribute("userOrders", userOrders);
        // Chuyển tiếp request và response đến trang purchase-history.jsp để hiển thị lịch sử mua hàng
        request.getRequestDispatcher("purchase-history.jsp").forward(request, response);
    }
}
