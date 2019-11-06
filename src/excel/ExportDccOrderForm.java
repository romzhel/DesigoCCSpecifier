package excel;

import core.AppCore;
import dialogs.Dialogs;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import price_list.OrderPosition;
import tables_data.feature_sets.FeatureSet;
import window_fill_order_form_dcc.OrderForm;
import window_fill_order_form_dcc.OrderFormItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ExportDccOrderForm {
    private InputStream appDataIS = AppCore.class.getResourceAsStream("/dcc_licences_order_form.xlsx");
    private XSSFWorkbook workbook;

    public ExportDccOrderForm(OrderForm orderForm, FeatureSet featureSet) {
        openResourceFile();
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFRow row;
        XSSFCell cell;

        XSSFCellStyle originalCellStyle = sheet.getRow(35).getCell(0).getCellStyle();
        final XSSFCellStyle STYLE_DATA = ExcelCellStyleFactory.createStyle(workbook).copyBorders(originalCellStyle)
                .setAlignment(HorizontalAlignment.LEFT).get();
        final XSSFCellStyle STYLE_LEFT = ExcelCellStyleFactory.createStyle(workbook).copyBorders(originalCellStyle)
                .setAlignment(HorizontalAlignment.LEFT).get();
        final XSSFCellStyle STYLE_CENTER = ExcelCellStyleFactory.createStyle(workbook).copyBorders(originalCellStyle)
                .setAlignment(HorizontalAlignment.CENTER).get();
        final XSSFCellStyle STYLE_CURRENCY = ExcelCellStyleFactory.createStyle(workbook).copyBorders(originalCellStyle)
                .setAlignment(HorizontalAlignment.CENTER).setCurrencyFormat().get();
        final XSSFCellStyle STYLE_CURRENCY_BOLD = ExcelCellStyleFactory.createStyle(workbook).copyBorders(originalCellStyle)
                .setAlignment(HorizontalAlignment.CENTER).setCurrencyFormat().setBold().get();

        if (orderForm.getMigration()) {
            sheet.getRow(20).getCell(8).setCellStyle(STYLE_DATA);
            sheet.getRow(20).getCell(9).setCellStyle(STYLE_DATA);
            sheet.getRow(22).createCell(8).setCellStyle(STYLE_DATA);
            sheet.getRow(22).createCell(9).setCellStyle(STYLE_DATA);
            sheet.addMergedRegion(CellRangeAddress.valueOf("H21:J21"));
            sheet.addMergedRegion(CellRangeAddress.valueOf("H23:J23"));

            XSSFCellStyle cs = sheet.getRow(32).getCell(8).getCellStyle();
            sheet.getRow(20).getCell(5).setCellStyle(cs);
            sheet.getRow(22).getCell(5).setCellStyle(cs);
        }

        for (OrderFormItem ofi : orderForm.getItems()) {
            row = sheet.getRow(ofi.getRow());
            if (row != null) {
                cell = row.getCell(ofi.getColumn());
                if (cell != null) {
                    cell.setCellValue(ofi.getValue());
                    if (!ofi.isHidden()) {
                        cell.setCellStyle(STYLE_DATA);
                    }
                }
            }
        }



        double totalCost = 0;
        int rowNum = 36;
        int index = 1;
        for (OrderPosition op : featureSet.getSpecification()) {
            if (op == null || op.getArticle().equals("CMD.04")) continue;

            row = sheet.createRow(rowNum++);

            sheet.addMergedRegion(CellRangeAddress.valueOf("B" + rowNum + ":C" + rowNum));
            sheet.addMergedRegion(CellRangeAddress.valueOf("D" + rowNum + ":E" + rowNum));
            sheet.addMergedRegion(CellRangeAddress.valueOf("F" + rowNum + ":O" + rowNum));
            sheet.addMergedRegion(CellRangeAddress.valueOf("Q" + rowNum + ":R" + rowNum));
            sheet.addMergedRegion(CellRangeAddress.valueOf("S" + rowNum + ":T" + rowNum));

            for (int i = 0; i < 20; i++) {
                cell = row.createCell(i);
                cell.setCellStyle(STYLE_CENTER);
            }
            cell = row.createCell(0, CellType.STRING);
            cell.setCellStyle(STYLE_CENTER);
            cell.setCellValue(index++);

            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(op.getSsn());
            cell.setCellStyle(STYLE_CENTER);

            cell = row.createCell(3, CellType.STRING);
            cell.setCellValue(op.getArticle());
            cell.setCellStyle(STYLE_CENTER);

            cell = row.createCell(5, CellType.STRING);
            cell.setCellValue(op.getDescriptionRu());
            cell.setCellStyle(STYLE_LEFT);

            cell = row.createCell(15, CellType.NUMERIC);
            cell.setCellValue(op.getAmount());
            cell.setCellStyle(STYLE_CENTER);

            cell = row.createCell(16, CellType.NUMERIC);
            cell.setCellValue(op.getCost());
            cell.setCellStyle(STYLE_CURRENCY);

            cell = row.createCell(18, CellType.NUMERIC);
            cell.setCellStyle(STYLE_CURRENCY);
            cell.setCellValue(op.getAmount() * op.getCost());

            totalCost += op.getAmount() * op.getCost();
        }

        //total cost
        row = sheet.createRow(rowNum++);
        sheet.addMergedRegion(CellRangeAddress.valueOf("S" + rowNum + ":T" + rowNum));

        cell = row.createCell(18, CellType.NUMERIC);
        cell.setCellValue(totalCost);
        cell.setCellStyle(STYLE_CURRENCY_BOLD);

        cell = row.createCell(19, CellType.NUMERIC);
        cell.setCellStyle(STYLE_CURRENCY_BOLD);

        String fileName = orderForm.getFileNamePart().concat(".xlsx");
        File destinationFile = new Dialogs().saveFile(fileName);
        if (destinationFile != null) {
            try {
                FileOutputStream fos = new FileOutputStream(destinationFile);
                workbook.write(fos);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        closeResourceFile();
    }

    private void openResourceFile() {
        try {
            workbook = (XSSFWorkbook) WorkbookFactory.create(appDataIS);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    private void closeResourceFile() {
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
}
