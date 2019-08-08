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

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ExportSpecToExcel {

    public ExportSpecToExcel(ArrayList<OrderPosition> specification, String fileName) {
        boolean fileNeedToBeOpened = fileName == null ? true : false;
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

            cell = row.createCell(5, CellType.NUMERIC);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(op.getCost());

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
            if (fileName == null) {
                template = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
                if ((fileName = new Dialogs().textInput("Сохранение файла", "Пожалуйста, " +
                        "введите название файла", "DesigoCC_" + template.format(new Date()))) != null) {

                    if (fileName.length() > 0) fileName.replaceAll("[<>:\"\\/|?*]", "_");
                    else fileName = "DesigoCC_" + template.format(new Date());

                    fileName = fileName.concat(".xls");
                }
            }
            file = new File(fileName);

            outFile = new FileOutputStream(file);
            workbook.write(outFile);

            System.out.println("Created file: " + file.getAbsolutePath());

            workbook.close();
            outFile.close();

        } catch (Exception e) {
            System.out.println("Something wrong.... " + e.getMessage());
        } finally {
            try {
                if (workbook != null) workbook.close();
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
