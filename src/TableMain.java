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

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.util.Callback;

public class TableMain extends Application {
    private int currentZurId = 1;
    private ObservableList<ObservableList> data;
    private TableView tableview;

    public static void main(String[] args) {
        launch(args);
    }


    SQLite db = new SQLite("jdbc:sqlite:test.db");

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();



        Button zurnalas1Button = new Button("Zurnalas1");
        Button zurnalas2Button = new Button("Zurnalas2");


        zurnalas1Button.setOnAction(e -> handleZurnalas1Action());
        zurnalas2Button.setOnAction(e -> handleZurnalas2Action());


        HBox topMenu = new HBox();
        topMenu.getChildren().addAll(zurnalas1Button, zurnalas2Button);
        topMenu.setAlignment(Pos.CENTER);
        topMenu.setSpacing(10);


        root.setTop(topMenu);

        //TableView
        tableview = new TableView();
        buildData(1);

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


    public void buildData(int zurId) {
        data = FXCollections.observableArrayList();
        tableview.getColumns().clear();
        try {

            List<String> columnNames = ZUR_DET.getColumnNames(db, zurId);

            for (int i = 0; i < columnNames.size(); i++) {
                final int j = i;
                TableColumn<ObservableList<String>, String> col = new TableColumn<>(columnNames.get(i));


                col.setCellValueFactory(cellDataFeatures ->
                        new SimpleStringProperty(cellDataFeatures.getValue().get(j))
                );

                tableview.getColumns().add(col);
            }


            List<DUOM_DET> duomDetList = DUOM_DET.getDUOM_DETList(db, zurId);

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

    private void handleZurnalas1Action() {
        tableview.getColumns().clear();
        tableview.getItems().clear();
        currentZurId = 1;

        buildData(1);
    }

    private void handleZurnalas2Action() {
        tableview.getColumns().clear();
        tableview.getItems().clear();
        currentZurId = 2;
        buildData(2);
    }


    private void handleButtonAction() {
        MultiTextFieldInputDialog dialog = new MultiTextFieldInputDialog();
        dialog.setHeaderText("Ideti duomenis");

        Optional<String[]> result = dialog.showAndWait();
        result.ifPresent(userInput -> {
            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                String lastIdQuery = "SELECT MAX(ID), MAX(ZUR_DET_ID), MAX(LINE_ID) FROM DUOM_DET WHERE ZUR_ID = ?";
                try (PreparedStatement lastIdStatement = connection.prepareStatement(lastIdQuery)) {
                    lastIdStatement.setInt(1, currentZurId);
                    ResultSet lastIdResult = lastIdStatement.executeQuery();
                    int lastId = lastIdResult.getInt(1);
                    int lastZurDetId = lastIdResult.getInt(2);
                    int lastLineId = lastIdResult.getInt(3);


                    if (currentZurId == 1 && (lastZurDetId == 104 || lastZurDetId == 108)) {
                        lastZurDetId = 100;
                    } else if (currentZurId == 2 && lastZurDetId < 104) {
                        lastZurDetId = 104;
                    }


                    int newLineId = lastLineId + 1;
                    int temp2 = lastZurDetId;
                    int newId = lastId + 1;
                    int newZurDetId = temp2 + 1;

                    for (int i = 0; i < userInput.length; i++) {
                        String fieldValue = userInput[i].trim();
                        String insertQuery = "INSERT INTO DUOM_DET (ID, ZUR_ID, ZUR_DET_ID, LINE_ID, DUOM) VALUES (?, ?, ?, ?, ?)";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                            preparedStatement.setInt(1, newId);
                            preparedStatement.setInt(2, currentZurId);
                            preparedStatement.setInt(3, newZurDetId);
                            preparedStatement.setInt(4, newLineId);
                            preparedStatement.setString(5, fieldValue);
                            preparedStatement.executeUpdate();
                        }

                        newId++;
                        newZurDetId++;
                    }

                    connection.close();
                    tableview.getColumns().clear();
                    tableview.getItems().clear();
                    buildData(currentZurId);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error on Building Data");
            }
        });
    }
}