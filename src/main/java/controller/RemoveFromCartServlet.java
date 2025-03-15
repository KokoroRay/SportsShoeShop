package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import model.CartItem;

/**
 *
 * @author Nguyen Minh Tam - CE181522
 */
@WebServlet("/RemoveFromCartServlet")
public class RemoveFromCartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        try {
            String productIdStr = URLDecoder.decode(request.getParameter("productId"), "UTF-8");
            int productId = Integer.parseInt(productIdStr); // Chuyển sang int
            String size = URLDecoder.decode(request.getParameter("size"), "UTF-8");

            if (cart != null) {
                cart.removeIf(item
                        -> item.getProduct_Id() == productId
                        && // So sánh int với int
                        item.getSize().equals(size)
                );
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID format");
        }
    }
}
