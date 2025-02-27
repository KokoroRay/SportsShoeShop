package controller;

import dao.ProductDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/DeleteProductServlet")
public class DeleteProductServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy tham số productId từ request và chuyển đổi sang kiểu số nguyên
        int productId = Integer.parseInt(request.getParameter("productId"));
        
        // Khởi tạo đối tượng ProductDAO để thao tác với sản phẩm trong cơ sở dữ liệu
        ProductDAO productDAO = new ProductDAO();
        
        // Gọi phương thức deleteProduct để xóa sản phẩm theo productId
        productDAO.deleteProduct(productId);
        
        // Chuyển hướng người dùng về trang quản lý sản phẩm sau khi xóa thành công
        response.sendRedirect("manage-products.jsp");
    }
}
