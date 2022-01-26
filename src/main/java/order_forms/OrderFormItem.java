package order_forms;

import excel.CellStyle;
import javafx.scene.control.*;

public class OrderFormItem<T extends Control> {
    private T control;
    private Label label;
    private boolean visible = true;
    private Position position;
    private String value;
    private boolean hidden;
    private CellStyle cellStyle = CellStyle.STYLE_LEFT_BORDER_PROTECTED;
    private CheckType checkType = CheckType.LETTERS_EN;
    private CheckConditions linkedItem;

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

    public OrderFormItem setValue(String value) {
        this.value = value;
        return this;
    }

    public boolean isVisible() {
        return visible;
    }

    public OrderFormItem setVisible(boolean visible) {
        this.visible = visible;
        if (control != null) control.setVisible(visible);
        if (label != null) label.setVisible(visible);
        return this;
    }

    public boolean isWrongFilled() {
        if (control != null && control.isVisible()) {
            boolean thisItemIsWrong = getValue() == null || getValue().isEmpty() || checkType.check(getValue());

            OrderFormItem otherLinkedItem;
            if (linkedItem != null && (otherLinkedItem = linkedItem.getLinkedItem()) != null && otherLinkedItem.isVisible()) {
                boolean noValue = otherLinkedItem.getValue() == null || otherLinkedItem.getValue().isEmpty();
                boolean wrongValue = otherLinkedItem.checkType.check(otherLinkedItem.getValue());

                boolean otherItemIsWrong = noValue || wrongValue;

                control.setStyle(thisItemIsWrong && otherItemIsWrong ? "-fx-border-color: orange;" : "-fx-border-color: green;");
                return thisItemIsWrong && otherItemIsWrong;
            } else {
                control.setStyle(thisItemIsWrong ? "-fx-border-color: red;" : "-fx-border-color: green;");
                return thisItemIsWrong;
            }
        }
        return false;
    }

    public T getControl() {
        return control;
    }

    public OrderFormItem setControl(T control) {
        this.control = control;
        return this;
    }

    public OrderFormItem setLabel(Label label) {
        this.label = label;
        return this;
    }

    public OrderFormItem setLinkedItem(CheckConditions check) {
        linkedItem = check;
        return this;
    }

    public boolean isHidden() {
        return hidden;
    }

    public OrderFormItem setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    public Position getPosition() {
        return position;
    }

    public OrderFormItem setPosition(String position) {
        this.position = new Position(position);
        return this;
    }

    public CellStyle getCellStyle() {
        return cellStyle;
    }

    public OrderFormItem setCellStyle(CellStyle cellStyle) {
        this.cellStyle = cellStyle;
        return this;
    }

    public OrderFormItem setCheckType(CheckType checkType) {
        this.checkType = checkType;
        return this;
    }

    public interface CheckConditions {
        OrderFormItem getLinkedItem();
    }

    public void displayValue(String value) {
        if (control != null) {
            if (control instanceof TextField) {
                ((TextField) control).setText(value);
            } else if (control instanceof TextArea) {
                ((TextArea) control).setText(value);
            } else if (control instanceof ComboBox) {
                ((ComboBox<String>) control).setValue(value);
            }
        }
    }
}