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
        // Lấy giỏ hàng và người dùng từ session
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (user == null || cart == null || cart.isEmpty()) {
            // Nếu chưa đăng nhập hoặc giỏ hàng trống, chuyển hướng về trang giỏ hàng
            response.sendRedirect("viewcart.jsp");
            return;
        }

        // Tính tổng giá trị đơn hàng
        double total = 0;
        for (CartItem item : cart) {
            total += item.getTotalPrice();
        }

        // Lưu đơn hàng vào cơ sở dữ liệu
        OrderDAO orderDAO = new OrderDAO();
        boolean orderSaved = orderDAO.saveOrder(user, cart, total);

        if (orderSaved) {
            // Nếu đặt hàng thành công, xóa giỏ hàng khỏi session
            session.removeAttribute("cart");

            // Gửi email xác nhận
            String toEmail = user.getEmail();
            String subject = "Order confirmation from Group 4 Shop";
            String messageContent = buildOrderConfirmationMessage(user, cart, total);
            EmailUtil.sendEmail(toEmail, subject, messageContent);

            // Chuyển hướng đến trang xác nhận đơn hàng
            response.sendRedirect("order-confirmation.jsp");
        } else {
            response.sendRedirect("viewcart.jsp?error=payment_failed");
        }
    }

    // Hàm tạo nội dung email xác nhận đơn hàng
    private String buildOrderConfirmationMessage(User user, List<CartItem> cart, double total) {
        StringBuilder message = new StringBuilder();
        message.append("Hello!, ").append(user.getUserName()).append("\n\n");
        message.append("Thank you for purchasing at our shop.\n");
        message.append("Here are your order details:\n\n");

        message.append("Product:\n");
        for (CartItem item : cart) {
            message.append("- ").append(item.getProduct_Name())
                   .append(" (Size: ").append(item.getSize())
                   .append(", Quantity: ").append(item.getQuantity())
                   .append("): $").append(item.getTotalPrice()).append("\n");
        }

        message.append("\nTotal: $").append(total).append("\n");
        message.append("Your order will be processed as soon as possible.\n");
        message.append("Thank you again for shopping at our shop!");

        return message.toString();
    }
}
