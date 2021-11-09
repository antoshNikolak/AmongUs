package Packet.NestedPane;

import Packet.EntityState.NewAnimatedEntityState;
import Packet.NestedPane.AddsPane;
import Packet.NestedPane.NodeInfo;
import Packet.Packet;

import java.util.ArrayList;
import java.util.List;

public class AddSudokuPane implements Packet, AddsPane {
    private Integer [][] sudoku;
    private int paneWidth, paneHeight ,paneX, paneY;

    public AddSudokuPane(Integer[][] sudoku,  int paneX, int paneY, int paneWidth, int paneHeight) {
        this.sudoku = sudoku;
        this.paneWidth = paneWidth;
        this.paneHeight = paneHeight;
        this.paneX = paneX;
        this.paneY = paneY;
    }


    private AddSudokuPane() {
    }

    public int getPaneWidth() {
        return paneWidth;
    }

    public int getPaneHeight() {
        return paneHeight;
    }

    public int getPaneX() {
        return paneX;
    }

    public int getPaneY() {
        return paneY;
    }

    @Override
    public List<NewAnimatedEntityState> getNewEntityStates() {
        return new ArrayList<>();
    }

    @Override
    public List<NodeInfo> getNodes() {
        return new ArrayList<>();
    }

    public Integer[][] getSudoku() {
        return sudoku;
    }
}
