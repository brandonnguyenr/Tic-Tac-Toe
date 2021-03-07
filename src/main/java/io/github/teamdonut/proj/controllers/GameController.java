package io.github.teamdonut.proj.controllers;

import io.github.teamdonut.proj.common.Board;
import io.github.teamdonut.proj.common.Player;
import io.github.teamdonut.proj.listener.EventManager;
import io.github.teamdonut.proj.listener.IObserver;
import io.github.teamdonut.proj.listener.ISubject;

import java.util.Objects;
import java.util.Random;

/**
 * Game logic controller
 * @author Kord Boniadi
 * @author Brandon Nguyen
 */
public class GameController implements ISubject, IObserver {

    /**
     * Container for data to be sent out to IObservers subscribed to this class
     * @see EventManager#notify(ISubject, Object)
     * @author Kord Boniadi
     */
    public static class DrawInfo {
        private final Board updatedBoard;

        public DrawInfo(Board board) {
            this.updatedBoard = board;
        }

        public Board getUpdatedBoard() {
            return updatedBoard;
        }

    }

    public static class Results {
        private final Player winner;

        public Results() {
            this(null);
        }
        public Results(Player winner) {
            this.winner = winner;
        }

        public Player getWinner() {
            return winner;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Results)) return false;
            Results results = (Results) o;
            return Objects.equals(getWinner(), results.getWinner());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getWinner());
        }

        @Override
        public String toString() {
            return "Results{" +
                    "winner=" + winner +
                    '}';
        }
    }

    private final Board board;
    private final Player player1;
    private final Player player2;
    private Player swap;

    /**
     * Constructor
     * @author Kord Boniadi
     * @author Brandon Nguyen
     */
    public GameController() {
        this(
                new Player("Player1", 'X'),
                new Player("Player2", 'O'),
                new Board()
        );
    }

    /**
     * Constructor
     * @author Utsav Parajuli
     * @param player1 player object instance
     */
    public GameController(Player player1) {
        this(
                player1,
                new Player("Player2", 'O'),
                new Board()
        );
    }


    /**
     * Constructor
     * @param player1 player object instance
     * @param player2 player object instance
     * @author Kord Boniadi
     * @author Brandon Nguyen
     * @author Utsav Parajuli
     */
    public GameController(Player player1, Player player2) {
        this(player1, player2, new Board());
    }
    /**
     * Constructor
     * @param player1 player object instance
     * @param player2 player object instance
     * @param board Object instance
     * @author Kord Boniadi
     * @author Brandon Nguyen
     */
    public GameController(Player player1, Player player2, Board board) {
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * @return bard instance
     * @author Kord Boniadi
     * @author Brandon Nguyen
     */
    public Board getBoard() {
        return board;
    }

    /**
     * @return player instance
     * @author Brandon Nguyen
     */
    public Player getPlayer1() {
        return player1;
    }

    /**
     * @return player instance
     * @author Brandon Nguyen
     */
    public Player getPlayer2() {
        return player2;
    }

    public void startGame() {
        Random rand = new Random();
        swap = (rand.nextInt(2) == 0) ? this.player1 : this.player2;
        EventManager.register(player1, this);
        EventManager.register(player2, this);

        swap.makeMove(this.board);
    }

    public boolean gameOver() {
        return board.isBoardFull() || hasWon(board) == 'X' || hasWon(board) == 'O';
    }

    public char hasWon(Board board) {
        char[][] boardArr = board.getUnderlyingBoard();

        for (int i = 0; i < board.BOARD_ROWS; i++) {
            if (boardArr[i][0] == 'X' && boardArr[i][1] == 'X' && boardArr[i][2] == 'X') {
                return 'X';
            }
        }

        for (int i = 0; i < 3; i++) {
            if (boardArr[i][0] == 'O' && boardArr[i][1] == 'O' && boardArr[i][2] == 'O') {
                return 'O';
            }
        }

        for (int j = 0; j < 3; j++) {
            if (boardArr[0][j] == 'X' && boardArr[1][j] == 'X' && boardArr[2][j] == 'X') {
                return 'X';
            }
        }

        for (int j = 0; j < 3; j++) {
            if (boardArr[0][j] == 'O' && boardArr[1][j] == 'O' && boardArr[2][j] == 'O') {
                return 'O';
            }
        }

        if (boardArr[0][0] == 'X' && boardArr[1][1] == 'X' && boardArr[2][2] == 'X') {
            return 'X';
        }

        if (boardArr[0][0] == 'O' && boardArr[1][1] == 'O' && boardArr[2][2] == 'O') {
            return 'O';
        }

        if (boardArr[0][2] == 'X' && boardArr[1][1] == 'X' && boardArr[2][0] == 'X') {
            return 'X';
        }

        if (boardArr[0][2] == 'O' && boardArr[1][1] == 'O' && boardArr[2][0] == 'O') {
            return 'O';
        }
        return 'C';
    }

    public Player whoWon(Board board, Player player1, Player player2) {
        if (hasWon(board) == player1.getPlayerToken())
            return player1;
        return (hasWon(board) == player2.getPlayerToken()) ? player2 : null;
    }

    /**
     * New info is received through this method. Object decoding is needed
     *
     * @param eventType General Object type
     * @author Kord Boniadi
     */
    @Override
    public void update(Object eventType) {
        if (eventType instanceof Player.MoveInfo) {
            Player.MoveInfo info = (Player.MoveInfo) eventType;
            boolean gameOver;
            if (swap == info.getPlayerInstance() && board.getToken(info.getX(), info.getY()) == board.EMPTY_VALUE) {
                board.updateToken(info.getX(), info.getY(), info.getPlayerInstance().getPlayerToken());
                gameOver = gameOver();

                System.out.println("y: " + info.getY() + " x: " + info.getX());
                System.out.println(this.board);
                swap = (gameOver) ? null : (swap == player1) ? player2 : player1;

                EventManager.notify(this, new DrawInfo(this.board));

                if (!gameOver)
                    swap.makeMove(this.board);
                else
                    EventManager.notify(this, new GameController.Results(whoWon(board, player1, player2)));
            }
        }
    }
}
