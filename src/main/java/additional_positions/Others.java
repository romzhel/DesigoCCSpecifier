package additional_positions;


import core.App;
import javafx.scene.control.RadioMenuItem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import price_list.OrderPosition;
import price_list.PriceList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Others {
    private static Others instance;
    private final String SHEET_NAME = "others";
    private List<OthersItem> items;

    private Others() {
        items = new ArrayList<>();
    }

    public static Others getInstance() {
        if (instance == null) {
            instance = new Others();
        }
        return instance;
    }

    public void init(Workbook workbook) {
        Sheet sheet = workbook.getSheet(SHEET_NAME);
        Row row = null;
        int rowIndex = 0;

        while ((row = sheet.getRow(++rowIndex)) != null) {
            OthersItem oi = new OthersItem(row);
            items.add(oi);
        }
    }

    public ArrayList<RadioMenuItem> getMenuItems() {
        ArrayList<RadioMenuItem> result = new ArrayList<>();
        HashSet<String> differentItems = new HashSet<>();
        for (OthersItem oi : items) {
            differentItems.add(oi.getDescription());
        }

        for (String title : differentItems) {
            RadioMenuItem rmi = new RadioMenuItem("     " + title);
            rmi.setSelected(isByDefault(title));
            rmi.selectedProperty().addListener((observable, oldValue, newValue) -> {
                String rmiTitle = rmi.getText().trim();
                for (OthersItem oi : items) {
                    if (oi.getDescription().equals(rmiTitle)) {
                        oi.setOrdered(newValue);
                    }
                }
                App.refreshTables();
            });
            result.add(rmi);
        }
        return result;
    }

    private boolean isByDefault(String title) {
        for (OthersItem oi : items) {
            if (oi.getDescription().equals(title)) {
                return oi.isDefault();
            }
        }

        return false;
    }

    public ArrayList<OrderPosition> getOtherSpecification() {
        ArrayList<OrderPosition> result = new ArrayList<>();
        for (OthersItem oi : items) {
            if (oi.isOrdered()) {
                result.add(PriceList.getInstance().getNewOrderPosition(oi.getArticle(), 1));
            }
        }
        return result;
    }
}
