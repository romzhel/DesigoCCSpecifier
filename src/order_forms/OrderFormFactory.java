package order_forms;

import core.AppCore;
import core.Calculator;
import dialogs.Dialogs;
import order_forms.window_fill_order_form_dcc.DccOrderFormWindowController;
import order_forms.window_fill_order_form_eng.EngOrderFormWindowController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static excel.CellStyle.*;
import static order_forms.OrderFormItem.create;
import static order_forms.OrderFormType.*;

public class OrderFormFactory {
    private ArrayList<OrderFormItem> orderFormItems = new ArrayList<>();
    private OrderFormType formType;
    private String id;

    private OrderFormItem DEBTOR_NUMBER;
    private OrderFormItem DEBTOR_EN;
    private OrderFormItem DEBTOR_RU;

    private OrderFormItem SYSTEM;
    private OrderFormItem ID;
    private OrderFormItem NEW_PROJECT;
    private OrderFormItem EXTENSION;
    private OrderFormItem MIGRATION;

    private OrderFormItem COUNTRY_FULL;
    private OrderFormItem COUNTRY_SHORT;
    private OrderFormItem CUSTOMER;
    private OrderFormItem PROJECT_NAME;
    private OrderFormItem END_CUSTOMER_RU;
    private OrderFormItem ADDRESS_EN;
    private OrderFormItem ADDRESS_RU;
    private OrderFormItem POST_INDEX;
    private OrderFormItem BUILDING_TYPE;
    private OrderFormItem MIGRATED_SYSTEM_LABEL;
    private OrderFormItem MIGRATED_SYSTEM;
    private OrderFormItem MIGRATED_SYSTEM_NAME_LABEL;
    private OrderFormItem MIGRATED_SYSTEM_NAME;
    private OrderFormItem DONGLE;
    private OrderFormItem CSID;


    private OrderFormFactory() {
        id = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        ID = create().setValue(id).setPosition("C11").setCellStyle(STYLE_LEFT_BORDER_PROTECTED);
    }

    public static OrderFormFactory createDccOrderForm(DccOrderFormWindowController controller) {

        boolean isNewProject = AppCore.getCalculator().getCalcType() == Calculator.CalcType.NEW;
        boolean isMigration = !AppCore.getCalculator().getCalcType().getMigrationSuffix().isEmpty();
        OrderFormFactory orderForm = new OrderFormFactory();
        orderForm.formType = DCC;
        orderForm.DEBTOR_NUMBER = create().setControl(controller.tfDebtorN).setCheckType(CheckType.DIGITS).setPosition("C13");
        orderForm.DEBTOR_EN = create().setControl(controller.tfDebtorEn).setPosition("C15");
        orderForm.DEBTOR_RU = create().setControl(controller.tfDebtorRu).setCheckType(CheckType.LETTERS_RU).setPosition("C17");
        orderForm.NEW_PROJECT = create().setValue(isMigration ? "0" : isNewProject ? "1" : "0").setPosition("E25").setHidden(true);
        orderForm.EXTENSION = create().setValue(isMigration ? "0" : isNewProject ? "0" : "1").setPosition("E27").setHidden(true);
        orderForm.MIGRATION = create().setValue(isMigration ? "1" : "0").setPosition("E29").setHidden(true);
        orderForm.COUNTRY_FULL = create().setControl(controller.tfCountryFull).setPosition("C19");
        orderForm.COUNTRY_SHORT = create().setControl(controller.tfCountryShort).setPosition("J19").setCellStyle(STYLE_CENTER_BORDER);
        orderForm.CUSTOMER = create().setControl(controller.tfCompany).setPosition("C21");
        orderForm.PROJECT_NAME = create().setControl(controller.tfProjectName).setPosition("C23");
        orderForm.END_CUSTOMER_RU = create().setControl(controller.tfCustomer).setCheckType(CheckType.LETTERS_RU).setPosition("C31");
        orderForm.ADDRESS_EN = create().setControl(controller.taAddressEn).setPosition("C33");
        orderForm.ADDRESS_RU = create().setControl(controller.taAddressRu).setCheckType(CheckType.LETTERS_RU).setPosition("C35");
        orderForm.POST_INDEX = create().setControl(controller.tfPostIndex).setCheckType(CheckType.DIGITS).setPosition("C37");
        orderForm.BUILDING_TYPE = create().setControl(controller.cbBuildingType).setPosition("C39");

        orderForm.MIGRATED_SYSTEM_LABEL = create().setValue("System type*").setPosition("F27")
                .setVisible(isMigration).setHidden(!isMigration).setCellStyle(STYLE_LEFT_BOLD);
        orderForm.MIGRATED_SYSTEM = create().setControl(controller.cbMigrSystemType)
                .setLabel(controller.lMigration).setVisible(isMigration).setPosition("H27:J27").setHidden(!isMigration);
        orderForm.MIGRATED_SYSTEM_NAME_LABEL = create().setValue("System Name*").setPosition("F29")
                .setVisible(isMigration).setHidden(!isMigration).setCellStyle(STYLE_LEFT_BOLD);
        orderForm.MIGRATED_SYSTEM_NAME = create().setControl(controller.tfMigrSystemName)
                .setLabel(controller.lMigrSystemName).setVisible(isMigration).setPosition("H29:J29").setHidden(!isMigration);

        orderForm.DONGLE = create().setControl(controller.tfDongle).setLabel(controller.lDongle)
                .setVisible(!isNewProject).setCheckType(CheckType.DIGITS).setPosition("C41").setLinkedItem(() -> orderForm.CSID);
        orderForm.CSID = create().setControl(controller.tfCsid).setLabel(controller.lCsid)
                .setVisible(!isNewProject && !isMigration).setCheckType(CheckType.DIGITS).setPosition("L41")
                .setLinkedItem(() -> orderForm.DONGLE);

        orderForm.orderFormItems.addAll(Arrays.asList(orderForm.ID, orderForm.NEW_PROJECT, orderForm.EXTENSION,
                orderForm.COUNTRY_FULL, orderForm.COUNTRY_SHORT, orderForm.CUSTOMER, orderForm.PROJECT_NAME,
                orderForm.END_CUSTOMER_RU, orderForm.ADDRESS_EN, orderForm.ADDRESS_RU, orderForm.POST_INDEX, orderForm.BUILDING_TYPE, orderForm.DONGLE,
                orderForm.CSID, orderForm.MIGRATION, orderForm.MIGRATED_SYSTEM, orderForm.MIGRATED_SYSTEM_NAME,
                orderForm.MIGRATED_SYSTEM_LABEL, orderForm.MIGRATED_SYSTEM_NAME_LABEL,
                orderForm.DEBTOR_NUMBER, orderForm.DEBTOR_EN, orderForm.DEBTOR_RU));
        return orderForm;
    }

    public static OrderFormFactory createEngOrderForm(EngOrderFormWindowController controller, OrderFormType type) {
        OrderFormFactory orderForm = new OrderFormFactory();
        orderForm.formType = type;

        orderForm.DEBTOR_NUMBER = create().setControl(controller.tfDebtorN).setCheckType(CheckType.DIGITS).setPosition("C13");
        orderForm.DEBTOR_EN = create().setControl(controller.tfDebtorEn).setPosition("C15");
        orderForm.DEBTOR_RU = create().setControl(controller.tfDebtorRu).setCheckType(CheckType.LETTERS_RU).setPosition("C17");
        orderForm.SYSTEM = create().setValue(orderForm.formType == DCC_ENG ? "Desigo CC" : "Desigo XWorks").setPosition("C19");
        orderForm.NEW_PROJECT = create().setValue(type == XWORKS_ENG_EXT ? "0" : "1").setPosition("E21").setHidden(true);
        orderForm.EXTENSION = create().setValue(type == XWORKS_ENG_EXT ? "1" : "0").setPosition("E23").setHidden(true);
        orderForm.END_CUSTOMER_RU = create().setControl(controller.tfCustomer).setCheckType(CheckType.LETTERS_RU).setPosition("C25");
        orderForm.DONGLE = create().setControl(controller.tfDongle).setLabel(controller.lDongle).setVisible(type == XWORKS_ENG_EXT)
                .setCheckType(CheckType.MULTI_DIGITS).setPosition("C27");

        orderForm.orderFormItems.addAll(Arrays.asList(orderForm.ID, orderForm.SYSTEM, orderForm.NEW_PROJECT,
                orderForm.EXTENSION, orderForm.END_CUSTOMER_RU, orderForm.DONGLE, orderForm.DEBTOR_NUMBER, orderForm.DEBTOR_EN, orderForm.DEBTOR_RU));
        return orderForm;
    }

    public boolean checkFieldsFilling() {
        boolean wrongFilled = false;
        for (OrderFormItem ofi : orderFormItems) {
            if (ofi.isWrongFilled()) {
                wrongFilled = true;
            }
        }
        if (wrongFilled) {
            new Dialogs().showMessage("Ввод данных", "Необходимо заполнить все поля согласно подсказок:\n" +
                    "- поля с латиницей не должны содержать кириллицы и должны иметь хотя бы один символ на латинице\n" +
                    "- поля с цифрами должны содержать только цифры\n\n");
        }

        return wrongFilled;
    }

    public ArrayList<OrderFormItem> getItems() {
        return orderFormItems;
    }

    public String getFileNamePart() {
        if (formType == DCC) {
            return ID.getValue().concat("_").concat(DEBTOR_EN.getValue()).concat("_").concat(PROJECT_NAME.getValue());
        } else {
            return ID.getValue().concat("_").concat(DEBTOR_EN.getValue()).concat("_").concat(formType.getName());
        }
    }

    public List<OrderFormItem> getUnicalItems() {
        return Arrays.asList(ID, NEW_PROJECT, EXTENSION, MIGRATION, MIGRATED_SYSTEM, MIGRATED_SYSTEM_NAME, DONGLE,
                BUILDING_TYPE);
    }
}
