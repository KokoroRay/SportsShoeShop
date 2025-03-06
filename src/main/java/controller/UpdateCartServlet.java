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

        // Nhận dữ liệu từ request
        String productIdStr = request.getParameter("productId");
        String newSize = request.getParameter("size");
        String quantityStr = request.getParameter("quantity");

        // Kiểm tra dữ liệu hợp lệ
        if (productIdStr == null || productIdStr.isEmpty()
                || newSize == null || newSize.isEmpty()
                || quantityStr == null || quantityStr.isEmpty()) {
            response.sendRedirect("viewcart.jsp?error=invalid_input");
            return;
        }

        int productId = Integer.parseInt(productIdStr);
        int newQuantity = Integer.parseInt(quantityStr);

        // Cập nhật giỏ hàng
        if (cart != null) {
            for (CartItem item : cart) {
                if (item.getProduct_Id() == productId) {
                    item.setSize(newSize); // Cập nhật size
                    item.setQuantity(newQuantity); // Cập nhật số lượng
                    break;
                }
            }
        }

        // Lưu lại giỏ hàng vào session
        session.setAttribute("cart", cart);
        response.sendRedirect("viewcart.jsp");
    }
}
