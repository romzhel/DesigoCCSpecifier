package tables_data.feature_sets;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class FeatureSets {
    private final String SHEET_NAME = "feat_sets";
    private final String SHEET_NAME_FS_SIZE = "fs_size";
    private final String SHEET_NAME_FS_OPTIONS = "fs_options";
    private ObservableList<FeatureSet> featureSets;

    public FeatureSets(Workbook workbook) {
        featureSets = FXCollections.observableArrayList();
        initSize(workbook);
        initOptions(workbook);
    }

    private void initSize(Workbook workbook) {
        Sheet sheet = workbook.getSheet(SHEET_NAME);
        Row row = null;
        int rowIndex = 0;
        while ((row = sheet.getRow(++rowIndex)) != null) {
            featureSets.add(new FeatureSet(row));
        }

        sheet = workbook.getSheet(SHEET_NAME_FS_SIZE);
        rowIndex = 0;
        Cell cell;
        while ((row = sheet.getRow(++rowIndex)) != null) {
            int colIndex = 1;
            int fsIndex = 0;
            while (row.getCell(colIndex) != null && row.getCell(colIndex + 1) != null && fsIndex < featureSets.size()) {

                int pointsIncluded = (int) row.getCell(colIndex++).getNumericCellValue();
                int pointsMaximun = (int) row.getCell(colIndex++).getNumericCellValue();

                featureSets.get(fsIndex++).addSizeLimits(pointsIncluded, pointsMaximun);
            }
        }
    }

    private void initOptions(Workbook workbook) {
        Sheet sheet;
        int rowIndex;
        Row row;

        sheet = workbook.getSheet(SHEET_NAME_FS_OPTIONS);
        rowIndex = 0;
        while ((row = sheet.getRow(++rowIndex)) != null) {
            int colIndex = 1;
            int fsIndex = 0;
            while (row.getCell(colIndex) != null && fsIndex < featureSets.size()) {

                int optionAccessibility = (int) row.getCell(colIndex++).getNumericCellValue();

                featureSets.get(fsIndex++).addOrderAccessibility(optionAccessibility);
            }
        }
    }

    public ObservableList<FeatureSet> getItems() {
        return featureSets;
    }

    public FeatureSet get(int index) {
        return featureSets.get(index);
    }
}
