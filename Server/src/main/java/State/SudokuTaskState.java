package State;

import Client.Client;
import ConnectionServer.ConnectionServer;
import Entity.CollidableRect;
import Entity.EntityReturnBuffer;
//import Packet.NestedPane.AddSudokuPane;
import Packet.NestedPane.AddSudokuPane;
import Packet.EntityState.NewEntityState;
import Sudoku.Sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SudokuTaskState extends TaskState {

    private Sudoku sudoku;


    public SudokuTaskState() {
        super();
    }

    public void handleCompletionVerification(Integer[][] sudokuValues) {
        if (checkTaskCompleted(sudokuValues)) {
            super.endState();
            super.incrementTaskBar();
        }
    }

    private boolean checkTaskCompleted(Integer[][] sudokuValues) {

        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                System.out.print(sudokuValues[y][x]);
            }
            System.out.println();
        }
        for (int i = 0; i < 9; i++) {
            if (Sudoku.checkColumnDuplicates(sudokuValues, i)) {
                System.out.println("col duplicate");
//                System.out.println("column duplicate at column "+  i);
//                for (int y = 0; y < 9; y++) {
//                    int val = sudokuValues[y][i];
//                    System.out.println(val+" ");
//                }
                return false;
            }
        }
        for (int i = 0; i < 9; i++) {
            if (Sudoku.checkRowDuplicates(sudokuValues, i)){
                System.out.println("row duplicate");
//                System.out.println("row duplicate at row: "+ i);
//                for (int x = 0; x < 9; x++) {
//                    int val = sudokuValues[i][x];
//                    System.out.println(val+" ");
//                }
                return false;
            }
        }
        for (int i = 0; i < 9; i += 3) {
            for (int j = 0; j < 9; j += 3) {
                if (Sudoku.checkGridDuplicates(sudokuValues, i, j)) {
                    System.out.println("grid duplicate");
//                    System.out.println("grid duplicate at: "+ i + " "+ j);
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void removeClient(Client client) {

    }

    @Override
    public void init() {
        new Thread(()->{

            startSudoku();
            sendClientSudoku();
            System.out.println("sudoku sent to client");
        }).start();
    }

    private void startSudoku() {
        this.sudoku = new Sudoku();
        this.sudoku.start();
    }

    private void sendClientSudoku() {
        ConnectionServer.sendTCP(new AddSudokuPane(sudoku.getIntegerMatrix(), 0, 0, 450, 450), player.getConnectionID());
    }


//    private List<NewEntityState> getNewEntityStateList(List<CollidableRect> collidableRects) {
//        ArrayList<NewEntityState> newEntityStates = new ArrayList<>(EntityReturnBuffer.adaptCollectionToNewLineStates(collidableRects));
//        return newEntityStates;
//    }

    @Override
    protected void startSystems() {

    }
}
