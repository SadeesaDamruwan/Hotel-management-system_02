package com.hotel.management.service;

import com.hotel.management.util.EnvLoader;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.Map;

public class PayHereService {
    
    private static final String MERCHANT_ID = EnvLoader.get("PAYHERE_MERCHANT_ID");
    private static final String MERCHANT_SECRET = EnvLoader.get("PAYHERE_MERCHANT_SECRET");
    private static final boolean SANDBOX_MODE = Boolean.parseBoolean(EnvLoader.get("PAYHERE_SANDBOX_MODE", "true"));
    private static final String CURRENCY = EnvLoader.get("PAYHERE_CURRENCY", "LKR");
    private static final String RETURN_URL = EnvLoader.get("PAYHERE_RETURN_URL", "http://localhost/payment/return");
    private static final String CANCEL_URL = EnvLoader.get("PAYHERE_CANCEL_URL", "http://localhost/payment/cancel");
    private static final String NOTIFY_URL = EnvLoader.get("PAYHERE_NOTIFY_URL", "http://localhost/payment/notify");
    
    private static final String SANDBOX_URL = "https://sandbox.payhere.lk/pay/checkout";
    private static final String LIVE_URL = "https://www.payhere.lk/pay/checkout";
    
    public String generateHash(String orderId, double amount) {
        String amountFormatted = String.format("%.2f", amount);
        String merchantSecretMD5 = DigestUtils.md5Hex(MERCHANT_SECRET).toUpperCase();
        String hashString = MERCHANT_ID + orderId + amountFormatted + CURRENCY + merchantSecretMD5;
        
        System.out.println("=== PayHere Hash Debug ===");
        System.out.println("Merchant ID: " + MERCHANT_ID);
        System.out.println("Order ID: " + orderId);
        System.out.println("Amount: " + amountFormatted);
        System.out.println("Currency: " + CURRENCY);
        System.out.println("Merchant Secret MD5: " + merchantSecretMD5);
        System.out.println("Hash String: " + hashString);
        String finalHash = DigestUtils.md5Hex(hashString).toUpperCase();
        System.out.println("Final Hash: " + finalHash);
        System.out.println("==========================");
        
        return finalHash;
    }
    
    @Deprecated
    private String getMerchantSecretHash() {
        return DigestUtils.md5Hex(MERCHANT_SECRET).toUpperCase();
    }
    
    public String generatePaymentUrl(Map<String, String> paymentData) {
        String orderId = paymentData.get("order_id");
        double amount = Double.parseDouble(paymentData.get("amount"));
        String hash = generateHash(orderId, amount);
        
        StringBuilder url = new StringBuilder(SANDBOX_MODE ? SANDBOX_URL : LIVE_URL);
        url.append("?merchant_id=").append(MERCHANT_ID);
        url.append("&return_url=").append(RETURN_URL);
        url.append("&cancel_url=").append(CANCEL_URL);
        url.append("&notify_url=").append(NOTIFY_URL);
        url.append("&order_id=").append(orderId);
        url.append("&items=").append(urlEncode(paymentData.get("items")));
        url.append("&currency=").append(CURRENCY);
        url.append("&amount=").append(String.format("%.2f", amount));
        url.append("&first_name=").append(urlEncode(paymentData.get("first_name")));
        url.append("&last_name=").append(urlEncode(paymentData.get("last_name")));
        url.append("&email=").append(urlEncode(paymentData.get("email")));
        url.append("&phone=").append(urlEncode(paymentData.get("phone")));
        url.append("&address=").append(urlEncode(paymentData.get("address")));
        url.append("&city=").append(urlEncode(paymentData.get("city")));
        url.append("&country=").append(urlEncode(paymentData.get("country")));
        url.append("&hash=").append(hash);
        
        return url.toString();
    }
    
    public String generatePaymentForm(Map<String, String> paymentData) {
        String orderId = paymentData.get("order_id");
        double amount = Double.parseDouble(paymentData.get("amount"));
        String hash = generateHash(orderId, amount);
        String checkoutUrl = SANDBOX_MODE ? SANDBOX_URL : LIVE_URL;
        
        System.out.println("=== Payment Form Data ===");
        System.out.println("Merchant ID: " + MERCHANT_ID);
        System.out.println("Order ID: " + orderId);
        System.out.println("Amount: " + String.format("%.2f", amount));
        System.out.println("Currency: " + CURRENCY);
        System.out.println("Items: " + paymentData.get("items"));
        System.out.println("First Name: " + paymentData.get("first_name"));
        System.out.println("Last Name: " + paymentData.get("last_name"));
        System.out.println("Email: " + paymentData.get("email"));
        System.out.println("Hash: " + hash);
        System.out.println("========================");
        
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html><head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<title>Processing Payment...</title>");
        html.append("<style>");
        html.append("body { font-family: 'Segoe UI', sans-serif; background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%); ");
        html.append("color: white; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }");
        html.append(".loader { text-align: center; }");
        html.append(".spinner { border: 4px solid rgba(255,180,60,0.3); border-top: 4px solid #FFB43C; ");
        html.append("border-radius: 50%; width: 50px; height: 50px; animation: spin 1s linear infinite; margin: 0 auto 20px; }");
        html.append("@keyframes spin { 0% { transform: rotate(0deg); } 100% { transform: rotate(360deg); } }");
        html.append("</style>");
        html.append("</head><body>");
        html.append("<div class='loader'>");
        html.append("<div class='spinner'></div>");
        html.append("<h2>Redirecting to Payment Gateway...</h2>");
        html.append("<p>Please wait while we securely redirect you to PayHere.</p>");
        html.append("</div>");
        html.append("<form id='paymentForm' method='POST' action='").append(checkoutUrl).append("'>");
        html.append("<input type='hidden' name='merchant_id' value='").append(MERCHANT_ID).append("'/>");
        html.append("<input type='hidden' name='return_url' value='").append(RETURN_URL).append("'/>");
        html.append("<input type='hidden' name='cancel_url' value='").append(CANCEL_URL).append("'/>");
        html.append("<input type='hidden' name='notify_url' value='").append(NOTIFY_URL).append("'/>");
        html.append("<input type='hidden' name='order_id' value='").append(orderId).append("'/>");
        html.append("<input type='hidden' name='items' value='").append(escapeHtml(paymentData.get("items"))).append("'/>");
        html.append("<input type='hidden' name='currency' value='").append(CURRENCY).append("'/>");
        html.append("<input type='hidden' name='amount' value='").append(String.format("%.2f", amount)).append("'/>");
        html.append("<input type='hidden' name='first_name' value='").append(escapeHtml(paymentData.get("first_name"))).append("'/>");
        html.append("<input type='hidden' name='last_name' value='").append(escapeHtml(paymentData.get("last_name"))).append("'/>");
        html.append("<input type='hidden' name='email' value='").append(escapeHtml(paymentData.get("email"))).append("'/>");
        html.append("<input type='hidden' name='phone' value='").append(escapeHtml(paymentData.get("phone"))).append("'/>");
        html.append("<input type='hidden' name='address' value='").append(escapeHtml(paymentData.get("address"))).append("'/>");
        html.append("<input type='hidden' name='city' value='").append(escapeHtml(paymentData.get("city"))).append("'/>");
        html.append("<input type='hidden' name='country' value='").append(escapeHtml(paymentData.get("country"))).append("'/>");
        html.append("<input type='hidden' name='delivery_address' value='").append(escapeHtml(paymentData.get("address"))).append("'/>");
        html.append("<input type='hidden' name='delivery_city' value='").append(escapeHtml(paymentData.get("city"))).append("'/>");
        html.append("<input type='hidden' name='delivery_country' value='").append(escapeHtml(paymentData.get("country"))).append("'/>");
        html.append("<input type='hidden' name='custom_1' value='Hotel Booking'/>");
        html.append("<input type='hidden' name='custom_2' value='").append(orderId).append("'/>");
        html.append("<input type='hidden' name='hash' value='").append(hash).append("'/>");
        
        System.out.println("=== Form Fields Being Sent ===");
        System.out.println("merchant_id: " + MERCHANT_ID);
        System.out.println("order_id: " + orderId);
        System.out.println("amount: " + String.format("%.2f", amount));
        System.out.println("currency: " + CURRENCY);
        System.out.println("hash: " + hash);
        System.out.println("Merchant Secret (first 10 chars): " + MERCHANT_SECRET.substring(0, Math.min(10, MERCHANT_SECRET.length())));
        System.out.println("==============================");
        
        html.append("</form>");
        html.append("<script>document.getElementById('paymentForm').submit();</script>");
        html.append("</body></html>");
        
        return html.toString();
    }
    
    private String escapeHtml(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#x27;");
    }
    
    public Map<String, String> createPaymentData(
            String orderId,
            String guestName,
            String email,
            String phone,
            String address,
            String roomType,
            String roomNumber,
            int nights,
            double totalAmount
    ) {
        Map<String, String> data = new HashMap<>();
        
        String[] nameParts = guestName.trim().split("\\s+", 2);
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        
        data.put("order_id", orderId);
        data.put("items", roomType + " - Room " + roomNumber + " (" + nights + " nights)");
        data.put("amount", String.format("%.2f", totalAmount));
        data.put("first_name", firstName);
        data.put("last_name", lastName);
        data.put("email", email);
        data.put("phone", phone);
        data.put("address", address);
        data.put("city", "Colombo");
        data.put("country", "Sri Lanka"); 
        
        return data;
    }
    
    private String urlEncode(String value) {
        if (value == null) return "";
        try {
            return java.net.URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) {
            return value;
        }
    }
    
    public boolean isPaymentSuccessful(String url) {
        return url != null && (
            url.contains("payment_id") || 
            url.contains("status=success") ||
            url.contains(RETURN_URL)
        );
    }
    
    
    public boolean isPaymentCancelled(String url) {
        return url != null && (
            url.contains(CANCEL_URL) ||
            url.contains("status=cancel")
        );
    }
    
    public String extractPaymentId(String url) {
        if (url == null) return null;
        
        try {
            String[] params = url.split("[?&]");
            for (String param : params) {
                if (param.startsWith("payment_id=")) {
                    return param.substring("payment_id=".length());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public static String generateOrderId() {
        return "ORD" + System.currentTimeMillis();
    }
}
