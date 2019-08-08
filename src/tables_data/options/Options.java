package tables_data.options;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class Options {
    private final String SHEET_NAME = "options";
    private ObservableList<Option> options;

    public Options(Workbook workbook) {
        options = FXCollections.observableArrayList();

        Sheet sheet = workbook.getSheet(SHEET_NAME);
        Row row = null;
        int rowIndex = 0;

        while ((row = sheet.getRow(++rowIndex)) != null) {
            options.add(new Option(row));
        }
    }

    public ObservableList<Option> getItems() {
        return options;
    }
}
