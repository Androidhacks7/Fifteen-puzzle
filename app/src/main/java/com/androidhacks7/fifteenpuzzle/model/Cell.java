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
 * Convenience class
 *
 * @author AndroidHacks7
 */
public class Cell {

    private int xPos;
    private int yPos;
    private int positionInBoard;

    public int getPositionInBoard() {
        return positionInBoard;
    }

    public Cell(float xPos, float yPos, int xCoordinate, int yCoordinate, int squareSize) {

        this.xPos = (int) xPos;
        this.yPos = (int) yPos;
        createCellValue(xCoordinate, yCoordinate, squareSize);
    }

    // Logic that creates the position on board based on touch position

    public void createCellValue(final int xCord, final int yCord, final int squareSize) {

        int x = xCord;
        int y = yCord + squareSize;
        int xEnd = xCord + (squareSize * Constants.MAXROWS);
        int yEnd = yCord + (squareSize * Constants.MAXROWS);


        if ((xPos < x) || (yPos < y) || (xPos >= xEnd) || (yPos >= (yEnd + squareSize)))
            return;


        while (y <= yEnd) {

            while (x <= xEnd) {

                if ((xPos > x) && (xPos < (x + squareSize)) && (yPos > y) && (yPos < (y + squareSize))) {

                    int xPosition = (xPos - xCord) / squareSize;
                    int yPosition = (yPos - (yCord + squareSize)) / squareSize;
                    positionInBoard = Constants.MAXROWS * yPosition + xPosition;

                    break;

                }

                x += squareSize;
            }

            x = yCord;
            y += squareSize;
        }
    }
}
