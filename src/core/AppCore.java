package core;

import additional_positions.Others;
import dialogs.Dialogs;
import excel.ExcelRow;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
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

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppCore {
    private static String buildInfo = "1.2.5 от 30.04.2020";
    private static InputStream appDataIS = AppCore.class.getResourceAsStream("/data.xlsx");
    private static Workbook workbook;
    private static Stage mainStage;
    private static AnchorPane infoPane;
    private static List<Tables> tables = new ArrayList<>();
    private static SpecTables specTables;

    public static void init() {
        openResourceFile();

        Options.getInstance().init(workbook);
        Size.getInstance().init(workbook);
        FeatureSets.getInstance().init(workbook);
        PriceList.getInstance().init(workbook);
        PointMatrix.getInstance().init(workbook);

        Others.getInstance().init(workbook);

        PointPackets.getInstance().init(PriceList.getInstance());
        VersionInfo.getInstance().init(workbook);
        BuildingTypes.getInstance().init(workbook);

        closeResourceFile();

        Calculator.getInstance().calcCosts();
    }

    private static void closeResourceFile() {
        for (Closeable cl : new Closeable[]{workbook, appDataIS}) {
            try {
                cl.close();
            } catch (IOException e) {

            }
        }
    }

    private static void openResourceFile() {
        try {
            workbook = WorkbookFactory.create(appDataIS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addRefreshedTables(Tables... tables) {
        AppCore.tables.addAll(Arrays.asList(tables));
    }

    public static void refreshTables() {
        for (FeatureSet fs : FeatureSets.getInstance().getItems()) {
            fs.setOverLimited(false);
        }

        Calculator.getInstance().calcCosts();

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

    public static void extractPointsFromSpec() {
        File excelSpecFile = new Dialogs().openFile();
        if (excelSpecFile != null && excelSpecFile.exists()) {
            specTables = new SpecTables(excelSpecFile);
            new SpecTablesWindow();
            specTables.display(0);
        }
    }

    public static void addPointsToSpec() {
        for (SizeItem sizeItem : Size.getInstance().getItems()) {
            for (ExcelRow excelRow : specTables.getFoundPoints().getRows()) {
                String currPointType = sizeItem.getPointType();
                String calcPointType = excelRow.getCellValue(SpecTables.POINTS_CALCED_TABLE_POINT_TYPE_COLUMN);

                if (currPointType.equals(calcPointType)) {
                    int forOder = Integer.parseInt(excelRow.getCellValue(SpecTables.POINTS_CALCED_TABLE_POINTS_AMOUNT_COLUMN));
                    sizeItem.setForOrder(sizeItem.getForOrder() + forOder);
                }
            }
        }

//        Calculator.getInstance().calcCosts();
        refreshTables();
    }

    public static void resetOrderDetails() {
        for (SizeItem sizeItem : Size.getInstance().getItems()) {
            sizeItem.setForOrder(0);
        }

        for (Option option : Options.getInstance().getItems()) {
            option.setOrdered(false);
        }

        refreshTables();
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
}
