package window_main.menus;

import core.Calculator;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import tables_data.feature_sets.FeatureSet;
import tables_data.feature_sets.FeatureSets;

import java.util.function.Consumer;

public class FeatSetsMenu extends Menu {
    public static final String SPACE = "    ";

    public FeatSetsMenu(String text, Consumer<FeatureSet> action, MenuItem... menuItems) {
        super(text);

        Calculator.getInstance().calculationStatusProperty().addListener((observable, oldValue, newValue) -> {
            getItems().clear();
            for (FeatureSet fs : FeatureSets.getInstance().getItems()) {
                if (!fs.isOverLimited()) {
                    MenuItem menuItem = new MenuItem(String.format("%s [%,.2f EUR]%s", SPACE + fs.getDescriptionEn(),
                            fs.getSummaryСost(), SPACE));
                    menuItem.setOnAction(event -> action.accept(fs));
                    getItems().add(menuItem);
                }
            }

            getItems().addAll(menuItems);
        });
        Calculator.getInstance().resfreshCalculationStatus();
    }
}
