package controller;

import model.CartItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy thông tin sản phẩm từ form
        int productId = Integer.parseInt(request.getParameter("productId"));
        String productName = request.getParameter("productName");
        double price = Double.parseDouble(request.getParameter("price"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String size = request.getParameter("size");

        // Tạo đối tượng CartItem
        CartItem cartItem = new CartItem(productId, productName, price, quantity, size);

        // Lấy giỏ hàng từ session
        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        // Kiểm tra nếu sản phẩm với kích thước đã có trong giỏ hàng, tăng số lượng
        boolean found = false;
        for (CartItem item : cart) {
            if (item.getProduct_Id() == productId && item.getSize().equals(size)) {
                item.setQuantity(item.getQuantity() + quantity);
                found = true;
                break;  
            }
        }

        // Nếu sản phẩm chưa có trong giỏ hàng, thêm mới
        if (!found) {
            cart.add(cartItem);
        }

        // Cập nhật giỏ hàng vào session
        session.setAttribute("cart", cart);

        // Chuyển hướng đến trang viewcart.jsp
        response.sendRedirect("viewcart.jsp");
    }
}
