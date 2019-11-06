package excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelCellStyleFactory {
    private XSSFWorkbook workbook;
    private XSSFCellStyle cellStyle;

    public ExcelCellStyleFactory(XSSFWorkbook workbook) {
        this.workbook = workbook;
        cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeight((short) 200);
        cellStyle.setFont(font);
    }

    public static ExcelCellStyleFactory createStyle(XSSFWorkbook workbook) {
        ExcelCellStyleFactory excelCellStyle = new ExcelCellStyleFactory(workbook);
        return excelCellStyle;
    }

    public XSSFCellStyle get() {
        return cellStyle;
    }

    public ExcelCellStyleFactory copyBorders(CellStyle anotherCellStyle) {
        cellStyle.setBorderBottom(anotherCellStyle.getBorderBottomEnum());
        cellStyle.setBorderLeft(anotherCellStyle.getBorderLeftEnum());
        cellStyle.setBorderRight(anotherCellStyle.getBorderBottomEnum());
        cellStyle.setBorderTop(anotherCellStyle.getBorderBottomEnum());
        return this;
    }

    public ExcelCellStyleFactory setAlignment(HorizontalAlignment alignment) {
        cellStyle.setAlignment(alignment);
        cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
        return this;
    }

    public ExcelCellStyleFactory setCurrencyFormat() {
        DataFormat dataFormat = workbook.createDataFormat();
        cellStyle.setDataFormat(dataFormat.getFormat("# ##0.00"));
        return this;
    }

    public ExcelCellStyleFactory setBold() {
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setBold(true);
        font.setFontHeight((short) 220);
        cellStyle.setFont(font);
        return this;
    }
}
