import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * A dialog that shows four TextFields as input.
 */
public class MultiTextFieldInputDialog extends Dialog<String[]> {

    private final GridPane grid;
    private final TextField[] textFields;
    private final String[] defaultValues;

    /**
     * Creates a new MultiTextFieldInputDialog without default values.
     */
    public MultiTextFieldInputDialog() {
        this(new String[]{});
    }

    /**
     * Creates a new MultiTextFieldInputDialog with default values.
     */
    public MultiTextFieldInputDialog(@NamedArg("defaultValues") String[] defaultValues) {
        final DialogPane dialogPane = getDialogPane();

        // -- text fields
        this.textFields = new TextField[4];
        for (int i = 0; i < textFields.length; i++) {
            textFields[i] = new TextField(i < defaultValues.length ? defaultValues[i] : "");
            textFields[i].setMaxWidth(Double.MAX_VALUE);
            GridPane.setHgrow(textFields[i], Priority.ALWAYS);
            GridPane.setFillWidth(textFields[i], true);
        }

        this.defaultValues = defaultValues;

        this.grid = new GridPane();
        this.grid.setHgap(10);
        this.grid.setMaxWidth(Double.MAX_VALUE);
        this.grid.setAlignment(Pos.CENTER_LEFT);

        dialogPane.contentTextProperty().addListener(o -> updateGrid());

        setTitle("Iraryti zurnala");
        dialogPane.setHeaderText("Multi-Text Input");
        dialogPane.getStyleClass().add("text-input-dialog");
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        updateGrid();

        setResultConverter((dialogButton) -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            if (data == ButtonBar.ButtonData.OK_DONE) {
                String[] result = new String[textFields.length];
                for (int i = 0; i < textFields.length; i++) {
                    result[i] = textFields[i].getText();
                }
                return result;
            } else {
                return null;
            }
        });
    }

    public final TextField[] getTextFields() {
        return textFields;
    }

    public final String[] getDefaultValues() {
        return defaultValues;
    }

    private void updateGrid() {
        grid.getChildren().clear();

        for (int i = 0; i < textFields.length; i++) {
            grid.add(new Label("Field " + (i + 1)), 0, i);
            grid.add(textFields[i], 1, i);
        }

        getDialogPane().setContent(grid);

        Platform.runLater(() -> textFields[0].requestFocus());
    }
}
