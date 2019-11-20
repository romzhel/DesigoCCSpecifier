package order_forms.window_fill_order_form_eng;

import core.AppCore;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import order_forms.OrderFormType;

import java.io.IOException;

public class EngOrderFormWindow {
    private static OrderFormType type;

    private EngOrderFormWindowController controller;

    public EngOrderFormWindow(OrderFormType type) {
        EngOrderFormWindow.type = type;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("engOrderFormWindow.fxml"));
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(loader.load()));
        } catch (
                IOException e) {
            e.printStackTrace();
        }

        controller = loader.getController();

        stage.initOwner(AppCore.getMainStage());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Заполнение данных");
        stage.setResizable(false);

        stage.show();
    }

    public static OrderFormType getType() {
        return type;
    }
}
