package controller;

import dao.OrderDAO;
import model.Order;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

// Định nghĩa servlet xử lý yêu cầu cập nhật trạng thái đơn hàng
@WebServlet("/UpdateOrderStatusServlet")
public class UpdateOrderStatusServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy thông tin orderId từ request và chuyển đổi sang số nguyên
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        // Lấy trạng thái đơn hàng mới từ request
        String status = request.getParameter("status");

        // Kiểm tra tính hợp lệ của trạng thái (chỉ cho phép 3 giá trị: pending, completed, canceled)
        if (!status.equals("pending") && !status.equals("completed") && !status.equals("canceled")) {
            // Nếu trạng thái không hợp lệ, đặt thông báo lỗi và chuyển tiếp về trang quản lý đơn hàng
            request.setAttribute("errorMessage", "Invalid status provided.");
            request.getRequestDispatcher("manage-orders.jsp").forward(request, response);
            return;
        }

        // Tạo đối tượng OrderDAO để thao tác với cơ sở dữ liệu đơn hàng
        OrderDAO orderDAO = new OrderDAO();
        // Gọi phương thức cập nhật trạng thái đơn hàng và lưu kết quả trả về (true nếu thành công)
        boolean success = orderDAO.updateOrderStatus(orderId, status);

        // Kiểm tra kết quả cập nhật trạng thái đơn hàng
        if (success) {
            // Nếu cập nhật thành công, chuyển hướng về trang quản lý đơn hàng kèm thông báo thành công
            response.sendRedirect("manage-orders.jsp?message=Order status updated successfully");
        } else {
            // Nếu cập nhật thất bại, đặt thông báo lỗi và chuyển tiếp về trang quản lý đơn hàng
            request.setAttribute("errorMessage", "Failed to update order status.");
            request.getRequestDispatcher("manage-orders.jsp").forward(request, response);
        }
    }
}
