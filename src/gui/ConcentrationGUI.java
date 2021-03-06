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
     * The model for the view and controller.
     */
    private ConcentrationModel model;

    /**
     * list that the cards will be in
     */
    private ArrayList<Button> cardList;
    private ArrayList<Button> cheatCardList;

    /**
     * list of pokemon images that will appear on the face of each card
     */
    private ArrayList<Image> images;

    /**
     * labels that are initalized in start and updated in update as the game is played
     */
    private Label instructions;
    private Label moves;

    /**
     *
     * @param i the index of the image I am getting from the image list
     * @return returns an ImageView of the image selected from the list of pictures
     */
    private ImageView getImageView(int i) {
        return new ImageView(this.images.get(i));
    }

    /**
     * process command line args, pre GUI setup
     *
     * @throws Exception
     */
    @Override
    public void init() throws Exception {
        System.out.println("init: Initialize and connect to model!");
        this.model = new ConcentrationModel();
        this.model.addObserver( this );
    }

    /**
     * Initialize the view
     */
    public void initializeView() {
        update( this.model, null );
    }

    /**
     * start constructs the layout for the game
     *
     * @param stage the stage of the gui, which holds the border pane, title, buttons, etc
     */
    @Override
    public void start( Stage stage ) throws Exception {
        BorderPane layout = new BorderPane();
        Text title = new Text("Gotta Match Em All!");
        GridPane cards = new GridPane();
        Button reset = new Button("Reset");
        Button undo = new Button("Undo");
        Button cheat = new Button("Cheat");
        this.moves = new Label("Moves: " + this.model.getMoveCount());
        this.instructions = new Label("Select the first card.");
        HBox buttons = new HBox();
        this.cardList = new ArrayList<>();
        this.images = new ArrayList<>();

        this.images.add(new Image(getClass().getResourceAsStream("resources/cinderace.png")));
        this.images.add(new Image(getClass().getResourceAsStream("resources/ditto.png")));
        this.images.add(new Image(getClass().getResourceAsStream("resources/grimm.png")));
        this.images.add(new Image(getClass().getResourceAsStream("resources/luxray.png")));
        this.images.add(new Image(getClass().getResourceAsStream("resources/obstagoon.png")));
        this.images.add(new Image(getClass().getResourceAsStream("resources/sceptile.png")));
        this.images.add(new Image(getClass().getResourceAsStream("resources/tapu.png")));
        this.images.add(new Image(getClass().getResourceAsStream("resources/zarude.png")));

        buttons.getChildren().addAll(reset, undo, cheat, moves);
        buttons.setAlignment(Pos.BASELINE_CENTER);
        stage.setScene(new Scene(layout));
        stage.setTitle("Gotta Match Em All!");
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                ImageView card = new ImageView(new Image(getClass().getResourceAsStream("resources/pokeball.png")));
                Button cardButton = new Button();
                this.cardList.add(cardButton);
                int finalRow = row;
                int finalCol = col;
                cardButton.setOnAction(e -> this.model.selectCard(finalRow * 4 + finalCol));
                cardButton.setGraphic(card);
                cards.add(cardButton, row, col);
            }
        }

        reset.setOnAction((event) -> {
            this.model.reset();
        });
        undo.setOnAction((event) -> {
            this.model.undo();
        });
        cheat.setOnAction((event) -> {
            this.model.cheat();
        });


        layout.setTop(instructions);
        layout.setCenter(cards);
        layout.setBottom(buttons);

        stage.show();

    }

    /**
     * opens a new window showing the correct order of cards
     * @param cheatCards An ArrayList containing each of the cards in the correct order they are placed in
     * @param stage the stage used in the GUI to display the cards
     */
    public void cheatWindow(ArrayList< Card > cheatCards, Stage stage) {
        BorderPane layout = new BorderPane();
        GridPane cards = new GridPane();
        this.cheatCardList = new ArrayList<>();
        ArrayList<Image> images = new ArrayList<>();

        images.add(new Image(getClass().getResourceAsStream("resources/cinderace.png")));
        images.add(new Image(getClass().getResourceAsStream("resources/ditto.png")));
        images.add(new Image(getClass().getResourceAsStream("resources/grimm.png")));
        images.add(new Image(getClass().getResourceAsStream("resources/luxray.png")));
        images.add(new Image(getClass().getResourceAsStream("resources/obstagoon.png")));
        images.add(new Image(getClass().getResourceAsStream("resources/sceptile.png")));
        images.add(new Image(getClass().getResourceAsStream("resources/tapu.png")));
        images.add(new Image(getClass().getResourceAsStream("resources/zarude.png")));

        stage.setScene(new Scene(layout));
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                stage.setTitle("Cheat Window!");
                Button cardButton = new Button();
                this.cheatCardList.add(cardButton);
                cards.add(cardButton, row, col);
            }
        }
        for (int i = 0; i < cheatCardList.size(); i++){
            Button cardButton = this.cheatCardList.get(i);
            int index = cheatCards.get(i).getNumber();
            cardButton.setGraphic(this.getImageView(index));
        }

        layout.setCenter(cards);
        stage.show();
    }

    /**
     * Update the UI. This method is called by an object in the game model. The contents of the buttons are changed
     * based on the card faces in the model. Changes in the the text in the labels may also occur based on the changed
     * model state.
     *
     * @param concentrationModel the model object that knows the current board state
     * @param o null ??? non-cheating mode; non-null ??? cheating mode
     */
    @Override
    public void update( ConcentrationModel concentrationModel, Object o ) {
        this.moves.setText("Moves: " + String.valueOf(this.model.getMoveCount()));
        for (int i = 0; i < cardList.size(); i++) {
            //flip a card
            if (this.model.getCards().get(i).isFaceUp()) {
                int index = this.model.getCards().get(i).getNumber();
                cardList.get(i).setGraphic(this.getImageView(index));
            }
            if (!this.model.getCards().get(i).isFaceUp()) {
                cardList.get(i).setGraphic(new ImageView(new Image(getClass().getResourceAsStream("resources/pokeball.png"))));
            }
        }
        if (this.model.howManyCardsUp() == 0) {
            this.instructions.setText("Select the First card.");
        } else if (this.model.howManyCardsUp() == 1) {
            this.instructions.setText("Select the Second card.");
        } else if (this.model.howManyCardsUp() == 2) {
            this.instructions.setText("No Match: Undo or select a card.");
        }

        //cheat
        if(o != null) {
            cheatWindow(this.model.getCheat(), new Stage());
        }

        // display a win if all cards are face up (not cheating)
        if ( this.model.getCards().stream().allMatch( face -> face.isFaceUp() ) ) {
            System.out.println( "YOU WIN!" );
        }
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
