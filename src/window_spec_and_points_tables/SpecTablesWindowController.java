package window_spec_and_points_tables;

import core.AppCore;
import dialogs.Dialogs;
import excel.ExcelRow;
import excel.SelectedColumns;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class SpecTablesWindowController implements Initializable {
    @FXML
    ComboBox<String> cbExcelSheetName;

    @FXML
    TableView<ExcelRow> tvExcelTable;
    @FXML
    TableColumn<ExcelRow, String> tcExcelCol0;
    @FXML
    ContextMenu contextMenu;

    @FXML
    Button btnApply;
    @FXML
    Button btnCancel;

    @FXML
    Label lFileName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        AppCore.getSpecTables().setTableView(tvExcelTable);

        cbExcelSheetName.getItems().addAll(AppCore.getSpecTables().getSheetNames());
        cbExcelSheetName.getSelectionModel().selectFirst();

        cbExcelSheetName.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (!newValue.equals(oldValue)) {
                    setColumnSelectMode();
                    AppCore.getSpecTables().display((int) newValue);
                }
            }
        });

        tvExcelTable.setPlaceholder(new Label("Нет данных"));
        lFileName.setText(AppCore.getSpecTables().getFileName());
        tvExcelTable.getSelectionModel().setCellSelectionEnabled(true);

    }


    public void windowCancelButton() {
        AppCore.getSpecTables().closeWindow();
    }

    public void windowApplyButton() {
        if (btnApply.getText().equals("Применить")) {

            int selectedIDColumn = AppCore.getSpecTables().getSelectedColumns().getIdCol();
            int selectedValueColumn = AppCore.getSpecTables().getSelectedColumns().getValCol();

            if (selectedIDColumn != SelectedColumns.NOT_DEFINED && selectedValueColumn != SelectedColumns.NOT_DEFINED) {
                setPointsAddMode();
            } else {
                new Dialogs().showMessage("Выбор столбцов", "Не выбраны столбцы с данными");
            }

        } else {
            System.out.println("try to add the points");
            btnApply.setDisable(true);
            btnCancel.setText("Закрыть");
            AppCore.addPointsToSpec();
        }

    }

    public void contextMenuSelectIDcolumn() {
        TablePosition tp = tvExcelTable.getSelectionModel().getSelectedCells().get(0); //selecting by cell
        AppCore.getSpecTables().getSelectedColumns().setIdCol(tp.getColumn());
        tvExcelTable.refresh();
    }

    public void contextMenuSelectValueColumn() {
        TablePosition tp = tvExcelTable.getSelectionModel().getSelectedCells().get(0); //selecting by cell
        AppCore.getSpecTables().getSelectedColumns().setValCol(tp.getColumn());
        tvExcelTable.refresh();
    }

    public void setColumnSelectMode() {
        tvExcelTable.setTooltip(new Tooltip("Кликните правой кнопкой мыши для выбора столбцов"));
        contextMenu.getItems().get(0).setDisable(false);
        contextMenu.getItems().get(1).setDisable(false);

        btnApply.setText("Применить");
        btnApply.setDisable(false);
    }

    public void setPointsAddMode() {
        tvExcelTable.setTooltip(null);

        AppCore.getSpecTables().displayFoundPoints();

        contextMenu.getItems().get(0).setDisable(true);
        contextMenu.getItems().get(1).setDisable(true);

        btnApply.setText("Добавить");
    }

}
