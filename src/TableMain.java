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
        tableview.getColumns().clear();
        try {
            // Uzpildom TableView stulpelius
            List<String> columnNames = ZUR_DET.getColumnNames(db, 1);
            for (int i = 0; i < columnNames.size(); i++) {
                final int j = i;
                TableColumn<ObservableList<String>, String> col = new TableColumn<>(columnNames.get(i));

                // Supaprastinta su lambda funkcija
                col.setCellValueFactory(cellDataFeatures ->
                        new SimpleStringProperty(cellDataFeatures.getValue().get(j))
                );

                tableview.getColumns().add(col);
            }


            List<DUOM_DET> duomDetList = DUOM_DET.getDUOM_DETList(db, 1);

            // Uzpildom TableView duomenimis
            for (int i = 0; i < duomDetList.size(); i += 4) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int j = 0; j < 4; j++) {
                    if ((i + j) < duomDetList.size()) {
                        row.add(duomDetList.get(i + j).getDuom());
                    } else {
                        row.add("");
                    }
                }
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

                String lastIdQuery = "SELECT MAX(ID), MAX(ZUR_DET_ID), MAX(LINE_ID) FROM DUOM_DET";
                ResultSet lastIdResult = connection.createStatement().executeQuery(lastIdQuery);
                int lastId = lastIdResult.getInt(1);
                int lastZurDetId = lastIdResult.getInt(2);
                int lastLineId = lastIdResult.getInt(3);
                // Pakeisti

                if(lastZurDetId == 104) {
                    lastZurDetId = 100;
                }
                if(lastLineId == 4) {
                    lastLineId = 0;
                }
                int temp1 = lastLineId;
                int newLineId = temp1 + 1;
                int temp2 = lastZurDetId;
                int newId = lastId + 1;
                int newZurDetId = temp2 + 1;

                for (int i = 0; i < userInput.length; i++) {
                    String fieldValue = userInput[i].trim();
                    String insertQuery = "INSERT INTO DUOM_DET (ID, ZUR_ID, ZUR_DET_ID, LINE_ID, DUOM) VALUES (?, 1, ?, ?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                        preparedStatement.setInt(1, newId);
                        preparedStatement.setInt(2, newZurDetId);
                        preparedStatement.setInt(3, newLineId);
                        preparedStatement.setString(4, fieldValue);
                        preparedStatement.executeUpdate();
                    }

                    newId++;
                    newZurDetId++;
                    newLineId++;
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
