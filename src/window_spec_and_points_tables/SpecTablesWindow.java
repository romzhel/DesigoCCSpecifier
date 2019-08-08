package window_spec_and_points_tables;

import core.AppCore;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class SpecTablesWindow {
    Stage stage;

    public SpecTablesWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("spec_window.fxml"));
        AnchorPane rootAnchorPane = null;
        try {
            rootAnchorPane = (AnchorPane) loader.load();
        } catch (IOException e) {
            System.out.println("ERROR - AnchorPane rootAnchorPane = (AnchorPane) loader.rootAnchorPane(); " + e.getMessage());
        }

        stage = new Stage();
        stage.setScene(new Scene(rootAnchorPane));

        stage.initOwner(AppCore.getMainStage());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Выбор столбцов с данными");
        stage.setResizable(false);

        AppCore.getSpecTables().setStage(stage);

        stage.setOnCloseRequest(event -> { //--- actions if windows was closed
            System.out.println("trying close spec workbook");
            AppCore.getSpecTables().closeExcelSpecFile();
        });

        stage.show();
    }
}
