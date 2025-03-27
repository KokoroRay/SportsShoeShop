package controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import dao.TrademarkDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Nguyen Minh Tam - CE181522
 */
@WebServlet("/DeleteTrademarkServlet")
public class DeleteTrademarkServlet extends HttpServlet {
    // Danh sách ID không được xóa
    private static final List<Integer> PROTECTED_IDS = Arrays.asList(1, 2, 3, 4, 5, 6);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        
        // Kiểm tra ID có trong danh sách bảo vệ
        if (PROTECTED_IDS.contains(id)) {
            response.sendRedirect("manage-trademark.jsp?error=cannot_delete_default");
            return;
        }
        
        TrademarkDAO dao = new TrademarkDAO();
        dao.deleteTrademark(id);
        response.sendRedirect("manage-trademark.jsp?status=delete_success");
    }
}