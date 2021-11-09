package SudokuHandler;

import ConnectionClient.ConnectionClient;
//import Packet.NestedPane.AddSudokuPane;
import Packet.NestedPane.AddSudokuPane;
import Position.Pos;
import Screen.GameScreen;
import SudokuPacket.VerifySudokuRequest;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.HashMap;

public class SudokuHandler {//todo remove static
    private static HashMap<TextField, Pos> inputPositionMap = new HashMap<>();

    public static void addSudokuToScreen(AddSudokuPane packet) {
        GameScreen gameScreen = NestedScreenHandler.createGameScreen(packet);
//        addWhiteBackground(gameScreen);
        drawLines(gameScreen);
        addNumbersAndInput(gameScreen, packet.getSudoku());
        addCheckButton(gameScreen, packet.getSudoku());
    }

    private static void addCheckButton(GameScreen gameScreen, Integer[][] sudoku) {
        Button button = new Button("check sudoku");
        button.setLayoutX(400);
        button.setLayoutY(180);
        button.setOnAction(b -> verifySudoku(sudoku));
        gameScreen.addNode(button);
    }

    public static void verifySudoku(Integer[][] sudoku) {
        Integer[][] sudokuCopy = Arrays.stream(sudoku).map(Integer[]::clone).toArray(Integer[][]::new);
        boolean sudokuCompleted = completeSudokuWithUserInputs(sudokuCopy);
        if (sudokuCompleted) {
            ConnectionClient.sendTCP(new VerifySudokuRequest(sudokuCopy));
        }
    }

    private static boolean completeSudokuWithUserInputs(Integer[][] sudoku) {
        for (TextField textField : inputPositionMap.keySet()) {
            Pos pos = inputPositionMap.get(textField);
            try {
                sudoku[(int) pos.getX()][(int) pos.getY()] = Integer.parseInt(textField.getText());
            } catch (NumberFormatException e) {
//                sudoku[(int) pos.getX()][(int) pos.getY()] = 0;
                return false; //if text field is empty, or not a number, false is returned
            }
        }
        return true;
    }

    private static void addNumbersAndInput(GameScreen gameScreen, Integer[][] sudoku) {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (sudoku[y][x] == 0) {
                    createTextField(gameScreen, x, y);
                } else {
                    addSudokuNumber(gameScreen, sudoku, x, y);
                    Text text = new Text(String.valueOf(sudoku[y][x]));
                    text.setX(x * 40 + 17);
                    text.setY(y * 40 + 17);
                    gameScreen.addNode(text);
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

    private static void createTextField(GameScreen gameScreen, int x, int y) {
        TextField textField = new TextField();
        textField.setLayoutX(x * 40 + 3);
        textField.setLayoutY(y * 40 + 3);
        textField.setPrefWidth(34);
        textField.setPrefHeight(34);
        gameScreen.addNode(textField);
        inputPositionMap.put(textField, new Pos(x, y));
    }

    private static void drawLines(GameScreen gameScreen) {
        for (int i = 40; i <= 320; i += 40) {
            addVerticalLine(gameScreen, i);
            addHorizontalLine(gameScreen, i);
        }
    }

    private static void addVerticalLine(GameScreen gameScreen, int x) {
        Line line = new Line(x, 0, x, 360);
        configLine(gameScreen, line, x);
    }

    private static void addHorizontalLine(GameScreen gameScreen, int y) {
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

    private static void addWhiteBackground(GameScreen gameScreen) {
        Rectangle rectangle = new Rectangle(0, 0, gameScreen.getPane().getPrefWidth(), gameScreen.getPane().getPrefHeight());
        rectangle.setFill(Color.WHITE);
        gameScreen.addNode(rectangle);
    }
}
