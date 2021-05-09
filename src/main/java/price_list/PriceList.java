package price_list;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.List;

public class PriceList {
    private static PriceList instance;
    private final String SHEET_NAME = "price_list";
    private List<OrderPosition> priceList;

    public PriceList() {
        priceList = new ArrayList<>();
    }

    public static PriceList getInstance() {
        if (instance == null) {
            instance = new PriceList();
        }
        return instance;
    }

    public void init(Workbook workbook) {
        Sheet sheet = workbook.getSheet(SHEET_NAME);
        Row row = null;
        int rowIndex = 0;

        while ((row = sheet.getRow(++rowIndex)) != null) {
            priceList.add(new OrderPosition(row));
        }
    }

    public List<OrderPosition> getItems() {
        return priceList;
    }

    public double getCost(String article) {
        for (OrderPosition orderPosition : priceList) {
            if (orderPosition.getArticle().equals(article)) {
                return orderPosition.getCost();
            }
        }
        return 0;
    }

    public OrderPosition getOrderPosition(String article) {
        for (OrderPosition orderPosition : priceList) {
            if (orderPosition.getArticle().equals(article)) {
                return orderPosition;
            }
        }
        return null;
    }

    public OrderPosition getNewOrderPosition(String article) {
        return new OrderPosition(getOrderPosition(article), 0);
    }

    public OrderPosition getNewOrderPosition(String article, int amount) {
        OrderPosition copiedOrderPosition = getOrderPosition(article);
        if (copiedOrderPosition == null) {
            System.out.println("can't find " + article + " in price");
            return null;
        }
        return new OrderPosition(copiedOrderPosition, amount);
    }
}
