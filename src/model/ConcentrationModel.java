package model;

import gui.ConcentrationGUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Definition for the model of a concentration card game.
 *
 * @author Arthur Nunes-Harwitt
 * @author ben k steele
 * @author Sean Strout
 */
public class ConcentrationModel {
    /**
     * The default size (of one side) of the board.
     */
    public static final int BOARD_SIZE = 4;

    /**
     * The total number of cards assuming a square board.
     */
    public static final int NUM_CARDS = BOARD_SIZE * BOARD_SIZE;

    /**
     * The number of pairs.
     */
    public static final int NUM_PAIRS = NUM_CARDS / 2;

    /**
     * Those objects that are watching this object's every move
     */
    private List< Observer< ConcentrationModel, Object > > observers;
    /**
     * The undo stack for the game tracks pairing selections in progress.
     * The undo stack support the undo operation to undo 1 or 2 cards
     * if there are any card flips to undo.
     */
    private ArrayList< Card > undoStack;

    /**
     * There are NUM_PAIRS cards each of which contains an image
     * loaded from a file.
     */
    private ArrayList< Card > cards;

    /**
     * Store the number of moves made in the game.
     * A move is a card selection.
     */
    private int moveCount;

    /**
     * Construct a ConcentrationModel; there is only one configuration.
     */
    public ConcentrationModel() {
        this.observers = new LinkedList<>();
        this.cards = new ArrayList<>();

        for ( int n = 0; n < NUM_PAIRS; ++n ) {
            Card card1 = new Card( n, true );
            Card card2 = new Card( n, true );
            this.cards.add( card1 );
            this.cards.add( card2 );
        }
        this.reset();
    }

    /**
     * Push a card onto the undo stack.
     *
     * @param card The card to push.
     */
    private void push( Card card ) {
        undoStack.add( card );
    }

    /**
     * Pop a card from the undo stack.
     *
     * @param toggle Flag indicates whether or not the state
     *               of the card will enable toggling or not.
     *               If the toggle is true, the card will be flipped and
     *               further flipping of the card will be permitted.
     *               If the toggle is false, the card will not be flipped and
     *               further flipping of the card will be prohibited until a
     *               reset.
     */
    private void pop( boolean toggle ) {
        int s = undoStack.size();
        if ( s > 0 ) {
            Card card = undoStack.get( s - 1 );
            undoStack.remove( s - 1 );
            if ( toggle ) {
                // re-enable flipping this card.
                card.toggleFace( toggle );
            }
        }
    }

    /**
     * Pop a card from the undo stack. (There are no parameters.)
     */
    private void pop() {
        pop( false );
    }

    /**
     * Undo selecting a card.
     */
    public void undo() {
        pop( true );
        announce( null );
    }

    /**
     * Turn over a card.
     *
     * @param n An integer referring to the nth card.
     */
    private void add( int n ) {
        Card card = cards.get( n );
        if ( !card.isFaceUp() ) {
            card.toggleFace();
            push( card );
            ++this.moveCount;
        }
    }

    /**
     * Check to see if the two cards on the top of the undo stack have
     * the same value, and pop them off the undo stack if they match.
     */
    private void checkMatch() {
        if ( undoStack.size() == 2 &&
             undoStack.get( 0 ).getNumber() ==
             undoStack.get( 1 ).getNumber() ) {
            pop();
            pop();
        }
    }

    /**
     * Select a card to turn face up from cards.
     * If there are already two cards selected, turn those back over.
     *
     * @param n An integer referring to the nth card.
     */
    public void selectCard( int n ) {

        if ( 0 <= n && n < NUM_CARDS ) {
            switch ( undoStack.size() ) {
                case 2:
                    undo();
                    undo();
                case 0:
                    add( n );
                    break;
                case 1:
                    add( n );
                    checkMatch();
                    break;
                default:
                    throw
                            new RuntimeException(
                                    "Internal Error: undoStack too big." );
            }
            announce( null );
        }
    }

    /**
     * Get the cards.
     *
     * @return An ArrayList containing the cards on the board.
     */
    public ArrayList< Card > getCards() {

        ArrayList< Card > faces = new ArrayList<>( this.cards );
        return faces;
    }

    /**
     * The controller tells the model that the view should get the "cheat"
     * with all cards face up.
     */
    public void cheat() {
        announce( "cheat" );
    }

    /**
     * Get the cards showing them all.
     *
     * @return An ArrayList containing the cards on the board, all facing up.
     */
    public ArrayList< Card > getCheat() {
        ArrayList< Card > faces = new ArrayList<>();

        for ( Card card : cards ) {
            Card copy = new Card( card );
            copy.setFaceUp();
            faces.add( copy );
        }
        return faces;
    }

    /**
     * Get the number of moves, i.e., the count of card selections.
     *
     * @return An integer that represents the number of moves.
     */
    public int getMoveCount() {
        return this.moveCount;
    }

    /**
     * Reset the board.  All the cards are turned face-down and are
     * shuffled.  The undo stack and the number of moves are cleared.
     */
    public void reset() {

        for ( Card card : cards ) {
            if ( card.isFaceUp() ) {
                card.toggleFace( true );
            }
        }
        Collections.shuffle( cards );

        this.undoStack = new ArrayList<>();

        this.moveCount = 0;

        announce( null );
    }

    /**
     * Return the number of cards currently selected.
     *
     * @return An integer that represents the number of cards
     * selected.
     */
    public int howManyCardsUp() {
        return undoStack.size();
    }

    /**
     * Add a new observer to the list for this model
     * @param obs an object that wants an
     *            {@link Observer#update(Object, Object)}
     *            when something changes here
     */
    public void addObserver( Observer< ConcentrationModel, Object > obs ) {
        this.observers.add( obs );
    }

    /**
     * Announce to observers the model has changed;
     */
    private void announce( String arg ) {
        for ( var obs : this.observers ) {
            obs.update( this, arg );
        }
    }
}

