package point_packets;

import core.AppCore;
import price_list.OrderPosition;

import java.util.ArrayList;
import java.util.Collections;

public class PointPacket {
    private String pointType;
    private ArrayList<Packet> packets;

    public PointPacket(String pointType) {
        this.pointType = pointType;
        packets = new ArrayList<>();
    }

    public String getPointType() {
        return pointType;
    }

    public void setPointType(String pointType) {
        this.pointType = pointType;
    }

    public void addPacket(int pointAmount, String article) {
        packets.add(new Packet(pointAmount, article));
        Collections.sort(packets);
    }

    public ArrayList<OrderPosition> getSpecification(int pointAmount) {
        getPacketsAmount(pointAmount);
        optimizePacketsAmount();

        return getOptimizedSpecification();
    }

    private ArrayList<OrderPosition> getOptimizedSpecification() {
        ArrayList<OrderPosition> result = new ArrayList<>();
        for (int index = (packets.size() - 1); index >= 0; index--) {
            Packet packet = packets.get(index);

            if (packet.forOrder != 0) {
                result.add(AppCore.getPriceList().getNewOrderPosition(
                        packet.article.concat(AppCore.getCalculator().getMigrationSuffix()),
                        packet.forOrder));
            }
        }
        return result;
    }

    private void getPacketsAmount(int pointAmount) {
        Packet packet;
        for (int index = packets.size() - 1; index >= 0; index--) {
            packet = packets.get(index);
            packet.forOrder = 0;

            if (pointAmount >= packet.pointAmount) {
                packet.forOrder = pointAmount / packet.pointAmount;
                pointAmount %= packet.pointAmount;
            }

            if (index == 0 && packet.pointAmount >= pointAmount && pointAmount != 0) {
                packet.forOrder += 1;
            }
        }
    }

    private void optimizePacketsAmount() {
        for (int currPacketIndex = 1; currPacketIndex < packets.size(); currPacketIndex++) {
            Packet currentPacket = packets.get(currPacketIndex);
            int currentPacketPoints = currentPacket.pointAmount;
            double currentPacketCost = AppCore.getPriceList().getCost(currentPacket.article);

            int previousPacketsPoints = 0;
            double previousPacketsCosts = 0;
            for (int prevPacketIndex = 0; prevPacketIndex < currPacketIndex; prevPacketIndex++) {
                Packet previousPacket = packets.get(prevPacketIndex);
                previousPacketsPoints += previousPacket.forOrder * previousPacket.pointAmount;
                previousPacketsCosts += previousPacket.forOrder * AppCore.getPriceList().getCost(previousPacket.article);
            }

            if (previousPacketsPoints <= currentPacketPoints && previousPacketsCosts >= currentPacketCost) {
                for (int prevPackIndex = 0; prevPackIndex < currPacketIndex; prevPackIndex++) {
                    packets.get(prevPackIndex).forOrder = 0;
                }
                currentPacket.forOrder += 1;
            }
        }
    }

    private class Packet implements Comparable<Packet> {
        private int pointAmount;
        private String article;
        private int forOrder;

        public Packet(int pointAmount, String article) {
            this.pointAmount = pointAmount;
            this.article = article;
        }

        @Override
        public int compareTo(Packet o) {
            return pointAmount - o.pointAmount;
        }
    }
}
