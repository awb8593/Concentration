package ptui;

import java.util.Scanner;
import java.util.ArrayList;

import model.*;

/**
 * Class definition for the textual view and controller.
 *
 * @author Arthur Nunes-Harwitt
 * @author Sean Strout
 */

public class ConcentrationPTUI
        implements Observer< ConcentrationModel, Object > {

    /**
     * The model for the view and controller.
     */
    private ConcentrationModel model;

    /**
     * Construct the PTUI
     */
    public ConcentrationPTUI() {
        this.model = new ConcentrationModel();
        initializeView();
    }

    // CONTROLLER

    /**
     * Read a command and execute loop.
     */
    private void run() {
        Scanner in = new Scanner( System.in );
        for ( ; ; ) {
            System.out.print( "game command: " );
            String line = in.nextLine();
            String[] words = line.split( "\\s+" );
            if ( words.length > 0 ) {
                if ( words[ 0 ].startsWith( "q" ) ) {
                    break;
                }
                else if ( words[ 0 ].startsWith( "r" ) ) {
                    this.model.reset();
                }
                else if ( words[ 0 ].startsWith( "c" ) ) {
                    this.model.cheat();
                }
                else if ( words[ 0 ].startsWith( "u" ) ) {
                    this.model.undo();
                }
                else if ( words[ 0 ].startsWith( "s" ) ) {
                    int n = Integer.parseInt( words[ 1 ] );
                    this.model.selectCard( n );
                }
                else {
                    displayHelp();
                }
            }
        }
    }

    // VIEW

    /**
     * Initialize the view
     */
    public void initializeView() {
        this.model.addObserver( this );
        update( this.model, null );
    }

    /**
     * Print on standard out the cards as a grid , the move count, and
     * brief directions.
     *
     * @param n     An integer that represents the number of moves.
     * @param up    An integer that represents the number of cards
     *              selected.
     * @param faces An ArrayList of CardFace that represents the board.
     * @param cheat true if user selected to cheat
     */
    private void displayBoard( int n, int up, ArrayList< Card > faces,
                               boolean cheat ) {
        System.out.println( "Move count: " + n );
        switch ( up ) {
            case 0:
                System.out.println( "Select the first card." );
                break;
            case 1:
                System.out.println( "Select the second card." );
                break;
            case 2:
                System.out.println( "No Match: Undo or select a card." );
                break;
        }
        int pos = 1;
        for ( Card f : faces ) {
            if ( f.isFaceUp() ) {
                System.out.print( "-" + f.getNumber() + "-" );
            }
            else {
                System.out.print( "***" );
            }
            if ( pos % 4 == 0 ) {
                System.out.println();
            }
            else {
                System.out.print( " | " );
            }
            ++pos;
        }
    }

    /**
     * Print on standard out help for the game.
     */
    private void displayHelp() {
        System.out.println( " 00 | 01 | 02 | 03" );
        System.out.println( " 04 | 05 | 06 | 07" );
        System.out.println( " 08 | 09 | 10 | 11" );
        System.out.println( " 12 | 13 | 14 | 15" );
        System.out.println( "s(elect) n  -- select the card n to flip" );
        System.out.println( "u(ndo)      -- undo last flip" );
        System.out.println( "q(uit)      -- quit the game" );
        System.out.println( "r(eset)     -- start a new game" );
        System.out.println( "c(heat)     -- see where the cards are" );
    }

    public void update( ConcentrationModel o, Object arg ) {
        // if arg is not null, it means the user wants to get the "cheat" board
        // with all cards face up
        displayBoard( this.model.getMoveCount(),
                      this.model.howManyCardsUp(),
                      arg == null ? this.model.getCards()
                              : this.model.getCheat(),
                      false
        );

        // display a win if all cards are face up (not cheating)
        if ( this.model.getCards().stream()
                       .allMatch( face -> face.isFaceUp() ) ) {
            System.out.println( "YOU WIN!" );
        }
    }

    /**
     * The main method used to play a game.
     *
     * @param args Command line arguments -- unused
     */
    public static void main( String[] args ) {
        ConcentrationPTUI ptui = new ConcentrationPTUI();
        ptui.run();
    }
}
