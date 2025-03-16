package controller;

import dao.NotificationDAO;
import dao.OrderDAO;
import model.Notification;
import model.User;
import utils.EmailUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ConfirmOrderServlet", urlPatterns = {"/ConfirmOrderServlet"})
public class ConfirmOrderServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int notificationId = Integer.parseInt(request.getParameter("notificationId"));
            String status = request.getParameter("status");

            if (!"completed".equals(status) && !"canceled".equals(status)) {
                response.sendRedirect(request.getContextPath() + "/adminNotifications.jsp?error=Invalid status provided");
                return;
            }

            Notification notification = notificationDAO.getNotificationById(notificationId);

            if (notification == null) {
                response.sendRedirect(request.getContextPath() + "/adminNotifications.jsp?error=Notification not found");
                return;
            }

            int orderId = notification.getOrderId();
            int userId = notification.getUserId();

            boolean orderUpdated = orderDAO.updateOrderStatus(orderId, status);

            if (orderUpdated) {
                notificationDAO.createNotification(orderId, userId,
                        "completed".equals(status) ? "Your order has been confirmed!" : "Your order has been canceled!",
                        status, notification.getFullName(), notification.getPhoneNumber(),
                        notification.getAddress(), notification.getPaymentMethod(),
                        notification.getOrderDetails(), notification.getTotalPrice());

                if ("completed".equals(status)) {
                    User user = notificationDAO.getUserByNotificationId(notificationId);

                    if (user != null) {
                        String emailContent = buildOrderConfirmationMessage(
                                notification.getFullName(),
                                notification.getPhoneNumber(),
                                notification.getAddress(),
                                notification.getPaymentMethod(),
                                notification.getOrderDetails(),
                                notification.getTotalPrice()
                        );

                        EmailUtil.sendEmail(user.getEmail(), "Order Confirmed - Group 7 Shop", emailContent);
                        System.out.println("Email sent successfully to: " + user.getEmail());
                    } else {
                        System.err.println("User not found for notificationId: " + notificationId);
                    }
                }

                response.sendRedirect(request.getContextPath() + "/manage-orders.jsp?success=Order confirmed");
            } else {
                response.sendRedirect(request.getContextPath() + "/adminNotifications.jsp?error=Failed to update order");
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/adminNotifications.jsp?error=Invalid notification ID");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/adminNotifications.jsp?error=Unexpected error");
        }
    }

    private String buildOrderConfirmationMessage(String fullName, String phoneNumber, String address,
            String paymentMethod, String orderDetails, double totalPrice) {
        StringBuilder message = new StringBuilder();
        message.append("Hello ").append(fullName).append(",\n\n");
        message.append("Thank you for your order! Here are your order details:\n\n");

        message.append("Shipping Information:\n");
        message.append("- Name: ").append(fullName).append("\n");
        message.append("- Phone: ").append(phoneNumber).append("\n");
        message.append("- Address: ").append(address).append("\n");
        message.append("- Payment Method: ").append(paymentMethod).append("\n\n");

        message.append("Order Details:\n").append(orderDetails).append("\n");
        message.append("Total Amount: $").append(totalPrice).append("\n\n");
        message.append("Your order will be processed and shipped soon.\n");
        message.append("Thank you for shopping with us!\n");

        return message.toString();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet handling order confirmation and sending email notifications.";
    }
}
