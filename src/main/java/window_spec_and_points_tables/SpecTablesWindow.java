package window_spec_and_points_tables;

import core.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SpecTablesWindow {
    private static final Logger logger = LogManager.getLogger(SpecTablesWindow.class);
    private Stage stage;

    public SpecTablesWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/spec_window.fxml"));
        AnchorPane rootAnchorPane = null;
        try {
            rootAnchorPane = (AnchorPane) loader.load();
        } catch (Exception e) {
            logger.error("Loading UI error {}", e.getMessage(), e);
        }

        stage = new Stage();
        stage.setScene(new Scene(rootAnchorPane));

        stage.initOwner(App.getMainStage());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Выбор столбцов с данными");
        stage.setResizable(false);

        App.getSpecTables().setStage(stage);

        stage.setOnCloseRequest(event -> { //--- actions if windows was closed
            logger.trace("trying to close spec workbook");
            App.getSpecTables().closeExcelSpecFile();
        });

        stage.show();
    }
}
