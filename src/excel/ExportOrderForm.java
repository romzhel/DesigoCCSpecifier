package excel;

import core.AppCore;
import dialogs.Dialogs;
import order_forms.OrderForm;
import order_forms.OrderFormItem;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import price_list.OrderPosition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ExportOrderForm {
//    private InputStream appDataIS; = AppCore.class.getResourceAsStream("/dcc_licences_order_form.xlsx");
    private XSSFWorkbook workbook;

    public ExportOrderForm(OrderForm orderForm, ArrayList<OrderPosition> specification, String templateFile) {
        getTemplate(templateFile);
        fillSheet(orderForm, specification);
        saveToExcelFile(orderForm);
        closeWorkbook();
    }

    private void fillSheet(OrderForm orderForm, ArrayList<OrderPosition> specification) {
        XSSFSheet sheet = workbook.getSheetAt(0);

        CellDecorator cellDecorator = new CellDecorator(sheet);

        for (OrderFormItem ofi : orderForm.getItems()) {
            cellDecorator.decorate(ofi);
        }

        int rowNum = getTableFirstRowNum(sheet);

        double totalCost = 0D;
        int index = 1;
        for (OrderPosition op : specification) {
            if (op == null || op.getArticle().equals("CMD.04")) continue;

            cellDecorator.decorate("A" + rowNum, CellStyle.STYLE_CENTER_BORDER, index++);
            cellDecorator.decorate("B" + rowNum + ":C" + rowNum, CellStyle.STYLE_CENTER_BORDER, op.getSsn());
            cellDecorator.decorate("D" + rowNum + ":E" + rowNum, CellStyle.STYLE_CENTER_BORDER, op.getArticle());
            cellDecorator.decorate("F" + rowNum + ":O" + rowNum, CellStyle.STYLE_LEFT_BORDER, op.getDescriptionRu());
            cellDecorator.decorate("P" + rowNum, CellStyle.STYLE_CENTER_BORDER, op.getAmount());
            cellDecorator.decorate("Q" + rowNum + ":R" + rowNum, CellStyle.STYLE_CURRENCY_BORDER, op.getCost());
            cellDecorator.decorate("S" + rowNum + ":T" + rowNum, CellStyle.STYLE_CURRENCY_BORDER, op.getAmount() * op.getCost());

            totalCost += op.getAmount() * op.getCost();
            rowNum++;
        }

        //total cost
        cellDecorator.decorate("S" + rowNum + ":T" + rowNum, CellStyle.STYLE_CURRENCY_BIG_BOLD_BORDER, totalCost);
    }

    private void saveToExcelFile(OrderForm orderForm) {
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
        InputStream appDataIS = AppCore.class.getResourceAsStream(templateName);
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
