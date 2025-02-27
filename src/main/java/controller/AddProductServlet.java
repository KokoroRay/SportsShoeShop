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

// Định nghĩa servlet xử lý yêu cầu thêm sản phẩm
@WebServlet("/AddProductServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // Giới hạn kích thước tệp tối thiểu: 1 MB
                 maxFileSize = 1024 * 1024 * 10,  // Giới hạn kích thước tệp tối đa: 10 MB
                 maxRequestSize = 1024 * 1024 * 50) // Giới hạn kích thước tổng thể: 50 MB
public class AddProductServlet extends HttpServlet {
    
    private static final String UPLOAD_DIRECTORY = "uploads"; // Thư mục lưu trữ hình ảnh tải lên

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductDAO productDAO = new ProductDAO(); // Khởi tạo đối tượng DAO để thao tác với cơ sở dữ liệu

        // Lấy dữ liệu từ form
        String productName = request.getParameter("productName");
        String brand = request.getParameter("brand");
        double price = Double.parseDouble(request.getParameter("price"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String size = request.getParameter("size");
        String description = request.getParameter("description");
        String rateStr = request.getParameter("rate");
        String type = request.getParameter("type");
        
        // Chuyển đổi giá trị đánh giá từ chuỗi sang số, mặc định là 0.0 nếu không có dữ liệu
        double rate = rateStr != null && !rateStr.isEmpty() ? Double.parseDouble(rateStr) : 0.0;

        // Xử lý tải lên tệp hình ảnh
        Part filePart = request.getPart("image");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String imagePath = null;
        
        if (fileName != null && !fileName.isEmpty()) {
            // Xác định đường dẫn lưu trữ hình ảnh
            String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdir(); // Tạo thư mục nếu chưa tồn tại
            
            // Lưu tệp vào thư mục chỉ định
            filePart.write(uploadPath + File.separator + fileName);
            imagePath = UPLOAD_DIRECTORY + File.separator + fileName; // Lưu đường dẫn vào cơ sở dữ liệu
        }

        // Tạo một đối tượng sản phẩm mới
        Product newProduct = new Product();
        newProduct.setProduct_Name(productName);
        newProduct.setBrand(brand);
        newProduct.setPrice(price);
        newProduct.setQuantity(quantity);
        newProduct.setSize(size);
        newProduct.setDescription(description);
        newProduct.setImage(imagePath); // Gán đường dẫn hình ảnh nếu có
        newProduct.setRate(rate);
        newProduct.setType(type);

        // Thêm sản phẩm vào cơ sở dữ liệu
        boolean isProductAdded = productDAO.insertProduct(newProduct);
        
        if (isProductAdded) {
            // Chuyển hướng đến trang quản lý sản phẩm nếu thêm thành công
            response.sendRedirect("manage-products.jsp?message=Product added successfully.");
        } else {
            // Hiển thị thông báo lỗi nếu thêm sản phẩm thất bại
            request.setAttribute("errorMessage", "Failed to add product. Please try again.");
            request.getRequestDispatcher("add-product.jsp").forward(request, response);
        }
    }
}
