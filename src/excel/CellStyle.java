package excel;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import static org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER;
import static org.apache.poi.ss.usermodel.HorizontalAlignment.LEFT;

public enum CellStyle {
    STYLE_LEFT_BOLD {
        public XSSFCellStyle get(XSSFWorkbook workbook) {
            return StyleFactory.createStyle(workbook).setAlignment(LEFT).setBold().setBackground().get();
        }
    },
    STYLE_LEFT {
        public XSSFCellStyle get(XSSFWorkbook workbook) {
            return StyleFactory.createStyle(workbook).setAlignment(LEFT).get();
        }
    },
    STYLE_LEFT_BORDER {
        public XSSFCellStyle get(XSSFWorkbook workbook) {
            return StyleFactory.createStyle(workbook).setAlignment(LEFT).setBorders(BorderStyle.MEDIUM).get();
        }
    },
    STYLE_CENTER_BORDER {
        public XSSFCellStyle get(XSSFWorkbook workbook) {
            return StyleFactory.createStyle(workbook).setAlignment(CENTER).setBorders(BorderStyle.MEDIUM).get();
        }
    },
    STYLE_CURRENCY_BORDER {
        public XSSFCellStyle get(XSSFWorkbook workbook) {
            return StyleFactory.createStyle(workbook).setAlignment(CENTER).setCurrencyFormat().setBorders(BorderStyle.MEDIUM).get();
        }
    },
    STYLE_CURRENCY_BIG_BOLD_BORDER {
        public XSSFCellStyle get(XSSFWorkbook workbook) {
            return StyleFactory.createStyle(workbook).setAlignment(CENTER).setCurrencyFormat().setBorders(BorderStyle.MEDIUM)
                    .setBold().setFontSize(220).get();
        }
    };

    public abstract XSSFCellStyle get(XSSFWorkbook workbook);
}
