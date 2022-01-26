package excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.*;

public class ExcelCellStyleFactory {
    public final org.apache.poi.ss.usermodel.CellStyle CELL_ALIGN_HLEFT;
    public final org.apache.poi.ss.usermodel.CellStyle CELL_ALIGN_HLEFT_VCENTER;
    public final org.apache.poi.ss.usermodel.CellStyle CELL_ALIGN_HLEFT_VCENTER_WRAP;
    public final org.apache.poi.ss.usermodel.CellStyle CELL_ALIGN_HLEFT_BOLD;
    //    public final CellStyle CELL_ALIGN_HLEFT_WRAP;
    public final org.apache.poi.ss.usermodel.CellStyle CELL_ALIGN_HRIGHT;
    public final org.apache.poi.ss.usermodel.CellStyle CELL_ALIGN_HRIGHT_VCENTER;
    public final org.apache.poi.ss.usermodel.CellStyle CELL_ALIGN_HCENTER;
    public final org.apache.poi.ss.usermodel.CellStyle CELL_ALIGN_HCENTER_BOLD;
    public final org.apache.poi.ss.usermodel.CellStyle CELL_ALIGN_HCENTER_HCENTER;
    public final org.apache.poi.ss.usermodel.CellStyle CELL_CURRENCY_FORMAT;
    public final org.apache.poi.ss.usermodel.CellStyle CELL_CURRENCY_FORMAT_VCENTER;
    public final org.apache.poi.ss.usermodel.CellStyle CELL_ALIGN_HLEFT_BOLD_BROWN;
    public final org.apache.poi.ss.usermodel.CellStyle CELL_ALIGN_HLEFT_BROWN;
    public final org.apache.poi.ss.usermodel.CellStyle CELL_ALIGN_HCENTER_BROWN;
    public final org.apache.poi.ss.usermodel.CellStyle CELL_ALIGN_HCENTER_TEXT_FORMAT;
    public final CellStyle CELL_ALIGN_HLEFT_TEXT_FORMAT;
    public final CellStyle CELL_CURRENCY_FORMAT_CENTER;
    public final CellStyle CELL_CURRENCY_FORMAT_CENTER_BOLD;

    public ExcelCellStyleFactory(Workbook workbook) {
        Font boldFont = getFont(workbook, true, -1);
        Font boldBrownFont = getFont(workbook, true, IndexedColors.BROWN.index);
        Font brownFont = getFont(workbook, false, IndexedColors.BROWN.index);

        CELL_ALIGN_HLEFT = workbook.createCellStyle();
        CELL_ALIGN_HLEFT.setAlignment(HorizontalAlignment.LEFT);

        CELL_ALIGN_HLEFT_BROWN = workbook.createCellStyle();
        CELL_ALIGN_HLEFT_BROWN.setAlignment(HorizontalAlignment.LEFT);
        CELL_ALIGN_HLEFT_BROWN.setFont(brownFont);

        CELL_ALIGN_HLEFT_VCENTER = workbook.createCellStyle();
        CELL_ALIGN_HLEFT_VCENTER.setAlignment(HorizontalAlignment.LEFT);
        CELL_ALIGN_HLEFT_VCENTER.setVerticalAlignment(VerticalAlignment.CENTER);

        CELL_ALIGN_HLEFT_VCENTER_WRAP = workbook.createCellStyle();
        CELL_ALIGN_HLEFT_VCENTER_WRAP.setAlignment(HorizontalAlignment.LEFT);
        CELL_ALIGN_HLEFT_VCENTER_WRAP.setWrapText(true);

        CELL_ALIGN_HLEFT_BOLD = workbook.createCellStyle();
        CELL_ALIGN_HLEFT_BOLD.setAlignment(HorizontalAlignment.LEFT);
        CELL_ALIGN_HLEFT_BOLD.setFont(boldFont);

        CELL_ALIGN_HLEFT_BOLD_BROWN = workbook.createCellStyle();
        CELL_ALIGN_HLEFT_BOLD_BROWN.setAlignment(HorizontalAlignment.LEFT);
        CELL_ALIGN_HLEFT_BOLD_BROWN.setFont(boldBrownFont);

        CELL_ALIGN_HRIGHT = workbook.createCellStyle();
        CELL_ALIGN_HRIGHT.setAlignment(HorizontalAlignment.RIGHT);

        CELL_ALIGN_HRIGHT_VCENTER = workbook.createCellStyle();
        CELL_ALIGN_HRIGHT_VCENTER.setAlignment(HorizontalAlignment.RIGHT);
        CELL_ALIGN_HRIGHT_VCENTER.setVerticalAlignment(VerticalAlignment.CENTER);

        CELL_ALIGN_HCENTER = workbook.createCellStyle();
        CELL_ALIGN_HCENTER.setAlignment(HorizontalAlignment.CENTER);

        CELL_ALIGN_HCENTER_BROWN = workbook.createCellStyle();
        CELL_ALIGN_HCENTER_BROWN.setAlignment(HorizontalAlignment.CENTER);
        CELL_ALIGN_HCENTER_BROWN.setFont(brownFont);

        CELL_ALIGN_HCENTER_HCENTER = workbook.createCellStyle();
        CELL_ALIGN_HCENTER_HCENTER.setAlignment(HorizontalAlignment.CENTER);
        CELL_ALIGN_HCENTER_HCENTER.setVerticalAlignment(VerticalAlignment.CENTER);

        CELL_ALIGN_HCENTER_BOLD = workbook.createCellStyle();
        CELL_ALIGN_HCENTER_BOLD.setAlignment(HorizontalAlignment.CENTER);
        CELL_ALIGN_HCENTER_BOLD.setFont(boldFont);

        CELL_CURRENCY_FORMAT = workbook.createCellStyle();
        DataFormat dataFormat = workbook.createDataFormat();
        CELL_CURRENCY_FORMAT.setDataFormat(dataFormat.getFormat("# ##0.00\\ [$€-x-euro1];[Red]# ##0.00\\ [$€-x-euro1]"));

        CELL_CURRENCY_FORMAT_VCENTER = workbook.createCellStyle();
        CELL_CURRENCY_FORMAT_VCENTER.setDataFormat(dataFormat.getFormat("# ##0.00\\ [$€-x-euro1];[Red]# ##0.00\\ [$€-x-euro1]"));
        CELL_CURRENCY_FORMAT_VCENTER.setVerticalAlignment(VerticalAlignment.CENTER);

        CELL_ALIGN_HCENTER_TEXT_FORMAT = workbook.createCellStyle();
        DataFormat fmt = workbook.createDataFormat();
        CELL_ALIGN_HCENTER_TEXT_FORMAT.setAlignment(HorizontalAlignment.CENTER);
        CELL_ALIGN_HCENTER_TEXT_FORMAT.setDataFormat(fmt.getFormat("@"));

        CELL_ALIGN_HLEFT_TEXT_FORMAT = workbook.createCellStyle();
        fmt = workbook.createDataFormat();
        CELL_ALIGN_HLEFT_TEXT_FORMAT.setAlignment(HorizontalAlignment.LEFT);
        CELL_ALIGN_HLEFT_TEXT_FORMAT.setDataFormat(fmt.getFormat("@"));

        CELL_CURRENCY_FORMAT_CENTER = workbook.createCellStyle();
        CELL_CURRENCY_FORMAT_CENTER.cloneStyleFrom(CELL_CURRENCY_FORMAT_VCENTER);
        CELL_CURRENCY_FORMAT_CENTER.setAlignment(HorizontalAlignment.CENTER);

        CELL_CURRENCY_FORMAT_CENTER_BOLD = workbook.createCellStyle();
        CELL_CURRENCY_FORMAT_CENTER_BOLD.cloneStyleFrom(CELL_CURRENCY_FORMAT_CENTER);
        CELL_CURRENCY_FORMAT_CENTER_BOLD.setFont(boldFont);
    }

    private Font getFont(Workbook workbook, boolean isBold, int indexedColor) {
        Font font = workbook.createFont();
        font.setBold(isBold);

        if (indexedColor > 0) {
            font.setColor((short) indexedColor);
        } else {
            font.setColor(workbook.getFontAt((short) 0).getColor());
        }

        font.setFontName(workbook.getFontAt((short) 0).getFontName());
        font.setFontHeight(workbook.getFontAt((short) 0).getFontHeight());

        return font;
    }
}
