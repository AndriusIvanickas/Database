import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class MultiTextFieldInputDialog extends Dialog<String[]> {

    public MultiTextFieldInputDialog() {
        setTitle("Ivesti duomenis");
        setHeaderText(null);

        // Set the button types.
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField dataField = new TextField();
        dataField.setPromptText("Data");
        TextField pavadinimasField = new TextField();
        pavadinimasField.setPromptText("Pavadinimas");
        TextField svarstykliuField = new TextField();
        svarstykliuField.setPromptText("Svarstykliu Parodymai");
        TextField deziuField = new TextField();
        deziuField.setPromptText("Deziu Skaicius");

        grid.add(new Label("Data:"), 0, 0);
        grid.add(dataField, 1, 0);
        grid.add(new Label("Pavadinimas:"), 0, 1);
        grid.add(pavadinimasField, 1, 1);
        grid.add(new Label("Svarstykliu Parodymai:"), 0, 2);
        grid.add(svarstykliuField, 1, 2);
        grid.add(new Label("Deziu Skaicius:"), 0, 3);
        grid.add(deziuField, 1, 3);

        getDialogPane().setContent(grid);

        // Convert the result to a username-password-pair when the login button is clicked.
        setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new String[]{
                        dataField.getText(),
                        pavadinimasField.getText(),
                        svarstykliuField.getText(),
                        deziuField.getText()
                };
            }
            return null;
        });
    }}


