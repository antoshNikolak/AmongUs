package State;

import AuthorizationServer.AuthorizationServer;
import Client.Client;
import ConnectionServer.ConnectionServer;
//import Packet.NestedPane.AddSudokuPane;
import DataBase.DataBaseUtil;
import Packet.NestedPane.AddSudokuPane;
import Sudoku.Sudoku;
import TimerHandler.StopWatch;
import UserData.UserData;

public class SudokuTaskState extends TaskState {

    private Sudoku sudoku;
    private final StopWatch stopWatch = new StopWatch();


    public SudokuTaskState() {
        super();
    }

    public void handleCompletionVerification(Integer[][] sudokuValues, Client client) {
        if (checkTaskCompleted(sudokuValues)) {
            super.endState();
            super.incrementTaskBar();
            double timeDiffNano = stopWatch.stop();
            System.out.println(timeDiffNano);
            System.out.println(timeDiffNano * 1e-9);
            recordTimeInDataBase(client, timeDiffNano * 1e-9);
            //add to a data base, along with the day this was done
            //We could store the daily result on the server, and at the end of the day when the time is mid night add them to a data base.
            //Weekly, I can plot a graph of these result along with time.
        }
    }

    private void recordTimeInDataBase(Client client, double time) {
        UserData userData = AuthorizationServer.clientUserDataMap.get(client);
        DataBaseUtil.addTimeToSudokuAttempts(userData.getUserName(), time);
    }

    private boolean checkTaskCompleted(Integer[][] sudokuValues) {
        for (int i = 0; i < 9; i++) {
            if (Sudoku.checkColumnDuplicates(sudokuValues, i)) {
                System.out.println("col duplicate");
                System.out.println("column duplicate at column "+  i);
                for (int y = 0; y < 9; y++) {
                    int val = sudokuValues[y][i];
                    System.out.println(val+" ");
                }
                return false;
            }
        }
        for (int i = 0; i < 9; i++) {
            if (Sudoku.checkRowDuplicates(sudokuValues, i)) {
                System.out.println("row duplicate");
                System.out.println("row duplicate at row: "+ i);
                for (int x = 0; x < 9; x++) {
                    int val = sudokuValues[i][x];
                    System.out.println(val+" ");
                }
                return false;
            }
        }
        for (int i = 0; i < 9; i += 3) {
            for (int j = 0; j < 9; j += 3) {
                if (Sudoku.checkGridDuplicates(sudokuValues, i, j)) {
                    System.out.println("grid duplicate");
                    System.out.println("grid duplicate at: "+ i + " "+ j);
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
        new Thread(() -> {
            startSudoku();
            sendClientSudoku();
            stopWatch.start();
            //start stop watch timer
        }).start();
    }

    private void startSudoku() {
        this.sudoku = new Sudoku(5);
        this.sudoku.start();
    }

    private void sendClientSudoku() {
        ConnectionServer.sendTCP(new AddSudokuPane(sudoku.getIntegerMatrix(), 220, 0, 360, 450), player.getConnectionID());
    }


    @Override
    protected void startSystems() {

    }
}
