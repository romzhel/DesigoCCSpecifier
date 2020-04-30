package order_forms.window_fill_order_form_dcc;

import core.BuildingTypes;
import excel.ExportOrderForm;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import order_forms.DataLoaderFromExcelToUi;
import order_forms.OrderFormFactory;
import price_list.OrderPosition;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DccOrderFormWindowController implements Initializable {
    private List<OrderPosition> spec;
    @FXML
    public TextField tfDebtorN;
    @FXML
    public TextField tfDebtorEn;
    @FXML
    public TextField tfDebtorRu;
    @FXML
    public TextField tfCountryFull;
    @FXML
    public TextField tfCountryShort;
    @FXML
    public TextField tfCompany;
    @FXML
    public TextField tfProjectName;
    @FXML
    public TextField tfCustomer;
    @FXML
    public TextArea taAddressEn;
    @FXML
    public TextArea taAddressRu;
    @FXML
    public TextField tfPostIndex;
    @FXML
    public ComboBox<String> cbBuildingType;
    @FXML
    public Label lDongle;
    @FXML
    public TextField tfDongle;
    @FXML
    public Label lCsid;
    @FXML
    public TextField tfCsid;
    @FXML
    public Label lMigration;
    @FXML
    public ComboBox<String> cbMigrSystemType;
    @FXML
    public Label lMigrSystemName;
    @FXML
    public TextField tfMigrSystemName;
    @FXML
    public Button btnOk;
    @FXML
    public Button btnCancel;
    private OrderFormFactory orderForm;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbBuildingType.getItems().addAll(BuildingTypes.getInstance().getItems());
        cbMigrSystemType.getItems().addAll("Desigo Insight", "MM/MK8000");
        orderForm = OrderFormFactory.createDccOrderForm(this);

        tfProjectName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 40) {
                tfProjectName.setText(oldValue);
            }
        });
    }

    public void createForm() {
        orderForm = OrderFormFactory.createDccOrderForm(this);

        if (orderForm.checkFieldsFilling()) {
            return;
        }

        new ExportOrderForm(orderForm, spec, "/dcc_licences_order_form.xlsx");

        close();
    }

    public void close() {
        ((Stage) btnCancel.getScene().getWindow()).close();
    }

    public void setSpec(List<OrderPosition> spec) {
        this.spec = spec;
    }

    public void loadData() {
        new DataLoaderFromExcelToUi(orderForm);
    }
}
