package utils;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

public class APIUtils {
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();
    
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String BASE_URL = ConfigManager.getProperty("base.url", "");
    
    public static HttpResponse<String> get(String endpoint, Map<String, String> headers) {
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + endpoint))
                    .timeout(Duration.ofSeconds(30))
                    .GET();
            
            if (headers != null) {
                headers.forEach(requestBuilder::header);
            }
            
            return client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException("GET request failed", e);
        }
    }
    
    public static HttpResponse<String> post(String endpoint, String jsonBody, Map<String, String> headers) {
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + endpoint))
                    .timeout(Duration.ofSeconds(30))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody));
            
            if (headers != null) {
                headers.forEach(requestBuilder::header);
            }
            
            return client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException("POST request failed", e);
        }
    }
    
    public static JsonNode parseResponse(String responseBody) {
        try {
            return mapper.readTree(responseBody);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON response", e);
        }
    }
    
    public static boolean validateUserExists(String email) {
        HttpResponse<String> response = get("/api/users/validate?email=" + email, null);
        return response.statusCode() == 200;
    }
    
    public static String authenticateUser(String email, String password) {
        String loginPayload = String.format(
            "{\"email\":\"%s\",\"password\":\"%s\"}", 
            email, password
        );
        
        HttpResponse<String> response = post("/api/auth/login", loginPayload, null);
        
        if (response.statusCode() == 200) {
            JsonNode responseJson = parseResponse(response.body());
            return responseJson.get("token").asText();
        }
        
        return null;
    }
}

