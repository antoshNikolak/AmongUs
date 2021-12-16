package Packet.SudokuPacket;

import Packet.Packet;
import Position.Pos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SudokuFailedReturn implements Packet {
//    public Pos duplicate1;
//    public Pos duplicate2;
//
//    public SudokuFailedReturn(Pos duplicate1, Pos duplicate2) {
//        this.duplicate1 = duplicate1;
//        this.duplicate2 = duplicate2;
//    }

    public Set<Pos> errorsFound = new HashSet<>();

    public SudokuFailedReturn(Set<Pos> errorsFound) {
        this.errorsFound = errorsFound;
    }

    private SudokuFailedReturn() {
    }

    //    public SudokuFailedReturn(int xIndexDuplicate, int yIndexDuplicate) {
//        this.xIndexDuplicate = xIndexDuplicate;
//        this.yIndexDuplicate = yIndexDuplicate;
//    }


}
