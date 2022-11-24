package com.example;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.DragEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class WirePreviewPane extends Rectangle{

    WirePreviewPane(Group root, double width, double height) {
        this.setWidth(width);
        this.setHeight(height);
        //this.setFill(Color.TRANSPARENT);
        this.setFill(new Color(1,1,1,0));
        setupDrawing(this);
    }

    private void setupDrawing(WirePreviewPane self) { //Allows wire previews to render over the background

        self.setOnDragOver(new EventHandler<DragEvent>() { public void handle(DragEvent event) { //Target
            if (event.getDragboard().hasString()) {
                ((WireNode) event.getGestureSource()).drawWire(event.getSceneX(),event.getSceneY());
            }
        }});
    }
}
