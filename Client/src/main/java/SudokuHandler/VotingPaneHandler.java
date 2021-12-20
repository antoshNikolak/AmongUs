package SudokuHandler;

import ConnectionClient.ConnectionClient;
import Packet.NestedPane.AddVotingPane;
import Screen.GameScreen;
import Screen.TextureManager;
import Packet.Voting.ImpostorVote;
import Packet.Voting.VoteOption;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VotingPaneHandler {
    private final Map<VoteOption, Button> suspectButtonMap = new HashMap<>();//maps suspect to button
    private GameScreen gameScreen;


    //code to remove
    public void showVotes(Map<String, VoteOption> voterSuspectMap) {//could be string, vote option
        Map<Button, List<String>> buttonVotesMap = createButtonVotesMap(voterSuspectMap);
        for (Map.Entry<Button, List<String>> entry : buttonVotesMap.entrySet()) {
            Button button = entry.getKey();
            int counter = 0;
            for (String texture : entry.getValue()) {
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
        imageView.setFitHeight(30);
        Platform.runLater(() -> gameScreen.addNode(imageView));
    }


    private Map<Button, List<String>> createButtonVotesMap(Map<String, VoteOption> playerVoteMap) {
        //player vote map show what player with what texture voted for what vote option
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

    public void createVotingPane(AddVotingPane packet) {
        this.gameScreen = NestedScreenHandler.createGameScreen(packet);
        addSkipVoteButton();
        int counter = 0;
        for (Map.Entry<String, String> voteDataEntry : packet.getTextureNameTagMap().entrySet()){
            assembleVoteButton(voteDataEntry.getKey(), voteDataEntry.getValue(), counter);
            counter++;
        }
//        for (int i = 0; i < packet.getTextureNameTagMap().size(); i++) {
//            assembleVoteButton(packet.getTextureNameTagMap().get(i), i);
//        }
    }

    private void assembleVoteButton(String texture, String nameTag, int counter) {
        ImageView imageView = createPlayerImageView(texture);
        Pane inButtonPane = createInButtonPane(imageView, nameTag);
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

    private Button createVoteButton(Pane inButtonPane, String texture, int buttonCounter) {
        Button button = new Button();
        button.setPrefWidth(inButtonPane.getPrefWidth());
        button.setPrefHeight(inButtonPane.getPrefHeight());
        button.setGraphic(inButtonPane);
        final int buttonPerCol = 4;
        button.setLayoutX(350 * (Math.floor((double) buttonCounter / buttonPerCol)));
        button.setLayoutY((buttonCounter%buttonPerCol) * 50);
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
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);

        imageView.setX(10);
        imageView.setY(5);
        return imageView;
    }

    private Pane createInButtonPane(ImageView imageView, String nameTag) {
        Pane inButtonPane = new Pane();
        inButtonPane.setPrefWidth(100);
        inButtonPane.setPrefHeight(50);
        inButtonPane.getChildren().add(imageView);
        imageView.setX(0);
        Text text = new Text(nameTag);
//        text.setFont();
        text.setX(imageView.getX() + imageView.getFitWidth() + 1);
        text.setY(inButtonPane.getPrefHeight()/2);
        inButtonPane.getChildren().add(text);
        return inButtonPane;
    }
}
