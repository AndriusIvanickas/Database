import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DUOM_DET {
    private int id;
    private int zurId;
    private int zurDetId;
    private int lineId;
    private String duom;

    // Constructor
    public DUOM_DET(int id, int zurId, int zurDetId,int lineId, String duom) {
        this.id = id;
        this.zurId = zurId;
        this.zurDetId = zurDetId;
        this.lineId = lineId;
        this.duom = duom;

    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getZurId() {
        return zurId;
    }
    public int getLineId() {
        return lineId;
    }

    public void setZurId(int zurId) {
        this.zurId = zurId;
    }

    public int getZurDetId() {
        return zurDetId;
    }

    public void setZurDetId(int zurDetId) {
        this.zurDetId = zurDetId;
    }

    public String getDuom() {
        return duom;
    }

    public void setDuom(String duom) {
        this.duom = duom;
    }
    public void setLineId(int lineId) {
        this.lineId = lineId;
    }

    public static List<DUOM_DET> getDUOM_DETList(SQLite db, int zurId) {
        List<DUOM_DET> duomDetList = new ArrayList<>();
        String sql = "SELECT ID, ZUR_ID, ZUR_DET_ID,Line_ID, DUOM FROM DUOM_DET WHERE ZUR_ID = ?";

        try (Connection conn = db.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, zurId);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                DUOM_DET duomDet = new DUOM_DET(
                        rs.getInt("ID"),
                        rs.getInt("ZUR_ID"),
                        rs.getInt("ZUR_DET_ID"),
                        rs.getInt("LINE_ID"),
                        rs.getString("DUOM")
                );
                duomDetList.add(duomDet);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return duomDetList;
    }
}
    // Additional methods as required

