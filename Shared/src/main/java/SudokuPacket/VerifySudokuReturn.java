package SudokuPacket;

import Packet.Packet;

public class VerifySudokuReturn implements Packet {
    private boolean sudokuComplete;

    public VerifySudokuReturn(boolean sudokuComplete) {
        this.sudokuComplete = sudokuComplete;
    }

    private VerifySudokuReturn() {
    }

    public boolean isSudokuComplete() {
        return sudokuComplete;
    }
}
