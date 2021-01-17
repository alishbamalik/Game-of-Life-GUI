/*
 * Modified Game of Life
 * Alishba Malik
 * November 29, 2019
 * Displays a two-dimensional plane of cells that can either contian a creature or be empty
 * The state of a cell is determined by the number of creatures around that cell
 */
 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main{
  GameOfLife game;
  
    public Main(){
        game = new GameOfLife();
    }
    /** 
     * create and show GUI
     */
    private static void runGUI(){
        JFrame.setDefaultLookAndFeelDecorated(true);
        Main playGame = new Main();
    }
    
    public static void main(String[] args){
        /* methods that create and show a GUI should be run from an event dispatching thread*/
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run(){
                runGUI();
            }
        });
    }
}