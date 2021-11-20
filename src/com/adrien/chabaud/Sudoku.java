/*;==========================================
; Author: Adrien CHABAUD - B00376254
; Date:   Avr. 2020
;==========================================*/
package com.adrien.chabaud;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import javax.swing.*;

public class Sudoku extends JFrame implements ActionListener {

    //UI elements
    private JButton[][] buttons = new JButton[9][9];

    private int[][] sudokuGrid = new int[9][9];

    private boolean uniqueCaseSelection = false;

    private Sudoku() {

        //Initialise the window and show it
        this.setTitle("Sudoku Solver");
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /*
         * Menu
         */

        //Adding the menus in order
        //Menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        JMenu solveMenu = new JMenu("Solve");
        menuBar.add(solveMenu);
        //Menu objects
        JMenu actions = new JMenu("Actions");
        menuBar.add(actions);

        //Listener for the sub-menu
        //Resolve the grid actionListener
        //Sub menu objects
        JMenuItem resolveButton = new JMenuItem("Resolve the grid");
        resolveButton.addActionListener(e -> {
            System.out.println("Resolve button clicked!");

            if (Check.backtracking(sudokuGrid, sudokuGrid.length)) {
                for (int r = 0; r < 9; r++) {
                    for (int c = 0; c < 9; c++) {
                        buttons[r][c].setText(Integer.toString(sudokuGrid[r][c]));
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "No solution.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        //Reset the grid actionListener
        JMenuItem resetGridButton = new JMenuItem("Reset the grid");
        resetGridButton.addActionListener(e -> {
            System.out.println("Reset of the grid ...");
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    sudokuGrid[r][c] = 0;
                }
            }
            showGrid();
        });
        //Check the completion of the current grid
        JMenuItem checkGridButton = new JMenuItem("Check the grid");
        checkGridButton.addActionListener(e ->{
            boolean isBreak = false;
            System.out.println("Check the completion of the current grid ...");
            for(int r = 0; r < 9; r++){
                for(int c = 0; c < 9; c++){
                    if(!Check.isUnique(sudokuGrid, r, c, sudokuGrid[r][c])){
                        JOptionPane.showMessageDialog(null, "The grid isn't correctly completed");
                        isBreak = true;
                        break;
                    }
                }
                if(isBreak){
                    break;
                }
            }
            if(!isBreak) {
                JOptionPane.showMessageDialog(null, "The grid is correctly completed");
            }
        });
        //Resolve only one case of the grid
        JMenuItem resolveCaseButton = new JMenuItem("Resolve only a case");
        resolveCaseButton.addActionListener(e -> {
            System.out.println("Resolve only one case");
            uniqueCaseSelection = true;
            JOptionPane.showMessageDialog(null, "Select the case to modify");
        });
        //Load a grid
        JMenuItem loadGridButton = new JMenuItem("Load a grid");
        loadGridButton.addActionListener(e -> {
            System.out.println("Load clicked");
            Files newFile = new Files();
            if(newFile.loadFile()){
                newFile.getGrid(sudokuGrid);
                showGrid();
            }
        });
        //Save a grid
        JMenuItem saveGridButton = new JMenuItem("Save the grid");
        saveGridButton.addActionListener(e -> {
            System.out.println("Save clicked");
            Files newFile = new Files();
            newFile.saveFile(sudokuGrid);
        });

        //Adding the sub-menu
        solveMenu.add(resolveButton);
        solveMenu.add(checkGridButton);
        solveMenu.add(resolveCaseButton);
        actions.add(resetGridButton);
        fileMenu.add(saveGridButton);
        fileMenu.add(loadGridButton);

        //Adding the menuBar
        setJMenuBar(menuBar);

        /*
         * JPanels & JButtons showed in the window
         */

        JPanel board = new JPanel();
        board.setLayout(new GridLayout(3, 3));

        //Initialise an array of JPanel
        JPanel[][] panels = new JPanel[3][3];
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                JPanel panel = new JPanel(new GridLayout(3, 3));
                panels[r][c] = panel;
                panels[r][c].setBorder(BorderFactory.createLineBorder(Color.gray));
                board.add(panel);
            }
        }

        //Initialise the JButtons
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                buttons[r][c] = new JButton();
                buttons[r][c].addActionListener(this);
                buttons[r][c].setActionCommand(r + "/" + c);
            }
        }

        //Place the buttons inside of the panels
        for (int panelR = 0; panelR < 3; panelR++) {
            for (int panelC = 0; panelC < 3; panelC++) {
                int buttonR = panelR * 3;
                int buttonC = panelC * 3;

                for (int i = 0; i < 9; i++) {
                    panels[panelR][panelC].add(buttons[buttonR][buttonC]);
                    buttonC++;

                    if ((i + 1) % 3 == 0) {
                        buttonC = panelC * 3;
                        buttonR++;
                    }
                }
            }
        }

        this.setContentPane(board);
        this.setVisible(true);

    }

    //Used to modify the number selected inside of a button
    private String modifyDialogFrame() {
        JFrame frame = new JFrame("Modify the grid");
        //List of the possible inputs
        Object[] options = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        //Return the selected option by the user
        return (String) JOptionPane.showInputDialog(frame, "Click OK to continue", "Modify the grid"
                , JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Identifies which button has been clicked to change its text
        JButton sourceBtn = (JButton) e.getSource();
        int row = Integer.parseInt(e.getActionCommand().substring(0, 1));
        int column = Integer.parseInt(e.getActionCommand().substring(2, 3));

        //If the solving of only one case is not selected
        if(!uniqueCaseSelection) {
            //Show a dialog frame to let the user select the number
            String choice = modifyDialogFrame();

            //If the user aborted in the dialog Frame it won't change the number displayed
            if (choice != null) {
                if (Integer.parseInt(choice) != 0) {
                    //If the digit is unique in the box, columns and rows, return true
                    if (Check.isSafe(sudokuGrid, row, column, Integer.parseInt(choice))) {
                        //Update the array and show the updated grid
                        updateCase(column, row, Integer.parseInt(choice));
                        showGrid();
                    } else {
                        JOptionPane.showMessageDialog(null, "This digit is already taken.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    updateCase(column, row, Integer.parseInt(choice));
                    showGrid();
                }
            }
        }else{
            uniqueCaseSelection = false;
            System.out.println("only one case");

            //Copy the array containing the current sudoku grid into a temporary one
            int[][] tmpArray = new int[9][9];
            for (int r = 0; r < tmpArray.length; r++) {
                //Copy each row of the array into the temporary array
                tmpArray[r] = Arrays.copyOf(sudokuGrid[r], sudokuGrid[r].length);
            }


            if (Check.backtracking(tmpArray, tmpArray.length)) {
                sudokuGrid[row][column] = tmpArray[row][column];
                showGrid();
            } else {
                JOptionPane.showMessageDialog(null, "No solution.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }


        }
    }

    //Update the array containing the sudoku grid
    private void updateCase(int col, int row, int num) {
        sudokuGrid[row][col] = num;
    }

    private void showGrid() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                buttons[r][c].setText(sudokuGrid[r][c] == 0 ? "" : Integer.toString(sudokuGrid[r][c]));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        java.awt.EventQueue.invokeLater(Sudoku::new);
    }
}