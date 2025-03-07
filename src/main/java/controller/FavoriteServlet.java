/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dao.FavoriteDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;

/**
 *
 * @author Nguyen Minh Tam - CE181522
 */
@WebServlet("/favorite")
public class FavoriteServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int productId = Integer.parseInt(request.getParameter("productId"));
        String action = request.getParameter("action");
        
        FavoriteDAO favoriteDAO = new FavoriteDAO();
        boolean success = false;
        
        switch (action) {
            case "add":
                success = favoriteDAO.addFavorite(user.getUserId(), productId);
                break;
            case "remove":
                success = favoriteDAO.removeFavorite(user.getUserId(), productId);
                break;
        }
        
        if (request.getHeader("Referer") != null) {
            response.sendRedirect(request.getHeader("Referer"));
        } else {
            response.sendRedirect("home.jsp");
        }
    }
}