package com.example.gamesnake;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class SnakeGame extends Application {
    private final int height = 800;
    private final int width = 800;
    private final BorderPane borderPane = new BorderPane();
    private final Group group = new Group();
    private final Scene scene = new Scene(group, width, height);
    private final Button btn = new Button("Play");
    private GameStage gameStage;

    @Override
    public void start(Stage primaryStage) {
        Image image = new Image("/leto.jpg", true);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        btn.setPrefWidth(200);
        btn.setPrefHeight(50);
        btn.setLayoutX((width / 2 - 200 / 2));
        btn.setLayoutY((height / 2 - 50 / 2));
        borderPane.setCenter(imageView);
        group.getChildren().addAll(borderPane, btn);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Snake");
        primaryStage.setScene(scene);
        primaryStage.show();

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gameStage = new GameStage(height, width);
                gameStage.start();
                gameStage.setRootStage(primaryStage);
                primaryStage.close();
            }
        });
    }
}