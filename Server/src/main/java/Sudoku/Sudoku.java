package Sudoku;


import java.util.*;

public class Sudoku {
    private final Cell[][] cells = new Cell[9][9]; //dimensions of standard sudoku
    private final int cellsToTakeOut;
    private final int [][] completedSudoku = new int[9][9];

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

    private int addNewEmptyCell(Cell cell) {
        int cellOriginalValue = cell.getValue();
        emptyCells.add(cell);//add cell to queue of empty cells
        setupInvisCells();//replenish values available
        return cellOriginalValue;
    }

    private Cell peakEmptyCell() {
        return emptyCells.peek();
    }

    private final Deque<Cell> emptyCells = new LinkedList<>();

    private void removeValue() {
        List<Cell> cellList = getShuffledCellList();
        int counter = 0;
        while (emptyCells.size() != cellsToTakeOut+1) {
            Cell cell = cellList.get(counter);
            int cellOriginalValue = addNewEmptyCell(cell);
            Cell firstCell = peakEmptyCell();
            int solutions = getNumberOfSolutions(new LinkedList<>(emptyCells), 0, firstCell);//runs sudoku solver
            if (solutions > 1) {
                reverseCell(cell, cellOriginalValue);//put cell back where where it was
            } else {
                counter++;
            }
        }
    }


    private void reverseCell(Cell cell, int cellOriginalValue) {
        cell.setValue(cellOriginalValue);
        emptyCells.remove(cell);
    }

    private List<Cell> getShuffledCellList() {
        List<Cell> cellList = new ArrayList<>();
        for (int y = 0; y < 9; y++) {
            cellList.addAll(Arrays.asList(cells[y]));//add cells row by row
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

            emptyCells.poll();
            Cell nextCell = emptyCells.peek();

            if (nextCell == null) {//this is the last invisible cell to solve that's in the list, solved
                return possibleSolutions + 1;
            }
            possibleSolutions = getNumberOfSolutions(new LinkedList<>(emptyCells), possibleSolutions, nextCell);
            if (possibleSolutions == 0 || possibleSolutions == 1) {//hasn't been solved enough times keep trying
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

    //return true if sudoku completed, false if not completed
    private boolean addValue(int y, int x) {
        while (true) {//loop through until exhausted all values available
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
            return true;//sudoku has been completed
        }
    }

    //return true if all constraints met
    private boolean checkConstraints(int y, int x) {
        Integer[][] sudoku = transformCellToIntArray(cells);//store value of each cell in 2d array
        return !(checkHorizontalDuplicates(sudoku, y) ||
                checkVerticalDuplicates(sudoku, x) ||
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
        return (multiplier) * 3;//return lower bound position;
    }

    public static Integer[] trimVerticalArray(Integer[][] sudoku, int x) {//trim in y direction
        Integer[] row = new Integer[9];
        for (int y = 0; y < 9; y++) {
            row[y] = sudoku[y][x];
        }

//        System.arraycopy(sudoku[x], 0, row, 0, 9);
        return row;
    }

    //trim in x direction
    public static Integer[] trimHorizontalArray(Integer[][] sudoku, int y) {
        Integer[] col = new Integer[9];
        for (int x = 0; x < sudoku.length; x++) {
            col[x] = sudoku[y][x];
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

    public static boolean checkVerticalDuplicates(Integer[][] sudoku, int x) {
        List<Integer> colValues = new ArrayList<>(Arrays.asList(trimVerticalArray(sudoku, x)));
        colValues.removeIf(value -> value == 0);
        return checkDuplicates(colValues);
    }

    public static boolean checkHorizontalDuplicates(Integer[][] sudoku, int y) {//duplicated horizontal
        List<Integer> rowValues = new ArrayList<>(Arrays.asList(trimHorizontalArray(sudoku, y)));
        rowValues.removeIf(value -> value == 0);
        return checkDuplicates(rowValues);
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

    public int[][] getCompletedSudoku() {
        return completedSudoku;
    }
}


