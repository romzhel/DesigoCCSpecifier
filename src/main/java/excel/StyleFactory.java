package excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class StyleFactory {
    private static XSSFWorkbook workbook;
    private XSSFCellStyle cellStyle;

    public StyleFactory(XSSFWorkbook workbook) {
        this.workbook = workbook;
        cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeight((short) 200);
        cellStyle.setFont(font);
        cellStyle.setLocked(false);
    }

    public static StyleFactory createStyle(XSSFWorkbook workbook) {
        StyleFactory excelCellStyle = new StyleFactory(workbook);
        return excelCellStyle;
    }

    public XSSFCellStyle get() {
        return cellStyle;
    }

    public StyleFactory copyBorders(XSSFCellStyle anotherCellStyle) {
        cellStyle.setBorderBottom(anotherCellStyle.getBorderBottomEnum());
        cellStyle.setBorderLeft(anotherCellStyle.getBorderLeftEnum());
        cellStyle.setBorderRight(anotherCellStyle.getBorderBottomEnum());
        cellStyle.setBorderTop(anotherCellStyle.getBorderBottomEnum());
        return this;
    }

    public StyleFactory setBorders(BorderStyle borderStyle) {
        cellStyle.setBorderBottom(borderStyle);
        cellStyle.setBorderLeft(borderStyle);
        cellStyle.setBorderRight(borderStyle);
        cellStyle.setBorderTop(borderStyle);
        return this;
    }

    public StyleFactory setAlignment(HorizontalAlignment alignment) {
        cellStyle.setAlignment(alignment);
        cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
        return this;
    }

    public StyleFactory setCurrencyFormat() {
        DataFormat dataFormat = workbook.createDataFormat();
        cellStyle.setDataFormat(dataFormat.getFormat("# ##0.00"));
        return this;
    }

    public StyleFactory setFontSize(int size) {
        cellStyle.getFont().setFontHeight((short) size);
        return this;
    }

    public StyleFactory setBold() {
        cellStyle.getFont().setBold(true);
        return this;
    }

    public StyleFactory setBackground() {
        cellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(217, 217, 217)));
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return this;
    }

    public StyleFactory setProtected() {
        cellStyle.setLocked(true);
        return this;
    }
}
