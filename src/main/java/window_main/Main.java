package window_main;

import core.App;
import core.ConsoleMode;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main extends Application {
    private static final Logger logger = LogManager.getLogger(Main.class);
    private final String TITLE = "DesigoCCSpecifier";

    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.trace("Starting user interface...");
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/window_main.fxml"));
        primaryStage.setTitle(TITLE);
        primaryStage.setScene(new Scene(root));

        primaryStage.show();
        primaryStage.setMinWidth(primaryStage.getWidth());
        primaryStage.setMinHeight(primaryStage.getHeight());

        App.setMainStage(primaryStage);

        primaryStage.heightProperty().addListener((observable, oldValue, newValue) ->
                App.resizeView(primaryStage.getWidth(), (double) newValue));
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) ->
                App.resizeView((double) newValue, primaryStage.getHeight()));

        App.resizeView(App.getMainStage().getWidth(), App.getMainStage().getHeight());
    }


    public static void main(String[] args) {
        App.init();

        ConsoleMode consoleMode = new ConsoleMode(args);
        if (consoleMode.check()) {
            System.out.println("console mode has worked");
            Platform.exit();
        } else {
            launch(args);
        }
    }
}
