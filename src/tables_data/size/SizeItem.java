package tables_data.size;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.poi.ss.usermodel.Row;

public class SizeItem {
    private static final int DESCRIPTION_CELL_INDEX = 0;
    private static final int POINT_TYPE_CELL_INDEX = 1;
    private static final int ARTICLE_PART_CELL_INDEX = 2;
    private static final int ADD_ARTICLES_CELL_INDEX = 3;
    public static final int NOT_ACCESSIBLE = -1;
    public static final int NOT_LIMITED = 0;
    private StringProperty description;
    private String pointType;
    private String articlePart;
    private String addArticles;
    private IntegerProperty forOrder;

    public SizeItem(Row row) {
        String descriptionS = row.getCell(DESCRIPTION_CELL_INDEX) == null ? "" : row.getCell(DESCRIPTION_CELL_INDEX).getStringCellValue().trim();
        description = new SimpleStringProperty(descriptionS);

        pointType = row.getCell(POINT_TYPE_CELL_INDEX) == null ? "" : row.getCell(POINT_TYPE_CELL_INDEX).getStringCellValue().trim();
        articlePart = row.getCell(ARTICLE_PART_CELL_INDEX) == null ? "" : row.getCell(ARTICLE_PART_CELL_INDEX).getStringCellValue().trim();
        addArticles = row.getCell(ADD_ARTICLES_CELL_INDEX) == null ? "" : row.getCell(ADD_ARTICLES_CELL_INDEX).getStringCellValue().trim();

        forOrder = new SimpleIntegerProperty(0);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getPointType() {
        return pointType;
    }

    public void setPointType(String pointType) {
        this.pointType = pointType;
    }

    public String getArticlePart() {
        return articlePart;
    }

    public void setArticlePart(String articlePart) {
        this.articlePart = articlePart;
    }

    public int getForOrder() {
        return forOrder.get();
    }

    public IntegerProperty forOrderProperty() {
        return forOrder;
    }

    public void setForOrder(int forOrder) {
        this.forOrder.set(forOrder);
    }

    public String getAddArticles() {
        return addArticles;
    }
}
