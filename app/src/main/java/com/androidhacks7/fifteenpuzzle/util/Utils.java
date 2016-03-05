package com.androidhacks7.fifteenpuzzle.util;

import com.androidhacks7.fifteenpuzzle.model.Board;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Utils {

    /**
     * returns single dimensional array of the current state of board
     */
    public static int[] makeSingleArrayStream(Board board) {

        int count = 0;
        int[][] temp = board.getBoard();
        int[] retArray = new int[board.getBoardSize() * board.getBoardSize()];

        for (int i = 0; i < board.getBoardSize(); i++) {

            for (int j = 0; j < board.getBoardSize(); j++) {

                retArray[count++] = temp[i][j];
            }
        }

        return retArray;
    }


    /**
     * returns current time in seconds
     */

    public static int getCurrentTimeInSeconds() {

        Calendar calendar = new GregorianCalendar();
        int hours = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

        return ((hours * 60 * 60) + (minutes * 60) + seconds);
    }
}
