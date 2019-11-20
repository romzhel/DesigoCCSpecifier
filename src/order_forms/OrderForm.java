package order_forms;

import core.AppCore;
import order_forms.window_fill_order_form_dcc.DccOrderFormWindowController;
import order_forms.window_fill_order_form_eng.EngOrderFormWindowController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static excel.CellStyle.*;
import static order_forms.OrderFormItem.create;
import static order_forms.OrderFormType.*;

public class OrderForm {
    private ArrayList<OrderFormItem> orderFormItems = new ArrayList<>();
    private OrderFormType formType;
    private String id;

    private OrderFormItem DEBTOR_NUMBER;
    private OrderFormItem DEBTOR;

    private OrderFormItem SYSTEM;
    private OrderFormItem ID;
    private OrderFormItem NEW_PROJECT;
    private OrderFormItem EXTENSION;
    private OrderFormItem MIGRATION;

    private OrderFormItem COUNTRY_FULL;
    private OrderFormItem COUNTRY_SHORT;
    private OrderFormItem COMPANY;
    private OrderFormItem PROJECT_NAME;
    private OrderFormItem CUSTOMER;
    private OrderFormItem ADDRESS;
    private OrderFormItem POST_INDEX;
    private OrderFormItem BUILDING_TYPE;
    private OrderFormItem MIGRATED_SYSTEM_LABEL;
    private OrderFormItem MIGRATED_SYSTEM;
    private OrderFormItem MIGRATED_SYSTEM_NAME_LABEL;
    private OrderFormItem MIGRATED_SYSTEM_NAME;
    private OrderFormItem DONGLE;
    private OrderFormItem CSID;


    private OrderForm() {
        id = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        ID = create().setValue(id).setPosition("C11");
    }

    public static OrderForm createDccOrderForm(DccOrderFormWindowController controller) {
        boolean isNewProject = AppCore.getCalculator().isNewProject();
        boolean isMigration = !AppCore.getCalculator().getMigrationSuffix().isEmpty();
        OrderForm orderForm = new OrderForm();
        orderForm.formType =DCC;
        orderForm.DEBTOR_NUMBER = create().setControl(controller.tfDebtorN).setPosition("C13");
        orderForm.DEBTOR = create().setControl(controller.tfDebtor).setPosition("C15");
        orderForm.NEW_PROJECT = create().setValue(isMigration ? "0" : isNewProject ? "1" : "0").setPosition("E23").setHidden(true);
        orderForm.EXTENSION = create().setValue(isMigration ? "0" : isNewProject ? "0" : "1").setPosition("E25").setHidden(true);
        orderForm.MIGRATION = create().setValue(isMigration ? "1" : "0").setPosition("E27").setHidden(true);
        orderForm.COUNTRY_FULL = create().setControl(controller.tfCountryFull).setPosition("C17");
        orderForm.COUNTRY_SHORT = create().setControl(controller.tfCountryShort).setPosition("J17").setCellStyle(STYLE_CENTER_BORDER);
        orderForm.COMPANY = create().setControl(controller.tfCompany).setPosition("C19");
        orderForm.PROJECT_NAME = create().setControl(controller.tfProjectName).setPosition("C21");
        orderForm.CUSTOMER = create().setControl(controller.tfCustomer).setPosition("C29");
        orderForm.ADDRESS = create().setControl(controller.taAddress).setPosition("C31");
        orderForm.POST_INDEX = create().setControl(controller.tfPostIndex).setPosition("C33");
        orderForm.BUILDING_TYPE = create().setControl(controller.cbBuildingType).setPosition("C35");

        orderForm.MIGRATED_SYSTEM_LABEL = create().setValue("System type*").setPosition("F25")
                .setVisible(isMigration).setHidden(!isMigration).setCellStyle(STYLE_LEFT_BOLD);
        orderForm.MIGRATED_SYSTEM = create().setControl(controller.cbMigrSystemType)
                .setLabel(controller.lMigration).setVisible(isMigration).setPosition("H25:J25").setHidden(!isMigration);
        orderForm.MIGRATED_SYSTEM_NAME_LABEL = create().setValue("System Name*").setPosition("F27")
                .setVisible(isMigration).setHidden(!isMigration).setCellStyle(STYLE_LEFT_BOLD);
        orderForm.MIGRATED_SYSTEM_NAME = create().setControl(controller.tfMigrSystemName)
                .setLabel(controller.lMigrSystemName).setVisible(isMigration).setPosition("H27:J27").setHidden(!isMigration);

        orderForm.DONGLE = create().setControl(controller.tfDongle).setLabel(controller.lDongle)
                .setVisible(!isNewProject).setPosition("C37").setLinkedCheck(new OrderFormItem.CheckOtherConditions() {
                    @Override
                    public boolean checkIsWrong() {
                        return orderForm.CSID.isWrongFilled(false);
                    }
                });
        orderForm.CSID = create().setControl(controller.tfCsid).setLabel(controller.lCsid)
                .setVisible(!isNewProject && !isMigration).setPosition("L37").setLinkedCheck(new OrderFormItem.CheckOtherConditions() {
                    @Override
                    public boolean checkIsWrong() {
                        return orderForm.DONGLE.isWrongFilled(false);
                    }
                });

        orderForm.orderFormItems.addAll(Arrays.asList(orderForm.ID, orderForm.NEW_PROJECT, orderForm.EXTENSION,
                orderForm.COUNTRY_FULL, orderForm.COUNTRY_SHORT, orderForm.COMPANY, orderForm.PROJECT_NAME,
                orderForm.CUSTOMER, orderForm.ADDRESS, orderForm.POST_INDEX, orderForm.BUILDING_TYPE, orderForm.DONGLE,
                orderForm.CSID, orderForm.MIGRATION, orderForm.MIGRATED_SYSTEM, orderForm.MIGRATED_SYSTEM_NAME,
                orderForm.MIGRATED_SYSTEM_LABEL, orderForm.MIGRATED_SYSTEM_NAME_LABEL,
                orderForm.DEBTOR_NUMBER, orderForm.DEBTOR));
        return orderForm;
    }

    public static OrderForm createEngOrderForm(EngOrderFormWindowController controller, OrderFormType type) {
        OrderForm orderForm = new OrderForm();
        orderForm.formType = type;

        orderForm.DEBTOR_NUMBER = create().setControl(controller.tfDebtorN).setPosition("C13");
        orderForm.DEBTOR = create().setControl(controller.tfDebtor).setPosition("C15");
        orderForm.SYSTEM = create().setValue(orderForm.formType == DCC_ENG ? "Desigo CC" : "Desigo XWorks").setPosition("C17");
        orderForm.NEW_PROJECT = create().setValue(type == XWORKS_ENG_EXT ? "0" : "1").setPosition("E19").setHidden(true);
        orderForm.EXTENSION = create().setValue(type == XWORKS_ENG_EXT ? "1" : "0").setPosition("E21").setHidden(true);
        orderForm.CUSTOMER = create().setControl(controller.tfCustomer).setPosition("C23");
        orderForm.DONGLE = create().setControl(controller.tfDongle).setLabel(controller.lDongle).setVisible(type == XWORKS_ENG_EXT)
                .setPosition("C25");

        orderForm.orderFormItems.addAll(Arrays.asList(orderForm.ID, orderForm.SYSTEM, orderForm.NEW_PROJECT,
                orderForm.EXTENSION, orderForm.CUSTOMER, orderForm.DONGLE, orderForm.DEBTOR_NUMBER, orderForm.DEBTOR));
        return orderForm;
    }

    public boolean checkFieldsFilling() {
        boolean wrongFilled = false;
        for (OrderFormItem ofi : orderFormItems) {
            if (ofi.isWrongFilled(true)) {
                wrongFilled = true;
            }
        }
        return wrongFilled;
    }

    public ArrayList<OrderFormItem> getItems() {
        return orderFormItems;
    }

    public String getCustomer(){
        return CUSTOMER.getValue();
    }

    public String getFileNamePart() {
        if (formType == DCC) {
            return ID.getValue().concat("_").concat(CUSTOMER.getValue()).concat("_").concat(PROJECT_NAME.getValue());
        } else {
            return ID.getValue().concat("_").concat(CUSTOMER.getValue()).concat("_").concat(formType.getName());
        }
    }

    public String getId() {
        return id;
    }

    public boolean getMigration(){
        return (MIGRATION != null && MIGRATION.getValue().equals("1"));
    }
}
