package controller;

import dao.NotificationDAO;
import dao.OrderDAO;
import model.Order;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import model.Notification;

// Định nghĩa servlet xử lý yêu cầu cập nhật trạng thái đơn hàng
@WebServlet("/UpdateOrderStatusServlet")
public class UpdateOrderStatusServlet extends HttpServlet {

    private OrderDAO orderDAO = new OrderDAO();
    private NotificationDAO notificationDAO = new NotificationDAO();

// Kiểm tra nếu status không được cập nhật thành công, thêm logging để kiểm tra lỗi
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int notificationId = Integer.parseInt(request.getParameter("notificationId"));
            String status = request.getParameter("status");

            Notification notification = notificationDAO.getNotificationById(notificationId);
            if (notification == null) {
                response.sendRedirect(request.getContextPath() + "/adminNotifications.jsp?error=Notification not found");
                return;
            }

            int orderId = notification.getOrderId();
            int userId = notification.getUserId();

            boolean orderUpdated = orderDAO.updateOrderStatus(orderId, status);

            if (orderUpdated) {
                String message = status.equals("completed")
                        ? "Your order is on its way!"
                        : "Your order has been cancelled!";

                notificationDAO.createNotification(
                        orderId,
                        userId,
                        message,
                        status,
                        notification.getFullName(),
                        notification.getPhoneNumber(),
                        notification.getAddress(),
                        notification.getPaymentMethod(),
                        notification.getOrderDetails(),
                        notification.getTotalPrice()
                );

                String redirectUrl = status.equals("completed")
                        ? "/manage-orders.jsp?success=Order confirmed"
                        : "/adminNotifications.jsp?success=Order canceled";
                response.sendRedirect(request.getContextPath() + redirectUrl);
            } else {
                System.err.println("Order update failed for Order ID: " + orderId + " with status: " + status);
                response.sendRedirect(request.getContextPath() + "/adminNotifications.jsp?error=Failed to update order");
            }

        } catch (NumberFormatException e) {
            System.err.println("Invalid notification ID provided.");
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/adminNotifications.jsp?error=Invalid notification ID");
        } catch (Exception e) {
            System.err.println("Unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/adminNotifications.jsp?error=Unexpected error");
        }
    }
}
