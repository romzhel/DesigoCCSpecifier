package window_fill_order_form_dcc;

import core.AppCore;
import dialogs.Dialogs;
import excel.ExportDccOrderForm;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tables_data.feature_sets.FeatureSet;

import java.net.URL;
import java.util.ResourceBundle;

public class DccOrderFormWindowController implements Initializable {
    private FeatureSet featureSet;
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
    Label lMigration;
    @FXML
    ComboBox<String> cbMigrSystemType;
    @FXML
    Label lMigrSystemName;
    @FXML
    TextField tfMigrSystemName;
    @FXML
    Button btnOk;
    @FXML
    Button btnCancel;
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
            new Dialogs().showMessage("Ввод данных", "Необходимо заполнить все поля на латинице");
            return;
        }

        new ExportDccOrderForm(orderForm, featureSet);

        close();
    }

    public void close() {
        ((Stage) btnCancel.getScene().getWindow()).close();
    }

    public void setFeatureSet(FeatureSet featureSet) {
        this.featureSet = featureSet;
    }
}
