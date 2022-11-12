package com.example;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class App extends Application {

    private static Scene scene;
    ArrayList<Group> gates = new ArrayList<Group>(); //Each gate is a group containting the image, as well as wire terminals for connecting gates
    Group root = new Group();

    @Override
    public void start(Stage stage) throws IOException, URISyntaxException {
        
        Image bg = new Image(getClass().getResourceAsStream("bg.png"));
        ImageView bgView = new ImageView(bg);
        bgView.setX(0);
        bgView.setY(0);
        root.getChildren().add(bgView);

        scene = new Scene(root,640,480,Color.WHITE);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Logician's Folly");
        stage.show();
        
        createLogicPiece("and.jpg");
        createLogicPiece("or.jpg");
        gates.get(1).setTranslateX(200.0); //This is an example of how to set gate properties from this scope. This sets the translate of the group btw, children must be accessed by index (the order in which they were added to the group)
        gates.get(1).setTranslateY(100.0);
        
        for(Node element : gates) {
            root.getChildren().add(element);
        }
    }

    private void createLogicPiece(String name) { //Name must be the filename of the image, otherwise a crash will ocurr with a really long and cryptic error message
        try {
            Image image = new Image(getClass().getResourceAsStream(name));
            ImageView imageView = new ImageView(image);
            imageView.setX(0);
            imageView.setY(0);
            imageView.setFitHeight(50);
            imageView.setFitWidth(100);
            makeImageDragable(imageView); //Allows clicking and dragging to translate (change the tranlsation x and y, which apply after other positioning) to the group

            Circle input1 = new Circle();
            input1.setLayoutX(10);
            input1.setLayoutY(15);
            input1.setRadius(5);
            
            makeWirable(input1);

            Circle input2 = new Circle();
            input2.setLayoutX(10);
            input2.setLayoutY(35);
            input2.setRadius(5);

            Group gateGroup = new Group();
            gateGroup.getChildren().addAll(imageView, input1, input2);
            gates.add(gateGroup);

        } catch(Exception e) {
            System.out.println("Error: Invalid filename for gate");
        }
    }

    private void makeImageDragable(ImageView n) {
        final Delta dragDelta = new Delta(); //This sticks around as long as the gate does despite being declared in this function

        n.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                n.setMouseTransparent(true);
                dragDelta.x = n.getParent().getTranslateX() - event.getSceneX();
                dragDelta.y = n.getParent().getTranslateY() - event.getSceneY();
                //n.setCursor(Cursor.MOVE);
                event.setDragDetect(true);
            }
        });
        
        n.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                n.setMouseTransparent(false);
                n.getParent().setTranslateX(20 * Math.round(n.getParent().getTranslateX()/20));
                n.getParent().setTranslateY(20 * Math.round(n.getParent().getTranslateY()/20));
                System.out.println("Mouse released");
            }
        });
        
        n.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                //n.setCursor(Cursor.OPEN_HAND);
            }
        });
        
        n.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                n.getParent().setTranslateX(event.getSceneX() + dragDelta.x);
                n.getParent().setTranslateY(event.getSceneY() + dragDelta.y);
                event.setDragDetect(false);
            }
        });
    }

    private void makeWirable(Circle n) {
        final Line wire = new Line();

        n.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                n.setMouseTransparent(true);
                wire.setStartX(n.getLayoutX() + n.getParent().getTranslateX());
                wire.setStartY(n.getLayoutY() + n.getParent().getTranslateY());
                wire.setEndX(n.getLayoutX() + n.getParent().getTranslateX());
                wire.setEndY(n.getLayoutY() + n.getParent().getTranslateY());
                root.getChildren().add(wire);
                event.setDragDetect(true);
            }
        });
        
        n.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                n.setMouseTransparent(false);
                root.getChildren().remove(wire);
                //The following 2 lines are used to re-render the scene, because a bug in javafx causes the wire preview to still render after it's removed
                n.toBack();
                n.toFront();
            }
        });
        
        n.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent event) {
                n.setFill(Color.GREEN);}});

        n.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent event) { 
            n.setFill(Color.BLACK);}});
        
        n.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                wire.setEndX(event.getSceneX());
                wire.setEndY(event.getSceneY());
                event.setDragDetect(false);
            }
        });
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}