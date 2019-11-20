package order_forms;

import excel.CellStyle;
import javafx.scene.control.*;

public class OrderFormItem<T extends Control> {
    private T control;
    private Label label;
    private boolean visible = true;
    private String position;
    private String value;
    private boolean hidden;
    private CellStyle cellStyle = CellStyle.STYLE_LEFT_BORDER;
    private CheckOtherConditions checkOtherConditions;

    private OrderFormItem() {
    }

    public static OrderFormItem create() {
        return new OrderFormItem<>();
    }

    public String getValue() {
        if (value != null) return value;

        if (control instanceof TextField) {
            return ((TextField) control).getText();
        } else if (control instanceof TextArea) {
            return ((TextArea) control).getText().replaceAll("\n", " ");
        } else if (control instanceof ComboBox) {
            return ((ComboBox<String>) control).getValue();
        } else if (control instanceof CheckBox) {
            return ((CheckBox) control).isSelected() ? "ИСТИНА" : "ЛОЖЬ";
        }
        return "";
    }

    public OrderFormItem setVisible(boolean visible) {
        this.visible = visible;
        if (control != null) control.setVisible(visible);
        if (label != null) label.setVisible(visible);
        return this;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isWrongFilled(boolean checkOther) {

        if (control != null) {
            boolean thisItemIsWrong = (visible && (getValue() == null || getValue().isEmpty() || getValue().matches(".*[а-яА-Я]+.*")));

            if (checkOtherConditions != null && checkOther) {
                boolean otherItemsIsWrong = checkOtherConditions.checkIsWrong();

                control.setStyle(thisItemIsWrong && otherItemsIsWrong ? "-fx-border-color: orange;" : "-fx-border-color: green;");
                return thisItemIsWrong && otherItemsIsWrong;
            } else {
                if (checkOther) {
                    control.setStyle(thisItemIsWrong ? "-fx-border-color: red;" : "-fx-border-color: green;");
                }
                return thisItemIsWrong;
            }
        }
        return false;
    }


    public T getControl() {
        return control;
    }

    public OrderFormItem setCellStyle(CellStyle cellStyle) {
        this.cellStyle = cellStyle;
        return this;
    }

    public OrderFormItem setValue(String value) {
        this.value = value;
        return this;
    }

    public OrderFormItem setControl(T control) {
        this.control = control;
        return this;
    }

    public OrderFormItem setLabel(Label label) {
        this.label = label;
        return this;
    }

    public OrderFormItem setPosition(String position) {
        this.position = position;
        return this;
    }

    public OrderFormItem setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    public OrderFormItem setLinkedCheck(CheckOtherConditions check) {
        checkOtherConditions = check;
        return this;
    }

    public boolean isHidden() {
        return hidden;
    }

    public String getPosition() {
        return position;
    }

    public CellStyle getCellStyle() {
        return cellStyle;
    }

    public interface CheckOtherConditions {
        boolean checkIsWrong();
    }
}