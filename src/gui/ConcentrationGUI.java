package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Card;
import model.ConcentrationModel;
import model.Observer;

import java.util.*;

/**
 * The ConcentrationGUI application is the UI for Concentration.
 *
 * @author Adrian Burgos awb8593
 */
public class ConcentrationGUI extends Application
        implements Observer< ConcentrationModel, Object > {

    /**
     * DOCUMENTATION HERE
     *
     * @throws Exception
     */
    @Override
    public void init() throws Exception {
        System.out.println("init: Initialize and connect to model!");
    }

    /**
     * YOUR DOCUMENTATION HERE
     *
     * @param stage
     * @throws Exception
     */
    @Override
    public void start( Stage stage ) throws Exception {
        BorderPane layout = new BorderPane();
        Text title = new Text("Concentration");
        GridPane cards = new GridPane();
        Button reset = new Button("Reset");
        Button undo = new Button("Undo");
        Button cheat = new Button("Cheat");

        layout.getChildren().addAll(title, cards, reset, undo, cheat);
        stage.setScene(new Scene(layout));
        stage.setTitle("Concentration");
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                ImageView card = new ImageView(new Image(getClass().getResourceAsStream("resources/pokeball.png")));
                Button cardButton = new Button();
                cardButton.setGraphic(card);
                cards.add(cardButton, row, col);
            }
        }

        stage.show();

    }

    /**
     * YOUR DOCUMENTATION HERE
     *
     * @param concentrationModel
     * @param o
     */
    @Override
    public void update( ConcentrationModel concentrationModel, Object o ) {

    }


    /**
     * main entry point launches the JavaFX GUI.
     *
     * @param args not used
     */
    public static void main( String[] args ) {
        Application.launch( args );
    }
}
