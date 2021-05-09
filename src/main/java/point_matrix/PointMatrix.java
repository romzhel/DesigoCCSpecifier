package point_matrix;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import tables_data.size.Size;

import java.util.ArrayList;
import java.util.List;

public class PointMatrix {
    private static PointMatrix instance;
    private final String SHEET_NAME_PART = "matrix";
    private List<PointMatrixItem> items;

    private PointMatrix() {
        items = new ArrayList<>();
    }

    public static PointMatrix getInstance() {
        if (instance == null) {
            instance = new PointMatrix();
        }
        return instance;
    }

    public void init(Workbook workbook) {
        for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);

            if (sheet.getSheetName().contains(SHEET_NAME_PART)) {
                Row row = null;
                int rowIndex = 0;

                while ((row = sheet.getRow(++rowIndex)) != null) {
                    PointMatrixItem pmi = new PointMatrixItem(row);

                    if (Size.getInstance().isCorrectPointType(pmi.getPointType())) {
                        items.add(pmi);
                    } else {
                        System.out.println("invalid point type for " + pmi.getArticle() + " - " + pmi.getPointType() +
                                " (sheet name " + sheet.getSheetName() + ", line " + rowIndex + ")");
                    }
                }
            }
        }
    }

    public List<PointMatrixItem> findPoints(String idValue) {
        List<PointMatrixItem> result = new ArrayList<>();
        for (PointMatrixItem pmi : items) {
            if (pmi.getSsn().equals(idValue) || pmi.getArticle().equals(idValue)) {
                result.add(pmi);
            }
        }
        return result;
    }
}
