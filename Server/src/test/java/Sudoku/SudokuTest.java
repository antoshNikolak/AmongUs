package Sudoku;

import Position.Pos;
import State.SudokuTaskState;
import Sudoku.Sudoku;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.util.*;

public class SudokuTest {

    public static Integer [][] matrix;

    @BeforeAll
    public static void createSudoku(){
        System.out.println("before each test");
        Sudoku sudoku = new Sudoku(0);
        sudoku.start();
        matrix = sudoku.getIntegerMatrix();
    }

    @Test
    public void testRowDuplicates(){
        for (int y = 0; y < 9; y++) {
            Set<Integer> set = new HashSet<>(Arrays.asList(matrix[y]));
            assertEquals(9, set.size());

        }
    }

    @Test
    public void testColDuplicates(){
        for (int x = 0; x < 9; x++) {
            Set<Integer> set = new HashSet<>();
            for (int y = 0; y < 9; y++) {
                set.add(matrix[y][x]);
            }
            assertEquals(9, set.size());
        }
    }

    @Test
    public void testMiniGridDuplicates(){
        for (int y = 0; y < 9; y+=3) {
            for (int x = 0; x < 9; x+=3) {
                boolean duplicates = Sudoku.checkGridDuplicates(matrix, y, x);
                assertFalse(duplicates);
            }
        }
    }


}
