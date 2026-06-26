package com.ecommerce.shared.constants;

public class AppConstants {
    
    // User Roles
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_CUSTOMER = "CUSTOMER";
    public static final String ROLE_GUEST = "GUEST";

    // Order Status
    public static final String ORDER_STATUS_PENDING = "PENDING";
    public static final String ORDER_STATUS_CONFIRMED = "CONFIRMED";
    public static final String ORDER_STATUS_PAID = "PAID";
    public static final String ORDER_STATUS_SHIPPED = "SHIPPED";
    public static final String ORDER_STATUS_DELIVERED = "DELIVERED";
    public static final String ORDER_STATUS_CANCELLED = "CANCELLED";

    // Payment Status
    public static final String PAYMENT_STATUS_PENDING = "PENDING";
    public static final String PAYMENT_STATUS_COMPLETED = "COMPLETED";
    public static final String PAYMENT_STATUS_FAILED = "FAILED";
    public static final String PAYMENT_STATUS_REFUNDED = "REFUNDED";

    // User Status
    public static final String USER_STATUS_ACTIVE = "ACTIVE";
    public static final String USER_STATUS_INACTIVE = "INACTIVE";
    public static final String USER_STATUS_BLOCKED = "BLOCKED";

    // Notification Types
    public static final String NOTIFICATION_ORDER_CONFIRMED = "ORDER_CONFIRMED";
    public static final String NOTIFICATION_PAYMENT_SUCCESS = "PAYMENT_SUCCESS";
    public static final String NOTIFICATION_SHIPMENT = "SHIPMENT";
    public static final String NOTIFICATION_DELIVERY = "DELIVERY";

    private AppConstants() {
    }
}
