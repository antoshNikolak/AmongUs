package SudokuHandler;

import ConnectionClient.ConnectionClient;
import Packet.NestedPane.AddVotingPane;
import Position.Pos;
import Screen.GameScreen;
import Screen.ScreenManager;
import Screen.TextureManager;
import Voting.ImpostorVote;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VotingPaneHandler {
//    private final Map<Button, String> buttonTextureMap = new HashMap<>();
    private final Map<String, Button> textureButtonMap = new HashMap<>();

    private GameScreen gameScreen;

    public void showVotes(Map<String, String> playerTextureMap) {
        Map<Button, List<String>> buttonVotesMap = createButtonVotesMap(playerTextureMap);
        for (Map.Entry<Button, List<String>> entry : buttonVotesMap.entrySet()) {
            Button button = entry.getKey();
            int counter = 1;
            for (String texture : entry.getValue()) {
                placeVoterImage(button, texture, counter);
                counter++;
            }
        }
    }

    private void placeVoterImage(Button button, String texture, int counter){
        ImageView imageView = new ImageView(TextureManager.getTexture(texture));
        imageView.setY(button.getLayoutY() + button.getPrefHeight()/3);
        imageView.setX(button.getLayoutX() + button.getPrefWidth() + counter * 30);
        imageView.setFitWidth(28);
        imageView.setFitHeight(40);
        Platform.runLater(() -> gameScreen.addNode(imageView));
    }


    private Map<Button, List<String>> createButtonVotesMap(Map<String, String> playerVoteMap) {//map show what player with what texture voted for what player
        playerVoteMap.entrySet().forEach(System.out::println);
        Map<Button, List<String>> buttonVoterTexturesMap = new HashMap<>();
        for (Map.Entry<String, String> playerVote: playerVoteMap.entrySet()) {
            String voter = playerVote.getKey();
            String suspect = playerVote.getValue();
            Button suspectButton = textureButtonMap.get(suspect);
            //add entry
            if (buttonVoterTexturesMap.containsKey(suspectButton)) {
                buttonVoterTexturesMap.get(suspectButton).add(voter);
            } else {
                List<String> textureList = new ArrayList<>();
//                textureList.add(playerVoteMap.get(voter));
                textureList.add(voter);
                buttonVoterTexturesMap.put(suspectButton, textureList);
            }
        }
//        for (Button button : buttonTextureMap.keySet()) {
//            String buttonTexture = buttonTextureMap.get(button);//button texture = suspect
//            if (playerVoteMap.containsValue(buttonTexture)){
//                String voter = Ut
//            }

//            if (playerVoteMap.containsValue(buttonTexture)) {//change key to value
//                //add entry
//                if (buttonVoterTexturesMap.containsKey(button)) {
//                    buttonVoterTexturesMap.get(button).add(buttonTexture);
//                } else {
//                    List<String> textureList = new ArrayList<>();
//                    textureList.add(playerVoteMap.get(buttonTexture));
//                    textureList.add(buttonTexture);
//                    buttonVoterTexturesMap.put(button, textureList);
//                }
//            }
//        }
        return buttonVoterTexturesMap;
    }

    public void addVotingPane(AddVotingPane packet) {
        this.gameScreen = NestedScreenHandler.createGameScreen(packet);
        int counter = 0;
        for (String texture : packet.getPlayerTextures()) {
            ImageView imageView = createPlayerImageView(texture);
            Pane inButtonPane = createInButtonPane(imageView);
            Button button = createVoteButton(inButtonPane, texture, counter);
            if (!texture.contains("ghost")) {
                button.setText("vote");
                button.setOnAction(event -> ConnectionClient.sendTCP(new ImpostorVote(texture)));
            } else {
                addCrossToPane(button, inButtonPane);
            }
            gameScreen.addNode(button);
            counter++;
        }
    }

    private void addCrossToPane(Button button, Pane inButtonPane) {
        Line line1 = new Line(0, 0, button.getPrefWidth() - 10, button.getPrefHeight() - 10);
        line1.setStroke(Color.RED);
        Line line2 = new Line(button.getPrefWidth() - 10, 0, 0, button.getPrefHeight() - 10);
        line2.setStroke(Color.RED);
        inButtonPane.getChildren().add(line1);
        inButtonPane.getChildren().add(line2);
    }

    private Button createVoteButton(Pane inButtonPane, String texture, int counter) {
        Button button = new Button();
        button.setPrefWidth(90);
        button.setPrefHeight(70);
        button.setGraphic(inButtonPane);
        button.setLayoutX(25 * (1 + Math.floor((double) counter / 3)));
        button.setLayoutY(counter * 70);
        textureButtonMap.put(texture, button);
        return button;
    }

    private ImageView createPlayerImageView(String textureName) {
        ImageView imageView = new ImageView(TextureManager.getTexture(textureName));
        imageView.setX(10);
        imageView.setY(5);
        return imageView;
    }

    private Pane createInButtonPane(ImageView imageView) {
        Pane inButtonPane = new Pane();
        inButtonPane.setPrefWidth(90);
        inButtonPane.setPrefHeight(70);
        inButtonPane.getChildren().add(imageView);
        return inButtonPane;
    }
}
