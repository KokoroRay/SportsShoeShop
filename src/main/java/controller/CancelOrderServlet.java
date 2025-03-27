package controller;

import dao.NotificationDAO;
import dao.OrderDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Notification;

@WebServlet(name = "CancelOrderServlet", urlPatterns = {"/CancelOrderServlet"})
public class CancelOrderServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));

            // Kiểm tra đơn hàng có tồn tại không
            Notification notification = notificationDAO.getNotificationByOrderId(orderId);
            if (notification == null) {
                response.sendRedirect("orderStatus.jsp?error=Order not found");
                return;
            }

            // Kiểm tra trạng thái đơn hàng trước khi hủy
            if (notification.getStatus().equalsIgnoreCase("completed") || 
                notification.getStatus().equalsIgnoreCase("canceled")) {
                response.sendRedirect("orderStatus.jsp?error=Cannot cancel this order");
                return;
            }

            // Cập nhật trạng thái đơn hàng thành "canceled"
            boolean isUpdated = orderDAO.updateOrderStatus(orderId, "canceled");

            if (isUpdated) {
                // Thêm thông báo hủy đơn vào Notifications
                notificationDAO.createNotification(
                        orderId, notification.getUserId(),
                        "Your order has been canceled.",
                        "canceled",
                        notification.getFullName(), notification.getPhoneNumber(),
                        notification.getAddress(), notification.getPaymentMethod(),
                        notification.getOrderDetails(), notification.getTotalPrice());

                response.sendRedirect("orderStatus.jsp?success=Order canceled successfully");
            } else {
                response.sendRedirect("orderStatus.jsp?error=Failed to cancel order");
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("orderStatus.jsp?error=Invalid order ID");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("orderStatus.jsp?error=Unexpected error");
        }
    }
}
