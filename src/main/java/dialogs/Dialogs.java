package dialogs;

import core.App;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Modality;

import java.io.File;
import java.util.Optional;

public class Dialogs {

    public String textInput(String title, String text, String defaultValue) {
        ButtonType cancel = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(text);
        dialog.getEditor().setPrefWidth(250);
        dialog.getEditor().setText(defaultValue);

        dialog.getDialogPane().getButtonTypes().set(1, cancel);

// Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) return result.get();
        else return null;
    }

    public File openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл");
        FileChooser.ExtensionFilter excelFilter = new FileChooser.ExtensionFilter("Файл ExcelTable", "*.xls*");//Расширение
        fileChooser.getExtensionFilters().add(excelFilter);
        File file = fileChooser.showOpenDialog(App.getMainStage());//Указываем текущую сцену

        return file;
    }

    public File saveFile(String fileName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите место сохранения файла");
        FileChooser.ExtensionFilter excelFilter = new FileChooser.ExtensionFilter("Файлы Excel", "*.xls");//Расширение
        fileChooser.getExtensionFilters().add(excelFilter);
        fileChooser.setInitialFileName(fileName);
        return fileChooser.showSaveDialog(App.getMainStage());
    }

    public void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initModality(Modality.APPLICATION_MODAL);

        alert.showAndWait();
    }

    public boolean confirm(String title, String message) {
        ButtonType cancel = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK, cancel);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.initModality(Modality.APPLICATION_MODAL);

        Optional<ButtonType> option = alert.showAndWait();

        return option.get() == ButtonType.OK;
    }
}
