package excel;

import order_forms.OrderFormItem;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class CellDecorator {
    private XSSFSheet sheet;

    public CellDecorator(XSSFSheet sheet) {
        this.sheet = sheet;
    }

    public void decorate(OrderFormItem ofi) {
        String[] rangeParts = ofi.getPosition().getFullPosition().split("\\:");
        if (getRow(rangeParts[0]) < 0 || getColumn(rangeParts[0]) < 0) return;

        XSSFRow row = sheet.getRow(getRow(rangeParts[0]));
        XSSFCell cell = null;

        if ((cell = row.getCell(getColumn(rangeParts[0]))) == null) {
            cell = row.createCell(getColumn(rangeParts[0]));
        }

        if (ofi.isVisible() && ofi.getValue() != null && !ofi.getValue().isEmpty()) {
            if (ofi.getValue().matches("^\\d+$") && ofi.getValue().length() < 12) {
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(Long.parseLong(ofi.getValue()));
            } else {
                cell.setCellType(CellType.STRING);
                cell.setCellValue(ofi.getValue());
            }
        }

        if (ofi.getCellStyle() != null && !ofi.isHidden()) {
            cell.setCellStyle(ofi.getCellStyle().get(sheet.getWorkbook()));
        }

        if (ofi.getPosition().isInterval()) {
            int start = getColumn(rangeParts[0]) + 1;
            int end = getColumn(rangeParts[1]);

            for (int column = start; column <= end; column++) {

                if ((cell = row.getCell(column)) == null) {
                    cell = row.createCell(column, CellType.STRING);
                }

                if (ofi.getCellStyle() != null && !ofi.isHidden()) {
                    cell.setCellStyle(ofi.getCellStyle().get(sheet.getWorkbook()));
                }
            }

            sheet.addMergedRegion(CellRangeAddress.valueOf(ofi.getPosition().getFullPosition()));
        }
    }

    public void decorate(String cellRange, CellStyle cellStyle, Object value) {
        String[] rangeParts = cellRange.split("\\:");
        if (getRow(rangeParts[0]) < 0 || getColumn(rangeParts[0]) < 0) return;

        XSSFRow row;
        if ((row = sheet.getRow(getRow(rangeParts[0]))) == null) {
            row = sheet.createRow(getRow(rangeParts[0]));
        }

        XSSFCell cell = null;
        CellType cellType = null;

        if (value instanceof String) {
            cell = row.createCell(getColumn(rangeParts[0]), CellType.STRING);
            cellType = CellType.STRING;
            cell.setCellValue((String) value);
        } else if (value instanceof Double) {
            cell = row.createCell(getColumn(rangeParts[0]), CellType.NUMERIC);
            cellType = CellType.NUMERIC;
            cell.setCellValue((Double) value);
        } else if (value instanceof Integer) {
            cell = row.createCell(getColumn(rangeParts[0]), CellType.NUMERIC);
            cellType = CellType.NUMERIC;
            cell.setCellValue((Integer) value);
        }

        cell.setCellStyle(cellStyle.get(sheet.getWorkbook()));

        if (cellRange.contains(":")) {
            int start = getColumn(rangeParts[0]) + 1;
            int end = getColumn(rangeParts[1]);

            for (int column = start; column <= end; column++) {
                if ((cell = row.getCell(column)) == null) {
                    cell = row.createCell(column, cellType);
                }
                cell.setCellStyle(cellStyle.get(sheet.getWorkbook()));
            }

            sheet.addMergedRegion(CellRangeAddress.valueOf(cellRange));
        }
    }

    private int getRow(String position) {
        if (position == null || position.isEmpty()) return -1;
        String rowS = position.replaceAll("\\D", "");
        try {
            return Integer.parseInt(rowS) - 1;
        } catch (Exception e) {
            System.out.println("not correct Integer " + rowS);
        }
        return 0;
    }

    private int getColumn(String position) {
        if (position == null || position.isEmpty()) return -1;
        String intS = position.replaceAll("\\d", "");
        return Math.max(0, (int) intS.charAt(0) - 65);
    }
}
