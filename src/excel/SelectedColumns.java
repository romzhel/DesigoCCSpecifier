package excel;

public class SelectedColumns {
    public static final int NOT_DEFINED = -1;
    private int idCol;
    private int valCol;

    public SelectedColumns() {
        idCol = NOT_DEFINED;
        valCol = NOT_DEFINED;
    }

    public SelectedColumns(int idCol, int valCol) {
        this.idCol = idCol;
        this.valCol = valCol;
    }

    public int getIdCol() {
        return idCol;
    }

    public void setIdCol(int idCol) {
        this.idCol = idCol;
    }

    public int getValCol() {
        return valCol;
    }

    public void setValCol(int valCol) {
        this.valCol = valCol;
    }

}
