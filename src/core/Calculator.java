package core;

import additional_positions.Others;
import dialogs.Dialogs;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import point_packets.PointPackets;
import price_list.OrderPosition;
import price_list.PriceList;
import tables_data.feature_sets.FeatureSet;
import tables_data.feature_sets.FeatureSets;
import tables_data.options.Option;
import tables_data.options.Options;
import tables_data.size.Size;
import tables_data.size.SizeItem;

import java.util.ArrayList;

public class Calculator {
    private static Calculator instance;
    private static final OrderPosition EMPTY = null;
    private static final String STANDARD_MIGRATION_SUFFIX = "-SSM";
    private static final String PRIVILEGED_MIGRATION_SUFFIX = "-PSM";
    private String errorOrderPosition = "";
    private BooleanProperty calculationStatus;
    private CalcType calcType;

    private Calculator() {
        calculationStatus = new SimpleBooleanProperty(false);
        calcType = CalcType.NEW;
    }

    public static Calculator getInstance() {
        if (instance == null) {
            instance = new Calculator();
        }
        return instance;
    }

    public void calcCosts() {
        for (FeatureSet featureSet : FeatureSets.getInstance().getItems()) {
            featureSet.setSummaryСost(0);
            featureSet.getSpecification().clear();

            if (isFeatureSetOverlimited(featureSet)) continue;

            if (calcType != CalcType.EXTENSION) addFeatureSetToSpec(featureSet);
            addPointPacketsToSpec(featureSet);
            addOptionsToSpec(featureSet);

            if (calcType != CalcType.EXTENSION) addSupplyFeatureSetPositions(featureSet);
            if (featureSet.getSpecification().size() > 0) featureSet.addToSpecification(EMPTY);
            addSizeSupplyPositions(featureSet);

//            addOthersToSpec(featureSet);

            featureSet.addToSpecification(EMPTY);
            calcSummaryCostBySpec(featureSet);
        }

        resfreshCalculationStatus();
    }

    private boolean isFeatureSetOverlimited(FeatureSet featureSet) {
        featureSet.checkOverlimited();

        if (!featureSet.isSupportPSM() && calcType == CalcType.MIGRATION_PSM)
            featureSet.setOverLimited(true);
        if (!featureSet.isSupportSSM() && calcType == CalcType.MIGRATION_SSM)
            featureSet.setOverLimited(true);

        if (!featureSet.isDisplayingForExtension() && calcType == CalcType.EXTENSION) featureSet.setOverLimited(true);

        return featureSet.isOverLimited();
    }

    private void addFeatureSetToSpec(FeatureSet featureSet) {
        featureSet.addToSpecification(PriceList.getInstance().getNewOrderPosition(
                featureSet.getArticle().concat(calcType.migrationSuffix), featureSet.getAmount()));
    }

    private void addPointPacketsToSpec(FeatureSet featureSet) {
        int sizeItemIndex = 0;
        for (SizeItem sizeItem : Size.getInstance().getItems()) {
            int includedPoints = featureSet.getPointsIncluded(sizeItemIndex++);
            int orderedPoints = sizeItem.getForOrder();

            if (includedPoints == -1 || (includedPoints >= orderedPoints && calcType != CalcType.EXTENSION)) continue;

            int forOrderPoints = calcType == CalcType.EXTENSION ? orderedPoints : orderedPoints - includedPoints;
            featureSet.addToSpecification(PointPackets.getInstance().getSpecification(forOrderPoints, sizeItem.getPointType()));
        }
    }

    private void addOptionsToSpec(FeatureSet featureSet) {
        int optionIndex = 0;
        for (Option option : Options.getInstance().getItems()) {
            if (option.isOrdered() && featureSet.getOptionAccessibility(optionIndex) == Option.ORDERABLE) {
                if (option.getArticle() != null && !option.getArticle().isEmpty()) {
                    addPositionToSpec(featureSet, option.getArticle());
                }
            }
            optionIndex++;
        }
    }

    private void addPositionToSpec(FeatureSet featureSet, String article) {
        OrderPosition addedOrderPosition = PriceList.getInstance().getNewOrderPosition(article, 1);

        if (addedOrderPosition != null) {
            featureSet.addToSpecification(addedOrderPosition);
        } else {
            featureSet.addToSpecification(new OrderPosition(article, 1));

            if (!errorOrderPosition.equals(article)) {
                new Dialogs().showMessage("Формирование спецификации", "Не была найдена заказная позиция в прайсе " +
                        "с артиклем " + article + ".\nОбратите внимание, что конечная общая стоимость проекта с базовым пакетом " +
                        featureSet.getArticle() + " будет выше.");
                errorOrderPosition = article;
            }
        }
    }

    private void addSizeSupplyPositions(FeatureSet featureSet) {
        for (SizeItem sizeItem : Size.getInstance().getItems()) {
            if (sizeItem.getForOrder() > 0 && !sizeItem.getAddArticles().isEmpty()) {
                for (String article : sizeItem.getAddArticles().split("\\,")) {
                    addPositionToSpec(featureSet, article);
                }
            }
        }
    }

    public void addSupplyFeatureSetPositions(FeatureSet featureSet) {
        for (String article : featureSet.getSupplyPositions().split("\\,")) {
            if (article != null && !article.isEmpty()) addPositionToSpec(featureSet, article);
        }
    }

    private void calcSummaryCostBySpec(FeatureSet featureSet) {
        for (OrderPosition orderPosition : featureSet.getSpecification()) {
            if (orderPosition != null) {
                featureSet.addToSummaryCost(orderPosition.getCost() * orderPosition.getAmount());
            }
        }
    }

    public void addOthersToSpec(FeatureSet featureSet) {
        ArrayList<OrderPosition> otherSpec = Others.getInstance().getOtherSpecification();
        if (otherSpec != null) {
            featureSet.getSpecification().addAll(otherSpec);
        }
    }

    public void setCalcMode(CalcType calcType) {
        this.calcType = calcType;
        AppCore.refreshTables();
    }

    public BooleanProperty calculationStatusProperty() {
        return calculationStatus;
    }

    public void resfreshCalculationStatus() {
        calculationStatus.setValue(!calculationStatus.get());
    }

    public CalcType getCalcType() {
        return calcType;
    }

    public boolean isCalcTypeEquals(CalcType calcType) {
        return this.calcType == calcType;
    }

    public enum CalcType {
        NEW("Новая система", ""),
        EXTENSION("Расширение", ""),
        MIGRATION_SSM("Миграция SSM", STANDARD_MIGRATION_SUFFIX),
        MIGRATION_PSM("Миграция PSM", PRIVILEGED_MIGRATION_SUFFIX),
        ENG("Инженерная лицензия с ключом", "");

        private final String migrationSuffix;
        private final String name;

        CalcType(String name, String migrationSuffix) {
            this.migrationSuffix = migrationSuffix;
            this.name = name;
        }

        public String getMigrationSuffix() {
            return migrationSuffix;
        }

        public String getName() {
            return name;
        }
    }
}
