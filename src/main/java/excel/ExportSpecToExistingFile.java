package excel;

import core.App;
import org.apache.poi.ss.usermodel.*;
import price_list.OrderPosition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ExportSpecToExistingFile {
    private final static int DATA_SHEET_NUMBER = 0;
    private final static int RESERVE_ROWS_AMOUNT = 25;
    private Workbook workbook;
    private Sheet sheet;

    public ExportSpecToExistingFile(ArrayList<OrderPosition> specification, File targetFile) {
        workbook = App.getSpecTables().getWorkbook();
        sheet = workbook.getSheetAt(DATA_SHEET_NUMBER);
        export(specification, targetFile);
    }

    private void export(ArrayList<OrderPosition> specification, File targetFile) {
        SelectedColumns sc = App.getSpecTables().getSelectedColumns();
        int lastUsedCell = findFirstEmptyCell(sc.getIdCol(), 0);

        if (lastUsedCell < 0) {
            System.out.println("error, last used cell row wasn't found");
            return;
        }

        int firstRow = lastUsedCell + 1;
        Row row;
        Cell cell;

        boolean ok = true;
        for (OrderPosition op : specification) {
            if (op != null) {
                try {
                    row = sheet.getRow(firstRow) == null ? sheet.createRow(firstRow) : sheet.getRow(firstRow);

                    cell = row.getCell(sc.getIdCol()) == null ? row.createCell(sc.getIdCol(), CellType.STRING) : row.getCell(sc.getIdCol());
                    cell.setCellValue(op.getArticle());

                    cell = row.getCell(sc.getValCol()) == null ? row.createCell(sc.getValCol(), CellType.NUMERIC) : row.getCell(sc.getValCol());
                    cell.setCellValue(op.getAmount());

                    firstRow++;
                } catch (Exception e) {
                    System.out.println("error writing cell to book, data recording canceled");
                    firstRow++;
                    ok = false;
                }
            }
        }

        if (ok) {
            try {
                if (targetFile == null) System.out.println("return null file");

                FileOutputStream outputStream = new FileOutputStream(targetFile);
                workbook.write(outputStream);
                outputStream.close();

            } catch (FileNotFoundException ef) {
                System.out.println("error - file " + targetFile.getAbsolutePath() + " not found");
                System.out.println("Ошибка записи в файл " + targetFile.getAbsolutePath());
            } catch (IOException eio) {
                System.out.println("IO error");
            }
        }
    }

    private int findFirstEmptyCell(int columnIndex, int initialRow) {
        Row row;
        int emptyRows = 0;
        int lastUsedRow = -1;
        int currentRow = 0;

        int dataRows = App.getSpecTables().getSheetDataSize(DATA_SHEET_NUMBER).getRows();

        for (currentRow = initialRow; currentRow <= (dataRows + RESERVE_ROWS_AMOUNT); currentRow++) {
            row = sheet.getRow(currentRow);

            try {
                Cell cell = row.getCell(columnIndex);

//                System.out.println("row " + currentRow + ", value = " + cell.getStringCellValue());

                if (!cell.getStringCellValue().trim().isEmpty()) {
                    lastUsedRow = currentRow;
                }
            } catch (Exception e) {
                emptyRows++;
            }
        }

//        System.out.println("finding empty and used cells, empty: " + emptyRows + ", current: " + currentRow + ", last " +
//                "used: " + lastUsedRow);

        return lastUsedRow;
    }

}
