import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLite {

    private String url ; // Database URL

    // Constructor
    public SQLite(String url) {
        this.url = url;
    }

    // Method to connect to the database
    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public ResultSet executeQuery(String sql) {
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public void executeUpdate(String sql) {
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    // You can add additional methods for executing queries, updates, etc., if needed
}
