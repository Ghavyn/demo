package com.example;

import com.example.App.GateType;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

//controls Gate Cards

public class GateCard extends LogicGate{ //LogicGate extends Group

	private ImageView image;
	
     GateCard(){} //Don't use, this is just here so Java doesn't complain
    
     GateCard(Group root, GateType type) {	//the gate Card control
        try {
            String fileName = "";
            switch(type) { //This will be extended to include the logic in the future
                case OR:
                    fileName = "orcard.png";
                    break;
                case AND:
                    fileName = "andcard.png";
                    break;
                case NOT:
                    fileName = "notcard.png";
                    break;
                case SPLITTER:
                    fileName = "splitter.png";
                    break;
                case NOR:
                    fileName = "norcard.png";
                    break;
                case NAND:
                    fileName = "nandcard.png";
                    break;
                case XOR:
                    fileName = "xorcard.png";
                    break;
                default:
                    fileName = "andcard.png";
                    System.out.println("How did you get here?");
                    break;
            }
            Image image = new Image(getClass().getResourceAsStream(fileName));
            this.image = new ImageView(image);
            this.image.setX(0);
            this.image.setY(0);
            this.image.setFitHeight(225);	//will fix + standardize latter, this is for testing
            this.image.setFitWidth(150);
            setupDrag(this.image, this); //Allows clicking and dragging to translate (change the tranlsation x and y, which apply after other positioning) to the group
            setupClickSpawn(this.image, this, type);	//Allows click to spawn Gates from cards
            this.getChildren().add(this.image);
           
            root.getChildren().add(this);
            setupWirePreviewOverGate(this);	//not currently working 
            
        } catch(Exception e) {
            System.out.println("Error: Invalid filename for GateCard (or another error in this code block): " + type);
        }
    }
 //--------------------------------------------------------------------------------------------
     protected void setupClickSpawn(ImageView image, LogicGate self, GateType type) {
     
         image.setOnMousePressed(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent event) {
             
             App.SpawnGate(type);
             
         }});
     }
}
