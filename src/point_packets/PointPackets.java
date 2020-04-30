package point_packets;


import price_list.OrderPosition;
import price_list.PriceList;
import tables_data.size.Size;
import tables_data.size.SizeItem;

import java.util.ArrayList;
import java.util.List;

public class PointPackets {
    private static PointPackets instance;
    private List<PointPacket> pointPackets;

    private PointPackets() {
        pointPackets = new ArrayList<>();
    }

    public static PointPackets getInstance() {
        if (instance == null) {
            instance = new PointPackets();
        }
        return instance;
    }

    public void init(PriceList priceList) {
        String lookingText;
        for (SizeItem sizeItem : Size.getInstance().getItems()) {
            lookingText = sizeItem.getArticlePart();

            if (lookingText.isEmpty() || sizeItem.getPointType().isEmpty()) continue;

            PointPacket newPointPacket = new PointPacket(sizeItem.getPointType());
            for (OrderPosition op : priceList.getItems()) {
                if (op.getArticle().contains("PSM") || op.getArticle().contains("SSM")) continue;

                if (op.getArticle().contains(lookingText)) {

                    String[] articleParts = op.getArticle().split("\\-");

                    if (articleParts.length > 2) {
                        if (articleParts[1].matches("[0-9]+")) {
                            newPointPacket.addPacket(Integer.parseInt(articleParts[1]), op.getArticle());
                        } else if (articleParts[1].equals("MAX")) {
                            newPointPacket.addPacket(999, op.getArticle());
                        } else if (articleParts[2].matches("\\d+.*")) {
                            String packAmountS = articleParts[2].replaceAll("\\D", "");
                            newPointPacket.addPacket(Integer.parseInt(packAmountS), op.getArticle());
                        }
                    }
                }
            }

            pointPackets.add(newPointPacket);
        }
    }

    public ArrayList<OrderPosition> getSpecification(int pointAmount, String pointType) {
        for (PointPacket pointPacket : pointPackets) {
            if (pointPacket.getPointType().equals(pointType)) {
                return pointPacket.getSpecification(pointAmount);
            }
        }
        return null;
    }
}
