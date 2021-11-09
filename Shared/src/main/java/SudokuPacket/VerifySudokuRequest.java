package SudokuPacket;

import Packet.Packet;

public class VerifySudokuRequest implements Packet {
    private Integer [][] sudoku;

    public VerifySudokuRequest(Integer [][] sudoku) {
        this.sudoku = sudoku;
    }

    private VerifySudokuRequest() {
    }

    public Integer[][] getSudoku() {
        return sudoku;
    }
}
