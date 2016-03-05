/*
 * Copyright (c) 2016 Androidhacks7
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.androidhacks7.fifteenpuzzle.model;


import com.androidhacks7.fifteenpuzzle.Constants;

/**
 * @author AndroidHacks7
 */
public class Board {

    private int board[][] = new int[Constants.MAXROWS][Constants.MAXROWS];
    private int emptySpot;
    private int boardWinState[][] = new int[Constants.MAXROWS][Constants.MAXROWS];

    public int getBoardSize() {
        return Constants.MAXROWS;
    }

    public int getShuffleCount() {
        return Constants.SHUFFLECOUNT;
    }

    public int getEmptySpot() {
        return emptySpot;
    }

    public void setEmptySpot(int xPosition, int yPosition) {
        emptySpot = Constants.MAXROWS * xPosition + yPosition;
    }

    public int[][] getBoardWinState() {
        return boardWinState;
    }

    public void setBoardWinState() {
        // set the board state which user has to attain to win
        int i;
        int j = 0;
        int k = 0;
        for (i = 0; i < Constants.MAXROWS; i++)
            for (j = 0; j < Constants.MAXROWS; j++) {
                boardWinState[i][j] = ++k;
            }
        boardWinState[--i][--j] = 0;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setUpBoard() {
        // set up the board initially
        int i;
        int j = 0;
        int k = 0;
        for (i = 0; i < Constants.MAXROWS; i++)
            for (j = 0; j < Constants.MAXROWS; j++) {
                board[i][j] = ++k;
            }
        board[--i][--j] = 0;
        setEmptySpot(i, j);
    }

    public void updateBoard(int xCor, int yCor) {
        // make changes in the board
        int temp = board[xCor][yCor];
        board[xCor][yCor] = board[emptySpot / Constants.MAXROWS][emptySpot % Constants.MAXROWS];
        board[emptySpot / Constants.MAXROWS][emptySpot % Constants.MAXROWS] = temp;
        setEmptySpot(xCor, yCor);
    }
}
