package controller;

import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

// Định nghĩa servlet xử lý yêu cầu đánh giá sản phẩm
@WebServlet("/RateProductServlet")
public class RateProductServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy productId từ tham số của request và chuyển đổi sang số nguyên
        int productId = Integer.parseInt(request.getParameter("productId"));
        // Lấy rating từ tham số của request và chuyển đổi sang số nguyên
        int rating = Integer.parseInt(request.getParameter("rating"));

        // Khởi tạo đối tượng ProductDAO để thao tác với cơ sở dữ liệu sản phẩm
        ProductDAO productDAO = new ProductDAO();
        // Cập nhật đánh giá cho sản phẩm bằng cách gọi phương thức rateProduct
        productDAO.rateProduct(productId, rating);

        // Sau khi cập nhật đánh giá, chuyển hướng người dùng về trang lịch sử mua hàng
        response.sendRedirect("PurchaseHistoryServlet");
    }
}
