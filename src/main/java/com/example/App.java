package com.example;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class App extends Application {
    public static final int APPWIDTH = 1920;
    public static final int APPHEIGHT = 1080;

    private static Scene scene;
    private static ArrayList<LogicGate> gates = new ArrayList<LogicGate>(); //Each gate is a group containting the image, as well as wire terminals for connecting gates
    private static Group root = new Group();
    private static WirePreviewPane previewPane = new WirePreviewPane(root, APPWIDTH, APPHEIGHT);
    private static Rectangle forceRefresher = new Rectangle(0,0,0,0);

    @Override
    public void start(Stage stage) {

        scene = new Scene(root,APPWIDTH,APPHEIGHT,Color.rgb(6,6,6));
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setMaximized(true); //Windowed fullscreen. If the computer is not 1080p, game assets will be the wrong size
        stage.setTitle("Logician's Folly");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
        stage.setOpacity(0.0); //The opacity toggling is to prevent the window for flashbanging white for a few frames while loading in
        stage.show();
        stage.setOpacity(1.0);

        root.getChildren().add(previewPane); //Draws wires when mouse is dragging over background

        forceRefresher.setFill(Color.TRANSPARENT); //Used to force refresh
        root.getChildren().add(forceRefresher);
        forceRefresher.toBack();
        
        gates.add(new LogicGate(root, "andgate.png"));
        gates.add(new LogicGate(root, "orgate.png"));
        gates.add(new LogicGate(root, "norgate.png"));
        gates.add(new LogicGate(root, "andgate.png"));
        gates.add(new LogicGate(root, "orgate.png"));
        gates.add(new LogicGate(root, "norgate.png"));
        //gates.get(0).setTranslateX(200.0); 
    }

    public static Group getRoot() { //Public variables don't work across classes for some reason, so I use this instead
        return root;
    }

    public static void forceRefresh() {
        forceRefresher.toFront();
        forceRefresher.toBack();
    }

    public static void main(String[] args) {
        launch();
    }

}