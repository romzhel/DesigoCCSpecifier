package window_fill_order_form_dcc;

import core.AppCore;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import window_fill_order_form_eng.EngOrderFormWindowController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class OrderForm {
    private ArrayList<OrderFormItem> orderFormItems = new ArrayList<>();
//    private boolean newProject;
    private DccOrderFormWindowController controller;
    private String id;

    private OrderFormItem SYSTEM;
    private OrderFormItem ID;
    private OrderFormItem NEW_PROJECT;
    private OrderFormItem EXTENSION;
    private OrderFormItem MIGRATION;
    private OrderFormItem<TextField> COUNTRY_FULL;
    private OrderFormItem<TextField> COUNTRY_SHORT;
    private OrderFormItem<TextField> COMPANY;
    private OrderFormItem<TextField> PROJECT_NAME;
    private OrderFormItem<TextField> CUSTOMER;
    private OrderFormItem<TextArea> ADDRESS;
    private OrderFormItem<TextField> POST_INDEX;
    private OrderFormItem<ComboBox> BUILDING_TYPE;
    private OrderFormItem<ComboBox> MIGRATED_SYSTEM;
    private OrderFormItem<TextField> MIGRATED_SYSTEM_NAME;
    private OrderFormItem<TextField> DONGLE;
    private OrderFormItem<TextField> CSID;


    private OrderForm() {
        id = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        ID = new OrderFormItem<>(id);
    }

    public static OrderForm createDccOrderForm(DccOrderFormWindowController controller) {
        boolean isNewProject = AppCore.getCalculator().isNewProject();
        boolean isMigration = !AppCore.getCalculator().getMigrationSuffix().isEmpty();
        OrderForm orderForm = new OrderForm();
        orderForm.ID.setPosition("C11");
        orderForm.NEW_PROJECT = new OrderFormItem(isMigration ? "0" : isNewProject ? "1" : "0").setPosition("E19").setHidden(true);
        orderForm.EXTENSION = new OrderFormItem(isMigration ? "0" : isNewProject ? "0" : "1").setPosition("E21").setHidden(true);
        orderForm.MIGRATION = new OrderFormItem(isMigration ? "1" : "0").setPosition("E23").setHidden(true);
        orderForm.COUNTRY_FULL = new OrderFormItem<>(controller.tfCountryFull, "C13");
        orderForm.COUNTRY_SHORT = new OrderFormItem<>(controller.tfCountryShort, "J13");
        orderForm.COMPANY = new OrderFormItem<>(controller.tfCompany, "C15");
        orderForm.PROJECT_NAME = new OrderFormItem<>(controller.tfProjectName, "C17");
        orderForm.CUSTOMER = new OrderFormItem<>(controller.tfCustomer, "C25");
        orderForm.ADDRESS = new OrderFormItem<>(controller.taAddress, "C27");
        orderForm.POST_INDEX = new OrderFormItem<>(controller.tfPostIndex, "C29");
        orderForm.BUILDING_TYPE = new OrderFormItem<>(controller.cbBuildingType, "C31");
        orderForm.MIGRATED_SYSTEM = new OrderFormItem<>(controller.cbMigrSystemType, controller.lMigration, isMigration, "H21").setHidden(!isMigration);
        orderForm.MIGRATED_SYSTEM_NAME = new OrderFormItem<>(controller.tfMigrSystemName, controller.lMigrSystemName, isMigration, "H23").setHidden(!isMigration);
        orderForm.DONGLE = new OrderFormItem<>(controller.tfDongle, controller.lDongle, !isNewProject, "C33");
        orderForm.CSID = new OrderFormItem<>(controller.tfCsid, controller.lCsid, !isNewProject, "L33");
        orderForm.orderFormItems.addAll(Arrays.asList(orderForm.ID, orderForm.NEW_PROJECT, orderForm.EXTENSION,
                orderForm.COUNTRY_FULL, orderForm.COUNTRY_SHORT, orderForm.COMPANY, orderForm.PROJECT_NAME,
                orderForm.CUSTOMER, orderForm.ADDRESS, orderForm.POST_INDEX, orderForm.BUILDING_TYPE, orderForm.DONGLE,
                orderForm.CSID, orderForm.MIGRATION, orderForm.MIGRATED_SYSTEM, orderForm.MIGRATED_SYSTEM_NAME));
        return orderForm;
    }

    public static OrderForm createDccEngOrderForm(EngOrderFormWindowController controller) {
        OrderForm orderForm = new OrderForm();
        orderForm.ID.setPosition("C11");
        orderForm.SYSTEM = new OrderFormItem("Desigo CC").setPosition("C13");
        orderForm.NEW_PROJECT = new OrderFormItem("1").setPosition("E15").setHidden(true);
        orderForm.EXTENSION = new OrderFormItem("0").setPosition("E17").setHidden(true);
        orderForm.CUSTOMER = new OrderFormItem<>(controller.tfCustomer, "C19");
        orderForm.DONGLE = new OrderFormItem<>(controller.tfDongle, controller.lDongle, false, "C21");
        orderForm.orderFormItems.addAll(Arrays.asList(orderForm.ID, orderForm.SYSTEM, orderForm.NEW_PROJECT,
                orderForm.EXTENSION, orderForm.CUSTOMER, orderForm.DONGLE));
        return orderForm;
    }

    public static OrderForm createXwEngNewOrderForm(EngOrderFormWindowController controller) {
        OrderForm orderForm = new OrderForm();
        orderForm.ID.setPosition("C11");
        orderForm.SYSTEM = new OrderFormItem("Desigo XWorks").setPosition("C13");
        orderForm.NEW_PROJECT = new OrderFormItem("1").setPosition("E15").setHidden(true);
        orderForm.EXTENSION = new OrderFormItem("0").setPosition("E17").setHidden(true);
        orderForm.CUSTOMER = new OrderFormItem<>(controller.tfCustomer, "C19");
        orderForm.DONGLE = new OrderFormItem<>(controller.tfDongle, controller.lDongle, false, "C21");
        orderForm.orderFormItems.addAll(Arrays.asList(orderForm.ID, orderForm.SYSTEM, orderForm.NEW_PROJECT,
                orderForm.EXTENSION, orderForm.CUSTOMER, orderForm.DONGLE));
        return orderForm;
    }

    public static OrderForm createXwEngExtOrderForm(EngOrderFormWindowController controller) {
        OrderForm orderForm = new OrderForm();
        orderForm.ID.setPosition("C11");
        orderForm.SYSTEM = new OrderFormItem("Desigo XWorks").setPosition("C13");
        orderForm.NEW_PROJECT = new OrderFormItem("0").setPosition("E15").setHidden(true);
        orderForm.EXTENSION = new OrderFormItem("1").setPosition("E17").setHidden(true);
        orderForm.CUSTOMER = new OrderFormItem<>(controller.tfCustomer, "C19");
        orderForm.DONGLE = new OrderFormItem<>(controller.tfDongle, controller.lDongle, true, "C21");
        orderForm.orderFormItems.addAll(Arrays.asList(orderForm.ID, orderForm.SYSTEM, orderForm.NEW_PROJECT,
                orderForm.EXTENSION, orderForm.CUSTOMER, orderForm.DONGLE));
        return orderForm;
    }

    public boolean checkFieldsFilling() {
        for (OrderFormItem ofi : orderFormItems) {
            if (ofi.isWrongFilling()) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<OrderFormItem> getItems() {
        return orderFormItems;
    }

    public String getCustomer(){
        return CUSTOMER.getValue();
    }

    public String getFileNamePart() {
        return CUSTOMER.getValue().concat("_").concat(PROJECT_NAME.getValue()).concat("_").concat(ID.getValue());
    }

    public String getId() {
        return id;
    }

    public boolean getMigration(){
        return (MIGRATION != null && MIGRATION.getValue().equals("1"));
    }
}
