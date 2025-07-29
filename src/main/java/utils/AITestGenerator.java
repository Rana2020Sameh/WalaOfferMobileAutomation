package utils;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class AITestGenerator {
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = ConfigManager.getProperty("openai.api.key");
    
    public static class TestCase {
        private String testName;
        private String description;
        private List<String> steps;
        private String expectedResult;
        
        // Constructors, getters, setters
        public TestCase(String testName, String description, List<String> steps, String expectedResult) {
            this.testName = testName;
            this.description = description;
            this.steps = steps;
            this.expectedResult = expectedResult;
        }
        
        // Getters
        public String getTestName() { return testName; }
        public String getDescription() { return description; }
        public List<String> getSteps() { return steps; }
        public String getExpectedResult() { return expectedResult; }
    }
    
    public static List<TestCase> generateTestCases(String featureDescription, String appType) {
        try {
            String prompt = buildPrompt(featureDescription, appType);
            String response = callOpenAI(prompt);
            return parseTestCases(response);
        } catch (Exception e) {
            System.err.println("Failed to generate AI test cases: " + e.getMessage());
            return getDefaultTestCases();
        }
    }
    
    private static String buildPrompt(String featureDescription, String appType) {
        return String.format(
            "Generate comprehensive test cases for a %s mobile application feature: %s. " +
            "Return JSON format with testName, description, steps array, and expectedResult. " +
            "Include positive, negative, boundary, and edge test cases. " +
            "Focus on mobile-specific scenarios like orientation changes, network issues, and different screen sizes.",
            appType, featureDescription
        );
    }
    
    private static String callOpenAI(String prompt) throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode requestBody = mapper.createObjectNode();
        requestBody.put("model", "gpt-3.5-turbo");
        
        ArrayNode messages = mapper.createArrayNode();
        ObjectNode message = mapper.createObjectNode();
        message.put("role", "user");
        message.put("content", prompt);
        messages.add(message);
        
        requestBody.set("messages", messages);
        requestBody.put("max_tokens", 2000);
        requestBody.put("temperature", 0.7);
        
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OPENAI_API_URL))
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(requestBody)))
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            JsonNode responseJson = mapper.readTree(response.body());
            return responseJson.get("choices").get(0).get("message").get("content").asText();
        } else {
            throw new RuntimeException("OpenAI API call failed: " + response.statusCode());
        }
    }
    
    private static List<TestCase> parseTestCases(String aiResponse) {
        List<TestCase> testCases = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.readTree(aiResponse);
            
            if (jsonResponse.isArray()) {
                for (JsonNode testCaseNode : jsonResponse) {
                    testCases.add(parseTestCase(testCaseNode));
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to parse AI response: " + e.getMessage());
        }
        return testCases;
    }
    
    private static TestCase parseTestCase(JsonNode testCaseNode) {
        String testName = testCaseNode.get("testName").asText();
        String description = testCaseNode.get("description").asText();
        String expectedResult = testCaseNode.get("expectedResult").asText();
        
        List<String> steps = new ArrayList<>();
        JsonNode stepsArray = testCaseNode.get("steps");
        for (JsonNode step : stepsArray) {
            steps.add(step.asText());
        }
        
        return new TestCase(testName, description, steps, expectedResult);
    }
    
    private static List<TestCase> getDefaultTestCases() {
        List<TestCase> defaultCases = new ArrayList<>();
        
        List<String> loginSteps = List.of(
            "Launch the application",
            "Navigate to login screen",
            "Enter valid email and password",
            "Tap login button"
        );
        
        defaultCases.add(new TestCase(
            "Valid Login Test",
            "Verify user can login with valid credentials",
            loginSteps,
            "User should be logged in successfully"
        ));
        
        return defaultCases;
    }
}
