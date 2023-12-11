import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.util.Callback;

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
            ObservableList<String> row = null;
            while (rs1.next()) {
                String SQL2 = "select ID, DUOM from DuomenuBaze where ZUR_ID=1 and LINE_ID=" + rs1.getString(1);
                ResultSet rs2 = c.createStatement().executeQuery(SQL2);

                // Create a new ObservableList for each row
                row = FXCollections.observableArrayList();

                int count = 0; // Counter to track the number of data values added to the current row

                while (rs2.next()) {
                    String dataValue = rs2.getString(2);
                    System.out.println("Data Value: " + dataValue);
                    // Add each data value to the row ObservableList
                    row.add(dataValue);
                    count++;

                    // Check if 4 data values have been added, then start a new row
                    if (count == 4) {
                        System.out.println("Row added " + row);
                        data.add(row);
                        row = FXCollections.observableArrayList(); // Create a new row
                        count = 0; // Reset the counter
                    }
                }
            }

            // Add the last row if it has fewer than 4 data values
            if (!row.isEmpty()) {
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
        MultiTextFieldInputDialog dialog = new MultiTextFieldInputDialog();
        dialog.setHeaderText("Ideti duomenis");

        Optional<String[]> result = dialog.showAndWait();

        result.ifPresent(userInput -> {
            try {
                Connection connection = DriverManager.getConnection("jdbc:sqlite:test.db");

                String lastIdQuery = "SELECT MAX(ID), MAX(ZUR_DET_ID) FROM DuomenuBaze";
                ResultSet lastIdResult = connection.createStatement().executeQuery(lastIdQuery);
                int lastId = lastIdResult.getInt(1);
                int lastZurDetId = lastIdResult.getInt(2);

                int newId = lastId + 1;
                int newZurDetId = lastZurDetId + 1;

                for (int i = 0; i < userInput.length; i++) {
                    String fieldValue = userInput[i].trim();
                    String insertQuery = "INSERT INTO DuomenuBaze (ID, ZUR_ID, ZUR_DET_ID, LINE_ID, DUOM) VALUES (?, 1, ?, 1, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                        preparedStatement.setInt(1, newId);
                        preparedStatement.setInt(2, newZurDetId);
                        preparedStatement.setString(3, fieldValue);
                        preparedStatement.executeUpdate();
                    }

                    newId++;
                    newZurDetId++;
                }

                connection.close();

                // Clear existing columns and items in the TableView

                tableview.getColumns().clear();
                tableview.getItems().clear();
                // Rebuild the data and set it to the TableView
                buildData();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error on Building Data");
            }

        });
    }

}
