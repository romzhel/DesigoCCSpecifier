package tables_data.options;

import core.AppCore;
import core.Tables;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.ScrollEvent;
import javafx.util.Callback;
import tables_data.feature_sets.FeatureSet;
import window_main.Controller;

public class OptionsTable implements Tables {
    private TableView<Option> tableView;

    public OptionsTable(TableView<Option> tvOptions) {
        tableView = tvOptions;
        AppCore.addRefreshedTables(this);

        initDescriptionColumn();
        initOrderColumn();

        for (FeatureSet fs : AppCore.getFeatureSets().getItems()) {
            addFSColumns(fs);
        }

        tableView.getItems().addAll(AppCore.getOptions().getItems());
        tableView.setEditable(true);
    }

    private void initDescriptionColumn() {
        TableColumn<Option, String> descriptionCol = new TableColumn<>(Controller.DESCRIPTION_COLUMN_NAME);
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionCol.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionCol.setPrefWidth(Controller.DESCRIPTION_COLUMN_WIDTH);
        descriptionCol.setEditable(false);
        tableView.getColumns().add(descriptionCol);
    }

    private void initOrderColumn() {
        TableColumn<Option, Boolean> orderCol = new TableColumn<>(Controller.FOR_ORDER_COLUMN_NAME);
        orderCol.setCellValueFactory(param -> {
            Option op = param.getValue();
            SimpleBooleanProperty booleanProperty = new SimpleBooleanProperty(op.isOrdered());

            booleanProperty.addListener((observable, oldValue, newValue) -> { //---- изменили состояние флажка -------
                op.setOrdered(newValue);

//                Calculator.setFeatureSetsisOrdered(optNum, newValue);
//                Calculator.calc();


//                tableView.refresh();
                AppCore.refreshTables();
            });

            return booleanProperty;
        });

        orderCol.setCellFactory(CheckBoxTableCell.forTableColumn(orderCol));
        orderCol.setPrefWidth(Controller.FOR_ORDER_COLUMN_WIDTH);
        tableView.getColumns().add(orderCol);

        tableView.addEventFilter(ScrollEvent.ANY, scrollEvent -> {
            tableView.refresh();
        });
    }

    public void addFSColumns(FeatureSet featureSet) {
        TableColumn<Option, Boolean> tableColumn = new TableColumn<Option, Boolean>(featureSet.getArticle());
        tableColumn.setPrefWidth(Controller.FEATURE_SET_COLUMN_WIDTH);

//        tableColumn.getStyleClass().add("changed");

        tableColumn.setCellValueFactory(param -> {
            Option option = param.getValue();
            int index = AppCore.getOptions().getItems().indexOf(option);

            boolean checked = featureSet.getOrderAccessibility(index) == Option.INCLUDED ||
                    option.isOrdered() && featureSet.getOrderAccessibility(index) == Option.ORDERABLE;

            SimpleBooleanProperty booleanProperty = new SimpleBooleanProperty(checked);
            return booleanProperty;
        });

        Callback<TableColumn<Option, Boolean>, TableCell<Option, Boolean>> defaultCheckboxFieldCellFactory
                = CheckBoxTableCell.forTableColumn(tableColumn);

        tableColumn.setCellFactory(col -> {
            TableCell<Option, Boolean> cell = defaultCheckboxFieldCellFactory.call(col);

            cell.itemProperty().addListener((obs, oldValue, newValue) -> {
                TableRow row = cell.getTableRow();

                if (row == null) {
//                        cell.setEditable(false);
                } else if (row.getIndex() >= 0) {
                    int index = cell.getTableRow().getIndex();

                    boolean isOrdered = AppCore.getOptions().getItems().get(index).isOrdered();

                    int orderableAccessibility = featureSet.getOrderAccessibility(index);
//
//                        cell.setStyle("-fx-background-color: transparent;");

                    cell.getStyleClass().add("grey_by-default");

                    if (orderableAccessibility == Option.ORDERABLE) { //
//                            cell.setStyle("-fx-background-color: #efedfd;");
//                            cell.getStyleClass().clear();
                        cell.getStyleClass().add("green");
                    }

                    if (isOrdered && (orderableAccessibility == Option.ORDERABLE || orderableAccessibility == Option.INCLUDED)) {
//                            cell.setStyle("-fx-background-color: #e4fad6;");
//                            cell.getStyleClass().clear();
                        cell.getStyleClass().add("green");
                    } else if (isOrdered && orderableAccessibility == Option.NOT_ACCESSIBLE) {
//                            cell.setStyle("-fx-background-color: #fadbd6;");
//                            cell.getStyleClass().clear();
                        cell.getStyleClass().add("red");
//                            EvaluationSpec.tableResresh();
                        featureSet.setOverLimited(true);

                    }
                }
            });

            return cell;
        });
        //-------------------------------------------------------------------------------------------------------------
        tableColumn.setSortable(false);
        tableView.getColumns().add(tableColumn);
        tableColumn.setEditable(false);
    }


    @Override
    public void refresh() {
        tableView.refresh();
    }
}
