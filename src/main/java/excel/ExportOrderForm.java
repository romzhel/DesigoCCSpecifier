package excel;

import core.App;
import dialogs.Dialogs;
import order_forms.OrderFormFactory;
import order_forms.OrderFormItem;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import price_list.OrderPosition;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ExportOrderForm {
    private final String PASSWORD = "FiGVam";
    private XSSFWorkbook workbook;

    public ExportOrderForm(OrderFormFactory orderForm, List<OrderPosition> specification, String templateFile) {
        getTemplate(templateFile);
        fillSheet(orderForm, specification);
        saveToExcelFile(orderForm);
        closeWorkbook();
    }

    private void fillSheet(OrderFormFactory orderForm, List<OrderPosition> specification) {
        XSSFSheet sheet = workbook.getSheetAt(0);

        CellDecorator cellDecorator = new CellDecorator(sheet);

        for (OrderFormItem ofi : orderForm.getItems()) {
            cellDecorator.decorate(ofi);
        }

        int rowNum = getTableFirstRowNum(sheet);

        double totalCost = 0D;
        int index = 1;
        for (OrderPosition op : specification) {
            if (op == null || op.getArticle().startsWith("CMD.")) continue;

            cellDecorator.decorate("A" + rowNum, CellStyle.STYLE_CENTER_BORDER_PROTECTED, index++);
            cellDecorator.decorate("B" + rowNum + ":C" + rowNum, CellStyle.STYLE_CENTER_BORDER_PROTECTED, op.getSsn());
            cellDecorator.decorate("D" + rowNum + ":E" + rowNum, CellStyle.STYLE_CENTER_BORDER_PROTECTED, op.getArticle());
            cellDecorator.decorate("F" + rowNum + ":O" + rowNum, CellStyle.STYLE_LEFT_BORDER_PROTECTED, op.getDescriptionRu());
            cellDecorator.decorate("P" + rowNum, CellStyle.STYLE_CENTER_BORDER, op.getAmount());
            cellDecorator.decorate("Q" + rowNum + ":R" + rowNum, CellStyle.STYLE_CURRENCY_BORDER_PROTECTED, op.getCost());
            cellDecorator.decorate("S" + rowNum + ":T" + rowNum, CellStyle.STYLE_CURRENCY_BORDER_PROTECTED, "=P" + rowNum + "*Q" + rowNum);

            rowNum++;
        }

        //total cost
        cellDecorator.decorate("S" + rowNum + ":T" + rowNum, CellStyle.STYLE_CURRENCY_BIG_BOLD_BORDER_PROTECTED, "=SUM(S45:S" + (rowNum - 1) + ")");

        sheet.protectSheet(PASSWORD);
    }

    private void saveToExcelFile(OrderFormFactory orderForm) {
        String fileName = orderForm.getFileNamePart().concat(".xlsx");
        File destinationFile = new Dialogs().saveFile(fileName);
        if (destinationFile != null) {
            try {
                FileOutputStream fos = new FileOutputStream(destinationFile);
                workbook.write(fos);
                fos.close();

                Desktop.getDesktop().open(destinationFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int getTableFirstRowNum(XSSFSheet sheet) {
        int rowNum = 1;
        XSSFCell cell = null;

        do {
            XSSFRow row = sheet.getRow(rowNum++);
            if (row != null) {
                cell = row.getCell(0);
            }
        } while (cell != null && !cell.getStringCellValue().equals("Pos."));
        rowNum++;

        return rowNum;
    }

    private void getTemplate(String templateName) {
        InputStream appDataIS = App.class.getResourceAsStream(templateName);
        try {
            workbook = (XSSFWorkbook) WorkbookFactory.create(appDataIS);
            appDataIS.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    private void closeWorkbook() {
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
