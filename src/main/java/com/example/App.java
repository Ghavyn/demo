package com.example;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {

    private static Scene scene;
    ArrayList<LogicGate> gates = new ArrayList<LogicGate>(); //Each gate is a group containting the image, as well as wire terminals for connecting gates
    Group root = new Group();

    public static final int APPWIDTH = 1920;
    public static final int APPHEIGHT = 1080;

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
        
        gates.add(new LogicGate(root, "andgate.png"));
        gates.get(0).setTranslateX(200.0); 
        //This is an example of how to set gate properties from this scope. This sets the translate of the group btw, children must be accessed by index (the order in which they were added to the group)
    }

    public static void main(String[] args) {
        launch();
    }

}