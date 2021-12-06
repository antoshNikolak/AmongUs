package SudokuHandler;

import ConnectionClient.ConnectionClient;
import Packet.NestedPane.AddVotingPane;
import Screen.GameScreen;
import Screen.TextureManager;
import Voting.ImpostorVote;
import Voting.VoteOption;
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
    private final Map<VoteOption, Button> suspectButtonMap = new HashMap<>();//maps candidate to button
    private GameScreen gameScreen;


    //code to remove
    public void showVotes(Map<String, VoteOption> voterSuspectMap) {//could be string, vote option
        Map<Button, List<String>> buttonVotesMap = createButtonVotesMap(voterSuspectMap);
        for (Map.Entry<Button, List<String>> entry : buttonVotesMap.entrySet()) {
            Button button = entry.getKey();
            System.out.println("button: "+button);
            int counter = 0;
            for (String texture : entry.getValue()) {
//                System.out.println("texture: "+ texture);
                placeVoterImage(button, texture, counter);
                counter++;
            }
        }
    }

    private void placeVoterImage(Button button, String texture, int counter) {
        ImageView imageView = new ImageView(TextureManager.getTexture(texture));
        imageView.setY(button.getLayoutY() + button.getHeight() / 3);
        imageView.setX(button.getLayoutX() + button.getWidth() + counter * 30+ 7);//maybe add 10
        imageView.setFitWidth(28);
        imageView.setFitHeight(40);
        Platform.runLater(() -> gameScreen.addNode(imageView));
    }


    //map show what player with what texture voted for what player
    private Map<Button, List<String>> createButtonVotesMap(Map<String, VoteOption> playerVoteMap) {//string vote option
//        playerVoteMap.entrySet().forEach(System.out::println);
        Map<Button, List<String>> buttonVoterTexturesMap = new HashMap<>();
        for (Map.Entry<String, VoteOption> playerVote : playerVoteMap.entrySet()) {
            String voter = playerVote.getKey();
            VoteOption suspect = playerVote.getValue();
            Button suspectButton = suspectButtonMap.get(suspect);
            //add entry
            if (buttonVoterTexturesMap.containsKey(suspectButton)) {
                buttonVoterTexturesMap.get(suspectButton).add(voter);
            } else {
                List<String> textureList = new ArrayList<>();
                textureList.add(voter);
                buttonVoterTexturesMap.put(suspectButton, textureList);
            }
        }
        return buttonVoterTexturesMap;
    }
    //todo clear up terms candidate and suspect

    //code to add voting
    public void addVotingPane(AddVotingPane packet) {
        this.gameScreen = NestedScreenHandler.createGameScreen(packet);
        addSkipVoteButton();
        for (int i = 0; i < packet.getPlayerTextures().size(); i++) {
            assembleVoteButton(packet.getPlayerTextures().get(i), i);
        }
    }

    private void assembleVoteButton(String texture, int counter) {
        ImageView imageView = createPlayerImageView(texture);
        Pane inButtonPane = createInButtonPane(imageView);
        Button button = createVoteButton(inButtonPane, texture, counter);
        gameScreen.addNode(button);
    }

    private void customiseButton(Button button, Pane inButtonPane, String texture) {
        if (!texture.contains("ghost")) {
            button.setText("vote");
            button.setOnAction(event -> ConnectionClient.sendTCP(new ImpostorVote(getVoteOption(texture))));
        } else {
            addCrossToPane(button, inButtonPane);
        }

    }







//    private VoteOption getVoteOption(String playerTexture) {
//        for (VoteOption voteOption : VoteOption.values()) {
//            if (playerTexture.contains(voteOption.getColour())) {
//                return voteOption;
//            }
//        }
//        throw new IllegalStateException("player texture invalid");
//    }

    private void addSkipVoteButton() {
        Button button = new Button("skip vote");
        button.setLayoutX(20);
        button.setLayoutY(gameScreen.getPane().getPrefHeight() - 50);
        button.setOnAction(e -> handleSkipVoteLogic());
        suspectButtonMap.put(VoteOption.SKIP, button);
        gameScreen.addNode(button);
    }

    private void handleSkipVoteLogic() {
        ConnectionClient.sendTCP(new ImpostorVote(VoteOption.SKIP));
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
        suspectButtonMap.put(getVoteOption(texture), button);
        customiseButton(button, inButtonPane, texture);
        return button;
    }

    private VoteOption getVoteOption(String texture){
        for (VoteOption voteOption: VoteOption.values()){
            if (texture.contains(voteOption.getColour())){
                return voteOption;
            }
        }
        return VoteOption.SKIP;
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
