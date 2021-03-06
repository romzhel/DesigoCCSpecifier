package tables_data.size;

import core.App;
import core.Calculator;
import core.Tables;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tables_data.feature_sets.FeatureSet;
import tables_data.feature_sets.FeatureSets;

import java.util.ArrayList;
import java.util.List;

import static javafx.scene.control.cell.TextFieldTableCell.forTableColumn;
import static tables_data.size.SizeItem.NOT_ACCESSIBLE;
import static tables_data.size.SizeItem.NOT_LIMITED;
import static window_main.Controller.*;

public class SizeTable implements Tables {
    private static final Logger logger = LogManager.getLogger(SizeTable.class);
    private static final String COL_STYLE = " -fx-border-width: 0 1 0 0; -fx-border-color: #c0c0c0;";
    private TableView<SizeItem> tableView;

    public SizeTable(TableView<SizeItem> tableView) {
        this.tableView = tableView;
        App.addRefreshedTables(this);

        initDescriptionColumn(tableView);
        initOrderColumn(tableView);

        for (FeatureSet fs : FeatureSets.getInstance().getItems()) {
            addFSColumns(fs);
        }

        tableView.getItems().addAll(Size.getInstance().getItems());
        tableView.setEditable(true);

        tableView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getClickCount() == 2) {
                tableView.edit(tableView.getSelectionModel().getSelectedIndex(), tableView.getColumns().get(1));
            }
        });
    }

    private void initOrderColumn(TableView<SizeItem> tableView) {
        TableColumn<SizeItem, Integer> inputCol = new TableColumn<>(FOR_ORDER_COLUMN_NAME);
        inputCol.setCellValueFactory(new PropertyValueFactory<>("forOrder"));

        StringConverter<Integer> converter = new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                return String.format("%,d", object);
            }

            @Override
            public Integer fromString(String string) {
                int result = 0;
                try {
                    result = Integer.parseInt(string.replaceAll("[\\h\\s]", ""));
                } catch (NumberFormatException e) {
                    logger.warn("invalid points number '{}'", string);
                }
                return result;
            }
        };

        inputCol.setCellFactory(new Callback<TableColumn<SizeItem, Integer>, TableCell<SizeItem, Integer>>() {
            @Override
            public TableCell<SizeItem, Integer> call(TableColumn<SizeItem, Integer> column) {
                return new EditCell<SizeItem, Integer>(converter) {
                    @Override
                    public void commitEdit(Integer item) {
                        super.commitEdit(item);
                        App.refreshTables();
                    }
                };
            }
        });

        inputCol.setStyle(COL_STYLE.concat(" -fx-alignment: center;"));
        inputCol.setEditable(true);
        inputCol.setPrefWidth(FOR_ORDER_COLUMN_WIDTH);
        tableView.getColumns().add(inputCol);
    }

    private void initDescriptionColumn(TableView<SizeItem> tableView) {
        TableColumn<SizeItem, String> descriptionCol = new TableColumn<>(DESCRIPTION_COLUMN_NAME);
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionCol.setCellFactory(forTableColumn());
        descriptionCol.setPrefWidth(DESCRIPTION_COLUMN_WIDTH);
        descriptionCol.setEditable(false);
        descriptionCol.setStyle(COL_STYLE);
        tableView.getColumns().add(descriptionCol);

        descriptionCol.addEventHandler(TableColumn.editAnyEvent(), event -> {

            System.out.println(event);
            event.consume();
        });

    }

    public void addFSColumns(FeatureSet featureSet) {
        List<TableColumn> FSColumns = new ArrayList<>();

        FSColumns.add(new TableColumn<FeatureSet, Integer>(featureSet.getArticle()));
        FSColumns.add(new TableColumn<SizeItem, Integer>("Включ."));

        FSColumns.get(1).setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                SizeItem currSizeItem = (SizeItem) param.getValue();
                int index = Size.getInstance().getItems().indexOf(currSizeItem);

//                if (index < 0) return null;

                int value = featureSet.getPointsIncluded(index);
                if (value == NOT_ACCESSIBLE) {                                          // не доступно "-"
                    return new SimpleStringProperty("\u2014");
                } else if (value == NOT_LIMITED) {                                    // не ограничено
                    return new SimpleStringProperty("0");
                } else {                                                    // реальное значение
                    String sumSymbol = "";
                    if (featureSet.getTotalLimit() != null) {
                        if (featureSet.getTotalLimit().getPointsWithLimitation().indexOf(currSizeItem.getPointType()) >= 0)
                            sumSymbol = "\u2211";
                    }
                    return new SimpleStringProperty(sumSymbol + String.format("%,d", value));
                }
            }
        });

        FSColumns.add(new TableColumn<SizeItem, Integer>("Макс."));
        FSColumns.get(2).setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                SizeItem currSizeItem = (SizeItem) param.getValue();
                int index = Size.getInstance().getItems().indexOf(currSizeItem);

                int value = featureSet.getPointMaximum(index);
                if (value == NOT_ACCESSIBLE) {                                          // не доступно
                    return new SimpleStringProperty("\u2014");
                } else if (value == NOT_LIMITED) {                                    // не ограничено
                    return new SimpleStringProperty("\u221E");
                } else {                                                    // реальное значение
                    String sumSymbol = "";
                    if (featureSet.getTotalLimit() != null) {
                        if (featureSet.getTotalLimit().getPointsWithLimitation().indexOf(currSizeItem.getPointType()) >= 0)
                            sumSymbol = "\u2211";
                    }
                    return new SimpleStringProperty(sumSymbol + String.format("%,d", value));
                }
            }
        });

        //--- cells colorizing (including)
        FSColumns.get(1).setCellFactory(column -> {
            return new TableCell<SizeItem, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty); //This is mandatory

                    if (item == null || empty) { //If the cell is empty
                        setText(null);
                        setStyle("");
                    } else { //If the cell is not empty

                        setText(item); //Put the String data in the cell

                        //We get here all the info of the Person of this row
                        SizeItem sizeItem = getTableView().getItems().get(getIndex());
//                        int index = AppCore.getSize().getItems().indexOf(sizeItem);
                        int index = getIndex();

                        if (sizeItem.getForOrder() > 0) {
                            /*if (featureSet.isTotalLimited(sizeItem.getPointType()) *//*&& AppCore.getSize().isTotallyOverLimited(featureSet.getTotalLimit())*//*) {
                                setTextFill(Color.RED);
                                setStyle("-fx-font-weight: normal; -fx-alignment: CENTER;");
                            } else*/
                            if (sizeItem.getForOrder() <= featureSet.getPointsIncluded(index)) {
                                setTextFill(Color.GREEN);
                                setStyle("-fx-font-weight: bold;");
                            } else if (featureSet.getPointsIncluded(index) == NOT_ACCESSIBLE) {
                                setTextFill(Color.RED);
                                setStyle("-fx-font-weight: bold;");
                            } else if (featureSet.getPointMaximum(index) > 0 && featureSet.getPointMaximum(index) < sizeItem.getForOrder()) {
                                setTextFill(Color.RED);
                                setStyle("-fx-font-weight: normal;");
                            } else {
                                setTextFill(Color.GREEN);
                                setStyle("-fx-font-weight: normal;");
                            }
//
                        } else {
                            setTextFill(Color.BLACK);
                        }
                        setStyle(getStyle().concat(" -fx-alignment: CENTER; "));
                    }
                }
            };
        });

        //--- cells colorizing (max)
        FSColumns.get(2).setCellFactory(column -> {
            return new TableCell<SizeItem, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty); //This is mandatory

                    if (item == null || empty) { //If the cell is empty
                        setText(null);
                        setStyle(COL_STYLE);
                    } else { //If the cell is not empty

                        setText(item); //Put the String data in the cell

                        //We get here all the info of the Person of this row
                        SizeItem sizeItem = getTableView().getItems().get(getIndex());
                        int index = Size.getInstance().getItems().indexOf(sizeItem);

                        if (sizeItem.getForOrder() > 0) {
                            boolean isSizeExceeded = sizeItem.getForOrder() > featureSet.getPointMaximum(index) &&
                                    featureSet.getPointMaximum(index) != 0;
                            boolean isTotalOverLimited = /*featureSet.isTotalLimited(sizeItem.getPointType()) &&
                                    AppCore.getSize().isTotallyOverLimited(featureSet.getTotalLimit())*/ false;
                            boolean isExtensionOverLimited = Calculator.getInstance().isCalcTypeEquals(Calculator.CalcType.EXTENSION) &&
                                    featureSet.getPointMaximum(index) != 0 &&
                                    (sizeItem.getForOrder() > (featureSet.getPointMaximum(index) - featureSet.getPointsIncluded(index)));

                            if (isSizeExceeded || isTotalOverLimited || isExtensionOverLimited) {
                                setTextFill(Color.RED); //The text in red
                                setStyle("-fx-font-weight: bold;");
                                featureSet.setOverLimited(featureSet.isOverLimited() | true);
                            } else if (sizeItem.getForOrder() > featureSet.getPointsIncluded(index)) {
                                setTextFill(Color.GREEN);
                                setStyle("-fx-font-weight: bold;");
                            } else {
                                setTextFill(Color.GREEN);
                                setStyle("-fx-font-weight: normal;");
                            }
//
                        } else {
                            setTextFill(Color.BLACK);
                        }
                        setStyle(getStyle().concat(COL_STYLE).concat(" -fx-alignment: center;"));
                    }
                }
            };
        });

        FSColumns.get(0).getColumns().addAll(FSColumns.get(1), FSColumns.get(2));
        FSColumns.get(0).setSortable(false);
        FSColumns.get(1).setSortable(false);
        FSColumns.get(2).setSortable(false);
        FSColumns.get(2).setEditable(false);
        FSColumns.get(1).setEditable(false);
        FSColumns.get(0).setEditable(false);
        FSColumns.get(0).setPrefWidth(FEATURE_SET_COLUMN_WIDTH);
        FSColumns.get(1).setPrefWidth(FEATURE_SET_COLUMN_WIDTH / 2);
        FSColumns.get(1).setStyle("-fx-alignment: CENTER; ");

        FSColumns.get(2).setPrefWidth(FEATURE_SET_COLUMN_WIDTH / 2);

        tableView.getColumns().add(FSColumns.get(0));
    }

    @Override
    public void refresh() {
        tableView.refresh();
    }
}
