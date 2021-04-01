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
import javafx.stage.Stage;
import model.Card;
import model.ConcentrationModel;
import model.Observer;

import java.util.*;

/**
 * The ConcentrationGUI application is the UI for Concentration.
 *
 * @author YOUR NAME HERE
 */
public class ConcentrationGUI extends Application
        implements Observer< ConcentrationModel, Object > {


    /**
     * YOUR DOCUMENTATION HERE
     *
     * @param stage
     * @throws Exception
     */
    @Override
    public void start( Stage stage ) throws Exception {

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
