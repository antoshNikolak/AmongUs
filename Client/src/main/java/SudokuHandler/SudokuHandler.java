package SudokuHandler;

import ConnectionClient.ConnectionClient;
//import Packet.NestedPane.AddSudokuPane;
import Packet.NestedPane.AddSudokuPane;
import Position.Pos;
import Screen.GameScreen;
import Packet.SudokuPacket.VerifySudokuRequest;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import Utils.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SudokuHandler {
    private final HashMap<TextField, Pos> inputPositionMap = new HashMap<>();
    //map text field obj to position in the sudoku matrix

    public SudokuHandler() {}

    public  void addSudokuToScreen(AddSudokuPane packet) {
        GameScreen gameScreen = NestedScreenHandler.createGameScreen(packet);
        drawLines(gameScreen);
        addNumbersAndInput(gameScreen, packet.getSudoku());
        addCheckButton(gameScreen, packet.getSudoku());
    }

    private  void addCheckButton(GameScreen gameScreen, Integer[][] sudoku) {
        //initialise check button
        Button button = new Button("check sudoku");
        button.setLayoutX((gameScreen.getPane().getPrefWidth() / 2) - 60);
        button.setLayoutY(390);
        button.setPrefWidth(120);
        button.setPrefHeight(50);
        button.setOnAction(b -> verifySudoku(sudoku));
        gameScreen.addNode(button);
    }

    public  void verifySudoku(Integer[][] sudoku) {
        plugInUserInputValues(sudoku);//add user input values into the matrix
        ConnectionClient.sendTCP(new VerifySudokuRequest(sudoku));//send sudoku matrix to server for verification
    }

    private  void plugInUserInputValues(Integer[][] sudoku) {
        //insert user input into the corresponding slot of the matrix
        for (Map.Entry<TextField, Pos> entry : inputPositionMap.entrySet()) {
            int input = getUserInput(entry.getKey().getText());
            Pos pos = entry.getValue();
            sudoku[(int) pos.getY()][(int) pos.getX()] = input;
        }
    }

    private int getUserInput(String userInput) {
        try {
            return Integer.parseInt(userInput);
        } catch (NumberFormatException e) {
            return -1;//return -1 for invalid input
        }
    }


    private void addNumbersAndInput(GameScreen gameScreen, Integer[][] sudoku) {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (sudoku[y][x] == 0) {
                    createTextField(gameScreen, x, y);
                } else {
                    addSudokuNumber(gameScreen, sudoku, x, y);
                }
            }
        }
    }


    private static void addSudokuNumber(GameScreen gameScreen, Integer[][] sudoku, int x, int y) {
        Text text = new Text(String.valueOf(sudoku[y][x]));
        text.setX(x * 40 + 17);
        text.setY(y * 40 + 17);
        gameScreen.addNode(text);
    }

    private  void createTextField(GameScreen gameScreen, int x, int y) {
        TextField textField = new TextField();
        textField.setLayoutX(x * 40 + 3);
        textField.setLayoutY(y * 40 + 3);
        textField.setPrefWidth(34);
        textField.setPrefHeight(34);
        gameScreen.addNode(textField);
        inputPositionMap.put(textField, new Pos(x, y));
    }

    private void drawLines(GameScreen gameScreen) {
        for (int i = 40; i <= 320; i += 40) {
            addVerticalLine(gameScreen, i);
            addHorizontalLine(gameScreen, i);
        }
    }

    private  void addVerticalLine(GameScreen gameScreen, int x) {
        Line line = new Line(x, 0, x, 360);
        configLine(gameScreen, line, x);
    }

    private void addHorizontalLine(GameScreen gameScreen, int y) {
        Line line = new Line(0, y, 360, y);
        configLine(gameScreen, line, y);
    }

    private static void configLine(GameScreen gameScreen, Line line, int co_ord) {
        setLineColour(line, co_ord);
        line.setStrokeWidth(3);
        gameScreen.addNode(line);
    }

    private static void setLineColour(Line line, int co_ord) {
        if (co_ord % 120 == 0) {
            line.setStroke(Color.BLACK);
        } else {
            line.setStroke(Color.GRAY.brighter());
        }
    }


    public  void highlightError(Pos error) {
        TextField textField = CollectionUtils.getKey(inputPositionMap, error);
        Objects.requireNonNull(textField).setStyle("-fx-border-color: red");//set textfield border to red
    }

    public  void unhighlightAllErrors() {
        //reset text fields to original style
        for (TextField textField : inputPositionMap.keySet()) {
            textField.setStyle(null);
        }
    }
}
