package com.example;

import javafx.scene.Group;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;

public class WireNode extends Circle{

    public static final int APPWIDTH = 1920;
    public static final int APPHEIGHT = 1080; //Not sure why these don't work from App

    private Boolean isInput;
    
    WireNode(Group root, double x, double y, Boolean isInput) { //If not input, output

        this.isInput = isInput;

        this.setLayoutX(x);
        this.setLayoutY(y);
        this.setRadius(6);
        this.setFill(Color.GREEN);

        makeWirable(root,this);
    }

    private void makeWirable(Group root, WireNode self) { 
        //sets up event handling for dragging out wires from this node
        //Also I need to use self becaue "this" refers to the mouseEvent inside of handle, which is annoying
        final CubicCurve wire = new CubicCurve();
        wire.setFill(Color.TRANSPARENT);
        wire.setStroke(Color.GREEN);
        wire.setStrokeWidth(2);
        wire.getStrokeDashArray().addAll(5d, 5d);

        self.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                self.setMouseTransparent(true);
                double startX = self.getLayoutX() + self.getParent().getTranslateX();
                double startY = self.getLayoutY() + self.getParent().getTranslateY();
                
                wire.setStartX(startX);
                wire.setStartY(startY);

                wire.setControlX1(startX);
                wire.setControlY1(startY);

                wire.setControlX2(startX);
                wire.setControlY2(startY);

                wire.setEndX(self.getLayoutX() + self.getParent().getTranslateX());
                wire.setEndY(self.getLayoutY() + self.getParent().getTranslateY());
                root.getChildren().add(wire);
                event.setDragDetect(true);
            }
        });
        
        self.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                self.setMouseTransparent(false);
                root.getChildren().remove(wire);
                //The following 2 lines are used to re-render the scene, because a bug in javafx causes the wire preview to still render after it's removed
                self.toBack();
                self.toFront();
            }
        });
        
        self.setOnMouseEntered(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent event) {
                self.setFill(Color.LIGHTGREEN);}});

        self.setOnMouseExited(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent event) { 
            self.setFill(Color.GREEN);}});
        
        self.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {

                double startX = self.getLayoutX() + self.getParent().getTranslateX();
                double startY = self.getLayoutY() + self.getParent().getTranslateY();
                double endX = event.getSceneX();
                double endY = event.getSceneY();

                wire.setEndX(endX);
                wire.setEndY(endY);

                //Wow Java handles dividing by zero with no complaints
                //It literally ouputs "Infinity", and diving by Infinity gives zero
                //So the following code does in fact work
                wire.setControlX1(endX + ((startX - endX) / (APPWIDTH / Math.abs(startX - endX)))); 
                wire.setControlY1(startY);
                
                wire.setControlX2(startX - ((startX - endX) / (APPWIDTH / Math.abs(startX - endX))));
                wire.setControlY2(endY);
                
                event.setDragDetect(false);
            }
        });
    }
}
