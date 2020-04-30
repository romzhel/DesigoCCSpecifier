package window_main;

import core.AppCore;
import core.VersionInfo;
import dialogs.Dialogs;
import excel.ExportSpecToExcel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import order_forms.window_fill_order_form_dcc.DccOrderFormWindow;
import tables_data.feature_sets.FeatureSet;
import tables_data.feature_sets.FeatureSets;
import tables_data.feature_sets.FeatureSetsTable;
import tables_data.options.Option;
import tables_data.options.OptionsTable;
import tables_data.size.SizeItem;
import tables_data.size.SizeTable;
import window_main.menus.CalcTypeMenu;
import window_main.menus.FileMenu;
import window_main.menus.LicFormMenu;

import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public static final String DESCRIPTION_COLUMN_NAME = "Описание";
    public static final String FOR_ORDER_COLUMN_NAME = "Заказ";
    public static final int DESCRIPTION_COLUMN_WIDTH = 325;
    public static final int FOR_ORDER_COLUMN_WIDTH = 85;
    public static final int FEATURE_SET_COLUMN_WIDTH = 126;
    public static final int DEFAULT_PANE = 1;
    public static final String WEB_PAGE_URI = "https://new.siemens.com/ru/ru/produkty/avtomatizaciya-i-bezopasnost-" +
            "zdanij/avtomatizaciya-zdanij/desigo/sistemy-upravleniya-zdaniem/desigo-cc.html";
    public static final String CLOUD_URI = "https://cloud.mail.ru/public/KHua/8pmtkDn2S";
    public static final String MANUAL_URI = "https://cloud.mail.ru/public/GLEx/X1dDhoU9u";

    @FXML
    TableView<SizeItem> tvSize;
    @FXML
    TableView<Option> tvOptions;
    @FXML
    TableView<FeatureSet> tvSpec;
    @FXML
    Accordion accrd;
    @FXML
    Menu mnuCalcType;
    @FXML
    Menu mnuLicForms;
    @FXML
    Menu mnuCompCalcs;
    @FXML
    Menu additionalComponents;
    @FXML
    Menu mnuFile;
    @FXML
    Label lDCCVersionInfo;
    @FXML
    Label lSchemeVersionInfo;
    @FXML
    AnchorPane backgroundPane;
    @FXML
    AnchorPane infoPane;
    @FXML
    Label build;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initVersionLabels();
        initTables();
        initPanes();
        initMenu();

        AppCore.setInfoPane(infoPane);
    }

    private void initVersionLabels() {
        lDCCVersionInfo.setText(VersionInfo.getInstance().getVersion(0));
        lSchemeVersionInfo.setText(VersionInfo.getInstance().getVersion(1));
        build.setText("(Версия программы " + AppCore.getBuildInfo() + ")");
    }

    private void initMenu() {
        new CalcTypeMenu(mnuCalcType);
        new LicFormMenu(mnuLicForms);
        new FileMenu(mnuFile);
    }

    private void initTables() {
        new SizeTable(tvSize);
        new OptionsTable(tvOptions);
        new FeatureSetsTable(tvSpec);
    }

    private void initPanes() {
        checkTitledPanes(accrd.getPanes().get(DEFAULT_PANE), null);

        accrd.expandedPaneProperty().addListener((observable, oldValue, newValue) -> {
            checkTitledPanes(newValue, oldValue);
        });
    }

    private void checkTitledPanes(TitledPane newPane, TitledPane oldPane) {
        if (newPane == null) {
            int closedPaneIndex = accrd.getPanes().indexOf(oldPane);
            int newIndex = closedPaneIndex + 1 < accrd.getPanes().size() ? closedPaneIndex + 1 : 0;
            accrd.setExpandedPane(accrd.getPanes().get(newIndex));
        } else {
            for (TitledPane titledPane : accrd.getPanes()) {
                if (!titledPane.equals(newPane)) {
                    titledPane.setExpanded(false);
                }
            }
            accrd.setExpandedPane(newPane);
        }
    }

    public void contextMenuShow() {
    }

    public void exportToExcel() {
        int index = tvSpec.getSelectionModel().getSelectedIndex();
        FeatureSet fs = tvSpec.getItems().get(index);
        if (!fs.isOverLimited()) {
            if (index != (FeatureSets.getInstance().getItems().size() - 1)) {
                if (new Dialogs().confirm("Выбор базового пакета", "ОБРАТИТЕ ВНИМАНИЕ," +
                        " выбран базовый пакет с ограниченной функциональностью. \n\nПродолжить?")) {
                    new ExportSpecToExcel(fs.getSpecification(), null);
                }
            } else {
                new ExportSpecToExcel(fs.getSpecification(), null);
            }

        } else {
            new Dialogs().showMessage("Экспорт спецификации в Excel", "Данный базовый пакет не может быть " +
                    "использован из-за превышения его возможностей");
        }
    }

    /*public void useSpecification() {
        AppCore.extractPointsFromSpec();
    }*/

    public void infoAbout() {
        backgroundPane.setVisible(true);
        infoPane.setVisible(true);
    }

    public void closeInfoPane() {
        infoPane.setVisible(false);
        backgroundPane.setVisible(false);
    }

    public void sendEmail() {
        try {
            Desktop.getDesktop().browse(new URI(String.format("mailto:roman.zheludkov@siemens.com?subject=%s",
                    lDCCVersionInfo.getText().replaceAll("\\s", "%20"))));
        } catch (Exception e) {
            new Dialogs().showMessage("Ошибка открытия почтового клиента", "К сожалению не удалось открыть " +
                    "клиента для отправки сообщения. Пожалуйста, откройте его вручную.");
        }
    }

    public void openWebPage() {
        openURI(WEB_PAGE_URI);
    }

    public void openCloud() {
        openURI(CLOUD_URI);
    }

    public void openManual() {
        openURI(MANUAL_URI);
    }

    private void selectMenuItems(Menu menu, boolean value) {
        for (MenuItem menuItem : menu.getItems()) {
            ((RadioMenuItem) menuItem).setSelected(value);
        }
    }

    private void openURI(String uri) {
        try {
            Desktop.getDesktop().browse(new URI(uri));
        } catch (Exception e) {
            new Dialogs().showMessage("Ошибка открытия страницы", "Не удалось перейти на страницу\n" + uri);
        }

    }

    public void generateOrderForm() {
        FeatureSet selectedFs = tvSpec.getSelectionModel().getSelectedItem();
        if (selectedFs != null) {
            if (!selectedFs.isOverLimited()) {
                new DccOrderFormWindow(tvSpec.getSelectionModel().getSelectedItem().getSpecification());
            } else {
                new Dialogs().showMessage("Экспорт спецификации в Excel", "Данный базовый пакет не может быть " +
                        "использован из-за превышения его возможностей");
            }
        }
    }
}
