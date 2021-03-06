package tables_data.options;

import core.App;
import core.Tables;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tables_data.feature_sets.FeatureSet;
import tables_data.feature_sets.FeatureSets;
import window_main.Controller;

public class OptionsTable implements Tables {
    private static final Logger logger = LogManager.getLogger(OptionsTable.class);
    private TableView<Option> tableView;

    public OptionsTable(TableView<Option> tvOptions) {
        tableView = tvOptions;
        App.addRefreshedTables(this);

        initDescriptionColumn();
        initOrderColumn();

        for (FeatureSet fs : FeatureSets.getInstance().getItems()) {
            addFSColumns(fs);
        }

        tableView.getItems().addAll(Options.getInstance().getItems());
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
                logger.info("Option '{}' was changed '{}' => '{}'", op.getDescription(), oldValue, newValue);
                App.refreshTables();
            });

            return booleanProperty;
        });

        orderCol.setCellFactory(CheckBoxTableCell.forTableColumn(orderCol));
        orderCol.setPrefWidth(Controller.FOR_ORDER_COLUMN_WIDTH);
        tableView.getColumns().add(orderCol);

        tableView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getClickCount() == 2) {
                Option selectedOption = tableView.getSelectionModel().getSelectedItem();
                selectedOption.setOrdered(!selectedOption.isOrdered());
                App.refreshTables();
            }
        });

        tableView.addEventFilter(ScrollEvent.ANY, scrollEvent -> {
            tableView.refresh();
        });
    }

    public void addFSColumns(FeatureSet featureSet) {
        TableColumn<Option, Boolean> tableColumn = new TableColumn<Option, Boolean>(featureSet.getArticle());
        tableColumn.setPrefWidth(Controller.FEATURE_SET_COLUMN_WIDTH);

        tableColumn.setCellValueFactory(param -> {
            Option option = param.getValue();
            int index = Options.getInstance().getItems().indexOf(option);

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

                    boolean isOrdered = Options.getInstance().getItems().get(index).isOrdered();

                    int orderableAccessibility = featureSet.getOrderAccessibility(index);
                    cell.getStyleClass().add("grey_by-default");

                    if (orderableAccessibility == Option.ORDERABLE) { //
                        cell.getStyleClass().add("green");
                    }

                    if (isOrdered && (orderableAccessibility == Option.ORDERABLE || orderableAccessibility == Option.INCLUDED)) {
                        cell.getStyleClass().add("green");
                    } else if (isOrdered && orderableAccessibility == Option.NOT_ACCESSIBLE) {
                        cell.getStyleClass().add("red");
                        featureSet.setOverLimited(true);
                    }
                }
            });

            return cell;
        });
        //-------------------------------------------------------------------------------------------------------------
        tableColumn.setSortable(false);
        tableColumn.setEditable(false);
        tableView.getColumns().add(tableColumn);
    }


    @Override
    public void refresh() {
        tableView.refresh();
    }
}
