package Sudoku;

import java.util.ArrayList;

public class Cell {
    private int value = 0;
    private final ArrayList<Integer> valuesAvailable = new ArrayList<>(9);
    private final int y, x;

    public Cell(int x, int y) {
        this.y = y;
        this.x = x;

        replenishValuesAvailable();
    }

    public void replenishValuesAvailable(){
        for (int i = 1; i <10 ; i++) {
            valuesAvailable.add(i);
        }
    }


    public ArrayList<Integer> getValuesAvailable() {
        return valuesAvailable;
    }


//    @Override
//    public int compareTo(Cell cell) {
//        if (this.row > cell.getRow()) {
//            return -1;
//        } else if (this.row < cell.getRow()) {
//            return 1;
//        } else {//both cells on the same row
//            return Integer.compare(cell.getColumn(), this.column);
//        }
//    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }
}