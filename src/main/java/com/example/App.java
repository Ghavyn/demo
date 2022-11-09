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
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    ArrayList<ImageView> tiles = new ArrayList<ImageView>();

    @Override
    public void start(Stage stage) throws IOException, URISyntaxException {
        
        Group root = new Group();
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


        //createTile("and.jpg"); //Tiles are automatically added to the tiles list
        //createTile("or.jpg");
        //createTile("and.jpg");
        
        //tiles.get(0).setX(200);

        
        Image image2 = new Image(getClass().getResourceAsStream("and.jpg"));
        ImageView imageView2 = new ImageView(image2);
        imageView2.setX(100);
        imageView2.setY(0);
        imageView2.setFitHeight(50);
        imageView2.setFitWidth(100);
        makeImageDragable(imageView2);

        Image image3 = new Image(getClass().getResourceAsStream("and.jpg"));
        ImageView imageView3 = new ImageView(image3);
        imageView3.setX(300);
        imageView3.setY(0);
        imageView3.setFitHeight(50);
        imageView3.setFitWidth(100);
        makeImageDragable(imageView3);

        Group test = new Group();

        //test.getStyleClass().add("pane");

        test.getChildren().add(imageView2);
        test.getChildren().add(imageView3);
        root.getChildren().add(test);
        


        //GaussianBlur g = new GaussianBlur();  
        //g.setRadius(5);  
        //rect.setEffect(g);  
        
        /*
        RotateTransition rotate = new RotateTransition();
        rotate.setAxis(Rotate.Z_AXIS);    
        rotate.setByAngle(360);    
        rotate.setCycleCount(500);   
        rotate.setDuration(Duration.millis(1000));  
        rotate.setAutoReverse(true);  
        rotate.setNode(tiles.get(0));
        rotate.play(); 
        */
        
        for(Node element : tiles) {
            root.getChildren().add(element);
        }
    }

    private void createTile(String name) {
        Image image = new Image(getClass().getResourceAsStream(name));
        ImageView imageView = new ImageView(image);
        imageView.setX(0);
        imageView.setY(0);
        imageView.setFitHeight(50);
        imageView.setFitWidth(100);
        makeImageDragable(imageView);
        tiles.add(imageView);
    }

    private void makeImageDragable(ImageView n) {
        final Delta dragDelta = new Delta();

        n.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                n.setMouseTransparent(true);
                dragDelta.x = n.getParent().getTranslateX() - event.getSceneX(); //n.getLayoutX() - event.getSceneX();
                dragDelta.y = n.getParent().getTranslateY() - event.getSceneY(); //n.getLayoutY() - event.getSceneY();
                //n.setCursor(Cursor.MOVE);
                event.setDragDetect(true);
            }
        });
        
        n.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                n.setMouseTransparent(false);
                n.setLayoutX(20 * Math.round(n.getLayoutX()/20));
                n.setLayoutY(20 * Math.round(n.getLayoutY()/20));
                System.out.println("Mouse released");
            }
        });
        
        n.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                n.setCursor(Cursor.OPEN_HAND);
            }
        });
        
        n.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                System.out.println("X: " + n.getLayoutX() + ", Y: " + n.getLayoutY());
                n.getParent().setTranslateX(event.getSceneX() + dragDelta.x);
                n.getParent().setTranslateY(event.getSceneY() + dragDelta.y);
                //n.setLayoutX(event.getSceneX() + dragDelta.x);
                //n.setLayoutY(event.getSceneY() + dragDelta.y);
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