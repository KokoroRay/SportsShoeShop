package controller;

import dao.NotificationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.Notification;

@WebServlet("/UpdateOrderServlet")
public class UpdateOrderServlet extends HttpServlet {

    private final NotificationDAO notificationDAO = new NotificationDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            String fullName = request.getParameter("fullName");
            String phoneNumber = request.getParameter("phoneNumber");
            String address = request.getParameter("address");
            String paymentMethod = request.getParameter("paymentMethod");

            Notification notification = notificationDAO.getNotificationByOrderId(orderId);

            if (notification == null || !"pending".equalsIgnoreCase(notification.getStatus())) {
                response.sendRedirect("orderStatus.jsp?error=Order cannot be edited");
                return;
            }

            boolean updated = notificationDAO.updateNotificationDetails(orderId, fullName, phoneNumber, address, paymentMethod);

            if (updated) {
                response.sendRedirect("orderStatus.jsp?success=Order updated successfully");
            } else {
                response.sendRedirect("orderStatus.jsp?error=Failed to update order");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("orderStatus.jsp?error=Unexpected error occurred");
        }
    }
}
