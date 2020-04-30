package core;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.List;

public class BuildingTypes {
    private static BuildingTypes instance;
    private List<String> items;

    private BuildingTypes() {
        items = new ArrayList<>();
    }

    public static BuildingTypes getInstance() {
        if (instance == null) {
            instance = new BuildingTypes();
        }
        return instance;
    }

    public void init(Workbook workbook) {
        Sheet sheet = workbook.getSheet("building_types");
        Row row;
        int rowIndex = 0;

        while ((row = sheet.getRow(rowIndex++)) != null) {
            items.add(row.getCell(0).getStringCellValue());
        }
    }

    public List<String> getItems() {
        return items;
    }
}
