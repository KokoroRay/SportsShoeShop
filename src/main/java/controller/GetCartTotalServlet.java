/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.nimbusds.jose.shaded.json.JSONObject;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.CartItem;

/**
 *
 * @author Nguyen Minh Tam - CE181522
 */
@WebServlet("/GetCartTotalServlet")
public class GetCartTotalServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        double subtotal = 0;
        double shipping = 0;

        if (cart != null) {
            subtotal = cart.stream().mapToDouble(CartItem::getTotalPrice).sum();
        }

        // Tính phí vận chuyển (ví dụ)
        String province = (String) session.getAttribute("province");
        String northernProvinces = "Hà Nội,Hải Phòng,Bắc Giang,Bắc Kạn,Bắc Ninh,Cao Bằng,Hà Giang,Hà Nam,Hưng Yên,Lạng Sơn,Lào Cai,Nam Định,Ninh Bình,Phú Thọ,Quảng Ninh,Thái Bình,Thái Nguyên,Thanh Hóa,Tuyên Quang,Vĩnh Phúc,Yên Bái,Điện Biên,Hòa Bình,Lai Châu,Sơn La,Hải Dương";
        shipping = northernProvinces.contains(province) ? 2.00 : 0.00;

        // Tạo JSON response
        JSONObject json = new JSONObject();
        json.put("subtotal", subtotal);
        json.put("shipping", shipping);

        response.setContentType("application/json");
        response.getWriter().write(json.toString());
    }
}
