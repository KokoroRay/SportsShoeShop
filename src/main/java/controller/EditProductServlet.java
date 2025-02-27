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
import java.io.PrintWriter;
import java.nio.file.Paths;

// Định nghĩa servlet xử lý yêu cầu chỉnh sửa sản phẩm
@WebServlet("/EditProductServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, // Giới hạn kích thước tệp tối thiểu: 1 MB
    maxFileSize = 1024 * 1024 * 10,    // Giới hạn kích thước tệp tối đa: 10 MB
    maxRequestSize = 1024 * 1024 * 50  // Giới hạn kích thước tổng thể của yêu cầu: 50 MB
)
public class EditProductServlet extends HttpServlet {

    // Thư mục chứa hình ảnh được tải lên
    private static final String UPLOAD_DIRECTORY = "uploads";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Khởi tạo đối tượng DAO để thao tác với cơ sở dữ liệu sản phẩm
        ProductDAO productDAO = new ProductDAO();

        // Lấy ID sản phẩm từ tham số request và chuyển đổi sang số nguyên
        int productId = Integer.parseInt(request.getParameter("productId"));
        // Lấy đối tượng sản phẩm từ cơ sở dữ liệu dựa trên ID
        Product product = productDAO.getProductById(String.valueOf(productId));

        // Nếu không tìm thấy sản phẩm, chuyển hướng đến trang not-found và dừng xử lý
        if (product == null) {
            request.setAttribute("errorMessage", "Product not found.");
            response.sendRedirect("not-found.jsp");
            return;
        }

        // Lấy dữ liệu từ form gửi lên
        String productName = request.getParameter("productName");
        String brand = request.getParameter("brand");
        double price = Double.parseDouble(request.getParameter("price")); // Chuyển đổi giá thành số thực
        int quantity = Integer.parseInt(request.getParameter("quantity")); // Chuyển đổi số lượng sang số nguyên
        String size = request.getParameter("size");
        String description = request.getParameter("description");
        String rateStr = request.getParameter("rate");
        String type = request.getParameter("type");

        // Nếu thông số rate hợp lệ, chuyển đổi sang số thực, ngược lại gán giá trị mặc định 0.0
        double rate = rateStr != null && !rateStr.isEmpty() ? Double.parseDouble(rateStr) : 0.0;

        // Xử lý tải lên tệp hình ảnh
        // Lấy đối tượng Part chứa hình ảnh từ request
        Part filePart = request.getPart("image");
        // Lấy tên tệp hình ảnh từ đối tượng Part
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        // Gán đường dẫn hình ảnh mặc định là hình ảnh hiện có của sản phẩm
        String imagePath = product.getImage();

        // Nếu có tệp hình ảnh mới được tải lên (tên tệp không rỗng)
        if (fileName != null && !fileName.isEmpty()) {
            // Xác định đường dẫn lưu trữ hình ảnh trên máy chủ
            String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
            File uploadDir = new File(uploadPath);
            // Nếu thư mục chưa tồn tại, tạo mới thư mục
            if (!uploadDir.exists()) uploadDir.mkdir();

            // Lưu tệp hình ảnh vào thư mục đã chỉ định
            filePart.write(uploadPath + File.separator + fileName);
            // Cập nhật đường dẫn hình ảnh để lưu vào cơ sở dữ liệu
            imagePath = UPLOAD_DIRECTORY + File.separator + fileName;
        }

        // Cập nhật thông tin sản phẩm với dữ liệu mới từ form
        product.setProduct_Name(productName);
        product.setBrand(brand);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setSize(size);
        product.setDescription(description);
        product.setImage(imagePath); // Cập nhật đường dẫn hình ảnh (mới hoặc giữ nguyên nếu không có thay đổi)
        product.setRate(rate);
        product.setType(type);

        // Cập nhật thông tin sản phẩm trong cơ sở dữ liệu
        boolean updateSuccess = productDAO.updateProduct(product);

        // Kiểm tra kết quả cập nhật và điều hướng người dùng phù hợp
        if (updateSuccess) {
            // Nếu cập nhật thành công, chuyển hướng đến trang quản lý sản phẩm kèm thông báo thành công
            response.sendRedirect("manage-products.jsp?message=Product updated successfully.");
        } else {
            // Nếu cập nhật thất bại, đặt thông báo lỗi và chuyển tiếp đến trang chỉnh sửa sản phẩm
            request.setAttribute("errorMessage", "Failed to update product.");
            request.getRequestDispatcher("edit-product.jsp?productId=" + productId).forward(request, response);
        }
    }
}
