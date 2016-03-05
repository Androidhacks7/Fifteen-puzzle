/*
 * Copyright (c) 2016 Androidhacks7
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.androidhacks7.fifteenpuzzle;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.androidhacks7.fifteenpuzzle.model.Board;
import com.androidhacks7.fifteenpuzzle.model.Cell;
import com.androidhacks7.fifteenpuzzle.util.Utils;

import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Puzzle activity controller
 *
 * @author AndroidHacks7
 */
public class PuzzleStartActivity extends Activity implements OnTouchListener {

    private static final String TAG = PuzzleStartActivity.class.getSimpleName();

    private PuzzleView puzzleView;

    private Board boardGame;

    private int moveCount;
    // keeps track of number of user moves
    private int userMoveCount;

    private boolean isUndoAllowed;
    private boolean isGameForfeited;
    private boolean boardIsBeingResolved;

    private int timeOfPlay;
    private int startTime;

    // keep track of visited positions
    private Stack<Integer> traversedPositions;

    // keep track of user moves
    private Stack<Integer> userMoveHistory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        beginGame();
    }

    public void beginGame() {

        // start the game and make it ready for the user to play

        puzzleView = new PuzzleView(this);

        setContentView(puzzleView);

        puzzleView.setOnTouchListener(this);

        boardGame = new Board();
        traversedPositions = new Stack<Integer>();
        userMoveHistory = new Stack<Integer>();

        boardGame.setUpBoard();
        boardGame.setBoardWinState();

        shuffleBoard();
        displayBoard();

        isUndoAllowed = false;
        isGameForfeited = false;
        boardIsBeingResolved = false;
        startTime = Utils.getCurrentTimeInSeconds();

    }

    public void shuffleBoard() {

        // shuffle the board in a random manner
        int noOfShuffles = boardGame.getShuffleCount();
        for (int count = 0; count < noOfShuffles; count++) {
            double randomDouble = Math.random();
            // generates random position (to move to n/e/s/w)
            int positionIndicator = (int) (randomDouble * 100) / 25 + 1;
            // if valid make move
            if (isValidPosition(positionIndicator)) {
                traversedPositions.push(new Integer(positionIndicator));
                moveToPosition(positionIndicator);
            } else {
                noOfShuffles++;
            }
        }
    }

    public boolean isValidPosition(int positionIndicator) {

        // return true if the position to be moved is a valid one
        int emptySpot = boardGame.getEmptySpot();
        int boardSize = boardGame.getBoardSize();
        int x = emptySpot / boardSize;
        int y = emptySpot % boardSize;
        switch (positionIndicator) {
            case 1:
                if (x > 0 && x < boardSize) {
                    return true;
                }
                break;
            case 2:
                if (y >= 0 && y < boardSize - 1) {
                    return true;
                }
                break;
            case 3:
                if (x >= 0 && x < boardSize - 1) {
                    return true;
                }
                break;
            case 4:
                if (y > 0 && y < boardSize) {
                    return true;
                }
                break;
        }
        return false;
    }

    public void moveToPosition(int positionIndicator) {

        // move the empty spot to the indicated position

        int emptySpot = boardGame.getEmptySpot();
        int boardSize = boardGame.getBoardSize();

        int x = emptySpot / boardSize;
        int y = emptySpot % boardSize;

        switch (positionIndicator) {
            case 1:
                boardGame.updateBoard(x - 1, y);
                break;
            case 2:
                boardGame.updateBoard(x, y + 1);
                break;
            case 3:
                boardGame.updateBoard(x + 1, y);
                break;
            case 4:
                boardGame.updateBoard(x, y - 1);
                break;
        }
    }

    public void showMoves() {

        boardIsBeingResolved = true;

        TimerTask moveRetracer = new TimerTask() {

            @Override
            public void run() {

                if (userMoveCount > 0) {
                    while (userMoveCount > 0) {
                        deassembleUserMove();
                        userMoveCount--;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {

                            e.printStackTrace();
                        }
                    }

                }

                moveCount = boardGame.getShuffleCount();

                while (moveCount > 0) {

                    solveSingleStepBackward();
                    moveCount--;

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                }

                isGameForfeited = true;
                displayBoard();
                boardIsBeingResolved = false;
            }

            ;
        };

        Timer timer = new Timer();
        timer.schedule(moveRetracer, 0);

    }

    public void deassembleUserMove() {

        // reverse each move of the user
        Integer temp = null;
        try {
            temp = userMoveHistory.pop();
            int lastMoveMade = temp.intValue();
            int emptySpot = boardGame.getEmptySpot();
            int boardSize = boardGame.getBoardSize();
            int x = emptySpot / boardSize;
            int y = emptySpot % boardSize;
            switch (lastMoveMade) {
                case 1:
                    boardGame.updateBoard(x + 1, y);
                    break;
                case 2:
                    boardGame.updateBoard(x, y - 1);
                    break;
                case 3:
                    boardGame.updateBoard(x - 1, y);
                    break;
                case 4:
                    boardGame.updateBoard(x, y + 1);
                    break;
            }
            isUndoAllowed = false;
            displayBoard();
        } catch (java.util.EmptyStackException e) {
            Log.d(TAG, " No user moves made");
        } catch (NullPointerException e) {
            Log.d(TAG, " Error in retracing");
        }
    }

    public void solveSingleStepBackward() {

        // resolve each shuffle made initially
        Integer temp = null;
        try {
            temp = traversedPositions.pop();
        } catch (java.util.EmptyStackException e) {
            Log.d(TAG, " Error in retracer");
        }
        int lastVisitedPosition = temp.intValue();
        int emptySpot = boardGame.getEmptySpot();
        int boardSize = boardGame.getBoardSize();
        int x = emptySpot / boardSize;
        int y = emptySpot % boardSize;
        switch (lastVisitedPosition) {
            case 1:
                boardGame.updateBoard(x + 1, y);
                break;
            case 2:
                boardGame.updateBoard(x, y - 1);
                break;
            case 3:
                boardGame.updateBoard(x - 1, y);
                break;
            case 4:
                boardGame.updateBoard(x, y + 1);
                break;
        }
        displayBoard();
    }

    public void makeUserMove(int boardValue) {

        // perform the move indicated by the user after checking if the move is
        // allowed
        int positionOfValue = boardValue;
        int xPosition = positionOfValue / Constants.MAXROWS;
        int yPosition = positionOfValue % Constants.MAXROWS;
        if (isUserMoveValid(boardValue)) {
            userMoveHistory.push(new Integer(getUserPositionIndicator(
                    xPosition, yPosition)));
            boardGame.updateBoard(xPosition, yPosition);
            userMoveCount++;
            isUndoAllowed = true;
            displayBoard();
            if (isWinConditionAttained()) {

                showDialog(Constants.GAME_WON);
            }
        }
    }

    public boolean isUserMoveValid(int boardValue) {

        // check if user move is valid
        if (boardValue >= 0
                && boardValue <= (Constants.MAXROWS * Constants.MAXROWS) - 1) {
            int positionOfValue = boardValue;
            int xPosition = positionOfValue / Constants.MAXROWS;
            int yPosition = positionOfValue % Constants.MAXROWS;
            int EmptySpot = boardGame.getEmptySpot();
            int x = EmptySpot / Constants.MAXROWS;
            int y = EmptySpot % Constants.MAXROWS;
            if (((xPosition - x == 1) && (yPosition - y == 0))
                    || ((xPosition - x == 0) && (yPosition - y == -1))
                    || ((xPosition - x == -1) && (yPosition - y == 0))
                    || ((xPosition - x == 0) && (yPosition - y == 1))) {
                return true;
            } else {
                Log.i(TAG, "Not a valid move");
                return false;
            }
        } else {
            Log.i(TAG, " Not a valid position");
            return false;
        }
    }

    public int getUserPositionIndicator(int xPosition, int yPosition) {

        // return the direction indicator n/s/w/e

        int EmptySpot = boardGame.getEmptySpot();
        int x = EmptySpot / Constants.MAXROWS;
        int y = EmptySpot % Constants.MAXROWS;
        if ((xPosition - x == 1) && (yPosition - y == 0)) {
            return 3;
        } else if ((xPosition - x == 0) && (yPosition - y == -1)) {
            return 4;
        } else if ((xPosition - x == -1) && (yPosition - y == 0)) {
            return 1;
        } else if ((xPosition - x == 0) && (yPosition - y == 1)) {
            return 2;
        }
        return 0;
    }

    public void displayBoard() {

        // display state of the current game

        int[] arrayToBeDisplayed = Utils.makeSingleArrayStream(boardGame);
        puzzleView.setBoard(arrayToBeDisplayed);

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (isGameForfeited) {
                    showDialog(Constants.GAME_LOST);
                }
                puzzleView.invalidate();
            }
        });

    }

    public boolean isWinConditionAttained() {

        boolean flag = false;
        int[][] currentBoardState = boardGame.getBoard();
        int[][] winState = boardGame.getBoardWinState();
        for (int i = 0; i < boardGame.getBoardSize(); i++) {
            // compare current board state with win state set initially
            for (int j = 0; j < boardGame.getBoardSize(); j++) {
                if (currentBoardState[i][j] == winState[i][j]) {
                    flag = true;
                } else {
                    flag = false;
                    return false;
                }
            }
        }
        return flag;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        // Make a cell based upon user touch event

        Cell cellSelected = new Cell(event.getX(), event.getY(), puzzleView.getXCoord(), puzzleView.getYCoord(), puzzleView.getSquareSize());
        int movePosition = cellSelected.getPositionInBoard();

        makeUserMove(movePosition);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0, 1, 0, "Resolve");
        menu.add(0, 2, 0, "Undo");
        menu.add(0, 3, 0, "Restart");
        menu.add(0, 4, 0, "Exit");
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        // Make menus visible and not visible depending upon the state of the
        // game

        if (isUndoAllowed) {
            menu.getItem(1).setEnabled(true);

        } else {
            menu.getItem(1).setEnabled(false);
        }

        if (boardIsBeingResolved) {

            menu.getItem(0).setEnabled(false);
            menu.getItem(1).setEnabled(false);
            menu.getItem(2).setEnabled(false);

        } else {

            menu.getItem(0).setEnabled(true);
            menu.getItem(2).setEnabled(true);
        }
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        switch (item.getItemId()) {

            case 1:
                showMoves();
                break;
            case 2:
                deassembleUserMove();
                userMoveCount--;
                break;
            case 3:
                beginGame();
                break;
            case 4:
                System.exit(0);
                break;
        }

        return true;
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        // GAMW WON DIALOG
        if (id == Constants.GAME_WON) {

            timeOfPlay = Utils.getCurrentTimeInSeconds() - startTime;

            return new AlertDialog.Builder(PuzzleStartActivity.this)

                    .setIcon(R.drawable.thumbs_up).setTitle("YOU WON!!!").setMessage(
                            "YOU FINISHED IN: "
                                    + Integer.toString((int) timeOfPlay / 60) + " min "
                                    + Integer.toString((int) timeOfPlay % 60) + " sec")
                    .setPositiveButton("RESTART",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {

                                    beginGame();
                                }
                            }).setNegativeButton("EXIT",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {

                                    System.exit(0);
                                }
                            }).create();

            // GAME LOST DIALOG
        } else if (id == Constants.GAME_LOST) {

            return new AlertDialog.Builder(PuzzleStartActivity.this).setIcon(
                    R.drawable.thumbs_down).setTitle("YOU LOST!!!")
                    .setPositiveButton("RESTART",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {

                                    beginGame();
                                    isGameForfeited = false;
                                }
                            }).setNegativeButton("EXIT",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {

                                    System.exit(0);
                                }
                            }).create();

        }

        return null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
