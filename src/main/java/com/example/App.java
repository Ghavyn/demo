package com.example;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.stage.Stage;

public class App extends Application {

    private static Scene scene;
    ArrayList<Group> gates = new ArrayList<Group>(); //Each gate is a group containting the image, as well as wire terminals for connecting gates
    Pane root = new Pane();

    final int APPWIDTH = 1920;
    final int APPHEIGHT = 1080;

    @Override
    public void start(Stage stage) throws IOException, URISyntaxException {
        stage.setMaximized(true);
        root.setId("pane");
        
        Image bg = new Image(getClass().getResourceAsStream("bg3.png"));
        ImageView bgView = new ImageView(bg);
        bgView.setX(0);
        bgView.setY(0);
        bgView.setFitWidth(APPWIDTH);
        bgView.setFitHeight(APPHEIGHT);
        //root.getChildren().add(bgView);

        scene = new Scene(root,APPWIDTH,APPHEIGHT,Color.WHITE);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Logician's Folly");
        stage.show();
        
        createLogicPiece("and2.png");
        createLogicPiece("or2.png");
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
            imageView.setFitHeight(55);
            imageView.setFitWidth(100);
            makeImageDragable(imageView); //Allows clicking and dragging to translate (change the tranlsation x and y, which apply after other positioning) to the group

            Circle input1 = new Circle();
            input1.setLayoutX(6);
            input1.setLayoutY(16.5);
            input1.setRadius(6);
            input1.setFill(Color.GREEN);
            makeWirable(input1);

            Circle input2 = new Circle();
            input2.setLayoutX(6);
            input2.setLayoutY(38);
            input2.setRadius(6);
            input2.setFill(Color.GREEN);
            makeWirable(input2);

            Circle output1 = new Circle();
            output1.setLayoutX(100);
            output1.setLayoutY(27);
            output1.setRadius(6);
            output1.setFill(Color.GREEN);
            makeWirable(output1);

            Group gateGroup = new Group();
            gateGroup.getChildren().addAll(imageView, input1, input2, output1);
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
                n.getParent().toFront();
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
        final CubicCurve wire = new CubicCurve();
        wire.setFill(Color.TRANSPARENT);
        wire.setStroke(Color.GREEN);
        wire.setStrokeWidth(2);
        wire.getStrokeDashArray().addAll(5d, 5d);

        n.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                n.setMouseTransparent(true);
                double startX = n.getLayoutX() + n.getParent().getTranslateX();
                double startY = n.getLayoutY() + n.getParent().getTranslateY();
                
                wire.setStartX(startX);
                wire.setStartY(startY);

                wire.setControlX1(startX);
                wire.setControlY1(startY);

                wire.setControlX2(startX);
                wire.setControlY2(startY);

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
                n.setFill(Color.LIGHTGREEN);}});

        n.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent event) { 
            n.setFill(Color.GREEN);}});
        
        n.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {

                double startX = n.getLayoutX() + n.getParent().getTranslateX();
                double startY = n.getLayoutY() + n.getParent().getTranslateY();
                double endX = event.getSceneX();
                double endY = event.getSceneY();

                wire.setEndX(endX);
                wire.setEndY(endY);

                System.out.println((startX - endX));
                wire.setControlX1(endX + ((startX - endX) / (APPWIDTH / Math.abs(startX - endX)))); //Don't worry about how complex this is. It just makes the wire previews fancy
                wire.setControlY1(startY);
                
                wire.setControlX2(startX - ((startX - endX) / (APPWIDTH / Math.abs(startX - endX))));
                wire.setControlY2(endY);
                
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