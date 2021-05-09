package excel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ExcelTable {
    private int columnsCount;
    private ObservableList<ExcelRow> rowsValues;

    public ExcelTable(int columnsCount) {
        this.columnsCount = columnsCount;
        rowsValues = FXCollections.observableArrayList();
    }

    public void addRow(String... cellsValues) {
        int rowIndex = rowsValues.size();
        rowsValues.add(new ExcelRow(columnsCount, rowIndex, cellsValues));
    }

    public void clear() {
        rowsValues.clear();
    }

    public ObservableList<ExcelRow> getRows() {
        return rowsValues;
    }

    public int getColumnsCount() {
        return columnsCount;
    }
}
