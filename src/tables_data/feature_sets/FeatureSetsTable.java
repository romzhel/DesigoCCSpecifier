package tables_data.feature_sets;

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

public class FeatureSetsTable implements Tables {
    private final String DESCRIPTION_COLUMN_TITLE = "Базовый пакет";
    private final String FS_DESCRIPTION_COLUMN_TITLE = "Описание";
    private final String COST_COLUMN_TITLE = "Ориентировочная стоимость";
    private final int DESCRIPTION_COLUMN_WIDTH = 329;
    private final int FS_DESCRIPTION_COLUMN_WIDTH = 500;
    private final int COST_COLUMN_WIDTH = 200;
    private TableView<FeatureSet> tableView;

    public FeatureSetsTable(TableView<FeatureSet> tableView) {
        this.tableView = tableView;
        AppCore.addRefreshedTables(this);

        initDescriptionColumn();
        initCostColumn();
        initDescriptionRuColumn(tableView);

        addContent();
    }

    private void addContent() {
        tableView.getItems().addAll(AppCore.getFeatureSets().getItems());
    }

    private void initDescriptionColumn() {
        TableColumn<FeatureSet, String> descriptionColumn = new TableColumn<>(DESCRIPTION_COLUMN_TITLE);
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("descriptionEn"));
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionColumn.setPrefWidth(DESCRIPTION_COLUMN_WIDTH);

        tableView.getColumns().add(descriptionColumn);
    }

    private void initCostColumn() {
        TableColumn<FeatureSet, String> costColumn = new TableColumn<>(COST_COLUMN_TITLE);

        costColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FeatureSet, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FeatureSet, String> param) {
                double c = param.getValue().getSummaryСost();
                return new SimpleStringProperty(String.format("%,.2f", param.getValue().getSummaryСost()));
            }
        });

//        costColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        costColumn.setStyle("-fx-alignment: CENTER");
        costColumn.setPrefWidth(COST_COLUMN_WIDTH);

        costColumn.setCellFactory(column -> {                           // ------ цвет текста спецификации-------------
            return new TableCell<FeatureSet, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty); //This is mandatory

                    if (item == null || empty) { //If the cell is empty
                        setText(null);
                        setStyle("");
                    } else { //If the cell is not empty

                        //We get here all the info of the Person of this row
//                        FeatureSet fs = getTableView().getItems().get(getIndex());
                        FeatureSet fs = AppCore.getFeatureSets().get(getIndex());

                        if (fs.isOverLimited()) {
                            setText("не применимо");
                            setTextFill(Color.RED); //The text in red
                            setStyle("-fx-font-weight: bold; -fx-alignment: CENTER;");
                        } else {
                            if (AppCore.getCalculator().isSystemExtension()) {
                                if (fs.getDescriptionEn().contains("Standart")) setTextFill(Color.GREEN);
                                else setTextFill(Color.ORANGE);
                            } else {
                                setTextFill(Color.GREEN);
                            }

                            setText(item); //Put the String data in the cell
                            setStyle("-fx-font-weight: bold; -fx-alignment: CENTER;");
                        }
                    }
                }
            };
        });

        tableView.getColumns().add(costColumn);
    }

    private void initDescriptionRuColumn(TableView<FeatureSet> tableView) {
        TableColumn<FeatureSet, String> descriptionRuColumn = new TableColumn<>(FS_DESCRIPTION_COLUMN_TITLE);
        descriptionRuColumn.setCellValueFactory(new PropertyValueFactory<>("descriptionRu"));
        descriptionRuColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionRuColumn.setPrefWidth(FS_DESCRIPTION_COLUMN_WIDTH);

        tableView.getColumns().add(descriptionRuColumn);
    }

    @Override
    public void refresh() {
        tableView.refresh();
    }
}
