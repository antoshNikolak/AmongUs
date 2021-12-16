package State;

import AuthorizationServer.AuthorizationServer;
import Client.Client;
import ConnectionServer.ConnectionServer;
//import Packet.NestedPane.AddSudokuPane;
import DataBase.DataBaseUtil;
import Packet.NestedPane.AddSudokuPane;
import Position.Pos;
import Sudoku.Sudoku;
import Packet.SudokuPacket.SudokuFailedReturn;
import TimerHandler.StopWatch;
import Packet.UserData.UserData;

import java.util.*;

public class SudokuTaskState extends TaskState {

    private Sudoku sudoku;
    private final StopWatch stopWatch = new StopWatch();


    public SudokuTaskState() {
        super();
    }

    public void handleCompletionVerification(Integer[][] sudokuValues, Client client) {
        if (checkTaskCompleted(sudokuValues, client.getConnectionID())) {
            super.endState();
            super.incrementTaskBar();
            double timeDiffNano = stopWatch.stop();
            recordTimeInDataBase(client, timeDiffNano * 1e-9);
        }
    }

    private void recordTimeInDataBase(Client client, double time) {
        UserData userData = AuthorizationServer.clientUserDataMap.get(client);
        DataBaseUtil.addTimeToSudokuAttempts(userData.getUserName(), time);
    }

    private boolean checkTaskCompleted(Integer[][] sudokuValues, int connectionID) {
        //sudoku values dont match what is on the screen

        Set<Pos> errorsFound = new HashSet<>(getInvalidInputIndexes(sudokuValues));

        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                if (!sudokuValues[y][x].equals(sudoku.getIntegerMatrix()[y][x]) && !sudoku.getIntegerMatrix()[y][x].equals(0)) {
                    throw new IllegalStateException("Sudoku values dont match - pre verification");
                }
            }
        }

        //column duplicate check
        for (int x = 0; x < 9; x++) {
            Map<Integer, Pos> itemIndex = new HashMap<>();
            for (int y = 0; y < 9; y++) {
                processDuplicate(errorsFound, itemIndex, sudokuValues[y][x], x, y);
            }
        }

        //row duplicate check
        for (int y = 0; y < sudokuValues.length; y++) {
            Map<Integer, Pos> itemIndex = new HashMap<>();
            for (int x = 0; x < sudokuValues[y].length; x++) {
                processDuplicate(errorsFound, itemIndex, sudokuValues[y][x], x, y);
            }
        }


        for (int i = 0; i < 9; i++) {//for each mini square
            int startY = (int) Math.floor(i / 3d) * 3;
            Map<Integer, Pos> itemIndex = new HashMap<>();
            for (int y = startY; y < startY + 3; y++) {
                int startX = 3 * (i % 3);
                for (int x = startX; x < startX + 3; x++) {
                    processDuplicate(errorsFound, itemIndex, sudokuValues[y][x], x, y);
//                    int num = sudokuValues[y][x];
//                    if (itemIndex.containsKey(num)) {//duplicate pair found
//                        errorsFound.add(new Pos(x, y));//add pos of current item
//                        errorsFound.add(itemIndex.get(num));//add pos of duplicate pair
//                    }
//                    itemIndex.put(num, new Pos(x, y));
                }
            }
        }
//        for (int i = 0; i < 9; i += 3) {//todo no need for cutting out arrays?
//            for (int j = 0 ; j < 9; j += 3) {//start coords of each grid
//                Integer[][] gridMatrix = new Integer[3][3]; //values of mini grid,
//                //Integer is immutable so no need to wory about shallow copy, or DO I?
//                for (int y = i; y <i +3 ; y++) {
//                    for (int x = j; x < j+3; x++) {
//                        gridMatrix[y-i][x-j] = sudokuValues[y][x];//get a 3 by 3 cut out of the array
//                    }
//                }
//                List <Integer> gridMatrixList = CollectionUtils.getArrayList(gridMatrix);
//                Set<Integer> duplicates = CollectionUtils.findDuplicates(gridMatrixList);
//                for (Integer duplicate: duplicates){
//                    complete = false;
//                    errorsFound.addAll(CollectionUtils.allIndexesOf(gridMatrix, duplicate));
////                    System.out.println("GRID DUPLICATE: "+ duplicate);
//                }
//            }
//        }

        if (!errorsFound.isEmpty()) {
            ConnectionServer.sendTCP(new SudokuFailedReturn(errorsFound), connectionID);
            System.out.println("ERRORS FOUND AT POS: ");
            for (Pos pos: errorsFound){
                System.out.print(pos +" | ");
            }
            return false;
        }
        System.out.println("NO ERRORS FOUND");
        return true;
    }

    public void processDuplicate(Set<Pos> errorsFound, Map<Integer, Pos> itemIndex, int num, int x, int y) {
        if (itemIndex.containsKey(num)) {
            addPosToErrorsFound(errorsFound, new Pos(x, y));
//            errorsFound.add(new Pos(x, y));
            addPosToErrorsFound(errorsFound, itemIndex.get(num));
//            errorsFound.add(itemIndex.get(num));
        }
        itemIndex.put(num, new Pos(x, y));
    }

    private void addPosToErrorsFound(Set<Pos> errorsFound, Pos pos){
        if (sudoku.getIntegerMatrix()[(int)pos.getY()][(int)pos.getX()] == 0){
            errorsFound.add(pos);
        }
    }

    //returns indexes of invalid nums
    public Set<Pos> getInvalidInputIndexes(Integer[][] array) {//sepetate method call
        Set<Pos> errorsFound = new HashSet<>();
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                Integer num = array[y][x];
                if (num < 1 || num > 9) {
                    errorsFound.add(new Pos(x, y));
                }
            }
        }
        return errorsFound;
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
        this.sudoku = new Sudoku(4);
        this.sudoku.start();
    }

    private void sendClientSudoku() {
        ConnectionServer.sendTCP(new AddSudokuPane(sudoku.getIntegerMatrix(), 220, 0, 360, 450), player.getConnectionID());
    }


    @Override
    protected void startSystems() {

    }
}
