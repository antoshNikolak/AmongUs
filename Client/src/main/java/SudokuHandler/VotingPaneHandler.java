package SudokuHandler;

import ConnectionClient.ConnectionClient;
import Packet.NestedPane.AddVotingPane;
import Packet.Voting.ChatMessageRequest;
import Packet.Voting.ChatMessageReturn;
import Screen.GameScreen;
import Screen.ScreenManager;
import Screen.TextureManager;
import Packet.Voting.ImpostorVote;
import Packet.Voting.VoteOption;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VotingPaneHandler {
    private final Map<VoteOption, Button> suspectButtonMap = new HashMap<>();//maps suspect to button
    private GameScreen votingScreen;
    private ChatHandler chatHandler;
    private GameScreen chattingScreen;
//    private Pane scrollingPane;


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
        //create player vote map show what player with what texture voted for what vote option
        Map<Button, List<String>> buttonVoterTexturesMap = new HashMap<>();
        for (Map.Entry<String, VoteOption> playerVote : playerVoteMap.entrySet()) {
            String voter = playerVote.getKey();
            VoteOption suspect = playerVote.getValue();
            Button suspectButton = suspectButtonMap.get(suspect);
            if (buttonVoterTexturesMap.containsKey(suspectButton)) {//check if entry exists
                buttonVoterTexturesMap.get(suspectButton).add(voter);
                //update existing entry
            } else {//add new entry
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
        this.chatHandler = new ChatHandler();
        this.chatHandler.start(packet);
    }


    private Rectangle createBlueSideBackGround(AddVotingPane packet) {
        Rectangle rectangle = new Rectangle(100, packet.paneHeight);
        rectangle.setX(packet.paneWidth - 100);
        rectangle.setY(0);
        rectangle.setFill(Color.BLUE);
        return rectangle;
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

    public void handleChat(ChatMessageReturn chat) {
        this.chatHandler.receiveChatMessage(chat);
    }

    public class ChatHandler {
        private VBox content;
        private Pane speechBubble;
        private ScrollBar scrollBar;
        private Pane scrollingPane;
        private int speechBubbleCounter = 0;
//        private int numOfMessages ;

        public void start(AddVotingPane packet) {
            chattingScreen = NestedScreenHandler.createGameScreen(packet);
            this.content = createChatContent(packet);
            Pane scrollingPane = createScrollPane(packet, content);
            chattingScreen.getPane().getChildren().add(createVotingScreenButton(packet));

            TextArea input = createTextArea(scrollingPane);
            createInputSenderButton(input);

//            Button inputSender = new Button("send chat");
//            chattingScreen.getPane().getChildren().add(inputSender);
//            inputSender.setLayoutX(input.getLayoutX() + input.getPrefWidth() + 5);
//            inputSender.setLayoutY(input.getLayoutY() + input.getPrefHeight() - input.getPrefHeight() / 2);
//            inputSender.setOnAction(e -> {
//                final int maxChar = 156;
//                String message = input.getText();
//                if (input.getText().length() > maxChar) {
//                    message = input.getText().substring(0, maxChar + 1);
//                }
//                input.replaceText(0, Math.min(maxChar + 1, message.length()), "");
//                ConnectionClient.sendTCP(new ChatMessageRequest(message));
//            });
        }

        private void createInputSenderButton(TextArea input){
            Button inputSender = new Button("send chat");
            chattingScreen.getPane().getChildren().add(inputSender);
            inputSender.setLayoutX(input.getLayoutX() + input.getPrefWidth() + 5);
            inputSender.setLayoutY(input.getLayoutY() + input.getPrefHeight() - input.getPrefHeight() / 2);
            inputSender.setOnAction(e -> { sendInput(input);

                //                String message = input.getText();
//                final int maxChar = getMaxNumOfChars(message);
//                if (message.length() > maxChar) {
//                    message = message.substring(0, maxChar + 1);
//                }
//                input.replaceText(0, Math.min(maxChar + 1, message.length()), "");
//                ConnectionClient.sendTCP(new ChatMessageRequest(message));
            });
        }

        private void sendInput(TextArea input){
            String message = input.getText();
            if (message.isEmpty()){ return; }//nothing to send
            final int maxChar = getMaxNumOfChars(message);
            if (message.length() > maxChar) {
                message = message.substring(0, maxChar);//send only part of message that fits         //todo +1 -> 0
            }
//            input.replaceText(0, Math.min(maxChar, message.length())+1, "");//remove text from text area
//            input.replaceText(0, Math.min(maxChar, message.length()), "");//remove text from text area
            input.replaceText(0, message.length(), "");//remove text from text area


            ConnectionClient.sendTCP(new ChatMessageRequest(message));
        }

        private int getMaxNumOfChars(String message){
                int charsOnTopLine = getCharsPerLine(message);
                String bottomLine = message.substring(charsOnTopLine);
                int charsOnBottomLine = getCharsPerLine(bottomLine);
                return charsOnBottomLine + charsOnTopLine;
        }

        private TextArea createTextArea(Pane scrollingPane) {
            TextArea input = new TextArea("Enter input text");
            chattingScreen.getPane().getChildren().add(input);
            input.setLayoutX(5);
            input.setLayoutY(scrollingPane.getPrefHeight() + 8);
            input.setPrefWidth(500);
            input.setPrefHeight(80);
            return input;
        }

        private VBox createChatContent(AddVotingPane packet) {
            VBox vBox = new VBox();
            vBox.setLayoutX(25);
            vBox.setLayoutY(5);
//            vBox.setPrefHeight(packet.paneHeight);
            vBox.setPrefWidth(packet.paneWidth - 75);
//            vBox.setPadding(new Insets(10, 0, 50, ));

            vBox.setSpacing(20);
            return vBox;
        }

        private Pane createScrollPane(AddVotingPane packet, VBox content) {
            scrollingPane = NestedScreenHandler.createPane(packet);
            scrollingPane.setPrefHeight(packet.paneHeight - 100);
            chattingScreen.getPane().getChildren().add(scrollingPane);
            clipChildren(scrollingPane);
            scrollingPane.getChildren().add(content);
            createScrollBar(scrollingPane, content);
            return scrollingPane;
        }

        private void createScrollBar(Pane scrollPane, VBox content) {
            scrollBar = new ScrollBar();
            scrollBar.setPrefHeight(scrollPane.getPrefHeight());
            scrollBar.setOrientation(Orientation.VERTICAL);
            scrollBar.setMin(0);
            scrollBar.setMax(0);//this value means how much of an offset it can create
            scrollBar.valueProperty().addListener((ov, old_val, new_val) -> content.setLayoutY(-new_val.doubleValue()));
            scrollPane.getChildren().add(scrollBar);
        }

        private Button createVotingScreenButton(AddVotingPane packet) {
            Button button = new Button("vote");
            button.setLayoutY(packet.paneHeight / 2d);
            button.setLayoutX(packet.paneWidth - 50);
            button.setOnAction(e -> {
                ScreenManager.getScreen(GameScreen.class).setNestedScreen(votingScreen);//change to voting screen

            });
            return button;
        }

        private void clipChildren(Pane pane) {
            Rectangle clip = new Rectangle(pane.getPrefWidth(), pane.getPrefHeight());
            pane.setClip(clip);
        }

        public void receiveChatMessage(ChatMessageReturn message) {
//            Rectangle rectangle = new Rectangle(this.content.getPrefWidth() - 50, 50);
//            rectangle.setLayoutY(30);
//            rectangle.setStroke(Color.GRAY);
//            rectangle.setFill(null);
            Rectangle borderRect = createBorderRect();
            createSpeechBubble(borderRect);

//            this.speechBubble = new Pane();
//            speechBubble.setPrefWidth(rectangle.getWidth());
//            speechBubble.setPrefHeight(rectangle.getHeight() + rectangle.getLayoutY());

//            ImageView player = new ImageView(TextureManager.getTexture(message.texture));
//            player.setPreserveRatio(true);
//            player.setFitHeight(23);
            ImageView player = createSpeechPlayerImageView(message.texture);

//            Label nameTag = new Label(message.username);
//            nameTag.setLayoutX(player.prefWidth(-1) + player.getLayoutX() + 3);
//            nameTag.setFont(Font.font(15));
            Label nameTag = createNameTag(player, message.username);

//            Text chat = new Text(insertSpacings(message.message));
//            chat.setFont(Font.font(15));
//            chat.setY(15 + rectangle.getLayoutY());
            Text chat = createMessageText(borderRect, message.message);


            speechBubble.getChildren().addAll(borderRect, chat, player, nameTag);
            Platform.runLater(() -> this.content.getChildren().add(speechBubble));
            this.speechBubbleCounter++;
            scrollBar.setMax(speechBubbleCounter * (speechBubble.getPrefHeight() + content.getSpacing()) - scrollingPane.getPrefHeight());
        }

        private Text createMessageText(Rectangle borderRect, String message) {
            Text chat = new Text(insertSpacings(message));
            chat.setFont(Font.font(15));
            chat.setY(15 + borderRect.getLayoutY());
            return chat;
        }

        private Label createNameTag(ImageView player, String username) {
            Label nameTag = new Label(username);
            nameTag.setLayoutX(player.prefWidth(-1) + player.getLayoutX() + 3);
            nameTag.setFont(Font.font(15));
            return nameTag;
        }

        private ImageView createSpeechPlayerImageView(String texture) {
            ImageView player = new ImageView(TextureManager.getTexture(texture));
            player.setPreserveRatio(true);
            player.setFitHeight(23);
            return player;
        }

        private Rectangle createBorderRect(){
            Rectangle rectangle = new Rectangle(this.content.getPrefWidth() - 50, 50);
            rectangle.setLayoutY(30);
            rectangle.setStroke(Color.GRAY);
            rectangle.setFill(null);
            return rectangle;
        }

        private void createSpeechBubble(Rectangle borderRect){
            this.speechBubble = new Pane();
            speechBubble.setPrefWidth(borderRect.getWidth());
            speechBubble.setPrefHeight(borderRect.getHeight() + borderRect.getLayoutY());
        }

        //inserts spacing to split string into 2 lines
        private String insertSpacings(String message) {
            StringBuilder sb = new StringBuilder(message);//string builder more efficient as its mutable, unlike string
            int charsPerLine = getCharsPerLine(message);
            sb.insert(charsPerLine, "\n");// \n starts a new line, output will string will go to next line
            return sb.toString();
        }

        private int getCharsPerLine(String message){
            for (int i = 50; i < message.length(); i++) {
                Text text = new Text(message.substring(0, i+1));
                if (text.getLayoutBounds().getWidth() >= speechBubble.getPrefWidth()-150) {//speech bubble width
                    return i;//return num of chars that fit onto top line
                }
            }
            return message.length();
        }
    }
}
