package core;

import dialogs.Dialogs;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import price_list.OrderPosition;
import tables_data.feature_sets.FeatureSet;
import tables_data.options.Option;
import tables_data.size.SizeItem;

import java.util.ArrayList;

public class Calculator {
    private static OrderPosition EMPTY = null;
    private static String STANDART_MIGRATION_SUFFIX = "-SSM";
    private static String PRIVILEGED_MIGRATION_SUFFIX = "-PSM";
    private String errorOrderPosition = "";
    private boolean isSystemExtension;
    private String migrationSuffix = "";


    public void getCosts() {
        for (FeatureSet featureSet : AppCore.getFeatureSets().getItems()) {
            featureSet.setSummaryСost(0);
            featureSet.getSpecification().clear();

            if (isFeatureSetOverlimited(featureSet)) continue;

            if (!isSystemExtension) addFeatureSetToSpec(featureSet);
            addPointPacketsToSpec(featureSet);
            addOptionsToSpec(featureSet);

            if (!isSystemExtension) addSupplyFeatureSetPositions(featureSet);
            if (featureSet.getSpecification().size() > 0) featureSet.addToSpecification(EMPTY);
            addSizeSupplyPositions(featureSet);

            addOthersToSpec(featureSet);

            featureSet.addToSpecification(EMPTY);
            calcSummaryCostBySpec(featureSet);
        }
    }

    private boolean isFeatureSetOverlimited(FeatureSet featureSet) {
        featureSet.checkOverlimited();

        if (!featureSet.isSupportPSM() && migrationSuffix.equals(PRIVILEGED_MIGRATION_SUFFIX))
            featureSet.setOverLimited(true);
        if (!featureSet.isSupportSSM() && migrationSuffix.equals(STANDART_MIGRATION_SUFFIX))
            featureSet.setOverLimited(true);

        if (!featureSet.isDisplayingForExtension() && isSystemExtension) featureSet.setOverLimited(true);

        return featureSet.isOverLimited();
    }

    private void addFeatureSetToSpec(FeatureSet featureSet) {
        featureSet.addToSpecification(AppCore.getPriceList().getNewOrderPosition(
                featureSet.getArticle().concat(migrationSuffix), 1));
    }

    private void addPointPacketsToSpec(FeatureSet featureSet) {
        int sizeItemIndex = 0;
        for (SizeItem sizeItem : AppCore.getSize().getItems()) {
            int includedPoints = featureSet.getPointsIncluded(sizeItemIndex++);
            int orderedPoints = sizeItem.getForOrder();

            if (includedPoints == -1 || (includedPoints >= orderedPoints && !isSystemExtension)) continue;

            int forOrderPoints = isSystemExtension ? orderedPoints : orderedPoints - includedPoints;
            featureSet.addToSpecification(AppCore.getPointPackets().getSpecification(forOrderPoints, sizeItem.getPointType()));
        }
    }

    private void addOptionsToSpec(FeatureSet featureSet) {
        int optionIndex = 0;
        for (Option option : AppCore.getOptions().getItems()) {
            if (option.isOrdered() && featureSet.getOptionAccessibility(optionIndex) == Option.ORDERABLE) {
                if (option.getArticle() != null && !option.getArticle().isEmpty()) {
                    addPositionToSpec(featureSet, option.getArticle());
                }
            }
            optionIndex++;
        }
    }

    private void addPositionToSpec(FeatureSet featureSet, String article) {
        OrderPosition addedOrderPosition = AppCore.getPriceList().getNewOrderPosition(article, 1);

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
        for (SizeItem sizeItem : AppCore.getSize().getItems()) {
            if (sizeItem.getForOrder() > 0 && !sizeItem.getAddArticles().isEmpty()) {
                for (String article : sizeItem.getAddArticles().split("\\,")) {
                    addPositionToSpec(featureSet, article);
                }
            }
        }
    }

    public void addSupplyFeatureSetPositions(FeatureSet featureSet) {
        for (String article : featureSet.getSupplyPositions().split("\\,")) {
            addPositionToSpec(featureSet, article);
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
        ArrayList<OrderPosition> otherSpec = AppCore.getOthers().getOtherSpecification();
        if (otherSpec != null) {
            featureSet.getSpecification().addAll(otherSpec);
        }
    }

    public void setCalcMode(ToggleGroup flags) {
        String id;
        migrationSuffix = "";
        for (Toggle toggle : flags.getToggles()) {
            id = ((RadioMenuItem) toggle).getId();
            if (id.equals("calcSystemExt")) isSystemExtension = toggle.isSelected();
            if (id.equals("calcSSMMigr") && toggle.isSelected()) migrationSuffix = STANDART_MIGRATION_SUFFIX;
            if (id.equals("calcPSMMigr") && toggle.isSelected()) migrationSuffix = PRIVILEGED_MIGRATION_SUFFIX;
        }

        AppCore.refreshTables();
    }

    public String getMigrationSuffix() {
        return migrationSuffix;
    }

    public boolean isSystemExtension() {
        return isSystemExtension;
    }
}
