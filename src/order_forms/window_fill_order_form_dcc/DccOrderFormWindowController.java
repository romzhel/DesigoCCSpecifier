package order_forms.window_fill_order_form_dcc;

import core.AppCore;
import dialogs.Dialogs;
import excel.ExportOrderForm;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import order_forms.OrderForm;
import tables_data.feature_sets.FeatureSet;

import java.net.URL;
import java.util.ResourceBundle;

public class DccOrderFormWindowController implements Initializable {
    private FeatureSet featureSet;
    @FXML
    public TextField tfDebtorN;
    @FXML
    public TextField tfDebtor;
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
    public TextArea taAddress;
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
    private OrderForm orderForm;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbBuildingType.getItems().addAll(AppCore.getOrderFormBuildingTypes());
        cbMigrSystemType.getItems().addAll("Desigo Insight", "MM/MK8000");
        orderForm = OrderForm.createDccOrderForm(this);
    }

    public void createForm() {
        orderForm = OrderForm.createDccOrderForm(this);

        if (orderForm.checkFieldsFilling()) {
            return;
        }

        new ExportOrderForm(orderForm, featureSet.getSpecification(), "/dcc_licences_order_form.xlsx");

        close();
    }

    public void close() {
        ((Stage) btnCancel.getScene().getWindow()).close();
    }

    public void setFeatureSet(FeatureSet featureSet) {
        this.featureSet = featureSet;
    }
}
