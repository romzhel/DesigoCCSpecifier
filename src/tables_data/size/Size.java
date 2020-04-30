package tables_data.size;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import tables_data.feature_sets.FeatureSet;

import java.util.ArrayList;
import java.util.List;

public class Size {
    private static final String SHEET_NAME = "size";
    private static Size instance;
    private List<SizeItem> size;

    private Size() {
        size = new ArrayList<>();
    }

    public static Size getInstance() {
        if (instance == null) {
            instance = new Size();
        }
        return instance;
    }

    public void init(Workbook workbook) {
        Sheet sheet = workbook.getSheet(SHEET_NAME);
        Row row = null;
        int rowIndex = 0;

        while ((row = sheet.getRow(++rowIndex)) != null) {
            size.add(new SizeItem(row));
        }
    }

    public List<SizeItem> getItems() {
        return size;
    }

    public int getAmount(FeatureSet.TotalLimit totalLimit) {
        if (totalLimit == null) return 1;
        int totalPoints = 0;

        for (SizeItem si : size) {
            if (totalLimit.getPointsWithLimitation().contains(si.getPointType())) {
                totalPoints += si.getForOrder();
            }
        }

        int amount = totalPoints % totalLimit.getTotalPoints() > 0 ?
                totalPoints / totalLimit.getTotalPoints() + 1 :
                totalPoints / totalLimit.getTotalPoints();

        return Math.max(1, amount);
    }

    public boolean isCorrectPointType(String pointType) {
        for (SizeItem sizeItem : size) {
            if (sizeItem.getPointType().equals(pointType)) {
                return true;
            }
        }
        return false;
    }
}
