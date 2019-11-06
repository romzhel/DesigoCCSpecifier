package window_fill_order_form_eng;

import com.sun.org.apache.xpath.internal.operations.Or;
import core.AppCore;
import dialogs.Dialogs;
import excel.ExportDccOrderForm;
import excel.ExportEngOrderForm;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import price_list.OrderPosition;
import window_fill_order_form_dcc.OrderForm;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EngOrderFormWindowController implements Initializable {
    private OrderForm orderForm;

    @FXML
    public TextField tfCustomer;
    @FXML
    public TextField tfDongle;
    @FXML
    public Label lDongle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (EngOrderFormWindow.type == EngOrderFormWindow.DCC_ENG) {
            orderForm = OrderForm.createDccEngOrderForm(this);
        } else if (EngOrderFormWindow.type == EngOrderFormWindow.XWORKS_ENG_NEW) {
            orderForm = OrderForm.createXwEngNewOrderForm(this);
        } else {
            orderForm = OrderForm.createXwEngExtOrderForm(this);
        }
    }


    public void createForm() {
        if (orderForm.checkFieldsFilling()) {
            new Dialogs().showMessage("Ввод данных", "Необходимо заполнить все поля на латинице");
            return;
        }

        ArrayList<OrderPosition> orderPositions = new ArrayList<>();
        if (EngOrderFormWindow.type == EngOrderFormWindow.DCC_ENG) {
//            orderForm = OrderForm.createDccEngOrderForm(this);
            orderPositions.add(AppCore.getPriceList().getNewOrderPosition("CCA-ENG", 1));
        } else if (EngOrderFormWindow.type == EngOrderFormWindow.XWORKS_ENG_NEW) {
//            orderForm = OrderForm.createXwEngNewOrderForm(this);
            orderPositions.add(AppCore.getPriceList().getNewOrderPosition("CTX-LEN.5M-NEW", 1));
        } else {
//            orderForm = OrderForm.createXwEngExtOrderForm(this);
            orderPositions.add(AppCore.getPriceList().getNewOrderPosition("CTX-LEN.5M-EXT", 1));
        }

        new ExportEngOrderForm(orderForm, orderPositions);

        close();
    }

    public void close() {
        ((Stage)tfCustomer.getScene().getWindow()).close();
    }
}
