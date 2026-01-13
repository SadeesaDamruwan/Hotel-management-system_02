package com.hotel.management.service;

import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class PayHereRetrievalService {
    
    private final Dotenv dotenv;
    private final String APP_ID;
    private final String APP_SECRET;
    private final boolean SANDBOX_MODE;
    private String accessToken = null;
    private long tokenExpiry = 0;
    
    public PayHereRetrievalService() {
        this.dotenv = Dotenv.load();
        this.APP_ID = dotenv.get("PAYHERE_APP_ID", "");
        this.APP_SECRET = dotenv.get("PAYHERE_APP_SECRET", "");
        this.SANDBOX_MODE = Boolean.parseBoolean(dotenv.get("PAYHERE_SANDBOX_MODE", "true"));
    }
   
    private String getAccessToken() throws Exception {
        if (accessToken != null && System.currentTimeMillis() < tokenExpiry) {
            System.out.println("Using cached access token");
            return accessToken;
        }
        
        System.out.println("Retrieving new access token from PayHere...");
        
        String authString = APP_ID + ":" + APP_SECRET;
        String authCode = Base64.getEncoder().encodeToString(authString.getBytes());
        
        String endpoint = SANDBOX_MODE 
            ? "https://sandbox.payhere.lk/merchant/v1/oauth/token"
            : "https://www.payhere.lk/merchant/v1/oauth/token";
        
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Basic " + authCode);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);
        
        String requestBody = "grant_type=client_credentials";
        try (OutputStream os = conn.getOutputStream()) {
            os.write(requestBody.getBytes());
            os.flush();
        }
        
        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            JSONObject jsonResponse = new JSONObject(response.toString());
            accessToken = jsonResponse.getString("access_token");
            int expiresIn = jsonResponse.getInt("expires_in");
            
            tokenExpiry = System.currentTimeMillis() + ((expiresIn - 10) * 1000);
            
            System.out.println("Access token retrieved successfully");
            System.out.println("Token expires in: " + expiresIn + " seconds");
            
            return accessToken;
        } else {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            System.err.println("Failed to get access token. Response code: " + responseCode);
            System.err.println("Error response: " + response.toString());
            throw new Exception("Failed to get access token: " + response.toString());
        }
    }
    
    public String getPaymentId(String orderId) {
        try {
            System.out.println("=== PayHere Retrieval API Request ===");
            System.out.println("Order ID: " + orderId);
            
            String token = getAccessToken();
            
            String endpoint = SANDBOX_MODE 
                ? "https://sandbox.payhere.lk/merchant/v1/payment/search?order_id=" + orderId
                : "https://www.payhere.lk/merchant/v1/payment/search?order_id=" + orderId;
            
            System.out.println("Endpoint: " + endpoint);
            
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Content-Type", "application/json");
            
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            
            BufferedReader in = new BufferedReader(new InputStreamReader(
                responseCode == 200 ? conn.getInputStream() : conn.getErrorStream()
            ));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            System.out.println("Response: " + response.toString());
            
            if (responseCode == 200) {
                JSONObject jsonResponse = new JSONObject(response.toString());
                int status = jsonResponse.getInt("status");
                
                if (status == 1) {
                    JSONArray data = jsonResponse.getJSONArray("data");
                    if (data.length() > 0) {
                        JSONObject paymentData = data.getJSONObject(0);
                        long paymentId = paymentData.getLong("payment_id");
                        String paymentStatus = paymentData.getString("status");
                        
                        System.out.println("Payment ID: " + paymentId);
                        System.out.println("Payment Status: " + paymentStatus);
                        System.out.println("=====================================");
                        
                        return String.valueOf(paymentId);
                    }
                } else {
                    System.err.println("No payment found or payment not successful");
                    System.err.println("Status: " + status);
                    System.err.println("Message: " + jsonResponse.getString("msg"));
                }
            }
            
            System.out.println("=====================================");
            return null;
            
        } catch (Exception e) {
            System.err.println("Error retrieving payment details: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
