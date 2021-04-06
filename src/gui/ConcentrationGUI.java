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

    /**
     * list of pokemon images that will appear on the face of each card
     */
    private ArrayList<ImageView> images;

    private Label instructions;
    private Label moves;

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
        //this.model.addObserver( this );
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
        Text title = new Text("Concentration");
        GridPane cards = new GridPane();
        Button reset = new Button("Reset");
        Button undo = new Button("Undo");
        Button cheat = new Button("Cheat");
        this.moves = new Label("Moves: " + this.model.getMoveCount());
        this.instructions = new Label("Select the first card.");
        HBox buttons = new HBox();
        this.cardList = new ArrayList<>();
        this.images = new ArrayList<>();

        this.images.add(new ImageView(new Image(getClass().getResourceAsStream("resources/abra.png"))));
        this.images.add(new ImageView(new Image(getClass().getResourceAsStream("resources/bulbasaur.png"))));
        this.images.add(new ImageView(new Image(getClass().getResourceAsStream("resources/charmander.png"))));
        this.images.add(new ImageView(new Image(getClass().getResourceAsStream("resources/jigglypuff.png"))));
        this.images.add(new ImageView(new Image(getClass().getResourceAsStream("resources/meowth.png"))));
        this.images.add(new ImageView(new Image(getClass().getResourceAsStream("resources/pikachu.png"))));
        this.images.add(new ImageView(new Image(getClass().getResourceAsStream("resources/squirtle.png"))));
        this.images.add(new ImageView(new Image(getClass().getResourceAsStream("resources/venomoth.png"))));

        buttons.getChildren().addAll(reset, undo, cheat, moves);
        buttons.setAlignment(Pos.BASELINE_CENTER);
        stage.setScene(new Scene(layout));
        stage.setTitle("Concentration");
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
     * Update the UI. This method is called by an object in the game model. The contents of the buttons are changed
     * based on the card faces in the model. Changes in the the text in the labels may also occur based on the changed
     * model state.
     *
     * @param concentrationModel the model object that knows the current board state
     * @param o null ⇒ non-cheating mode; non-null ⇒ cheating mode
     */
    @Override
    public void update( ConcentrationModel concentrationModel, Object o ) {
        this.moves.setText(String.valueOf(this.model.getMoveCount()));
        for (int i = 0; i < cardList.size(); i++) {
            //flip a card
            if (this.model.getCards().get(i).isFaceUp()) {
                int index = this.model.getCards().get(i).getNumber();
                cardList.get(i).setGraphic(this.images.get(index));
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
