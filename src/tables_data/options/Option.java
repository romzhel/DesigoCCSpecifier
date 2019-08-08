package tables_data.options;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.poi.ss.usermodel.Row;

public class Option {
    private final int DESCRIPTION_CELL_INDEX = 0;
    private final int ARTICLE_CELL_INDEX = 2;
    public static final int NOT_ACCESSIBLE = -1;
    public static final int ORDERABLE = 0;
    public static final int INCLUDED = 1;
    private StringProperty description;
    private boolean isOrdered;
    private String article;

    public Option(StringProperty description, String article) {
        this.description = description;
        this.article = article;
        isOrdered = false;
    }

    public Option(Row row) {
        if (row.getCell(DESCRIPTION_CELL_INDEX) != null) {
            description = new SimpleStringProperty(row.getCell(DESCRIPTION_CELL_INDEX).getStringCellValue().trim());
        } else {
            description = new SimpleStringProperty("");
        }

        if (row.getCell(ARTICLE_CELL_INDEX) != null) {
            article = row.getCell(ARTICLE_CELL_INDEX).getStringCellValue().trim();
        } else {
            article = "";
        }
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public boolean isOrdered() {
        return isOrdered;
    }

    public void setOrdered(boolean ordered) {
        isOrdered = ordered;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }
}
