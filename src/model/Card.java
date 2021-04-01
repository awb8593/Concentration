package model;

/**
 * A class to represent a card.
 *
 * @author ben k steele
 * @author Arthur Nunes-Harwitt
 * @author Sean Strout
 */
public class Card {
    /**
     * The number on the card.
     */
    private final int number;

    /**
     * The flag indicating whether or not the card is face-up.
     */
    private boolean isFaceUp;

    /**
     * canFlip flag indicates whether the card can be flipped face-up.
     */
    private boolean canFlip;

    /**
     * Constructor is protected for use by concrete subclasses.
     *
     * @param number  the secret, hidden id of the card (for text ui)
     * @param canFlip is true if the card can be flipped face up/down.
     */

    public Card(int number, boolean canFlip) {
        this.number = number;
        this.isFaceUp = false;
        this.canFlip = canFlip;

    }

    /**
     * Create a copy of the card.
     *
     * @param other the card to copy
     */
    public Card(Card other) {
        this.number = other.number;
        this.isFaceUp = other.isFaceUp;
        this.canFlip = other.canFlip;
    }

    /**
     * @return A boolean indicating whether or not card is face-up.
     */

    public boolean isFaceUp() {
        return this.isFaceUp;
    }

    /**
     * Sets the cards face to showing (used when "cheating").
     **/
    public void setFaceUp() {
        this.isFaceUp = true;
    }

    /**
     * @return An integer that is the number on the face of the card.
     */

    public int getNumber() {
        return (this.isFaceUp() ? this.number : -1);
    }


    /**
     * Toggle the flag indicating whether or not the card is face-up.
     * It only toggles the state of the card if this card allows flipping.
     */

    public void toggleFace() {
        if (this.canFlip) {
            this.isFaceUp = !this.isFaceUp;
        }
    }

    /**
     * This method sets canFlip and toggles face up/down if canFlip is true.
     * If canFlip is false, then no card flipping is performed until
     * canFlip is a true value.
     * It only toggles the state of the card if this card allows flipping.
     *
     * @param canFlip a true value allows the card to be flipped face up.
     */

    public void toggleFace(boolean canFlip) {
        this.canFlip = canFlip;
        toggleFace();
    }
}
