/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.ProductDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Product;

/**
 * Servlet này xử lý các yêu cầu liên quan đến sản phẩm trên trang chủ.
 * Nó sẽ lấy danh sách sản phẩm từ cơ sở dữ liệu và chuyển tiếp sang trang JSP để hiển thị.
 * 
 * @author ADMIN
 */
@WebServlet(name = "productController", urlPatterns = {"/home"})
public class productController extends HttpServlet {

    /**
     * Phương thức processRequest xử lý các yêu cầu chung cho cả GET và POST.
     * Ở đây, nó xuất ra một trang HTML mẫu. Phương thức này có thể được mở rộng nếu cần.
     *
     * @param request  Đối tượng HttpServletRequest chứa thông tin yêu cầu từ client
     * @param response Đối tượng HttpServletResponse dùng để gửi phản hồi về client
     * @throws ServletException Nếu có lỗi liên quan đến servlet xảy ra
     * @throws IOException Nếu có lỗi I/O xảy ra
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Đặt kiểu nội dung của response là HTML với mã hóa UTF-8
        response.setContentType("text/html;charset=UTF-8");
        // Tạo đối tượng PrintWriter để ghi dữ liệu HTML ra response
        try ( PrintWriter out = response.getWriter()) {
            // Xuất nội dung HTML mẫu ra trình duyệt
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet productController</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet productController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Phương thức doGet xử lý các yêu cầu HTTP GET.
     * Nó lấy danh sách sản phẩm từ cơ sở dữ liệu và chuyển tiếp đến trang JSP "all.jsp"
     * để hiển thị danh sách sản phẩm cho người dùng.
     *
     * @param request  Đối tượng HttpServletRequest chứa thông tin yêu cầu từ client
     * @param response Đối tượng HttpServletResponse dùng để gửi phản hồi về client
     * @throws ServletException Nếu có lỗi liên quan đến servlet xảy ra
     * @throws IOException Nếu có lỗi I/O xảy ra
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Tạo đối tượng ProductDAO để truy xuất dữ liệu sản phẩm từ cơ sở dữ liệu
        ProductDAO pDao = new ProductDAO();
        // Lấy danh sách tất cả sản phẩm từ cơ sở dữ liệu thông qua phương thức getAllProducts()
        List<Product> product = pDao.getAllProducts();
        // Đặt danh sách sản phẩm vào thuộc tính "products" của request để gửi sang trang JSP
        request.setAttribute("products", product);
        // Chuyển tiếp (forward) request và response sang trang "all.jsp" để hiển thị danh sách sản phẩm
        request.getRequestDispatcher("all.jsp").forward(request, response);
    }

    /**
     * Phương thức doPost xử lý các yêu cầu HTTP POST.
     * Ở đây, doPost được triển khai bằng cách gọi phương thức processRequest,
     * nghĩa là xử lý POST sẽ hiển thị ra trang HTML mẫu như định nghĩa trong processRequest.
     *
     * @param request  Đối tượng HttpServletRequest chứa thông tin yêu cầu từ client
     * @param response Đối tượng HttpServletResponse dùng để gửi phản hồi về client
     * @throws ServletException Nếu có lỗi liên quan đến servlet xảy ra
     * @throws IOException Nếu có lỗi I/O xảy ra
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Gọi phương thức processRequest để xử lý yêu cầu POST
        processRequest(request, response);
    }

    /**
     * Phương thức getServletInfo trả về mô tả ngắn gọn về servlet này.
     *
     * @return Chuỗi chứa mô tả ngắn của servlet
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
