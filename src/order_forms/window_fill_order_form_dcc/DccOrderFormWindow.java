package order_forms.window_fill_order_form_dcc;

import core.AppCore;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tables_data.feature_sets.FeatureSet;

import java.io.IOException;

public class DccOrderFormWindow {
    private DccOrderFormWindowController controller;

    public DccOrderFormWindow(FeatureSet featureSet) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dccOrderFormWindow.fxml"));
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        controller = loader.getController();
        controller.setFeatureSet(featureSet);

        stage.initOwner(AppCore.getMainStage());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Заполнение данных");
        stage.setResizable(false);

        stage.show();
    }
}

