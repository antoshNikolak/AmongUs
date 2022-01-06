package SudokuHandler;

import ConnectionClient.ConnectionClient;
import Packet.NestedPane.AddVotingPane;
import Packet.Voting.ChatMessageRequest;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

//public class ChatHandler {
//    private Pane scrollingPane;
//
//    public void start(AddVotingPane packet) {
//        this.chattingScreen = NestedScreenHandler.createGameScreen(packet);
//
//        VBox content = createChatContent(packet);
//        this.scrollingPane = createScrollPane(packet, content);
//        this.chattingScreen.getPane().getChildren().add(createVotingScreenButton(packet));
//
//        TextArea input = createTextArea(scrollingPane);
//
////        TextArea input = new TextArea("Enter input text");
////        this.chattingScreen.getPane().getChildren().add(input);
////        input.setLayoutX(5);
////        input.setLayoutY(scrollingPane.getPrefHeight() + 8);
////        input.setPrefWidth(500);
////        input.setPrefHeight(80);
//        Button inputSender = new Button("send chat");
//        this.chattingScreen.getPane().getChildren().add(inputSender);
//        inputSender.setLayoutX(input.getLayoutX() + input.getPrefWidth() + 5);
//        inputSender.setLayoutY(input.getLayoutY() + input.getPrefHeight() - input.getPrefHeight()/2);
//        inputSender.setOnAction(e ->{
//            ConnectionClient.sendTCP(new ChatMessageRequest(input.getText()));
//            input.clear();
//        });
//
//    }
//}
