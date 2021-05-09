package additional_positions;

import lombok.Data;
import org.apache.poi.ss.usermodel.Row;

@Data
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
}
