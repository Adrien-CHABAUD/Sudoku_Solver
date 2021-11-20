/*;==========================================
; Author: Adrien CHABAUD - B00376254
; Date:   Avr. 2020
;==========================================*/
package com.adrien.chabaud;

import java.util.Arrays;

class Check {

    //Bactracking algorithm, find the sudoku's solution
    static boolean backtracking(int[][] grid, int gridLength) {
        int row = -1, col = -1;
        boolean isEmpty = true;

        for (int i = 0; i < gridLength; i++) {
            for (int j = 0; j < gridLength; j++) {
                //Check if the case has to be completed of not
                if (grid[i][j] == 0) {
                    row = i;
                    col = j;

                    isEmpty = false;
                    break;
                }
            }
            if (!isEmpty) {
                break;
            }
        }
        if (isEmpty) {
            return true;
        }

        for (int num = 1; num <= gridLength; num++) {
            //Check if it can insert the selected number according to the sudoku's rules
            if (isSafe(grid, row, col, num)) {

                grid[row][col] = num;
                if (backtracking(grid, gridLength)) {
                    return true;
                } else {
                    grid[row][col] = 0;
                }
            }
        }
        return false;
    }

    private static boolean isInBox(int[][] grid, int row, int col, int num) {
        //Find the box
        int sqrt = (int) Math.sqrt(grid.length);
        int boxRowStart = row - row % sqrt;
        int boxColStart = col - col % sqrt;

        int[] boxValues = new int[9];
        int boxIndex = 0;

        for (int r = boxRowStart; r < boxRowStart + sqrt; r++) {
            for (int c = boxColStart; c < boxColStart + sqrt; c++) {
                boxValues[boxIndex] = grid[r][c];
                boxIndex++;
            }
        }

        //Sort the array
        Arrays.sort(boxValues);

        //Return false if the element is not found, true otherwise
        return binarySearch(boxValues, 0, boxValues.length - 1, num);
    }

    private static boolean binarySearch(int[] array, int barrier, int size, int searchedDigit) {
        if (size >= barrier) {
            int mid = barrier + (size - barrier) / 2;

            //If the element is in the middle
            if (array[mid] == searchedDigit) {
                return true;
            }

            //If the element is smaller than mid value
            //Than it can only be present in to left side of the array
            if (array[mid] > searchedDigit) {
                return binarySearch(array, 1, mid - 1, searchedDigit);
            }

            //The element can only be in the right side of the array
            return binarySearch(array, mid + 1, size, searchedDigit);
        }
        //We reach here when the element is not present in the array
        return false;
    }

    //Check function
    static boolean isSafe(int[][] grid, int row, int col, int num) {
        //Check in the row
        for (int c = 0; c < grid.length; c++) {
            if (grid[row][c] == num) {
                return false;
            }
        }
        //Check in the column
        for (int[] ints : grid) {
            if (ints[col] == num) {
                return false;
            }
        }
        //Check in the box
        return !isInBox(grid, row, col, num);
    }

    static boolean isUnique(int[][] grid, int row, int col, int num){
        //Check in the row
        for(int c = 0; c < grid.length; c++){
            if(c != col){
                if(grid[row][c] == num){
                    return false;
                }
            }
        }
        //Check in the column
        for(int r = 0; r <grid.length; r++){
            if(r != row){
                if(grid[r][col] == num){
                    return false;
                }
            }
        }
        //Check in the box
        int sqrt = (int) Math.sqrt(grid.length);
        int boxRowStart = row - row % sqrt;
        int boxColStart = col - col % sqrt;
        for(int r = boxRowStart; r < boxRowStart + sqrt; r++){
            for(int c = boxColStart; c < boxColStart + sqrt; c++){
                if(c != col && r != row){
                    if(grid[r][c] == num){
                        return false;
                    }
                }
            }
        }
        return true;

    }
}
