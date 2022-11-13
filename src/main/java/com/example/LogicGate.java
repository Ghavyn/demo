package com.example;

import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class LogicGate extends Group{
    
    private ImageView image;
    private ArrayList<WireNode> wireNodes = new ArrayList<WireNode>();
    
    LogicGate(){}

    LogicGate(Group root, String name) {
        try {
            Image image = new Image(getClass().getResourceAsStream(name));
            this.image = new ImageView(image);
            this.image.setX(0);
            this.image.setY(0);
            this.image.setFitHeight(55);
            this.image.setFitWidth(100);
            setupDrag(this.image); //Allows clicking and dragging to translate (change the tranlsation x and y, which apply after other positioning) to the group

            wireNodes.add(new WireNode(root, 6, 16.5, true));
            wireNodes.add(new WireNode(root, 6, 38, true));
            wireNodes.add(new WireNode(root, 100, 27, false));

            this.getChildren().add(this.image);
            wireNodes.forEach(node -> this.getChildren().add(node)); //Woo lambda expression
            root.getChildren().add(this);

        } catch(Exception e) {
            System.out.println("Error: Invalid filename for gate");
        }
    }

    private void setupDrag(ImageView image) {
        final Delta dragDelta = new Delta(); //This sticks around as long as the gate does despite being declared in this function

        image.setOnMousePressed(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent event) {
            image.setMouseTransparent(true);
            image.getParent().toFront();
            dragDelta.x = image.getParent().getTranslateX() - event.getSceneX();
            dragDelta.y = image.getParent().getTranslateY() - event.getSceneY();
            //image.setCursor(Cursor.MOVE);
            event.setDragDetect(true);
        }});
        image.setOnMouseReleased(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent event) {
            image.setMouseTransparent(false);
            image.getParent().setTranslateX(20 * Math.round(image.getParent().getTranslateX()/20));
            image.getParent().setTranslateY(20 * Math.round(image.getParent().getTranslateY()/20));
        }});
        image.setOnMouseEntered(new EventHandler<MouseEvent>() { @Override public void handle(MouseEvent event) {
            //image.setCursor(Cursor.OPEN_HAND);
        }});
        image.setOnMouseDragged(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent event) {
            image.getParent().setTranslateX(event.getSceneX() + dragDelta.x);
            image.getParent().setTranslateY(event.getSceneY() + dragDelta.y);
            event.setDragDetect(false);
        }});
    }
}
