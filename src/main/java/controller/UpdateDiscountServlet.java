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
import java.text.SimpleDateFormat;
import java.util.Date;
import model.Product;

/**
 *
 * @author Nguyen Minh Tam - CE181522
 */
@WebServlet("/UpdateDiscountServlet") // Đảm bảo ánh xạ đúng URL
public class UpdateDiscountServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Lấy tham số từ form
            int productId = Integer.parseInt(request.getParameter("productId"));
            double discountPercent = Double.parseDouble(request.getParameter("discountPercent"));
            String startDateStr = request.getParameter("discountStartDate");
            String endDateStr = request.getParameter("discountEndDate");

            // Chuyển đổi ngày tháng
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);

            // Lấy thông tin sản phẩm từ database
            ProductDAO dao = new ProductDAO();
            Product product = dao.getProductById(String.valueOf(productId));

            // Xử lý giá và Original_Price
            if (discountPercent > 0) {
                // Lưu giá gốc nếu chưa có
                if (product.getOriginalPrice() == 0) {
                    product.setOriginalPrice(product.getPrice());
                }
                // Tính giá mới dựa trên Original_Price
                double newPrice = product.getOriginalPrice() * (1 - discountPercent / 100);
                product.setPrice(Math.round(newPrice * 100.0) / 100.0); // Làm tròn 2 số thập phân
            } else {
                // Khôi phục giá từ Original_Price, KHÔNG reset Original_Price
                product.setPrice(product.getOriginalPrice());
            }

            // Cập nhật thông tin giảm giá
            product.setDiscountPercent(discountPercent);
            product.setDiscountStartDate(startDate);
            product.setDiscountEndDate(endDate);

            // Lưu thay đổi vào database
            boolean success = dao.updateProductDiscount(product);

            // Chuyển hướng và hiển thị thông báo
            response.sendRedirect("manage-discount.jsp?status=" + (success ? "success" : "error"));

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("manage-discount.jsp?status=invalid_data");
        }
    }
}
