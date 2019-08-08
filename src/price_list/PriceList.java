package price_list;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class PriceList {
    private final String SHEET_NAME = "price_list";
    private ObservableList<OrderPosition> priceList;

    public PriceList(Workbook workbook) {
        priceList = FXCollections.observableArrayList();

        Sheet sheet = workbook.getSheet(SHEET_NAME);
        Row row = null;
        int rowIndex = 0;

        while ((row = sheet.getRow(++rowIndex)) != null) {
            priceList.add(new OrderPosition(row));
        }
    }

    public ObservableList<OrderPosition> getItems() {
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
