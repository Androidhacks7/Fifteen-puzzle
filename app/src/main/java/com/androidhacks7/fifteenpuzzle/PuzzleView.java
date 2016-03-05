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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Game view
 *
 * @author AndroidHacks7
 */
public class PuzzleView extends View {

    private int squareSize;
    private int xCordinate;
    private int yCordinate;

    private int xValue;
    private int yValue;

    private Paint paint;

    private int[] board;

    public PuzzleView(Context context) {
        super(context);
        paint = new Paint();
    }

    public int getXCoord() {
        return xCordinate;
    }

    public int getYCoord() {
        return yCordinate;
    }

    public int getSquareSize() {
        return squareSize;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        xCordinate = width / 10;
        squareSize = width / 5;
        yCordinate = height / 10;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        xValue = xCordinate;
        yValue = yCordinate;

        canvas.drawColor(Color.BLACK);

        paint.setStyle(Paint.Style.FILL);

        for (int i = 0; i < board.length; i++) {

            paint.setColor(Color.BLUE);
            xValue += squareSize;

            if (i % Constants.MAXROWS == 0) {

                yValue += squareSize;
                xValue = xCordinate;
            }
            // draw Tile and text
            if (board[i] == 0) {
                paint.setColor(Color.BLACK);
                canvas.drawRect(xValue, yValue, xValue + squareSize, yValue
                        + squareSize, paint);

            } else {

                canvas.drawRect(xValue, yValue, xValue + squareSize, yValue
                        + squareSize, paint);
                paint.setColor(Color.WHITE);
                paint.setTextSize(40);
                canvas.drawText(Integer.toString(board[i]), xValue
                        + (squareSize / 2), yValue + (squareSize / 2), paint);
            }
            // draw Border
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
            paint.setColor(Color.WHITE);
            canvas.drawRect(xValue, yValue, xValue + squareSize, yValue + squareSize, paint);
            paint.setStyle(Paint.Style.FILL);

        }
        super.onDraw(canvas);
    }

    public void setBoard(int[] boardState) {
        this.board = boardState;
    }

}
