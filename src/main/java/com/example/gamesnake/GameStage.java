package com.example.gamesnake;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameStage {

    private final int width;
    private final int height;
    private final int rows = 20;
    private final int columns = rows;
    private final int squareSize;
    private final String[] fruitImages = new String[]{"frog.png", "rat.png",
            "shrew.png", "grasshopper.png"};

    private final int right = 0;
    private final int left = 1;
    private final int up = 2;
    private final int down = 3;

    private Point snakeHead;
    private Image foodImage;
    private boolean gameOver;
    private GraphicsContext graphicsContext;
    private final List<Point> snakeBody = new ArrayList();
    private int foodX;
    private int foodY;
    private int currentDirection;
    private int score = 0;
    private boolean pressFlag = false;
    private final Stage primaryStage = new Stage();
    private final Group root = new Group();
    private final Canvas canvas;
    private final Scene scene = new Scene(root);
    private Timeline timeline = null;
    private Stage rootStage;

    public GameStage(int height, int width) {
        this.height = height;
        this.width = width;
        squareSize = width / rows;
        canvas = new Canvas(width, height);
    }

    public void start() {
        primaryStage.setTitle("Snake");
        root.getChildren().add(canvas);
        primaryStage.setScene(scene);
        primaryStage.show();
        graphicsContext = canvas.getGraphicsContext2D();
        primaryStage.setResizable(false);

        control();
        generateFood();

        timeline = new Timeline(new KeyFrame(Duration.millis(200), e -> run()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void control() {
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.RIGHT || code == KeyCode.D) {
                if (currentDirection != left) {
                    currentDirection = right;
                }
                pressFlag = true;
            } else if (code == KeyCode.LEFT || code == KeyCode.A) {
                if (currentDirection != right) {
                    currentDirection = left;
                }
                pressFlag = true;
            } else if (code == KeyCode.UP || code == KeyCode.W) {
                if (currentDirection != down) {
                    currentDirection = up;
                }
                pressFlag = true;
            } else if (code == KeyCode.DOWN || code == KeyCode.S) {
                if (currentDirection != up) {
                    currentDirection = down;
                }
                pressFlag = true;
            }
        });

        for (int i = 0; i < 3; i++) {
            snakeBody.add(new Point(columns / 2, rows / 2));
        }
        snakeHead = snakeBody.get(0);
    }

    private void generateFood() {
        start:
        while (true) {
            foodX = (int) (Math.random() * rows);
            foodY = (int) (Math.random() * columns);

            for (Point snake : snakeBody) {
                if (snake.getX() == foodX && snake.getY() == foodY) {
                    continue start;
                }
            }
            foodImage = new Image(fruitImages[(int) (Math.random() * fruitImages.length)]);
            break;
        }
    }

    private void run() {
        if (gameOver) {
            resetWindow();
            timeline.stop();
        }

        drawBackground();
        drawScore();
        drawFood();
        drawSnake();

        for (int i = snakeBody.size() - 1; i >= 1; i--) {
            snakeBody.get(i).x = snakeBody.get(i - 1).x;
            snakeBody.get(i).y = snakeBody.get(i - 1).y;
        }
        switch (currentDirection) {
            case right -> snakeHead.x++;
            case left -> snakeHead.x--;
            case up -> snakeHead.y--;
            case down -> snakeHead.y++;
        }

        gameOver();
        eatFood();

        if (pressFlag) { //отображение пояснения к управлению
            gameOver();
        } else drawRules();
    }

    public void setRootStage(Stage root) {
        this.rootStage = root;
    }

    public void resetWindow() {
        Stage loseWindow = new Stage();
        BorderPane loseLayout = new BorderPane();
        Text text = new Text("YOU LOSE!");
        Button button = new Button("Menu");
        Pane pane = new Pane();
        pane.getChildren().addAll(text, button);
        loseLayout.setCenter(pane);
        Scene loseScene = new Scene(loseLayout, 200, 100);
        loseWindow.setResizable(false);
        loseWindow.setScene(loseScene);
        loseWindow.show();

        button.setLayoutX(80);
        button.setLayoutY(40);
        text.setLayoutX(75);
        text.setLayoutY(20);

        button.setOnAction(event -> {
            loseWindow.close();
            rootStage.show();
            primaryStage.close();
        });
    }

    public void drawRules() {
        graphicsContext.setFill(Color.web("EAFFBF"));
        graphicsContext.setFont(new Font("ShowCard Gothic", 35));
        graphicsContext.fillText("""
                -Start & Move:
                W,A,S,D  or ←↑↓→""", 470, 150);
    }

    private void drawBackground() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if ((i + j) % 2 == 0) {
                    graphicsContext.setFill(Color.web("AAD751"));
                } else {
                    graphicsContext.setFill(Color.web("A2D149"));
                }
                graphicsContext.fillRect(i * squareSize, j * squareSize, squareSize, squareSize);
            }
        }
    }


    private void drawFood() {
        graphicsContext.drawImage(foodImage, foodX * squareSize, foodY * squareSize, squareSize, squareSize);
    }

    private void drawSnake() {
        graphicsContext.setFill(Color.web("#502611"));
        graphicsContext.fillRoundRect(snakeHead.getX() * squareSize,
                snakeHead.getY() * squareSize, squareSize - 1, squareSize - 1, 20, 30);

        for (int i = 1; i < snakeBody.size(); i++) {
            graphicsContext.setFill(Color.web("#2d130b"));
            graphicsContext.fillRoundRect(snakeBody.get(i).getX() * squareSize,
                    snakeBody.get(i).getY() * squareSize,
                    squareSize - 1, squareSize - 1, 10, 10);
        }
    }

    public void gameOver() {
        if (snakeHead.x < 0 || snakeHead.y < 0 || snakeHead.x * squareSize >= width || snakeHead.y * squareSize >= height) {
            gameOver = true;
        }

        for (int i = 1; i < snakeBody.size(); i++) {
            if (snakeHead.x == snakeBody.get(i).getX() && snakeHead.getY() == snakeBody.get(i).getY()) {
                gameOver = true;
                break;
            }
        }
    }

    private void eatFood() {
        if (snakeHead.getX() == foodX && snakeHead.getY() == foodY) {
            snakeBody.add(new Point(-1, -1));
            generateFood();
            score += 15;
        }
    }

    private void drawScore() {
        graphicsContext.setFill(Color.web("#ff6709"));
        graphicsContext.setFont(new Font("ShowCard Gothic", 40));
        graphicsContext.fillText("Score: " + score, 300, 75);
    }
}
