/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dao.FavoriteDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

/**
 *
 * @author Nguyen Minh Tam - CE181522
 */
@WebServlet("/RemoveFavoriteServlet")
public class RemoveFavoriteServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Authentication check
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Get selected product IDs
        String[] productIds = request.getParameterValues("productIds");
        if (productIds == null || productIds.length == 0) {
            response.sendRedirect("favoriteList.jsp?status=error");
            return;
        }

        FavoriteDAO favoriteDAO = new FavoriteDAO();
        boolean success = true;

        // Process deletion
        for (String productIdStr : productIds) {
            try {
                int productId = Integer.parseInt(productIdStr);
                if (!favoriteDAO.removeFavorite(user.getUserId(), productId)) {
                    success = false;
                }
            } catch (NumberFormatException e) {
                success = false;
            }
        }

        // Redirect with status
        response.sendRedirect("favoriteList.jsp?status=" + (success ? "deleted" : "error"));
    }
}