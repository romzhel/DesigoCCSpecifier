package excel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ExcelRow {
    private StringProperty firstColumn;
    private String[] cellValues;

    public ExcelRow(int columnsCount, int rowIndex, String... cellsValues) {
        firstColumn = new SimpleStringProperty(Integer.toString(rowIndex));
        this.cellValues = cellsValues;
    }

    public String getFirstColumn() {
        return firstColumn.get();
    }

    public StringProperty firstColumnProperty() {
        return firstColumn;
    }

    public void setFirstColumn(String firstColumn) {
        this.firstColumn.set(firstColumn);
    }

    public String[] getCellValues() {
        return cellValues;
    }

    public void setCellValues(String[] cellValues) {
        this.cellValues = cellValues;
    }

    public String getCellValue(int index) {
        return cellValues[index];
    }
}
