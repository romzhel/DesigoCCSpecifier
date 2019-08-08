package core;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;

public class VersionInfo {
    private final String SHEET_NAME = "version";
    private Workbook workbook;
    private ArrayList<String> versions;

    public VersionInfo(Workbook workbook) {
        this.workbook = workbook;
        versions = new ArrayList<>();

        initVersions();
    }

    private ArrayList<String> initVersions() {
        Sheet sheet = workbook.getSheet(SHEET_NAME);
        versions.add(sheet.getRow(0).getCell(0).getStringCellValue());
        versions.add(sheet.getRow(1).getCell(0).getStringCellValue());

        return versions;
    }

    public String getVersion(int index) {
        return versions.get(index);
    }
}
