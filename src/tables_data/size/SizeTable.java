package tables_data.size;

import core.AppCore;
import core.Tables;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.StringConverter;
import tables_data.feature_sets.FeatureSet;

import java.util.ArrayList;

import static tables_data.size.SizeItem.NOT_ACCESSIBLE;
import static tables_data.size.SizeItem.NOT_LIMITED;
import static window_main.Controller.*;

public class SizeTable implements Tables {
    TableView<SizeItem> tableView;

    public SizeTable(TableView<SizeItem> tableView) {
        this.tableView = tableView;
        AppCore.addRefreshedTables(this);

        initDescriptionColumn(tableView);
        initOrderColumn(tableView);

        for (FeatureSet fs : AppCore.getFeatureSets().getItems()) {
            addFSColumns(fs);
        }

        tableView.getItems().addAll(AppCore.getSize().getItems());
        tableView.setEditable(true);
    }

    private void initOrderColumn(TableView<SizeItem> tableView) {
        TableColumn<SizeItem, Integer> inputCol = new TableColumn<>(FOR_ORDER_COLUMN_NAME);
        inputCol.setCellValueFactory(new PropertyValueFactory<>("forOrder"));

        inputCol.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                return String.format("%,d", object);
            }

            @Override
            public Integer fromString(String string) {

                int result = 0;

                try {
                    result = Integer.parseInt(string);
                } catch (NumberFormatException e) {
                    System.out.println("invalid points number");
                }

                return result;
            }
        }));

        inputCol.setOnEditCommit(event -> {
            event.getRowValue().setForOrder(event.getNewValue());

//            Calculator.calc();


            AppCore.refreshTables();
        });

        inputCol.setStyle("-fx-alignment: CENTER");
        inputCol.setEditable(true);
        inputCol.setPrefWidth(FOR_ORDER_COLUMN_WIDTH);
        tableView.getColumns().add(inputCol);
    }

    private void initDescriptionColumn(TableView<SizeItem> tableView) {
        TableColumn<SizeItem, String> descriptionCol = new TableColumn<>(DESCRIPTION_COLUMN_NAME);
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionCol.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionCol.setPrefWidth(DESCRIPTION_COLUMN_WIDTH);
        descriptionCol.setEditable(false);
        tableView.getColumns().add(descriptionCol);
    }

    public void addFSColumns(FeatureSet featureSet) {
        ArrayList<TableColumn> FSColumns = new ArrayList<>();

        FSColumns.add(new TableColumn<FeatureSet, Integer>(featureSet.getArticle()));
        FSColumns.add(new TableColumn<SizeItem, Integer>("Включ."));

        FSColumns.get(1).setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                SizeItem currSizeItem = (SizeItem) param.getValue();
                int index = AppCore.getSize().getItems().indexOf(currSizeItem);

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
                int index = AppCore.getSize().getItems().indexOf(currSizeItem);

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
                        int index = AppCore.getSize().getItems().indexOf(sizeItem);

                        if (sizeItem.getForOrder() > 0) {
                            if (featureSet.isTotalLimited(sizeItem.getPointType()) && AppCore.getSize().isTotallyOverLimited(featureSet.getTotalLimit())) {
                                setTextFill(Color.RED);
                                setStyle("-fx-font-weight: normal; -fx-alignment: CENTER;");
                            } else if (sizeItem.getForOrder() <= featureSet.getPointsIncluded(index)) {
                                setTextFill(Color.GREEN);
                                setStyle("-fx-font-weight: bold; -fx-alignment: CENTER;");
                            } else if (featureSet.getPointsIncluded(index) == NOT_ACCESSIBLE) {
                                setTextFill(Color.RED);
                                setStyle("-fx-font-weight: bold; -fx-alignment: CENTER;");
                            } else if (featureSet.getPointMaximum(index) > 0 && featureSet.getPointMaximum(index) < sizeItem.getForOrder()) {
                                setTextFill(Color.RED);
                                setStyle("-fx-font-weight: normal; -fx-alignment: CENTER;");
                            } else {
                                setTextFill(Color.GREEN);
                                setStyle("-fx-font-weight: normal; -fx-alignment: CENTER;");
                            }
//
                        } else {
                            setTextFill(Color.BLACK);
                            setStyle("-fx-alignment: CENTER;");
                        }
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
                        setStyle("");
                    } else { //If the cell is not empty

                        setText(item); //Put the String data in the cell

                        //We get here all the info of the Person of this row
                        SizeItem sizeItem = getTableView().getItems().get(getIndex());
                        int index = AppCore.getSize().getItems().indexOf(sizeItem);

                        if (sizeItem.getForOrder() > 0) {
                            boolean isSizeExceeded = sizeItem.getForOrder() > featureSet.getPointMaximum(index) &&
                                    featureSet.getPointMaximum(index) != 0;
                            boolean isTotalOverLimited = featureSet.isTotalLimited(sizeItem.getPointType()) &&
                                    AppCore.getSize().isTotallyOverLimited(featureSet.getTotalLimit());
                            boolean isExtensionOverLimited = AppCore.getCalculator().isSystemExtension() &&
                                    featureSet.getPointMaximum(index) != 0 &&
                                    (sizeItem.getForOrder() > (featureSet.getPointMaximum(index) - featureSet.getPointsIncluded(index)));

                            if (isSizeExceeded || isTotalOverLimited || isExtensionOverLimited) {
                                setTextFill(Color.RED); //The text in red
                                setStyle("-fx-font-weight: bold; -fx-alignment: CENTER;");
                                featureSet.setOverLimited(featureSet.isOverLimited() | true);
                            } else if (sizeItem.getForOrder() > featureSet.getPointsIncluded(index)) {
                                setTextFill(Color.GREEN);
                                setStyle("-fx-font-weight: bold; -fx-alignment: CENTER;");
                            } else {
                                setTextFill(Color.GREEN);
                                setStyle("-fx-font-weight: normal; -fx-alignment: CENTER;");
                            }
//
                        } else {
                            setTextFill(Color.BLACK);
                            setStyle("-fx-alignment: CENTER;");
                        }
                    }
                }
            };
        });

        FSColumns.get(0).getColumns().addAll(FSColumns.get(1), FSColumns.get(2));
        FSColumns.get(0).setSortable(false);
        FSColumns.get(1).setSortable(false);
        FSColumns.get(2).setSortable(false);
        FSColumns.get(0).setPrefWidth(FEATURE_SET_COLUMN_WIDTH);
        FSColumns.get(1).setPrefWidth(FEATURE_SET_COLUMN_WIDTH / 2);
        FSColumns.get(1).setStyle("-fx-alignment: CENTER");

        FSColumns.get(2).setPrefWidth(FEATURE_SET_COLUMN_WIDTH / 2);
        FSColumns.get(2).setStyle("-fx-alignment: CENTER");

        tableView.getColumns().add(FSColumns.get(0));
    }

    @Override
    public void refresh() {
        tableView.refresh();
    }
}
