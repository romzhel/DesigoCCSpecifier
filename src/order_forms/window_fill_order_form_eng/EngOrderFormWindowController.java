package order_forms.window_fill_order_form_eng;

import core.AppCore;
import excel.ExportOrderForm;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import order_forms.OrderForm;
import price_list.OrderPosition;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EngOrderFormWindowController implements Initializable {
    private OrderForm orderForm;

    @FXML
    public TextField tfDebtorN;
    @FXML
    public TextField tfDebtor;
    @FXML
    public TextField tfCustomer;
    @FXML
    public TextField tfDongle;
    @FXML
    public Label lDongle;
    @FXML
    public Label lAmount;
    @FXML
    public ComboBox<String> cbAmount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbAmount.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
        cbAmount.getSelectionModel().select(0);

        orderForm = OrderForm.createEngOrderForm(this, EngOrderFormWindow.getType());
    }

    public void createForm() {
        if (orderForm.checkFieldsFilling()) {
            return;
        }

        int amount = Integer.parseInt(cbAmount.getValue());
        ArrayList<OrderPosition> orderPositions = new ArrayList<>();

        switch (EngOrderFormWindow.getType()) {
            case DCC_ENG:
                orderPositions.add(AppCore.getPriceList().getNewOrderPosition("CCA-ENG", amount));
                break;
            case XWORKS_ENG_NEW:
                orderPositions.add(AppCore.getPriceList().getNewOrderPosition("CTX-LEN.5M-NEW", amount));
                break;
            case XWORKS_ENG_EXT:
                orderPositions.add(AppCore.getPriceList().getNewOrderPosition("CTX-LEN.5M-EXT", amount));
        }

        new ExportOrderForm(orderForm, orderPositions, "/eng_licences_order_form.xlsx");

        close();
    }

    public void close() {
        ((Stage) tfCustomer.getScene().getWindow()).close();
    }
}
