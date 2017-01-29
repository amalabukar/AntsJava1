package com.company;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;


public class Main extends Application {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public static final int ANT_COUNT = 100;

    ArrayList<Ant> ants;

    static Color b = Color.BLACK;
    static Color r = Color.RED;

    long lastTimeStamp = 0;




    public int fps(long now) {
        double diff = now - lastTimeStamp;
        double diffSeconds = diff / 1000000000;
        return (int) (1 / diffSeconds);
    }

    public static ArrayList<Ant> createAnts() {
        ArrayList<Ant> ants = new ArrayList<>();

        for (int i = 0; i < ANT_COUNT; i++) {
            Random r = new Random();
            ants.add(new Ant(r.nextInt(WIDTH), r.nextInt(HEIGHT)));
        }

        return ants;
    }

    public void drawAnts(GraphicsContext context) {
        context.clearRect(0, 0, WIDTH, HEIGHT);
        for (Ant ant : ants) {
            context.setFill(ant.color);
            context.fillOval(ant.x, ant.y, 5, 5);
        }
    }



    public static double randomStep() {
        return Math.random() * 2 - 1;
    }

    public Ant moveAnt(Ant ant) {

        ant.x += randomStep();
        ant.y += randomStep();
        aggregateAnt(ant);

        return ant;
    }

    public void moveAnts() {
        ants = ants.parallelStream()
//                .map(this::aggravateAnt)
                .map(this::moveAnt)
                .collect(Collectors.toCollection(ArrayList<Ant>::new));
    }


    public void aggregateAnt (Ant ant) {
        int count = 0;
        for (Ant a : ants) {
            if (Math.abs(ant.x-a.x)<10 && Math.abs(ant.y-a.y)<10){
                count++;
            }
            if (count > 1) {
                ant.setColor(r);
            } else {
                ant.setColor(b);
            }


        }
    }

    public static void main(String[] args) {
        launch(args);


        // write your code here
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        primaryStage.setTitle("Ants");
        primaryStage.setScene(scene);
        primaryStage.show();

        Canvas canvas = (Canvas) scene.lookup("#canvas");
        GraphicsContext context = canvas.getGraphicsContext2D();

        ants = createAnts();

        Label fpsLabel = (Label) scene.lookup("#fps");

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                moveAnts();
                drawAnts(context);
                fpsLabel.setText(fps(now) + "");
                lastTimeStamp = now;
            }
        };
        timer.start();

    }
}
