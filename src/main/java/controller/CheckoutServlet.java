package controller;

import dao.NotificationDAO;
import dao.OrderDAO;
import model.CartItem;
import model.User;
import utils.EmailUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import model.Notification;

@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {

    private OrderDAO orderDAO = new OrderDAO();
    private NotificationDAO notificationDAO = new NotificationDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (user == null || cart == null || cart.isEmpty()) {
            response.sendRedirect("viewcart.jsp");
            return;
        }

        String fullName = request.getParameter("fullName");
        String phoneNumber = request.getParameter("phoneNumber");
        String address = request.getParameter("address");
        String paymentMethod = request.getParameter("paymentMethod");

        double totalPrice = cart.stream().mapToDouble(CartItem::getTotalPrice).sum();

        boolean orderSaved = orderDAO.saveOrder(user, cart, totalPrice);

        if (orderSaved) {
            session.removeAttribute("cart");

            int orderId = orderDAO.getLastOrderId(user.getUserId());

            String orderDetails = "";
            for (CartItem item : cart) {
                orderDetails += item.getProduct_Name() + " (Size: " + item.getSize() + ", Quantity: " + item.getQuantity() + "): $" + item.getTotalPrice() + "\n";
            }

            notificationDAO.createNotification(
                    orderId, user.getUserId(), "New order #" + orderId + " awaiting confirmation.", "pending",
                    fullName, phoneNumber, address, paymentMethod, orderDetails, totalPrice
            );

            response.sendRedirect("orderStatus.jsp");
        } else {
            response.sendRedirect("viewcart.jsp?error=payment_failed");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if ("sendEmail".equals(request.getParameter("action"))) {
            try {
                int notificationId = Integer.parseInt(request.getParameter("notificationId"));
                Notification notification = notificationDAO.getNotificationById(notificationId);

                if (notification != null) {
                    User user = notificationDAO.getUserByNotificationId(notificationId);
                    if (user != null) {
                        String emailContent = buildOrderConfirmationMessage(notification);

                        // Gọi hàm sendEmail (void)
                        EmailUtil.sendEmail(user.getEmail(), "Order Confirmed - Group 7 Shop", emailContent);

                        // Luôn redirect về trang manage-orders.jsp và hiển thị thông báo thành công
                        response.sendRedirect(request.getContextPath() + "/manage-orders.jsp?success=Email sent");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/manage-orders.jsp?error=User not found");
                    }
                } else {
                    response.sendRedirect(request.getContextPath() + "/manage-orders.jsp?error=Notification not found");
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/manage-orders.jsp?error=Invalid notification ID");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/manage-orders.jsp?error=Unexpected error");
            }
        }
    }

    private String buildOrderConfirmationMessage(Notification notification) {
        StringBuilder message = new StringBuilder();
        message.append("Hello ").append(notification.getFullName()).append(",\n\n");
        message.append("Thank you for your order! Here are your order details:\n\n");
        message.append("Shipping Information:\n");
        message.append("- Name: ").append(notification.getFullName()).append("\n");
        message.append("- Phone: ").append(notification.getPhoneNumber()).append("\n");
        message.append("- Address: ").append(notification.getAddress()).append("\n");
        message.append("- Payment Method: ").append(notification.getPaymentMethod()).append("\n\n");
        message.append("Order Details:\n").append(notification.getOrderDetails()).append("\n");
        message.append("Total Amount: $").append(notification.getTotalPrice()).append("\n\n");
        message.append("Your order will be processed and shipped soon.\n");
        message.append("Thank you for shopping with us!\n");
        return message.toString();
    }
}
