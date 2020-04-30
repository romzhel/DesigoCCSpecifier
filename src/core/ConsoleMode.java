package core;

import excel.ExcelTable;
import excel.ExportSpecToExcel;
import excel.ExportSpecToExistingFile;
import excel.SelectedColumns;
import tables_data.feature_sets.FeatureSet;
import tables_data.feature_sets.FeatureSets;
import window_spec_and_points_tables.SpecTables;

import java.io.File;

public class ConsoleMode {
    private String[] args;

    public ConsoleMode(String[] args) {
        this.args = args;
    }

    public boolean check() {
        if (args.length == 0) return false;

        File targetFile = new File(args[0]);//file with data
        if (targetFile != null && targetFile.exists()) {
            System.out.println("console mode");

            SpecTables specTables = new SpecTables(targetFile); //создаём экземпляр для работы со спекой
            AppCore.setSpecTables(specTables);
            ExcelTable rawData = specTables.getSheetData(0);                //получаем данные 0 листа


            int articleColIndex = 0;
            int amountColIndex = 5;
            if (args.length > 1) articleColIndex = Integer.parseInt(args[1]);
            if (args.length > 2) amountColIndex = Integer.parseInt(args[2]);

            specTables.setSelectedColumns(new SelectedColumns(articleColIndex, amountColIndex));
            ExcelTable points = specTables.calcPointFromSpec();                     //получаем количество точек
            AppCore.addPointsToSpec();                          //добавляем точки в список для расчётов

            FeatureSet stdFset = FeatureSets.getInstance().getItems().get(FeatureSets.getInstance().getItems().size() - 1);

            if (args.length > 3) {//export result in new file indicated in args[]
                new ExportSpecToExcel(stdFset.getSpecification(), args[3]);
                System.out.println("specification was saved in file " + args[3]);
            } else {//adding to existing file
                new ExportSpecToExistingFile(stdFset.getSpecification(), targetFile);


            }

            /*try {
                Thread.currentThread().wait(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            specTables.closeExcelSpecFile();*/

            return true;
        }

        return false;
    }

}
