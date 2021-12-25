package SudokuHandler;

import ConnectionClient.ConnectionClient;
import Packet.NestedPane.AddVotingPane;
import Packet.NestedPane.AddsPane;
import Packet.Voting.ChatMessage;
import Screen.GameScreen;
import Screen.ScreenManager;
import Screen.TextureManager;
import Packet.Voting.ImpostorVote;
import Packet.Voting.VoteOption;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

public class VotingPaneHandler {
    private final Map<VoteOption, Button> suspectButtonMap = new HashMap<>();//maps suspect to button
    private GameScreen votingScreen;
    private GameScreen chattingScreen;
    private Pane scrollBarContent = new Pane();


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
        imageView.setX(button.getLayoutX() + button.getWidth() + counter * 30 + 7);//maybe add 10
        imageView.setFitWidth(28);
        imageView.setFitHeight(30);
        Platform.runLater(() -> votingScreen.addNode(imageView));
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
        createChattingScreen(packet);
        createVotingScreen(packet);
    }

    private void createChattingScreen(AddVotingPane packet) {
        this.chattingScreen = NestedScreenHandler.createGameScreen(packet);

//        Pane scrollingPane = NestedScreenHandler.createPane(packet);
//        scrollingPane.setPrefHeight(packet.paneHeight - 100);
//        this.chattingScreen.getPane().getChildren().add(scrollingPane);
//        clipChildren(scrollingPane);
        VBox content = createChatContent(packet);
        Pane scrollingPane = createScrollPane(packet, content);
//        VBox content = createChatContent(packet);
//        scrollingPane.getChildren().add(content);
//        scrollingPane.getChildren().add(createScrollBar(packet, content));
        this.chattingScreen.getPane().getChildren().add(createVotingScreenButton(packet));

        TextArea input = new TextArea("Enter input text");
        this.chattingScreen.getPane().getChildren().add(input);
        input.setLayoutX(5);
        input.setLayoutY(scrollingPane.getPrefHeight() + 8);
        input.setPrefWidth(500);
        input.setPrefHeight(80);

        Button inputSender = new Button("send chat");
        this.chattingScreen.getPane().getChildren().add(inputSender);
        inputSender.setLayoutX(input.getLayoutX() + input.getPrefWidth() + 5);
        inputSender.setLayoutY(input.getLayoutY() + input.getPrefHeight() - input.getPrefHeight()/2);
        inputSender.setOnAction(e ->{
            ConnectionClient.sendTCP(new ChatMessage(input.getText()));
            input.clear();
        });
//        this.chattingScreen.getPane().getChildren().add(scrollingPane);
//        this.chattingScreen.getPane().getChildren().add(createBlueSideBackGround(packet));
//        this.chattingScreen.getPane().getChildren().add(content);
//        this.chattingScreen.getPane().getChildren().add(createScrollBar(packet, content));
//        this.chattingScreen.getPane().getChildren().add(createVotingScreenButton(packet));
    }

//    private Pane create

    private Pane createScrollPane(AddVotingPane packet, VBox content){
        Pane scrollingPane = NestedScreenHandler.createPane(packet);
        scrollingPane.setPrefHeight(packet.paneHeight - 100);
        this.chattingScreen.getPane().getChildren().add(scrollingPane);
        clipChildren(scrollingPane);
        scrollingPane.getChildren().add(content);
        scrollingPane.getChildren().add(createScrollBar(packet, content));
        return scrollingPane;
    }

    private void clipChildren(Pane pane){
        Rectangle clip = new Rectangle(pane.getPrefWidth(), pane.getPrefHeight());
        pane.setClip(clip);
    }

    private Rectangle createBlueSideBackGround(AddVotingPane packet){
        Rectangle rectangle = new Rectangle(100, packet.paneHeight);
        rectangle.setX(packet.paneWidth - 100);
        rectangle.setY(0);
        rectangle.setFill(Color.BLUE);
        return rectangle;
    }

    private VBox createChatContent(AddVotingPane packet){
        VBox vBox = new VBox();
//        vBox.setPrefHeight(packet.getPaneHeight()-100);
        vBox.setPrefHeight(packet.paneHeight);
        vBox.setPrefWidth(packet.paneWidth-100);
        for (int i = 0; i < 10; i++) {
            vBox.getChildren().add(new ImageView(TextureManager.getTexture("grey-tile")));
        };
        return vBox;
    }

    private Button createVotingScreenButton(AddVotingPane packet) {
        Button button = new Button("vote");
        button.setLayoutY(packet.paneHeight/2d);
        button.setLayoutX(packet.paneWidth - 50);
        button.setOnAction(e -> {
            ScreenManager.getScreen(GameScreen.class).setNestedScreen(votingScreen);//change to voting screen

        });
        return button;
    }

    private ScrollBar createScrollBar(AddVotingPane packet, VBox content) {
        ScrollBar scrollBar = new ScrollBar();
        scrollBar.setPrefHeight(packet.paneHeight);
        scrollBar.setOrientation(Orientation.VERTICAL);
        scrollBar.setMin(0);
        scrollBar.setMax(50);//this value means how much of an offset it can create     //todo make this value change to the height of stuff there is under the screen

        scrollBar.setValue(0);
        scrollBar.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                content.setLayoutY(-new_val.doubleValue());
            }
        });
        return scrollBar;
    }

    private void createVotingScreen(AddVotingPane packet) {
        this.votingScreen = NestedScreenHandler.createGameScreen(packet);
        addSkipVoteButton();
        addChatRoomButton();
        int counter = 0;
        for (Map.Entry<String, String> voteDataEntry : packet.getTextureNameTagMap().entrySet()) {
            assembleVoteButton(voteDataEntry.getKey(), voteDataEntry.getValue(), counter);
            counter++;
        }
    }

    private void addChatRoomButton() {
        Button button = new Button("Chat");
        button.setLayoutX(600);
        button.setLayoutY(votingScreen.getPane().getPrefHeight() - 50);
        button.setOnAction(e -> {
            ScreenManager.getScreen(GameScreen.class).setNestedScreen(chattingScreen);//change to chat room
        });
        votingScreen.getPane().getChildren().add(button);
    }

    private void assembleVoteButton(String texture, String nameTag, int counter) {
        ImageView imageView = createPlayerImageView(texture);
        Pane inButtonPane = createInButtonPane(imageView, nameTag);
        Button button = createVoteButton(inButtonPane, texture, counter);
        votingScreen.addNode(button);
    }

//    public void addChatText()

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
        button.setLayoutY(votingScreen.getPane().getPrefHeight() - 50);
        button.setOnAction(e -> handleSkipVoteLogic());
        suspectButtonMap.put(VoteOption.SKIP, button);
        votingScreen.addNode(button);
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
        button.setLayoutY((buttonCounter % buttonPerCol) * 50);
        suspectButtonMap.put(getVoteOption(texture), button);
        customiseButton(button, inButtonPane, texture);
        return button;
    }

    private VoteOption getVoteOption(String texture) {
        for (VoteOption voteOption : VoteOption.values()) {
            if (texture.contains(voteOption.getColour())) {
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
        text.setY(inButtonPane.getPrefHeight() / 2);
        inButtonPane.getChildren().add(text);
        return inButtonPane;
    }
}
