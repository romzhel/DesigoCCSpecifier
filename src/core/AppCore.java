package core;

import additional_positions.Others;
import dialogs.Dialogs;
import excel.ExcelRow;
import javafx.beans.property.ListProperty;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import point_matrix.PointMatrix;
import point_packets.PointPackets;
import price_list.PriceList;
import tables_data.feature_sets.FeatureSet;
import tables_data.feature_sets.FeatureSets;
import tables_data.options.Option;
import tables_data.options.Options;
import tables_data.size.Size;
import tables_data.size.SizeItem;
import window_spec_and_points_tables.SpecTables;
import window_spec_and_points_tables.SpecTablesWindow;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppCore {
    private static String buildInfo = "1.2.4 от 26.12.2019";
    private static InputStream appDataIS = AppCore.class.getResourceAsStream("/data.xlsx");
    private static Workbook workbook;
    private static Stage mainStage;
    private static AnchorPane infoPane;
    private static Options options;
    private static Size size;
    private static FeatureSets featureSets;
    private static PriceList priceList;
    private static PointMatrix pointMatrix;
    private static PointPackets pointPackets;
    private static Calculator calculator;
    private static ArrayList<Tables> tables = new ArrayList<>();
    private static SpecTables specTables;
    private static Others others;
    private static VersionInfo versions;
    private static List<String> orderFormBuildingTypes = new ArrayList<>();
    //private static ListProperty<Calculation> calculations = new SimpleListProperty<>(FXCollections.observableArrayList());

    public static void init() {
        openResourceFile();

        options = new Options(workbook);
        size = new Size(workbook);
        featureSets = new FeatureSets(workbook);
        priceList = new PriceList(workbook);
        pointMatrix = new PointMatrix(workbook);
        others = new Others(workbook);

        pointPackets = new PointPackets(priceList);
        calculator = new Calculator();
        versions = new VersionInfo(workbook);
        getBuildingTypes();

        closeResourceFile();

        calculator.getCosts();
    }

    private static void getBuildingTypes() {
        Sheet sheet = workbook.getSheet("building_types");
        Row row;
        int rowIndex = 0;

        while ((row = sheet.getRow(rowIndex++)) != null) {
            orderFormBuildingTypes.add(row.getCell(0).getStringCellValue());
        }
    }

    private static void closeResourceFile() {
        try {
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            appDataIS.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void openResourceFile() {
        try {
            workbook = WorkbookFactory.create(appDataIS);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    public static Options getOptions() {
        return options;
    }

    public static Size getSize() {
        return size;
    }

    public static FeatureSets getFeatureSets() {
        return featureSets;
    }

    public static PriceList getPriceList() {
        return priceList;
    }

    public static PointPackets getPointPackets() {
        return pointPackets;
    }

    public static Calculator getCalculator() {
        return calculator;
    }

    public static void addRefreshedTables(Tables... tables) {
        AppCore.tables.addAll(Arrays.asList(tables));
    }

    public static void refreshTables() {
        for (FeatureSet fs : featureSets.getItems()) {
            fs.setOverLimited(false);
        }

        AppCore.getCalculator().getCosts();

        for (Tables table : tables) {
            table.refresh();
        }
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void setMainStage(Stage mainStage) {
        AppCore.mainStage = mainStage;
    }

    public static SpecTables getSpecTables() {
        return specTables;
    }

    public static void setSpecTables(SpecTables specTables) {
        AppCore.specTables = specTables;
    }

    public static String[] getSheetNames() {
        int sheetsCount = workbook.getNumberOfSheets();
        String[] result = new String[sheetsCount];

        for (int sheetIndex = 0; sheetIndex < sheetsCount; sheetIndex++) {
            String sheetName = workbook.getSheetName(sheetIndex);
            result[sheetIndex] = sheetName;
            System.out.println(workbook.getSheetName(sheetIndex));
        }

        return result;
    }

    public static void extractPointsFromSpec() {
        File excelSpecFile = new Dialogs().openFile();
        if (excelSpecFile != null && excelSpecFile.exists()) {
            specTables = new SpecTables(excelSpecFile);
            new SpecTablesWindow();
            specTables.display(0);
        }
    }


    public static PointMatrix getPointMatrix() {
        return pointMatrix;
    }

    public static void addPointsToSpec() {
        for (SizeItem sizeItem : size.getItems()) {
            for (ExcelRow excelRow : specTables.getFoundPoints().getRows()) {
                String currPointType = sizeItem.getPointType();
                String calcPointType = excelRow.getCellValue(SpecTables.POINTS_CALCED_TABLE_POINT_TYPE_COLUMN);

                if (currPointType.equals(calcPointType)) {
                    int forOder = Integer.parseInt(excelRow.getCellValue(SpecTables.POINTS_CALCED_TABLE_POINTS_AMOUNT_COLUMN));
                    sizeItem.setForOrder(sizeItem.getForOrder() + forOder);
                }
            }
        }

        calculator.getCosts();
        refreshTables();
    }

    public static void orderDetails() {
        for (SizeItem sizeItem : size.getItems()) {
            sizeItem.setForOrder(0);
        }

        for (Option option : options.getItems()) {
            option.setOrdered(false);
        }

        refreshTables();
    }

    public static Others getOthers() {
        return others;
    }

    public static VersionInfo getVersions() {
        return versions;
    }

    public static AnchorPane getInfoPane() {
        return infoPane;
    }

    public static void setInfoPane(AnchorPane infoPane) {
        AppCore.infoPane = infoPane;
    }

    public static void resizeView(double width, double height) {
        infoPane.setLayoutY((height - infoPane.getHeight()) / 2);
        infoPane.setLayoutX((width - infoPane.getWidth()) / 2);
    }

    public static String getBuildInfo() {
        return buildInfo;
    }

    public static List<String> getOrderFormBuildingTypes() {
        return orderFormBuildingTypes;
    }
}
