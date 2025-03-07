/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dao.ProductDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import model.CartItem;
import model.Product;
import model.User;

/**
 *
 * @author Nguyen Minh Tam - CE181522
 */
@WebServlet("/AddToCartServlet")
public class AddToCartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        ProductDAO productDAO = new ProductDAO();

        // 1. Kiểm tra đăng nhập
        if (user == null) {
            response.sendRedirect("login.jsp");
            return; // Dừng xử lý ngay nếu chưa đăng nhập
        }

        // 2. Lấy danh sách sản phẩm đã chọn từ form
        String[] selectedProducts = request.getParameterValues("productIds");
        
        // 3. Xử lý trường hợp không chọn sản phẩm
        if (selectedProducts == null || selectedProducts.length == 0) {
            // 3a. Redirect với tham số lỗi
            response.sendRedirect("favoriteList.jsp?error=no_selection");
            return; // Dừng xử lý
        }

        // 4. Lấy hoặc tạo giỏ hàng từ session
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        // 5. Xử lý từng sản phẩm đã chọn
        for (String productIdStr : selectedProducts) {
            try {
                // 5a. Chuyển đổi productId từ String sang int
                int productId = Integer.parseInt(productIdStr);
                
                // 5b. Lấy thông tin sản phẩm từ DAO
                Product product = productDAO.getProductById(productIdStr); // Sử dụng phương thức DAO nhận String
                
                // 5c. Kiểm tra sản phẩm tồn tại
                if (product == null) continue; // Bỏ qua nếu không tìm thấy

                // 5d. Xử lý size (lấy size đầu tiên)
                String size = "Default";
                if (product.getSize() != null && !product.getSize().isEmpty()) {
                    String[] sizes = product.getSize().split(",");
                    if (sizes.length > 0) {
                        size = sizes[0].trim();
                    }
                }

                // 5e. Kiểm tra sản phẩm đã có trong giỏ
                boolean exists = false;
                for (CartItem item : cart) {
                    if (item.getProduct_Id() == productId && item.getSize().equals(size)) {
                        item.setQuantity(item.getQuantity() + 1); // Tăng số lượng
                        exists = true;
                        break;
                    }
                }

                // 5f. Thêm mới nếu chưa tồn tại
                if (!exists) {
                    CartItem newItem = new CartItem(
                        productId,
                        product.getProduct_Name(),
                        product.getPrice(),
                        1, // Số lượng mặc định
                        size
                    );
                    cart.add(newItem);
                }

            } catch (NumberFormatException e) {
                // 5g. Xử lý lỗi chuyển đổi productId
                System.err.println("Invalid product ID: " + productIdStr);
            }
        }

        // 6. Cập nhật session và chuyển hướng
        session.setAttribute("cart", cart);
        response.sendRedirect("viewcart.jsp");
    }
}