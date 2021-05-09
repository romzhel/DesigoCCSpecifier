package order_forms.window_fill_order_form_eng;

import excel.ExportOrderForm;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import order_forms.DataLoaderFromExcelToUi;
import order_forms.OrderFormFactory;
import price_list.OrderPosition;
import price_list.PriceList;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EngOrderFormWindowController implements Initializable {
    private OrderFormFactory orderForm;

    @FXML
    public TextField tfDebtorN;
    @FXML
    public TextField tfDebtorEn;
    @FXML
    public TextField tfDebtorRu;
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

        orderForm = OrderFormFactory.createEngOrderForm(this, EngOrderFormWindow.getType());
    }

    public void createForm() {
        if (orderForm.checkFieldsFilling()) {
            return;
        }

        int amount = Integer.parseInt(cbAmount.getValue());
        ArrayList<OrderPosition> orderPositions = new ArrayList<>();

        switch (EngOrderFormWindow.getType()) {
            case DCC_ENG:
                orderPositions.add(PriceList.getInstance().getNewOrderPosition("CCA-ENG", amount));
                break;
            case XWORKS_ENG_NEW:
                orderPositions.add(PriceList.getInstance().getNewOrderPosition("CTX-LEN.5M-NEW", amount));
                break;
            case XWORKS_ENG_EXT:
                orderPositions.add(PriceList.getInstance().getNewOrderPosition("CTX-LEN.5M-EXT", amount));
        }

        new ExportOrderForm(orderForm, orderPositions, "/eng_licences_order_form.xlsx");

        close();
    }

    public void close() {
        ((Stage) tfCustomer.getScene().getWindow()).close();
    }

    public void loadData(){
        new DataLoaderFromExcelToUi(orderForm);
    }
}
