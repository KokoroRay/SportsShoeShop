package controller;

import dao.ProductDAO;
import model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

// Định nghĩa servlet xử lý yêu cầu thêm sản phẩm
@WebServlet("/AddProductServlet")
@MultipartConfig
public class AddProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy danh sách thương hiệu từ DAO
            ProductDAO productDAO = new ProductDAO();
            List<String> brands = productDAO.getAllBrands();
            request.setAttribute("brands", brands);
            request.getRequestDispatcher("add-product.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Xử lý dữ liệu từ form
            String productName = request.getParameter("productName");
            String brand = request.getParameter("brand");
            double price = Double.parseDouble(request.getParameter("price"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String size = request.getParameter("size");
            String description = request.getParameter("description");
            double rate = Double.parseDouble(request.getParameter("rate"));
            String type = request.getParameter("type");

            // Xử lý upload ảnh
            Part filePart = request.getPart("image");
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String imagePath = null;

            if (!fileName.isEmpty()) {
                String uploadPath = getServletContext().getRealPath("/") + File.separator + "uploads";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                filePart.write(uploadPath + File.separator + fileName);
                imagePath = "uploads/" + fileName;
            }

            // Tạo đối tượng Product
            Product newProduct = new Product();
            newProduct.setProduct_Name(productName);
            newProduct.setBrand(brand);
            newProduct.setPrice(price);
            newProduct.setQuantity(quantity);
            newProduct.setSize(size);
            newProduct.setDescription(description);
            newProduct.setImage(imagePath);
            newProduct.setRate(rate);
            newProduct.setType(type);

            // Thêm vào database
            ProductDAO productDAO = new ProductDAO();
            boolean isSuccess = productDAO.insertProduct(newProduct);

            if (isSuccess) {
                response.sendRedirect("manage-products.jsp?status=success");
            } else {
                request.setAttribute("errorMessage", "Thêm sản phẩm thất bại!");
                request.getRequestDispatcher("add-product.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi: " + e.getMessage());
            request.getRequestDispatcher("add-product.jsp").forward(request, response);
        }
    }
}
