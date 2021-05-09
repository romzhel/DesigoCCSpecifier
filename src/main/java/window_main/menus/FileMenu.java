package window_main.menus;

import core.App;
import excel.ExportSpecToExcel;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class FileMenu {
    public static final String SPACE = "    ";

    public FileMenu(Menu fxMenu) {
        MenuItem getFromFile = new MenuItem(SPACE + "Сделать расчёт по спецификации оборудования" + SPACE);
        getFromFile.setOnAction(event -> App.extractPointsFromSpec());

        FeatSetsMenu dccMenu = new FeatSetsMenu(SPACE + "Сохранить спецификацию результата расчёта" + SPACE,
                ExportSpecToExcel::new);

        fxMenu.getItems().addAll(getFromFile, new SeparatorMenuItem(), dccMenu);
    }
}
