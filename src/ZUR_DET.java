import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ZUR_DET {
    private int id;
    private int zurId;
    private String pav;
    private int tipId;
    private int len;
    private Integer min; // Using Integer to handle possible null values
    private Integer max; // Using Integer to handle possible null values


    // Constructor
    public ZUR_DET(int id, int zurId, String pav, int tipId, int len, Integer min, Integer max) {
        this.id = id;
        this.zurId = zurId;
        this.pav = pav;
        this.tipId = tipId;
        this.len = len;
        this.min = min;
        this.max = max;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getZurId() {
        return zurId;
    }

    public void setZurId(int zurId) {
        this.zurId = zurId;
    }

    public String getPav() {
        return pav;
    }

    public void setPav(String pav) {
        this.pav = pav;
    }

    public int getTipId() {
        return tipId;
    }

    public void setTipId(int tipId) {
        this.tipId = tipId;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    // Static method to get ZUR_DET records from the database
    public static List<ZUR_DET> getZURDETList(SQLite db, int zurId) {
        List<ZUR_DET> zurDetList = new ArrayList<>();
        String sql = "SELECT ID, ZUR_ID, PAV, TIP_ID, LEN, MIN, MAX FROM ZUR_DET WHERE ZUR_ID = 1";

        try (Connection conn = db.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ZUR_DET zurDet = new ZUR_DET(
                        rs.getInt("ID"),
                        rs.getInt("ZUR_ID"),
                        rs.getString("PAV"),
                        rs.getInt("TIP_ID"),
                        rs.getInt("LEN"),
                        rs.getObject("MIN") != null ? rs.getInt("MIN") : null,
                        rs.getObject("MAX") != null ? rs.getInt("MAX") : null
                );
                zurDetList.add(zurDet);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return zurDetList;
    }
    public static List<String> getColumnNames(SQLite db, int zurId) {
        List<String> columnNames = new ArrayList<>();
        String sql = "SELECT ID, PAV FROM ZUR_DET WHERE ZUR_ID = 1";

        try (Connection conn = db.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String columnName = rs.getString("PAV");
                System.out.println("Column Name: " + columnName);
                columnNames.add(columnName);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return columnNames;
    }


}
