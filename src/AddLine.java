import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddLine {

    public void addLineToDatabase(String ID, String ZUR_ID, String ZUR_DET_ID, String Line_ID, String DUOM) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            connection.setAutoCommit(false);

            String sql = "INSERT INTO DuomenuBaze (ID, ZUR_ID, ZUR_DET_ID, Line_ID, DUOM) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, ID);
                preparedStatement.setString(2, ZUR_ID);
                preparedStatement.setString(3, ZUR_DET_ID);
                preparedStatement.setString(4, Line_ID);
                preparedStatement.setString(5, DUOM);

                preparedStatement.executeUpdate();
            } // The try-with-resources statement automatically closes the PreparedStatement

            connection.commit();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
