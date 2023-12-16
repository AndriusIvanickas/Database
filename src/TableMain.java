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


    SQLite db = new SQLite("jdbc:sqlite:test.db");
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
        data = FXCollections.observableArrayList();
        tableview.getColumns().clear();  // Clear existing columns if necessary

        try {
            // Fetch column names and create columns in the TableView
            List<String> columnNames = ZUR_DET.getColumnNames(db, 1);
            for (int i = 0; i < columnNames.size(); i++) {
                final int j = i;
                TableColumn<ObservableList<String>, String> col = new TableColumn<>(columnNames.get(i));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j));
                    }
                });
                tableview.getColumns().add(col);
            }

            // Assume DUOM_DET.getDUOM_DETList() has been correctly implemented
            List<DUOM_DET> duomDetList = DUOM_DET.getDUOM_DETList(db, 1);

            // Process the data and populate the TableView
            for (int i = 0; i < duomDetList.size(); i += 4) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int j = 0; j < 4; j++) {
                    if ((i + j) < duomDetList.size()) {
                        row.add(duomDetList.get(i + j).getDuom());
                    } else {
                        row.add("");  // Fill remaining cells with empty strings if needed
                    }
                }
                data.add(row);
            }

            // Set the items for the TableView
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

                String lastIdQuery = "SELECT MAX(ID), MAX(ZUR_DET_ID) FROM DUOM_DET";
                ResultSet lastIdResult = connection.createStatement().executeQuery(lastIdQuery);
                int lastId = lastIdResult.getInt(1);
                int lastZurDetId = lastIdResult.getInt(2);

                int newId = lastId + 1;
                int newZurDetId = lastZurDetId + 1;

                for (int i = 0; i < userInput.length; i++) {
                    String fieldValue = userInput[i].trim();
                    String insertQuery = "INSERT INTO DUOM_DET (ID, ZUR_ID, ZUR_DET_ID, LINE_ID, DUOM) VALUES (?, 1, ?, 1, ?)";
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
