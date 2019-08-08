package additional_positions;

import org.apache.poi.ss.usermodel.Row;

public class OthersItem {
    private final int COLUMN_DESCRIPTION = 0;
    private final int COLUMN_ARTICLE = 1;
    private final int COLUMN_BY_DEFAULT = 2;
    private String description;
    private String article;
    private boolean isDefault;
    private boolean isOrdered;

    public OthersItem(Row row) {
        description = row.getCell(COLUMN_DESCRIPTION).getStringCellValue().trim();
        article = row.getCell(COLUMN_ARTICLE).getStringCellValue().trim();
        isDefault = row.getCell(COLUMN_BY_DEFAULT).getStringCellValue().trim().equals("да");
        isOrdered = isDefault;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public boolean isOrdered() {
        return isOrdered;
    }

    public void setOrdered(boolean ordered) {
        isOrdered = ordered;
    }
}
