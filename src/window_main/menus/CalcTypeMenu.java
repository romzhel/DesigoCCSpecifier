package window_main.menus;

import core.AppCore;
import core.Calculator;
import javafx.scene.control.*;

import java.util.Arrays;

public class CalcTypeMenu {
    public static final String SPACE = "    ";
    public static final String MENU_CAPTION = "Тип расчёта: ";
    public static final String CALC_NEW = "Новая система";
    public static final String CALC_EXTENSION = "Расширение системы";
    public static final String CALC_SSM = "Стандартная миграция - SSM";
    public static final String CALC_PSM = "Привилегированная миграция - PSM";
    public static final String RESET = "Сбросить данные расчёта";

    public CalcTypeMenu(Menu fxMenu) {
        RadioMenuItem calcNewSystem = new RadioMenuItem(SPACE + CALC_NEW + SPACE);
        RadioMenuItem calcSystemExt = new RadioMenuItem(SPACE + CALC_EXTENSION + SPACE);
        RadioMenuItem calcSSMMigr = new RadioMenuItem(SPACE + CALC_SSM + SPACE);
        RadioMenuItem calcPSMMigr = new RadioMenuItem(SPACE + CALC_PSM + SPACE);

        ToggleGroup calcMenuGroup = new ToggleGroup();
        RadioMenuItem[] calcItems = {calcNewSystem, calcSystemExt, calcSSMMigr, calcPSMMigr};
        for (RadioMenuItem rmi : calcItems) {
            rmi.setToggleGroup(calcMenuGroup);
        }

        calcMenuGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fxMenu.setText(MENU_CAPTION + ((RadioMenuItem) newValue).getText().trim());
                AppCore.getCalculator().setCalcMode(Calculator.CalcType.values()[Arrays.asList(calcItems).indexOf(newValue)]);
            }
        });

        calcNewSystem.setSelected(true);

        MenuItem resetCalc = new MenuItem(SPACE + RESET + SPACE);
        resetCalc.setOnAction(event -> AppCore.orderDetails());

        fxMenu.getItems().addAll(calcNewSystem, calcSystemExt, calcSSMMigr, calcPSMMigr, new SeparatorMenuItem(), resetCalc);
    }
}
