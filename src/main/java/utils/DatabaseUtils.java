package utils;


import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseUtils {
    private static final String DB_URL = ConfigManager.getProperty("db.url", "");
    private static final String DB_USER = ConfigManager.getProperty("db.username", "");
    private static final String DB_PASSWORD = ConfigManager.getProperty("db.password", "");
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    public static List<Map<String, Object>> executeQuery(String query) {
        List<Map<String, Object>> results = new ArrayList<>();
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getObject(i));
                }
                results.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Database query failed: " + e.getMessage());
        }
        
        return results;
    }
    
    public static void executeUpdate(String query) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate(query);
            
        } catch (SQLException e) {
            System.err.println("Database update failed: " + e.getMessage());
        }
    }
    
    public static void cleanupTestData(String userId) {
        String query = "DELETE FROM user_sessions WHERE user_id = '" + userId + "'";
        executeUpdate(query);
    }
}
