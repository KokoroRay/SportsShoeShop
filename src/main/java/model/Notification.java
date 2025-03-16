package model;

import java.sql.Timestamp;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author lam gia bao ce180780
 */
public class Notification {

    private int notificationId;
    private int orderId;
    private int userId;
    private Integer adminId;
    private String message;
    private String status;
    private Timestamp createdAt;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String paymentMethod;
    private String orderDetails;
    private double totalPrice;

    // Constructor đầy đủ
    public Notification(int notificationId, int orderId, int userId, Integer adminId,
            String message, String status, Timestamp createdAt,
            String fullName, String phoneNumber, String address,
            String paymentMethod, String orderDetails, double totalPrice) {
        this.notificationId = notificationId;
        this.orderId = orderId;
        this.userId = userId;
        this.adminId = adminId;
        this.message = message;
        this.status = status;
        this.createdAt = createdAt;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.paymentMethod = paymentMethod;
        this.orderDetails = orderDetails;
        this.totalPrice = totalPrice;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(String orderDetails) {
        this.orderDetails = orderDetails;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    // Getter và Setter cho các thuộc tính
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    // Phương thức toString để in thông tin thông báo
    @Override
    public String toString() {
        return "Notification{"
                + "notificationId=" + notificationId
                + ", orderId=" + orderId
                + ", userId=" + userId
                + ", adminId=" + adminId
                + ", message='" + message + '\''
                + ", status='" + status + '\''
                + ", createdAt=" + createdAt
                + '}';
    }
}
