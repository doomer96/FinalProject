import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static final String URL = "jdbc:sqlite:employees.db";

    public static void init() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS employees (" +
                    "id TEXT PRIMARY KEY, name TEXT, email TEXT, " +
                    "pos TEXT, score INTEGER, salary REAL, type TEXT)");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void addEmployee(Employee e, String type) {
        String sql = "INSERT INTO employees VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, e.getId());
            pstmt.setString(2, e.getName());
            pstmt.setString(3, e.getEmail());
            pstmt.setString(4, e.getPosition());
            pstmt.setInt(5, e.getPerformanceScore());
            pstmt.setDouble(6, e.calculateSalary());
            pstmt.setString(7, type);
            pstmt.executeUpdate();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    public static void deleteEmployee(String id) {
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM employees WHERE id = ?")) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
