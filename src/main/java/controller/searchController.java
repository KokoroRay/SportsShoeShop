package controller;

import dao.ProductDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Product;

/**
 * Servlet này xử lý chức năng tìm kiếm sản phẩm dựa trên tên sản phẩm.
 * - Khi người dùng gửi yêu cầu GET, servlet chuyển hướng đến trang tìm kiếm (searchPage.jsp).
 * - Khi người dùng gửi yêu cầu POST (form tìm kiếm), servlet nhận tên sản phẩm từ form,
 *   sau đó sử dụng ProductDAO để truy xuất danh sách sản phẩm có tên khớp,
 *   đặt danh sách sản phẩm vào thuộc tính request ("searchResults") và chuyển hướng
 *   đến trang hiển thị kết quả tìm kiếm (searchResults.jsp).
 */
@WebServlet(name = "searchController", urlPatterns = {"/searchController"})
public class searchController extends HttpServlet {

    /**
     * Phương thức doGet xử lý yêu cầu GET.
     * Chuyển hướng (forward) request và response đến trang tìm kiếm searchPage.jsp.
     *
     * @param request  Đối tượng HttpServletRequest chứa thông tin yêu cầu từ client
     * @param response Đối tượng HttpServletResponse dùng để gửi phản hồi về client
     * @throws ServletException Nếu có lỗi liên quan đến servlet xảy ra
     * @throws IOException Nếu có lỗi I/O xảy ra
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Chuyển tiếp đến trang tìm kiếm (searchPage.jsp)
        request.getRequestDispatcher("searchPage.jsp").forward(request, response);
    }

    /**
     * Phương thức doPost xử lý yêu cầu POST.
     * Nhận tham số tên sản phẩm từ form, sử dụng ProductDAO để tìm sản phẩm,
     * đặt kết quả tìm kiếm vào thuộc tính "searchResults" và chuyển hướng đến trang hiển thị kết quả.
     *
     * @param request  Đối tượng HttpServletRequest chứa thông tin yêu cầu từ client
     * @param response Đối tượng HttpServletResponse dùng để gửi phản hồi về client
     * @throws ServletException Nếu có lỗi liên quan đến servlet xảy ra
     * @throws IOException Nếu có lỗi I/O xảy ra
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy tên sản phẩm mà người dùng nhập từ form tìm kiếm
        String productName = request.getParameter("productName");
        
        // Tạo đối tượng ProductDAO để tương tác với cơ sở dữ liệu sản phẩm
        ProductDAO dao = new ProductDAO();
        // Tìm kiếm các sản phẩm có tên khớp với từ khóa tìm kiếm
        List<Product> products = dao.getProductsByName(productName);
        // Đặt danh sách kết quả tìm kiếm vào thuộc tính "searchResults" của request
        request.setAttribute("searchResults", products);
        // Chuyển tiếp request và response đến trang hiển thị kết quả tìm kiếm (searchResults.jsp)
        request.getRequestDispatcher("searchResults.jsp").forward(request, response);
    }

    /**
     * Trả về mô tả ngắn gọn của servlet.
     *
     * @return Chuỗi mô tả servlet
     */
    @Override
    public String getServletInfo() {
        return "Search Controller Servlet";
    }
}
