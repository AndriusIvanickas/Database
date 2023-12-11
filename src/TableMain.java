import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import javafx.util.Callback;

import javax.xml.crypto.Data;

public class TableMain extends Application {

    private ObservableList<ObservableList> data;
    private TableView tableview;

    public static void main(String[] args) {
        launch(args);
    }

    ArrayList setA = new ArrayList();

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        //TableView
        tableview = new TableView();
        buildData();


        Button button = new Button("Prideti");
        button.setOnAction(e -> handleButtonAction());

        button.setAlignment(Pos.BOTTOM_CENTER);
        VBox vBox = new VBox(tableview);
        vBox.getChildren().add(button);
        vBox.setAlignment(Pos.TOP_CENTER);

        root.setCenter(vBox);

        Scene scene = new Scene(root, 800, 400);


        tableview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        primaryStage.setTitle("Duomenu baze");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void buildData() {
        Connection c;
        data = FXCollections.observableArrayList();
        try {
            c = DriverManager.getConnection("jdbc:sqlite:test.db");

            // Create a list to store column names
            List<String> columnNames = new ArrayList<>();

            // Print column names
            String SQL = "select ID, PAV from ZUR_DET where zur_id=1";
            ResultSet rs = c.createStatement().executeQuery(SQL);
            while (rs.next()) {
                String columnName = rs.getString(2);
                System.out.println("Column Name: " + columnName);
                columnNames.add(columnName);

                // Create TableColumn outside the loop
            }

            // Create TableColumn for each column name
            for (int i = 0; i < columnNames.size(); i++) {
                final int j = i;
                TableColumn col = new TableColumn(columnNames.get(j));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        List<String> rowValues = param.getValue();
                        if (j >= 0 && j < rowValues.size()) {
                            return new SimpleStringProperty(rowValues.get(j).toString());
                        } else {
                            return new SimpleStringProperty("");
                        }
                    }
                });

                tableview.getColumns().add(col);
            }

            // Print data
            String SQL1 = "select distinct LINE_ID from DuomenuBaze where zur_id=1";
            ResultSet rs1 = c.createStatement().executeQuery(SQL1);
            while (rs1.next()) {
                String SQL2 = "select ID, DUOM from DuomenuBaze where ZUR_ID=1 and LINE_ID=" + rs1.getString(1);
                ResultSet rs2 = c.createStatement().executeQuery(SQL2);

                ObservableList<String> row = FXCollections.observableArrayList();
                while (rs2.next()) {
                    String dataValue = rs2.getString(2);
                    System.out.println("Data Value: " + dataValue);
                    row.add(dataValue);
                }

                System.out.println("Row added " + row);
                data.add(row);
            }

            tableview.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }


    private void handleButtonAction() {
        // Create a MultiTextFieldInputDialog
        MultiTextFieldInputDialog dialog = new MultiTextFieldInputDialog();
        dialog.setHeaderText("Ideti duomenis");

        // Show the dialog and capture the result.
        Optional<String[]> result = dialog.showAndWait();

        result.ifPresent(userInput -> {
            // Process the input values
            try {
                // Establish a connection to the database
                Connection connection = DriverManager.getConnection("jdbc:sqlite:test.db");

                // Retrieve the last ID from the database
                String lastIdQuery = "SELECT MAX(ID) ,MAX(ZUR_DET_ID) FROM DuomenuBaze";
                ResultSet lastIdResult = connection.createStatement().executeQuery(lastIdQuery);
                int lastId = lastIdResult.getInt(1);
                int lastZurDetId = lastIdResult.getInt(2);

                // Increment the last ID to generate a new ID
                int newId = lastId + 1;
                int newZurDetId = lastZurDetId + 1;

                // Insert individual rows for each field
                for (int i = 0; i < userInput.length; i++) {
                    String fieldValue = userInput[i].trim();
                    String insertQuery = "INSERT INTO DuomenuBaze (ID, ZUR_ID, ZUR_DET_ID, LINE_ID, DUOM) VALUES (?, 1, ?, 1, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                        preparedStatement.setInt(1, newId++);
                        preparedStatement.setInt(2, newZurDetId++);
                        preparedStatement.setString(3, fieldValue);
                        preparedStatement.executeUpdate();
                    }
                }

                // Close the connection
                connection.close();

                // Update the TableView with the updated data from the database
                buildData();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error on Building Data");
            }
        });
    }

}

