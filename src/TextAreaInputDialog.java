import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * A dialog that shows a TextArea input
 */
public class TextAreaInputDialog extends Dialog<String> {

    private final GridPane grid;
    private final TextArea textArea;
    private final String defaultValue;

    /**
     * Creates a new TextInputDialog without a default value entered into the
     * dialog {@link TextArea}.
     */
    public TextAreaInputDialog() {
        this("");
    }

    /**
     * Creates a new TextInputDialog with the default value entered into the
     * dialog {@link TextArea}.
     */
    public TextAreaInputDialog(@NamedArg("defaultValue") String defaultValue) {
        final DialogPane dialogPane = getDialogPane();

        // -- textarea
        this.textArea = new TextArea(defaultValue);
        this.textArea.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        GridPane.setFillWidth(textArea, true);

        this.defaultValue = defaultValue;

        this.grid = new GridPane();
        this.grid.setHgap(10);
        this.grid.setMaxWidth(Double.MAX_VALUE);
        this.grid.setAlignment(Pos.CENTER_LEFT);

        dialogPane.contentTextProperty().addListener(o -> updateGrid());

        setTitle("Iraryti zurnala");
        dialogPane.setHeaderText("Text Input");
        dialogPane.getStyleClass().add("text-input-dialog");
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        updateGrid();

        setResultConverter((dialogButton) -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            return data == ButtonBar.ButtonData.OK_DONE ? textArea.getText() : null;
        });
    }

    public final TextArea getEditor() {
        return textArea;
    }

    public final String getDefaultValue() {
        return defaultValue;
    }

    private void updateGrid() {
        grid.getChildren().clear();

        grid.add(textArea, 1, 0);
        getDialogPane().setContent(grid);

        Platform.runLater(() -> textArea.requestFocus());
    }
}
