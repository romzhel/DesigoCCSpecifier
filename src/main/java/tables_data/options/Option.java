package tables_data.options;

import lombok.Data;
import org.apache.poi.ss.usermodel.Row;

@Data
public class Option {
    private final int DESCRIPTION_CELL_INDEX = 0;
    private final int ARTICLE_CELL_INDEX = 2;
    public static final int NOT_ACCESSIBLE = -1;
    public static final int ORDERABLE = 0;
    public static final int INCLUDED = 1;
    private String description;
    private boolean isOrdered;
    private String article;

    public Option(String description, String article) {
        this.description = description;
        this.article = article;
        isOrdered = false;
    }

    public Option(Row row) {
        if (row.getCell(DESCRIPTION_CELL_INDEX) != null) {
            description = row.getCell(DESCRIPTION_CELL_INDEX).getStringCellValue().trim();
        } else {
            description = "";
        }

        if (row.getCell(ARTICLE_CELL_INDEX) != null) {
            article = row.getCell(ARTICLE_CELL_INDEX).getStringCellValue().trim();
        } else {
            article = "";
        }
    }

    @Override
    public String toString() {
        return "Option{" +
                "description=" + description +
                ", isOrdered=" + isOrdered +
                ", article='" + article + '\'' +
                '}';
    }
}
