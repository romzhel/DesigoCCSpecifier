package excel;

import dialogs.Dialogs;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import price_list.OrderPosition;
import tables_data.feature_sets.FeatureSet;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExportSpecToExcel {

    public ExportSpecToExcel(FeatureSet fs) {
        export(fs.getSpecification(), null);
    }

    public ExportSpecToExcel(List<OrderPosition> specification, String fileName) {
        if (specification.stream().anyMatch(orderPosition -> orderPosition != null && orderPosition.getCost() == null)) {
            new Dialogs().showMessage("Экспорт спецификации", "ОБРАТИТЕ ВНИМАНИЕ!!!\n\nТребуется уточнение " +
                    "стоимости некоторых позиций, общая стоимость требует корректировки!");
        }
        export(specification, fileName);
    }

    private void export(List<OrderPosition> specification, String fileName) {
        boolean fileNeedToBeOpened = fileName == null;
        File file = null;
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Desigo CC specification");
        String[] titles = new String[]{"№ п/п", "Заказной номер", "Наименование", "Описание", "Количество", "Стоимость", "ИТОГО"};

        double totalCost = 0.0;
        int rowNum = 0;
        int emptyRows = 0;
        Cell cell;
        Row row;
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        //fill data
        for (OrderPosition op : specification) {

            rowNum++;
            row = sheet.createRow(rowNum);

            if (op == null) {
                emptyRows += 1;
                continue;
            }

            cell = row.createCell(0, CellType.STRING);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(rowNum - emptyRows);

            cell = row.createCell(1, CellType.STRING);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(op.getSsn());

            cell = row.createCell(2, CellType.STRING);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(op.getArticle());

            cell = row.createCell(3, CellType.STRING);
            cell.setCellValue(op.getDescriptionRu());

            cell = row.createCell(4, CellType.NUMERIC);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(op.getAmount());

            if (op.getCost() == null) {
                cell = row.createCell(5, CellType.STRING);
                cell.setCellValue("По запросу");
            } else {
                cell = row.createCell(5, CellType.NUMERIC);
                cell.setCellValue(op.getCost());
            }
            cell.setCellStyle(cellStyle);


            cell = row.createCell(6, CellType.NUMERIC);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(op.getAmount() * op.getCost());

            totalCost += op.getAmount() * op.getCost();
        }

        //total cost
        rowNum++;
        row = sheet.createRow(rowNum);
        cell = row.createCell(6, CellType.NUMERIC);
        cell.setCellValue(totalCost);
        cell.setCellStyle(cellStyle);

        //title row
        row = sheet.createRow(0);
        for (int col = 0; col < titles.length; col++) {
            cell = row.createCell(col, CellType.STRING);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(titles[col]);
            sheet.autoSizeColumn(col);
        }

        //output to file
        SimpleDateFormat template = null;
        FileOutputStream outFile = null;
        try {
            if (fileName == null || fileName.isEmpty()) {
                template = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
                file = new Dialogs().saveFile("DesigoCC_" + template.format(new Date()));
            }

            if (file == null) {
                assert fileName != null;
                file = new File(fileName.replaceAll("[<>:\"/|?*]", "_"));

            }

            outFile = new FileOutputStream(file);
            workbook.write(outFile);

            System.out.println("Created file: " + file.getAbsolutePath());

            workbook.close();
            outFile.close();
        } catch (Exception e) {
            System.out.println("Something wrong.... " + e.getMessage());
        } finally {
            try {
                workbook.close();
                if (outFile != null) outFile.close();
            } catch (IOException eio) {
                System.out.println("error of book or outFile closing");
            }
        }

        if (fileNeedToBeOpened) {
            try {
                Desktop.getDesktop().open(file);
            } catch (Exception e) {
                System.out.println("Can't open created file");
            }
        }
    }
}
