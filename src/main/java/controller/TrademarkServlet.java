/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dao.TrademarkDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Nguyen Minh Tam - CE181522
 */

@WebServlet("/TrademarkServlet")
public class TrademarkServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String action = request.getParameter("action");
            TrademarkDAO dao = new TrademarkDAO();
            String redirectUrl = "manage-trademark.jsp";

            if ("add".equals(action)) {
                String name = request.getParameter("trademarkName");
                boolean isSuccess = dao.addTrademark(name);
                redirectUrl += isSuccess ? "?status=success" : "?status=error";

            } else if ("update".equals(action)) {
                int id = Integer.parseInt(request.getParameter("trademarkId"));
                String newName = request.getParameter("trademarkName");
                boolean isSuccess = dao.updateTrademark(id, newName);
                redirectUrl += isSuccess ? "?status=success" : "?status=error";

            } else {
                // Xử lý action không hợp lệ
                redirectUrl = "error.jsp?error=invalid_action";
            }

            response.sendRedirect(redirectUrl);

        } catch (NumberFormatException e) {
            response.sendRedirect("error.jsp?error=invalid_id_format");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?error=server_error");
        }
    }
}
