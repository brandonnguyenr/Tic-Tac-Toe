package io.github.teamdonut.proj.PlayerType;

import io.github.teamdonut.proj.common.Board;
import io.github.teamdonut.proj.listener.EventManager;
import io.github.teamdonut.proj.listener.IObserver;
import io.github.teamdonut.proj.listener.ISubject;
import io.github.teamdonut.proj.utils.DataValidation;

import java.util.Random;

/**
 * @author utsavparajuli
 * @version 3.0
 *
 * This class will implement the NPC interface as an easy difficulty. The logic in making the move for the NPC
 * is based on just finding an empty spot.
 */
public class NPCEasyMode implements IPlayerType {

    /**
     * Constructor for the NPCEasyMode class. Does not initialize anything. Overrides the compiler generated
     * constructor
     */
    public NPCEasyMode() {
    }

    /**
     * This method will make a move for the NPC which is in easy difficulty mode. It will generate a x and y
     * number which will be placed in the board. The method will iterate through the board until it finds
     * any empty spaces. Once the spot is found it will exit the loop and update the call the notify method of
     * the EventManager and pass the data.
     * @param board : the tic tac toe board
     * @param c : the token
     */
    @Override
    public void makeMove(Board board, char c) {
        DataValidation.ensureObjectNotNull("Board", board);

        int x;      //row
        int y;      //col
        char[][] boardAr = board.getUnderlyingBoard();    //tic tac toe board

        //initialization for random class
        Random num = new Random();

        boolean valid = false;

        //loops until the NPC finds an empty spot
        do{
            x = num.nextInt(3);
            y = num.nextInt(3);

            //checks if the board is not empty
            if (boardAr[x][y] != ' ') {
                System.out.println("That spot is taken already - try again!\n");
            }
            else {
                valid = true;
            }

        }while(!valid);

        //notify method called for the event manager
        EventManager.notify(this, new IPlayerType.BoardMoveInfo(x, y));
    }

    /**
     * Checks to see if the board is full or not (all cells have a token that
     * is not the character ' ').
     * @param board the board to test
     * @return true if all cells have a value besides ' ', false otherwise
     * @author Grant Goldsworth
     */
    public static boolean isFullBoard(Board board) {
        for (char[] row : board.getUnderlyingBoard()) {
            for (char col : row) {
                // does cell have a valid token? If not, board isn't empty
                if (col == ' ')
                    return false;
            }
        }

        // all board cells have a token
        return true;
    }

    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hash tables such as those provided by
     * @return : a hash code value
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return super.toString();
    }
}
