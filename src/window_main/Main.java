package window_main;

import core.AppCore;
import core.ConsoleMode;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private final String TITLE = "DesigoCCSpecifier";

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("window_main.fxml"));
        primaryStage.setTitle(TITLE);
        primaryStage.setScene(new Scene(root));

        primaryStage.show();
        primaryStage.setMinWidth(primaryStage.getWidth());
        primaryStage.setMinHeight(primaryStage.getHeight());

        AppCore.setMainStage(primaryStage);

        primaryStage.heightProperty().addListener((observable, oldValue, newValue) ->
                AppCore.resizeView(primaryStage.getWidth(), (double) newValue));
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) ->
                AppCore.resizeView((double) newValue, primaryStage.getHeight()));

        AppCore.resizeView(AppCore.getMainStage().getWidth(), AppCore.getMainStage().getHeight());
    }


    public static void main(String[] args) {
        AppCore.init();

        ConsoleMode consoleMode = new ConsoleMode(args);
        if (consoleMode.check()) {
            System.out.println("console mode has worked");
            Platform.exit();
        } else {
            launch(args);
        }
    }
}
