package tables_data.options;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.List;

public class Options {
    private static Options instance;
    private final String SHEET_NAME = "options";
    private List<Option> options;

    private Options() {
        options = new ArrayList<>();
    }

    public static Options getInstance() {
        if (instance == null) {
            instance = new Options();
        }
        return instance;
    }

    public void init(Workbook workbook) {
        Sheet sheet = workbook.getSheet(SHEET_NAME);
        Row row;
        int rowIndex = 0;

        while ((row = sheet.getRow(++rowIndex)) != null) {
            options.add(new Option(row));
        }
    }

    public List<Option> getItems() {
        return options;
    }
}
