package window_spec_and_points_tables;

import core.AppCore;
import excel.DataSize;
import excel.ExcelRow;
import excel.ExcelTable;
import excel.SelectedColumns;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import point_matrix.PointMatrixItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SpecTables {
    public static final int POINTS_CALCED_TABLE_COLUMNS = 5;
    public static final int POINTS_CALCED_TABLE_POINTS_AMOUNT_COLUMN = 3;
    public static final int POINTS_CALCED_TABLE_POINT_TYPE_COLUMN = 4;
    private Stage stage;
    private String fileName;
    private Workbook workbook;
    private TableView<ExcelRow> tableView;
    private ExcelTable currentSheetData;
    private ExcelTable foundPoints;
    private SelectedColumns selectedColumns;

    public SpecTables(File excelSpecFile) {
        openExcelSpecFile(excelSpecFile);
    }

    private void openExcelSpecFile(File excelSpecFile) {
        try {
            InputStream inputStream = new FileInputStream(excelSpecFile);
            workbook = WorkbookFactory.create(inputStream);
            inputStream.close();
            fileName = excelSpecFile.getPath();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public String[] getSheetNames() {
        int sheetsCount = workbook.getNumberOfSheets();
        String[] result = new String[sheetsCount];

        for (int sheetIndex = 0; sheetIndex < sheetsCount; sheetIndex++) {
            String sheetName = workbook.getSheetName(sheetIndex);
            result[sheetIndex] = sheetName;
        }

        return result;
    }

    public ExcelTable getSheetData(int sheetIndex) {
        DataSize sheetDataSize = getSheetDataSize(sheetIndex);
        currentSheetData = new ExcelTable(sheetDataSize.getCols());
        Row row;

        int emptyRows = 0;

        for (int rowIndex = 0; rowIndex < sheetDataSize.getRows() + emptyRows; rowIndex++) {
            String[] colsValues = new String[sheetDataSize.getCols()];
            row = workbook.getSheetAt(sheetIndex).getRow(rowIndex);

            if (row == null) {
                emptyRows++;
                continue;
            }

            for (int colIndex = 0; colIndex < sheetDataSize.getCols(); colIndex++) {
                Cell cell = row.getCell(colIndex);

                if (cell == null) {
                    colsValues[colIndex] = "";
                    continue;
                } else if (cell.getCellTypeEnum().equals(CellType.STRING)) {
                    colsValues[colIndex] = cell.getStringCellValue().trim();
                } else if (cell.getCellTypeEnum().equals(CellType.NUMERIC)) {
                    colsValues[colIndex] = Double.toString(cell.getNumericCellValue());
                } else {
                    colsValues[colIndex] = "";
                }
            }

            currentSheetData.addRow(colsValues);
        }

        return currentSheetData;
    }

    public DataSize getSheetDataSize(int index) {
        Sheet sheet = workbook.getSheetAt(index);
        Row row;

        int colWithData = 0;
        int rowWithData = 0;
        int rowsFromFile = sheet.getLastRowNum();

        for (int rowIndex = 0; rowIndex <= rowsFromFile + 1; rowIndex++) {
            row = sheet.getRow(rowIndex);

            if (row != null) {
                rowWithData++;
                int currColsWithData = row.getLastCellNum();
                colWithData = colWithData > currColsWithData ? colWithData : currColsWithData;
            }
        }

        return new DataSize(colWithData + 1, rowWithData);
    }

    public void display(int sheetIndex) {
        if (workbook != null) {
            display(getSheetData(sheetIndex));
        } else {
            System.out.println("can't open excel file");
        }
    }

    public void display(ExcelTable data) {
        if (tableView.getColumns().size() > 1) {
            tableView.getColumns().remove(1, tableView.getColumns().size());
        }

        //--- display in table --------------------
        String colTitle = "";
        for (int i = 0; i < data.getColumnsCount(); i++) {
            if (data.getRows().get(0).getCellValues()[0] == "Заказной номер") {
                colTitle = data.getRows().get(0).getCellValues()[i];

                if (i == data.getColumnsCount() - 1) data.getRows().remove(0);
            } else {
                colTitle = "Столбец " + i;
            }

            TableColumn<ExcelRow, String> column = new TableColumn<>(colTitle);

            final int fi = i;
            column.setCellValueFactory(param -> {
                ExcelRow excelRow = param.getValue();

                String value = excelRow.getCellValues()[fi];
                try {
                    float dValue = Float.parseFloat(value);

                    if (dValue == (long) dValue) value = String.format("%d", (long) dValue);
                    else value = String.format("%.2f", dValue);

                } catch (Exception e) {
                    e.getMessage();
                }

                SimpleStringProperty stringProperty = new SimpleStringProperty(value);

                return stringProperty;
            });

            column.setCellFactory(TextFieldTableCell.forTableColumn());

            column.setCellFactory(new Callback<TableColumn<ExcelRow, String>, TableCell<ExcelRow, String>>() {
                @Override
                public TableCell<ExcelRow, String> call(TableColumn<ExcelRow, String> currColumn) {
                    return new TableCell<ExcelRow, String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty); //This is mandatory

                            if (item == null || empty) { //If the cell is empty
                                setText(null);
                                setStyle("");
                            } else { //If the cell is not empty

                                setText(item); //Put the String data in the cell

                                //We get here all the info of the Person of this row
                                int colIndex = tableView.getColumns().indexOf(currColumn) - 1;

                                if (colIndex == selectedColumns.getIdCol()) {
                                    setTextFill(Color.GREEN);
                                } else if (colIndex == selectedColumns.getValCol()) {
                                    setTextFill(Color.BLUE);
                                } else {
                                    setTextFill(Color.BLACK);
                                }
                            }
                        }
                    };
                }
            });

            column.setPrefWidth(120);
            column.setSortable(false);

            tableView.getColumns().add(column);
        }

        tableView.getItems().clear();
        selectedColumns = new SelectedColumns();
        tableView.getItems().addAll(data.getRows());
        tableView.refresh();
    }

    public void closeExcelSpecFile() {
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeWindow() {
        closeExcelSpecFile();
        stage.close();
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public TableView<ExcelRow> getTableView() {
        return tableView;
    }

    public void setTableView(TableView<ExcelRow> tableView) {
        this.tableView = tableView;
    }

    public String getFileName() {
        return fileName;
    }

    public SelectedColumns getSelectedColumns() {
        return selectedColumns;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public ExcelTable calcPointFromSpec() {
        foundPoints = new ExcelTable(POINTS_CALCED_TABLE_COLUMNS);
        foundPoints.addRow("Заказной номер", "Артикул", "Количество", "Точек", "Тип точек");

        String id;
        int amount;
        for (ExcelRow row : currentSheetData.getRows()) {
            id = toEN(row.getCellValue(selectedColumns.getIdCol()));

            try {
                amount = (int) Double.parseDouble(row.getCellValue(selectedColumns.getValCol()));
            } catch (Exception e) {
                if (row.getCellValue(selectedColumns.getIdCol()).matches(".*\\d+.*")) {
                    System.out.println("can't parse to int " + row.getCellValue(selectedColumns.getValCol()));
                }
                continue;
            }

            for (PointMatrixItem pointMatrixItem : AppCore.getPointMatrix().findPoints(id)) {
                foundPoints.addRow(
                        pointMatrixItem.getSsn(),
                        pointMatrixItem.getArticle(),
                        Integer.toString(amount),
                        Integer.toString(pointMatrixItem.getPointCount() * amount),
                        pointMatrixItem.getPointType()
                );
            }
        }

        return foundPoints;
    }

    public String toEN(String fromAny) {
        String en = "";

        for (int i = 0; i < fromAny.length(); i++) {
            char ch = fromAny.charAt(i);

            switch (ch) {
                case 'А':
                    en = en.concat("A");
                    break;
                case 'В':
                    en = en.concat("B");
                    break;
                case 'С':
                    en = en.concat("C");
                    break;
                case 'Е':
                    en = en.concat("E");
                    break;
                case 'О':
                    en = en.concat("O");
                    break;
                case 'Р':
                    en = en.concat("P");
                    break;
                case 'Т':
                    en = en.concat("T");
                    break;
                case 'К':
                    en = en.concat("K");
                    break;
                case 'М':
                    en = en.concat("M");
                    break;
                case 'Н':
                    en = en.concat("H");
                    break;
                case 'Х':
                    en = en.concat("X");
                    break;
                default:
                    en = en.concat(String.valueOf(ch));
            }
        }

        return en.toUpperCase();
    }

    public ExcelTable getCurrentSheetData() {
        return currentSheetData;
    }

    public ExcelTable getFoundPoints() {
        return foundPoints;
    }

    public void setFoundPoints(ExcelTable foundPoints) {
        this.foundPoints = foundPoints;
    }

    public void displayFoundPoints() {
        display(calcPointFromSpec());
    }

    public void addPointsToCalc() {

    }

    public void setSelectedColumns(SelectedColumns selectedColumns) {
        this.selectedColumns = selectedColumns;
    }
}
