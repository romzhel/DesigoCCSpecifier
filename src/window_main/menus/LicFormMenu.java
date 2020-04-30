package window_main.menus;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import order_forms.window_fill_order_form_dcc.DccOrderFormWindow;
import order_forms.window_fill_order_form_eng.EngOrderFormWindow;

import static order_forms.OrderFormType.*;

public class LicFormMenu {
    public static final String SPACE = "    ";

    public LicFormMenu(Menu fxMenu) {
        MenuItem dccEng = new MenuItem(SPACE + "Заказ инженерной лицензии" + SPACE);
        dccEng.setOnAction(event -> new EngOrderFormWindow(DCC_ENG));

        MenuItem compSpec = new CompSpecSelectMenuItem("Составной расчёт", DccOrderFormWindow::new);

        FeatSetsMenu dccMenu = new FeatSetsMenu(SPACE + "Desigo CC" + SPACE, spec -> new DccOrderFormWindow(spec.getSpecification()),
                new SeparatorMenuItem(), dccEng, new SeparatorMenuItem(), compSpec);

        Menu xwMenu = new Menu(SPACE + "XWorks" + SPACE);
        MenuItem newXWlic = new MenuItem(SPACE + "Заказ новой инженерной лицензии" + SPACE);
        newXWlic.setOnAction(event -> new EngOrderFormWindow(XWORKS_ENG_NEW));
        MenuItem extXWlic = new MenuItem(SPACE + "Заказ продления инженерной лицензии" + SPACE);
        extXWlic.setOnAction(event -> new EngOrderFormWindow(XWORKS_ENG_EXT));
        xwMenu.getItems().addAll(newXWlic, extXWlic);

        fxMenu.getItems().addAll(dccMenu, new SeparatorMenuItem(), xwMenu);
    }
}
