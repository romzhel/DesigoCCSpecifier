package point_matrix;

import core.AppCore;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;

public class PointMatrix {
    private final String SHEET_NAME_PART = "matrix";
    private ArrayList<PointMatrixItem> pointMatrixItems;

    public PointMatrix(Workbook workbook) {
        pointMatrixItems = new ArrayList<>();

        for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);

            if (sheet.getSheetName().contains(SHEET_NAME_PART)) {
                Row row = null;
                int rowIndex = 0;

                while ((row = sheet.getRow(++rowIndex)) != null) {
                    PointMatrixItem pmi = new PointMatrixItem(row);

                    if (AppCore.getSize().isCorrectPointType(pmi.getPointType())) {
                        pointMatrixItems.add(pmi);
                    } else {
                        System.out.println("invalid point type for " + pmi.getArticle() + " - " + pmi.getPointType() +
                                " (sheet name " + sheet.getSheetName() + ", line " + rowIndex + ")");
                    }
                }
            }
        }
    }

    public ArrayList<PointMatrixItem> findPoints(String idValue) {
        ArrayList<PointMatrixItem> result = new ArrayList<>();
        for (PointMatrixItem pmi : pointMatrixItems) {
            if (pmi.getSsn().equals(idValue) || pmi.getArticle().equals(idValue)) {
                result.add(pmi);
            }
        }
        return result;
    }
}
