package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import model.CartItem;

@WebServlet("/UpdateCartServlet")
public class UpdateCartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        try {
            // Đọc và parse tham số
            int productId = Integer.parseInt(request.getParameter("productId"));
            String size = request.getParameter("size");
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            // Cập nhật giỏ hàng
            if (cart != null) {
                for (CartItem item : cart) {
                    if (item.getProduct_Id() == productId && item.getSize().equals(size)) {
                        item.setQuantity(quantity);
                        break;
                    }
                }
            }

            // Tính lại tổng tiền
            double subtotal = cart.stream().mapToDouble(CartItem::getTotalPrice).sum();
            session.setAttribute("subtotal", subtotal);

            response.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dữ liệu không hợp lệ");
        }
    }
}
