package Packet.SudokuPacket;

import Packet.Packet;

public class VerifySudokuReturn implements Packet {
    private boolean sudokuComplete;
    private int yIndexDuplicate;//vertical,
    private int xIndexDuplicate;//horizontal


    public VerifySudokuReturn(boolean sudokuComplete) {
        this.sudokuComplete = sudokuComplete;
    }

    private VerifySudokuReturn() {
    }

    public boolean isSudokuComplete() {
        return sudokuComplete;
    }
}
