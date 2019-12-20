package order_forms;

import dialogs.Dialogs;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DataLoaderFromExcelToUi {
    private XSSFWorkbook dataSource;

    public DataLoaderFromExcelToUi(OrderFormFactory orderForm) {
        loadData(orderForm);
    }

    private void loadData(OrderFormFactory orderForm) {
        OrderFormItem[] exceptions = new OrderFormItem[]{};
        if ((dataSource = getWorkbook()) != null) {

            XSSFSheet sheet = dataSource.getSheetAt(0);
            XSSFRow row;
            XSSFCell cell = null;

            for (OrderFormItem ofi : orderForm.getItems()) {
                if (orderForm.getUnicalItems().indexOf(ofi) >= 0) continue;
                if ((row = sheet.getRow(ofi.getPosition().getRow())) == null) continue;
                cell = row.getCell(ofi.getPosition().getCol());
                if (cell == null) continue;

                if (cell.getCellTypeEnum().equals(CellType.STRING)) {
                    ofi.displayValue(cell.getStringCellValue());
                } else if (cell.getCellTypeEnum().equals(CellType.NUMERIC)) {
                    ofi.displayValue(Integer.toString((int) cell.getNumericCellValue()));
                }
            }

            try {
                dataSource.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private XSSFWorkbook getWorkbook() {
        File existForm = new Dialogs().openFile();
        if (existForm == null) return null;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(existForm);
            return (XSSFWorkbook) WorkbookFactory.create(fis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
//                e.printStackTrace();
            }
        }

        return null;
    }
}
