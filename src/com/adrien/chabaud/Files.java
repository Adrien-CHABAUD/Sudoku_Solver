/*;==========================================
; Author: Adrien CHABAUD - B00376254
; Date:   Avr. 2020
;==========================================*/
package com.adrien.chabaud;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

class Files {

    private int[][] tmpArray = new int[9][9];

    void saveFile(final int[][] gridArray){
        String file = saveWindow();
        if(file != null){
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(file + ".txt"));

                for (int[] ints : gridArray) {
                    StringBuilder line = new StringBuilder();
                    for (int c = 0; c < gridArray.length; c++) {
                        line.append(Integer.toString(ints[c]));
                    }
                    writer.write(line.toString());
                    writer.newLine();
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    boolean loadFile() {

        String file = loadWindow();

        BufferedReader bufferedreader = null;
        FileReader filereader = null;
        try {
            filereader = new FileReader(file);
            bufferedreader = new BufferedReader(filereader);

            String strCurrentLine;
            int rowIndex = 0;
            while ((strCurrentLine = bufferedreader.readLine()) != null) {
                if(!addLineArray(strCurrentLine, rowIndex)){
                    JOptionPane.showMessageDialog(null, "Corrupt file ...");
                    return false;
                }
                rowIndex++;
            }

        } catch (IOException i) {
            i.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error with the file path");
            return false;
        } finally {
            try {
                if (bufferedreader != null)
                    bufferedreader.close();
                if (filereader != null)
                    filereader.close();
            } catch (IOException r) {
                r.printStackTrace();
            }
        }
        return true;
    }

    private String loadWindow(){

        //Show the current directory
        File currentDirectory = null;
        try {
            //Get a File object which contains the current directory
            currentDirectory = new File(".").getCanonicalFile();
            System.out.println("Current Repertory : " + currentDirectory);
        } catch(IOException ignored) {}

        //creation of the dialog box in this current directory
        JFileChooser dialogue = new JFileChooser(currentDirectory);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt","txt");
        dialogue.setFileFilter(filter);

        //Show the dialog box
        dialogue.showOpenDialog(null);

        //Get the selected file
        System.out.println("Chosen file : " + dialogue.getSelectedFile());

        return dialogue.getSelectedFile().getAbsolutePath();
    }

    private String saveWindow(){
        JFrame parentFrame = new JFrame();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");

        int userSelection = fileChooser.showSaveDialog(parentFrame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            System.out.println("Save as file: " + fileToSave.getAbsolutePath());
            return fileToSave.getAbsolutePath();
        }
        return null;
    }

    private boolean addLineArray(String line, int rowIndex) {
        if (verifyLine(line) && rowIndex < 9) {
            for (int c = 0; c < tmpArray.length; c++) {
                tmpArray[rowIndex][c] = Character.getNumericValue(line.charAt(c));
            }
            return true;
        }
        return false;
    }

    private boolean verifyLine(String line) {
        return line.length() == 9;
    }

    void getGrid(int[][] gridArray) {
        for (int r = 0; r < gridArray.length; r++) {
            System.arraycopy(tmpArray[r], 0, gridArray[r], 0, gridArray.length);
        }
    }
}
