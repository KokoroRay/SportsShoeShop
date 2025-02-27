/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dao.ProductDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Product;

/**
 * Servlet typeController - Xử lý hiển thị sản phẩm theo loại.
 * 
 * @author ADMIN
 */
public class typeController extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * Đây là phương thức dùng chung xử lý yêu cầu và xuất ra trang HTML mẫu.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        // Đặt kiểu nội dung phản hồi là HTML với mã hóa UTF-8
        response.setContentType("text/html;charset=UTF-8");
        // Sử dụng try-with-resources để tự động đóng PrintWriter
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            // Xuất ra mã HTML mẫu
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet typeController</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet typeController at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * Xử lý yêu cầu GET: Lấy tham số và truy xuất danh sách sản phẩm theo loại.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        // Lấy tham số "productId" từ request (có thể dùng để đại diện cho loại sản phẩm)
        String productId = request.getParameter("productId");
        // Tạo đối tượng ProductDAO để truy xuất dữ liệu sản phẩm
        ProductDAO dao = new ProductDAO();
        // Lấy danh sách sản phẩm theo loại từ cơ sở dữ liệu
        List<Product> product = dao.getProductsByType(productId);
        // Đặt danh sách sản phẩm vào thuộc tính "products" của request
        request.setAttribute("products", product);
        // Chuyển tiếp đến trang product_detail.jsp để hiển thị sản phẩm
        request.getRequestDispatcher("product_detail.jsp").forward(request, response);
        
        // Nếu danh sách sản phẩm không null, đặt thuộc tính "product" và chuyển tiếp lại (lưu ý: đoạn code này được gọi sau khi forward trên, có thể gây lỗi)
        if (product != null) {
            request.setAttribute("product", product);
            request.getRequestDispatcher("product_detail.jsp").forward(request, response);
        }
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * Xử lý yêu cầu POST bằng cách gọi phương thức processRequest.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        // Gọi phương thức processRequest để xử lý POST
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * Trả về mô tả ngắn của servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
