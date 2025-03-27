package controller;

import dao.ProductDAO;
import model.CartItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Product;
import model.CartItem;

@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy thông tin sản phẩm từ request
        int productId = Integer.parseInt(request.getParameter("productId"));
        String productName = request.getParameter("productName");
        double price = Double.parseDouble(request.getParameter("price"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String size = request.getParameter("size");

        // Lấy thông tin giảm giá từ DAO
        ProductDAO productDAO = new ProductDAO();
        Product product = productDAO.getProductById(String.valueOf(productId));

        // Tính giá dựa trên giảm giá (nếu có)
        double finalPrice = product.getPrice();
        double originalPrice = product.getPrice(); // Mặc định là giá gốc
        double discountPercent = 0;

        if (productDAO.isDiscountActive(product)) {
            originalPrice = product.getOriginalPrice();
            discountPercent = product.getDiscountPercent();
            finalPrice = productDAO.getDiscountedPrice(product);
        }

                CartItem cartItem = new CartItem(productId, productName, price, quantity, size);


        // Lấy giỏ hàng từ session
        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        // Kiểm tra sản phẩm đã có trong giỏ hàng chưa
        boolean found = false;
        for (CartItem item : cart) {
            if (item.getProduct_Id()== productId && item.getSize().equals(size)) {
                item.setQuantity(item.getQuantity() + quantity);
                found = true;
                break;
            }
        }

        if (!found) {
            cart.add(cartItem);
        }

        // Cập nhật session
        session.setAttribute("cart", cart);

        // Chuyển hướng đến trang giỏ hàng
        response.sendRedirect("viewcart.jsp");
    }
}