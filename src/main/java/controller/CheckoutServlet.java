package controller;

import model.CartItem;
import model.User;
import dao.OrderDAO;
import utils.EmailUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {

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

        // Lấy thông tin địa chỉ và phương thức thanh toán
        String fullName = request.getParameter("fullName");
        String phoneNumber = request.getParameter("phoneNumber");
        String address = request.getParameter("address");
        String paymentMethod = request.getParameter("paymentMethod");

        double total = 0;
        for (CartItem item : cart) {
            total += item.getTotalPrice();
        }

        OrderDAO orderDAO = new OrderDAO();
        boolean orderSaved = orderDAO.saveOrder(user, cart, total);

        if (orderSaved) {
            session.removeAttribute("cart");

            // Gửi email xác nhận
            String toEmail = user.getEmail();
            String subject = "Order Confirmation - Group 7 Shop";
            String messageContent = buildOrderConfirmationMessage(user, fullName, phoneNumber, address, paymentMethod, cart, total);
            EmailUtil.sendEmail(toEmail, subject, messageContent);

            response.sendRedirect("order-confirmation.jsp");
        } else {
            response.sendRedirect("viewcart.jsp?error=payment_failed");
        }
    }

    private String buildOrderConfirmationMessage(User user, String fullName, String phoneNumber, String address, String paymentMethod, List<CartItem> cart, double total) {
        StringBuilder message = new StringBuilder();
        message.append("Hello ").append(fullName).append(",\n\n");
        message.append("Thank you for your order! Here are your order details:\n\n");
        
        message.append("Shipping Information:\n");
        message.append("- Name: ").append(fullName).append("\n");
        message.append("- Phone: ").append(phoneNumber).append("\n");
        message.append("- Address: ").append(address).append("\n");
        message.append("- Payment Method: ").append(paymentMethod).append("\n\n");
        
        message.append("Order Details:\n");
        for (CartItem item : cart) {
            message.append("- ").append(item.getProduct_Name())
                   .append(" (Size: ").append(item.getSize())
                   .append(", Quantity: ").append(item.getQuantity())
                   .append("): $").append(item.getTotalPrice()).append("\n");
        }
        
        message.append("\nTotal Amount: $").append(total).append("\n\n");
        message.append("Your order will be processed and shipped soon.\n");
        message.append("Thank you for shopping with us!\n");
        
        return message.toString();
    }
}
