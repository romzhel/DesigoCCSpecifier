package core;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.List;

public class VersionInfo {
    private static VersionInfo instance;
    private final String SHEET_NAME = "version";
    private List<String> versions;

    private VersionInfo() {
        versions = new ArrayList<>();
    }

    public static VersionInfo getInstance() {
        if (instance == null) {
            instance = new VersionInfo();
        }
        return instance;
    }

    public void init(Workbook workbook) {
        Sheet sheet = workbook.getSheet(SHEET_NAME);
        versions.add(sheet.getRow(0).getCell(0).getStringCellValue());
        versions.add(sheet.getRow(1).getCell(0).getStringCellValue());
    }

    public String getVersion(int index) {
        return versions.get(index);
    }
}
