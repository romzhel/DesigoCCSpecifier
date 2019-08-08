package point_matrix;

import org.apache.poi.ss.usermodel.Row;

public class PointMatrixItem {
    private final int COLUMN_SSN = 0;
    private final int COLUMN_ARTICLE = 1;
    private final int COLUMN_POINT_COUNT = 2;
    private final int COLUMN_POINT_TYPE = 3;
    private String ssn;
    private String article;
    private int pointCount;
    private String pointType;

    public PointMatrixItem(Row row) {
        ssn = row.getCell(COLUMN_SSN).getStringCellValue().trim();
        article = row.getCell(COLUMN_ARTICLE).getStringCellValue().trim();
        pointCount = (int) row.getCell(COLUMN_POINT_COUNT).getNumericCellValue();
        pointType = row.getCell(COLUMN_POINT_TYPE).getStringCellValue().trim();
    }

    public String getSsn() {
        return ssn;
    }

    public String getArticle() {
        return article;
    }

    public int getPointCount() {
        return pointCount;
    }

    public String getPointType() {
        return pointType;
    }
}
