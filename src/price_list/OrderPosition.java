package price_list;

import org.apache.poi.ss.usermodel.Row;

public class OrderPosition {
    private final int SSN_COLUMN = 0;
    private final int ARTICLE_COLUMN = 1;
    private final int DESCRIPTION_EN_COLUMN = 2;
    private final int DESCRIPTION_RU_COLUMN = 3;
    private final int COST_COLUMN = 4;
    private String ssn;
    private String article;
    private String descriptionEn;
    private String descriptionRu;
    private double cost;
    private int amount;

    public OrderPosition(Row row) {
        ssn = row.getCell(SSN_COLUMN) == null ? "" : row.getCell(SSN_COLUMN).getStringCellValue();
        article = row.getCell(ARTICLE_COLUMN) == null ? "" : row.getCell(ARTICLE_COLUMN).getStringCellValue();
        descriptionEn = row.getCell(DESCRIPTION_EN_COLUMN) == null ? "" : row.getCell(DESCRIPTION_EN_COLUMN).getStringCellValue();
        descriptionRu = row.getCell(DESCRIPTION_RU_COLUMN) == null ? "" : row.getCell(DESCRIPTION_RU_COLUMN).getStringCellValue();
        cost = row.getCell(COST_COLUMN) == null ? 0.0 : row.getCell(COST_COLUMN).getNumericCellValue();

        amount = 0;
    }

    public OrderPosition(OrderPosition op, int amount) {
        ssn = op.ssn;
        article = op.article;
        descriptionEn = op.descriptionEn;
        descriptionRu = op.descriptionRu;
        cost = op.cost;
        this.amount = amount;
    }

    public OrderPosition(String article, int amount) {
        ssn = "";
        this.article = article;
        descriptionEn = "";
        descriptionRu = "";
        cost = 0.0;
        this.amount = amount;
    }

    public String getSsn() {
        return ssn;
    }

    public String getArticle() {
        return article;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public String getDescriptionRu() {
        return descriptionRu;
    }

    public double getCost() {
        return cost;
    }

    public int getAmount() {
        return amount;
    }
}
