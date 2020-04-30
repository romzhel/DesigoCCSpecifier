package tables_data.feature_sets;

import core.AppCore;
import core.Calculator;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.poi.ss.usermodel.Row;
import price_list.OrderPosition;
import tables_data.options.Option;
import tables_data.size.SizeItem;

import java.util.ArrayList;
import java.util.Arrays;

public class FeatureSet {
    private final int ARTICLE_CELL_INDEX = 0;
    private final int DESCRIPTION_EN_CELL_INDEX = 1;
    private final int DESCRIPTION_RU_CELL_INDEX = 2;
    private final int LIMIT_CELL_INDEX = 3;
    private final int SUPPLY_POSITIONS_CELL_INDEX = 4;
    private final int SSM_SUPPORT_CELL_INDEX = 5;
    private final int PSM_SUPPORT_CELL_INDEX = 6;
    private final int EXTENSION_COST_CELL_INDEX = 7;
    private StringProperty descriptionEn;
    private StringProperty descriptionRu;
    private String article;
    private TotalLimit totalLimit;
    private String supplyPositions;
    private ArrayList<SizeLimit> sizeLimits;
    private ArrayList<Integer> optionsAccessibilities;
    private boolean isSupportSSM;
    private boolean isSupportPSM;
    private boolean isDisplayingForExtension;
    private boolean overLimited;
    private double summaryСost;
    private ArrayList<OrderPosition> specification;
    private int amount = 1;

    public FeatureSet(Row row) {
        sizeLimits = new ArrayList<>();
        optionsAccessibilities = new ArrayList<>();

        article = row.getCell(ARTICLE_CELL_INDEX) == null ? "" : row.getCell(ARTICLE_CELL_INDEX).getStringCellValue();

        String descriptionEnS = row.getCell(DESCRIPTION_EN_CELL_INDEX) == null ? "" : row.getCell(DESCRIPTION_EN_CELL_INDEX).getStringCellValue();
        descriptionEn = new SimpleStringProperty(descriptionEnS);

        String descriptionRuS = row.getCell(DESCRIPTION_RU_CELL_INDEX) == null ? "" : row.getCell(DESCRIPTION_RU_CELL_INDEX).getStringCellValue();
        descriptionRu = new SimpleStringProperty(descriptionRuS);

        String pointsSummaryLimit = row.getCell(LIMIT_CELL_INDEX) == null ? "" : row.getCell(LIMIT_CELL_INDEX).getStringCellValue();
        if (pointsSummaryLimit.trim().length() > 2) {
            int separatorPos = pointsSummaryLimit.indexOf("|");
            String valueS = pointsSummaryLimit.substring(0, separatorPos).trim();
            int value = 0;

            try {
                value = Integer.parseInt(valueS);
            } catch (Exception e) {

            }

            String[] pointTypes = (pointsSummaryLimit.substring(separatorPos + 1).trim()).split("\\,");
            totalLimit = new TotalLimit(value, pointTypes);

        } else {
            totalLimit = null;
        }

        supplyPositions = row.getCell(SUPPLY_POSITIONS_CELL_INDEX) == null ? "" : row.getCell(SUPPLY_POSITIONS_CELL_INDEX).getStringCellValue();
        isSupportSSM = row.getCell(SSM_SUPPORT_CELL_INDEX).getStringCellValue().toLowerCase().equals("да") ? true : false;
        isSupportPSM = row.getCell(PSM_SUPPORT_CELL_INDEX).getStringCellValue().toLowerCase().equals("да") ? true : false;
        isDisplayingForExtension = row.getCell(EXTENSION_COST_CELL_INDEX).getStringCellValue().toLowerCase().equals("да") ? true : false;

        overLimited = false;
        summaryСost = 0.0;
        specification = new ArrayList<>();
    }

    public void addSizeLimits(int included, int maximum) {
        this.sizeLimits.add(new SizeLimit(included, maximum));
    }

    public String getDescriptionEn() {
        return descriptionEn.get();
    }

    public StringProperty descriptionEnProperty() {
        return descriptionEn;
    }

    public String getDescriptionRu() {
        return descriptionRu.get();
    }

    public StringProperty descriptionRuProperty() {
        return descriptionRu;
    }

    public String getArticle() {
        return article;
    }

    public ArrayList<SizeLimit> getSizeLimits() {
        return sizeLimits;
    }

    public int getPointsIncluded(int sizeLimitItem) {
        return sizeLimits.get(sizeLimitItem).included;
    }

    public int getPointMaximum(int sizeLimitItem) {
        return sizeLimits.get(sizeLimitItem).maximum;
    }

    public boolean isOverLimited() {
        return overLimited;
    }

    public void setOverLimited(boolean overLimited) {
        this.overLimited = overLimited;
    }

    public int getOrderAccessibility(int index) {
        return optionsAccessibilities.get(index);
    }

    public void addOrderAccessibility(int value) {
        optionsAccessibilities.add(value);
    }

    private class SizeLimit {
        private int included;
        private int maximum;

        public SizeLimit(int included, int maximum) {
            this.included = included;
            this.maximum = maximum;
        }
    }

    public class TotalLimit {
        private int totalPoints;
        private ArrayList<String> pointsWithLimitation;

        public TotalLimit(int totalPoints, String[] pointsWithLimitation) {
            this.totalPoints = totalPoints;
            this.pointsWithLimitation = new ArrayList<>(Arrays.asList(pointsWithLimitation));
        }

        public int getTotalPoints() {
            return totalPoints;
        }

        public ArrayList<String> getPointsWithLimitation() {
            return pointsWithLimitation;
        }
    }

    public TotalLimit getTotalLimit() {
        return totalLimit;
    }

    public boolean isTotalLimited(String pointType) {
        if (totalLimit == null) return false;
        return totalLimit.getPointsWithLimitation().indexOf(pointType) >= 0;
    }

    public double getSummaryСost() {
        return summaryСost;
    }

    public void setSummaryСost(double summaryСost) {
        this.summaryСost = summaryСost;
    }

    public void addToSummaryCost(double cost) {
        summaryСost += cost;
    }

    public void addToSpecification(OrderPosition orderPosition) {
        specification.add(orderPosition);
    }

    public void addToSpecification(String article, int amount) {
        specification.add(new OrderPosition(article, amount));
    }

    public void addToSpecification(ArrayList<OrderPosition> orderPositions) {
        if (orderPositions != null) {
            specification.addAll(orderPositions);
        }
    }

    public ArrayList<OrderPosition> getSpecification() {
        return specification;
    }

    public int getOptionAccessibility(int index) {
        return optionsAccessibilities.get(index);
    }

    public String getSupplyPositions() {
        return supplyPositions;
    }

    public boolean isSupportSSM() {
        return isSupportSSM;
    }

    public boolean isSupportPSM() {
        return isSupportPSM;
    }

    public boolean isDisplayingForExtension() {
        return isDisplayingForExtension;
    }

    public void checkOverlimited() {
        for (Option option : AppCore.getOptions().getItems()) {
            int optionIndex = AppCore.getOptions().getItems().indexOf(option);
            int orderableAccessibility = optionsAccessibilities.get(optionIndex);

            if (option.isOrdered() && orderableAccessibility == Option.NOT_ACCESSIBLE) {
                overLimited = true;
            }
        }

        for (SizeItem sizeItem : AppCore.getSize().getItems()) {
            int index = AppCore.getSize().getItems().indexOf(sizeItem);

            if (sizeItem.getForOrder() > 0) {
                boolean isSizeExceeded = sizeItem.getForOrder() > getPointMaximum(index) && getPointMaximum(index) != 0;
                boolean isExtensionOverLimited = AppCore.getCalculator().getCalcType() == Calculator.CalcType.EXTENSION &&
                        getPointMaximum(index) != 0 &&
                        (sizeItem.getForOrder() > (getPointMaximum(index) - getPointsIncluded(index)));

                if (isSizeExceeded || isExtensionOverLimited) {
                    overLimited = true;
                }
            }
        }

        amount = AppCore.getSize().getAmount(totalLimit);
    }

    public int getAmount() {
        return amount;
    }
}
