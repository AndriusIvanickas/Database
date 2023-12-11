import java.sql.*;

public class SQLiteJDBC {

    public static void main(String args[]) {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "CREATE TABLE DuomenuBaze " +
                    "(ID INT PRIMARY KEY     NOT NULL," +
                    " ZUR_ID           INT    NOT NULL, " +
                    " ZUR_DET_ID            INT     NOT NULL, " +
                    " LINE_ID        INT    NOT NULL, " +
                    " DUOM        TEXT)";

            String sql1 = "CREATE TABLE ZUR " +
                    "(ID INT PRIMARY KEY     NOT NULL," +
                    " PAV           TEXT) " ;


            String sql2= "CREATE TABLE ZUR_DET " +
                    "(ID INT PRIMARY KEY     NOT NULL," +
                    " ZUR_ID           INT    NOT NULL, " +
                    " PAV            TEXT     NOT NULL, " +
                    " TIP_ID        TEXT    NOT NULL, " +
                    " Len           INT    NOT NULL, " +
                    "Min           INT    NOT NULL, " +
                    "Max           INT    NOT NULL )";

            //stmt.executeUpdate(sql);
           //stmt.executeUpdate(sql1);
              //stmt.executeUpdate(sql2);

            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }
}
