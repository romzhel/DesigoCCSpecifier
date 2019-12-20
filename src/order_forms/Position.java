package order_forms;

public class Position {
    private String fullPosition;
    private int row;
    private int col;
    private boolean interval;

    public Position(String position) {
        fullPosition = position;
        String[] parts = position.split("\\:");

        row = getRow(parts[0]);
        col = getColumn(parts[0]);
        interval = parts.length > 1;
    }

    private int getRow(String position) {
        if (position == null || position.isEmpty()) return -1;
        String rowS = position.replaceAll("\\D", "");
        try {
            return Integer.parseInt(rowS) - 1;
        } catch (Exception e) {
            System.out.println("not correct Integer " + rowS);
        }
        return 0;
    }

    private int getColumn(String position) {
        if (position == null || position.isEmpty()) return -1;
        String intS = position.replaceAll("\\d", "");
        return Math.max(0, (int) intS.charAt(0) - 65);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getFullPosition() {
        return fullPosition;
    }

    public boolean isInterval() {
        return interval;
    }
}
