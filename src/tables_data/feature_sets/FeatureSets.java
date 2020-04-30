package tables_data.feature_sets;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.List;

public class FeatureSets {
    private static FeatureSets instance;
    private final String SHEET_NAME = "feat_sets";
    private final String SHEET_NAME_FS_SIZE = "fs_size";
    private final String SHEET_NAME_FS_OPTIONS = "fs_options";
    private List<FeatureSet> featureSets;

    private FeatureSets() {
        featureSets = new ArrayList<>();
    }

    public static FeatureSets getInstance() {
        if (instance == null) {
            instance = new FeatureSets();
        }
        return instance;
    }

    public void init(Workbook workbook) {
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

    public List<FeatureSet> getItems() {
        return featureSets;
    }

    public FeatureSet get(int index) {
        return featureSets.get(index);
    }
}
