package Sudoku;


import java.util.*;

public class Sudoku {
    private final Cell[][] cells = new Cell[9][9]; //dimensions of standard sudoku
    private final int cellsToTakeOut;

    public Sudoku(int cellsToTakeOut) {
        this.cellsToTakeOut = cellsToTakeOut;
    }

    private void init() {
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {//change
                cells[y][x] = new Cell(x, y);
            }
        }
    }

    public void start() {
        init();
        createSudoku();
    }


    private void createSudoku() {
        addValue(0, 0);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!checkConstraints(i, j)) {
                    throw new IllegalStateException("Sudoku invalid");
                }
            }

        }
        removeValue();
    }


    private void clearInvisCells() {
        for (Cell cell : emptyCells) {
            cell.setValue(0);
        }
    }

    private void setupInvisCells() {
        for (Cell cell : emptyCells) {
            cell.setValue(0);
            cell.replenishValuesAvailable();
        }
    }

    private int addNewInvisibleCell(Cell cell) {
        int cellOriginalValue = cell.getValue();
        emptyCells.add(cell);
        setupInvisCells();
        return cellOriginalValue;
    }

    private Cell peakEmptyCell() {
        return emptyCells.peek();
    }

    private final Deque<Cell> emptyCells = new LinkedList<>();

    private void removeValue() {
        List<Cell> cellList = getShuffledCellList();
        int counter = 0;
        do {
            Cell cell = cellList.get(counter);
            int cellOriginalValue = addNewInvisibleCell(cell);
            Cell firstCell = peakEmptyCell();
            int solutions = getNumberOfSolutions(new LinkedList<>(emptyCells), 0, firstCell);//runs sudoku solver
            if (solutions > 1) {//put cell back where where it was
                reverseCell(cell, cellOriginalValue);
            } else {
                counter++;
            }
        } while (emptyCells.size() != cellsToTakeOut);
        emptyCells.getLast().setValue(0);
    }


    private void reverseCell(Cell cell, int cellOriginalValue) {
        cell.setValue(cellOriginalValue);
        emptyCells.remove(cell);
    }

    private List<Cell> getShuffledCellList() {
        List<Cell> cellList = new ArrayList<>();
        for (int y = 0; y < 9; y++) {
            cellList.addAll(Arrays.asList(cells[y]).subList(0, 9));
        }
        Collections.shuffle(cellList);
        return cellList;
    }

    private int getNumberOfSolutions(LinkedList<Cell> emptyCells, int possibleSolutions, Cell currentCell) {
        while (true) {
            ArrayList<Integer> valuesAvailable = currentCell.getValuesAvailable();
            if (valuesAvailable.size() == 0) {//all available values have been previously guessed
                currentCell.replenishValuesAvailable();
                currentCell.setValue(0);
                return possibleSolutions;//back track
            }

            int randomItem = valuesAvailable.get(0);//solver doesnt have to be random
            currentCell.setValue(randomItem);
            currentCell.getValuesAvailable().remove(Integer.valueOf(randomItem));//remove this random value from the list so it wont be guessed again

            if (!checkConstraints(currentCell.getY(), currentCell.getX())) {//no duplicated in the same row, column or 3 x 3 square.
                continue;//try again for same cell with different values
            }

            Cell nextCell = emptyCells.poll();
            if (nextCell == null) {//this is the last invisible cell to solve that's in the list
                return possibleSolutions + 1;
            }
            possibleSolutions = getNumberOfSolutions(new LinkedList<>(emptyCells), possibleSolutions, nextCell);
            if (possibleSolutions == 0 || possibleSolutions == 1) {//hasn't been solved keep trying
                continue;//try again
            }
            return possibleSolutions;//sudoku solved, return
        }
    }


    private Cell findRandomVisibleCell() {
        while (true) {
            Random random = new Random();
            int x = random.nextInt(cells.length);
            int y = random.nextInt(cells.length);
            if ((emptyCells.contains(cells[y][x]))) {//check cell is invisible
                continue;
            }
            return cells[y][x];

        }

    }

    private boolean addValue(int y, int x) {
        while (true) {
            Cell currentCell = cells[y][x];
            ArrayList<Integer> valuesAvailable = currentCell.getValuesAvailable();
            if (currentCell.getValuesAvailable().size() == 0) {//no more values are left to guess
                currentCell.replenishValuesAvailable();
                currentCell.setValue(0);
                return false;//backtrack
            }
            int randomItem = valuesAvailable.get(new Random().nextInt(valuesAvailable.size()));
            this.cells[y][x].setValue(randomItem);
            currentCell.getValuesAvailable().remove(Integer.valueOf(randomItem));
            if (!checkConstraints(y, x)) {
                continue;//try again with different value
            }
            if (y == 8 && x == 8) {
                return true;//base case, sudoku has been completed
            }
            if (x == 8) {
                if (!addValue(y + 1, 0)) continue;//recursive call
            } else {
                if (!addValue(y, x + 1)) continue;//recursive call
            }
            return true;//base case, sudoku has been completed
        }
    }

    private boolean checkConstraints(int y, int x) {
        Integer[][] sudoku = transformCellToIntArray(cells);
        return !(checkColumnDuplicates(sudoku, x) ||
                checkRowDuplicates(sudoku, y) ||
                checkGridDuplicates(sudoku, y, x));
    }

    public static boolean checkGridDuplicates(Integer[][] sudoku, int y, int x) {
        ArrayList<Integer> gridValues = new ArrayList<>(9);
        int columnLowerBound = getGridCount(y);
        int columnUpperBound = columnLowerBound + 3;
        int rowLowerBound = getGridCount(x);
        int rowUpperBound = rowLowerBound + 3;

        for (int columnIndex = columnLowerBound; columnIndex < columnUpperBound; columnIndex++) {
            for (int rowIndex = rowLowerBound; rowIndex < rowUpperBound; rowIndex++) {
                int value = sudoku[columnIndex][rowIndex];
                if (value != 0) gridValues.add(value);
            }
        }
        return checkDuplicates(gridValues);
    }


    private static int getGridCount(int num) {
        int multiplier = num / 3;
        return (multiplier) * 3;
    }

    private static Integer[] trimRowArray(Integer[][] sudoku, int rowNum) {//todo remember is swapped
        Integer[] row = new Integer[9];
        System.arraycopy(sudoku[rowNum], 0, row, 0, 9);
        return row;
    }

    private static Integer[] trimColumnArray(Integer[][] sudoku, int colNum) {
        Integer[] col = new Integer[9];
        for (int i = 0; i < sudoku.length; i++) {
            col[i] = sudoku[i][colNum];
        }
        return col;
    }

    private static boolean checkDuplicates(List<Integer> values) {
        Set<Integer> uniqueValues = new HashSet<>(values);
        return uniqueValues.size() < values.size();
    }


    public Integer[][] transformCellToIntArray(Cell[][] cells) {
        Integer[][] sudoku = new Integer[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sudoku[i][j] = cells[i][j].getValue();
            }
        }
        return sudoku;
    }

    public static boolean checkRowDuplicates(Integer[][] sudoku, int row) {
        List<Integer> rowValues = new ArrayList<>(Arrays.asList(trimRowArray(sudoku, row)));
        rowValues.removeIf(value -> value == 0);
        return checkDuplicates(rowValues);
    }

    public static boolean checkColumnDuplicates(Integer[][] sudoku, int column) {
        List<Integer> columnValues = new ArrayList<>(Arrays.asList(trimColumnArray(sudoku, column)));
        columnValues.removeIf(value -> value == 0);
        return checkDuplicates(columnValues);
    }

    public Integer[][] getIntegerMatrix() {
        Integer[][] sudoku = new Integer[9][9];
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                sudoku[y][x] = cells[y][x].getValue();
            }
        }
        return sudoku;
    }
}


