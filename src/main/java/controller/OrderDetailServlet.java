package controller;

import dao.OrderDAO;
import model.Order;
import model.OrderItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/OrderDetailServlet")
public class OrderDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int orderId = Integer.parseInt(request.getParameter("orderId"));

        OrderDAO orderDAO = new OrderDAO();
        Order order = orderDAO.getOrderById(orderId); // Hàm này sẽ lấy thông tin đơn hàng từ cơ sở dữ liệu
        List<OrderItem> orderItems = orderDAO.getOrderItemsByOrderId(orderId); // Lấy danh sách sản phẩm trong đơn hàng

        request.setAttribute("order", order);
        request.setAttribute("orderItems", orderItems);
        
        request.getRequestDispatcher("order-detail.jsp").forward(request, response);
    }
}
