import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ZUR {
    private int id;
    private String pav;

    // Constructor
    public ZUR(int id, String pav) {
        this.id = id;
        this.pav = pav;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPav() {
        return pav;
    }

    public void setPav(String pav) {
        this.pav = pav;
    }

    // Database operations
    public static List<ZUR> getAllZUR(SQLite db) {
        List<ZUR> zurList = new ArrayList<>();
        String sql = "SELECT ID, PAV FROM ZUR";
        try (Connection conn = db.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ZUR zur = new ZUR(rs.getInt("ID"), rs.getString("PAV"));
                zurList.add(zur);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return zurList;
    }

    // Additional methods as required
}
