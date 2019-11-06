package window_fill_order_form_dcc;

import javafx.scene.control.*;

public class OrderFormItem<T extends Control> {
    private T control;
    private Label label;
    private boolean visible = true;
    private String position;
    private String value;
    private boolean hidden;

    public OrderFormItem(String value) {
        this.value = value;
    }

    public OrderFormItem(String value, String position) {
        this.value = value;
        this.position = position;
    }

    public OrderFormItem(T control, String position) {
        this.control = control;
        this.position = position;
    }

    public OrderFormItem(T control, Label label, String position) {
        this.control = control;
        this.label = label;
        this.position = position;
    }

    public OrderFormItem(T control, Label label, boolean visible, String position) {
        this.control = control;
        this.label = label;
        this.visible = visible;
        control.setVisible(visible);
        label.setVisible(visible);
        this.position = position;
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

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getRow() {
        if (position == null || position.isEmpty()) return -1;
        String rowS = position.replaceAll("\\D", "");
        try {
            return Integer.parseInt(rowS) - 1;
        } catch (Exception e) {
            System.out.println("not correct Integer " + rowS);
        }
        return 0;
    }

    public int getColumn() {
        if (position == null || position.isEmpty()) return -1;
        String intS = position.replaceAll("\\d", "");
        return Math.max(0, (int)intS.charAt(0) - 65);
    }

    public boolean isWrongFilling() {
        return /*(visible && (getValue() == null || getValue().isEmpty() || getValue().matches("[а-яА-Я]+")))*/ false;
    }

    public T getControl() {
        return control;
    }

    public void setValue(String value) {
        this.value = value;
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

    public boolean isHidden() {
        return hidden;
    }
}
