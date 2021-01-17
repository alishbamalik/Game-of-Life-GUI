
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

/**
 * Game Of Life Class
 */
public class GameOfLife implements ActionListener{
    JFrame frame;
    JPanel contentPane, gridPane, bottomPane1, bottomPane2, bottomPane3, bottomPane4;
    JLabel titleLabel, endingLabel;
    JComboBox presets;
    JTextField presetNameInput;
    JButton saveButton, goButton, quitButton;
    JButton[][] board;
    
    endGame endGame;
    NewGeneration newGeneration;
    selectPreset selectPreset;
    savePreset savePreset;
    
    File presetFile;
    FileWriter outStream;
    BufferedWriter writeText;
    FileReader inStream;
    BufferedReader readText;
    
    boolean canClick;
    
    public GameOfLife(){
        presetFile = new File("presets.txt");
        endGame = new endGame();
        newGeneration = new NewGeneration();
        selectPreset = new selectPreset();
        savePreset = new savePreset();
    
        /* creating frame */
        frame = new JFrame("- Game Of Life -");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        /* creating panel */
        contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        contentPane.setBorder(BorderFactory.createEmptyBorder(30, 30, 20, 30));
        contentPane.setBackground(Color.darkGray);
        
        /* creating title */
        titleLabel = new JLabel("| Game Of Life |");
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        titleLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.white);
        contentPane.add(titleLabel);
        
        /* creating 20 by 20 grid */
        gridPane = new JPanel();
        gridPane.setLayout(new GridLayout(0, 20, 5, 5));
        gridPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gridPane.setBackground(Color.darkGray);
        
        /*creating array to represent board*/
        board = new JButton[20][20];
        
        /*creating buttons to add to the array*/
        JButton b;
        canClick = true;
        for (int row = 0; row < board.length; row++){
            for (int col = 0; col < board[0].length; col++){
                b = new JButton("O");
                b.addActionListener(this);
                b.setForeground(Color.black);
                b.setAlignmentX(JButton.CENTER_ALIGNMENT);
                b.setBorder(BorderFactory.createLineBorder(Color.black, 1));
                board[row][col] = b;
                gridPane.add(b);
            }
        }
        contentPane.add(gridPane);
        
        /*creating bottom pane1 which contains combobox*/
        bottomPane1 = new JPanel();
        bottomPane1.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        bottomPane1.setBackground(Color.darkGray);
        
        /*creating combo box*/
        String textLine = "";
        presets = new JComboBox();
        
        /*setting presets*/
        try {
            inStream = new FileReader(presetFile);
            readText = new BufferedReader(inStream);
            readText.mark(0);
            readText.reset();
            
            /*setting default preset*/
            textLine = readText.readLine();
            presets.addItem(textLine);
            
            /*setting rest of presets*/
            while (textLine != null){
                for (int lineNumber = 0; lineNumber < 21; lineNumber++){
                    textLine = readText.readLine();
                }
                if (textLine != null){
                    presets.addItem(textLine);
                }
            } 
            
            readText.close();
            inStream.close();
        } catch (FileNotFoundException e){
            System.out.println("File could not be found.");
            System.err.println("FileNotFoundException: " + e.getMessage());
        } catch (IOException e){
            System.out.println("File could not been read.");
            System.err.println("IOException: " + e.getMessage());
        }
        
        presets.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
        presets.setSelectedIndex(0);
        presets.addActionListener(selectPreset);
        bottomPane1.add(presets);
        
        /*creating bottom pane2 which contains textfield and save button*/
        bottomPane2 = new JPanel();
        bottomPane2.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        bottomPane2.setBackground(Color.darkGray);
        
        /*creating text field for preset name*/
        presetNameInput = new JTextField(10);
        presetNameInput.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        presetNameInput.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        bottomPane2.add(presetNameInput);
        
        /*creates save button*/
        saveButton = new JButton("Save");
        saveButton.addActionListener(savePreset);
        saveButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        bottomPane2.add(saveButton);
        
        /*creating bottom pane 3 which contains go button and quit button*/
        bottomPane3 = new JPanel();
        bottomPane3.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        bottomPane3.setBackground(Color.darkGray);
        
        /*creates go button*/
        goButton = new JButton("Go");
        goButton.addActionListener(newGeneration);
        goButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        bottomPane3.add(goButton);
        
        /*creates quit button*/
        quitButton = new JButton("Quit");
        quitButton.addActionListener(endGame);
        quitButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        bottomPane3.add(quitButton);
        
        /*creating bottom pane 4 which contains ending label*/
        bottomPane4 = new JPanel();
        bottomPane4.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        bottomPane4.setBackground(Color.darkGray);
        
        /*creating ending label*/
        endingLabel = new JLabel(" ");
        endingLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        endingLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        endingLabel.setForeground(Color.white);
        bottomPane4.add(endingLabel);
        
        contentPane.add(bottomPane1);
        contentPane.add(bottomPane2);
        contentPane.add(bottomPane3);
        contentPane.add(bottomPane4);
        
        frame.setContentPane(contentPane);
        frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * handles user selections of first generation
     * pre: none
     * post: displays 'x' in the cells that the user selected to be alive 
     */
    public void actionPerformed(ActionEvent event){
        if (canClick){
            JButton buttonClicked = (JButton)event.getSource();

            if((buttonClicked.getText()).equals("O")){ //changing cell from dead to alive
                buttonClicked.setText("X");
                buttonClicked.setForeground(Color.white);
            } else if ((buttonClicked.getText()).equals("X")){  //changing cell from alive to dead
                buttonClicked.setText("O");
                buttonClicked.setForeground(Color.black);
            }
        }
    }
    
    class NewGeneration implements ActionListener{
        
        /** 
         * handles next button clicked
         * pre: none
         * post: the new generation has been calculated and displayed
         */
        public void actionPerformed(ActionEvent event){
            int i = -1;
            int neighbours;
            int changeCount; //keeps track of the amount of cells being changed so when it remains 0 the game is done
            
//do {
                changeCount = 0;
                /*testing cells to come up with new generation*/
                for (int row = 0; row < board.length; row++){
                    for (int col = 0; col < board[0].length; col++){
                        neighbours = 0;

                        /*checking how many cells are around*/
                        for (int x = row-1; x <= row+1; x++){                        
                            for (int y = col-1; y <= col+1; y++){
                                if(cellAvailable(x,y)){                         //if the index is not out of bound
                                    if((board[x][y].getText()).equals("X")){
                                        if(x != row && y != col){               //if the element is not the one we are checking
                                            neighbours += 1;
                                        }
                                    }
                                }
                            }
                        }
                        
                        /* if cell is alive*/
                        if ((board[row][col].getText()).equals("X")){
                            /*making cell alive or dead depending on number of neighbours*/
                            if (neighbours == 2 || neighbours == 3){
                                board[row][col].setText("X");
                                board[row][col].setForeground(Color.white);
                            } else {
                                board[row][col].setText("O");
                                board[row][col].setForeground(Color.black);
                                changeCount += 1;
                            }

                        /* if cell is dead */    
                        } else if ((board[row][col].getText()).equals("O")){
                            if (neighbours == 3){
                                board[row][col].setText("X");
                                board[row][col].setForeground(Color.white);
                                changeCount += 1;
                            } else {
                                board[row][col].setText("O");
                                board[row][col].setForeground(Color.black);
                            }
                        }
                    }
                }
                //for(double p=0; p<10000000; p+=0.1);
//} while (changeCount != 0);                     //if cells are no longer changing then the game is over
            endGame.end();
            canClick = true;
        }
        
        /**
        * Checks if an index in the 2d array is out of bounds
        * pre: none
        * post: true is returned if the index is not out of bounds and false is returned if the index is out of bounds
        */
        private boolean cellAvailable(int x, int y){
            if(x < 0 || x >= board.length || y < 0 || y >= board[0].length){            //if the x or y values are out of bound
                return false;
            } else {                                                                    //defaults to the x and y values not being out of bound
                return true;
            }
        }
    }
    
    class endGame implements ActionListener{
        /**
         * handles quit button clicked
         * pre: none
         * post: the number of remaining live cells are displayed
         * no more cells can be selected and another generation can not be generated
         */
        public void actionPerformed(ActionEvent event){
            end();
            //user can no longer play game
            goButton.removeActionListener(newGeneration);
            saveButton.removeActionListener(savePreset);
            presets.removeActionListener(selectPreset);
        }
        
        /**
         * ends game by displaying how many cells are left alive
         * pre: none
         * post: the number of cells left alive are displayed
         */
        public void end(){
            canClick = false; // user can no longer play 
            int liveCellsCount = 0;
            for (int row = 0; row < board.length; row++){
                for (int col = 0; col < board[0].length; col++){
                    if((board[row][col].getText()).equals("X")){
                        liveCellsCount += 1;
                    }
                }
            }
            /*displays how many cells are left*/
            endingLabel.setText("There are " + liveCellsCount + " live cells left.");
        }
    }
    
    class selectPreset implements ActionListener{
        /** handles the combo box of presets
         * pre: none
         * post: preset is loaded
         */
        public void actionPerformed(ActionEvent event){
            JComboBox comboBox = (JComboBox)event.getSource();
            String presetName = (String)comboBox.getSelectedItem();
            String textLine = "";
            
                try {
                    /* reading file to find selected preset*/
                    inStream = new FileReader(presetFile);
                    readText = new BufferedReader(inStream);
                    readText.mark(0);
                    readText.reset();

                    do {
                        textLine = readText.readLine();
                    } while (!textLine.equals(presetName));
                    
                    /*going through preset pattern*/
                    for (int row = 0; row < board.length; row++){
                        textLine = readText.readLine();
                        for (int col = 0; col < board[0].length; col++){
                            board[row][col].setText(Character.toString(textLine.charAt(col)));
                            if(board[row][col].getText().equals("X")){
                                board[row][col].setForeground(Color.white);
                            } else {
                                board[row][col].setForeground(Color.black);
                            }
                        }
                    }
                    readText.close();
                    inStream.close();
                } catch (FileNotFoundException e){
                    System.out.println("File could not be found.");
                    System.err.println("FileNotFoundException: " + e.getMessage());
                } catch (IOException e){
                    System.out.println("File could not been read.");
                    System.err.println("IOException: " + e.getMessage());
                }
                
        }
    }
    
    class savePreset implements ActionListener{
        /** handles the save button
         * pre: none
         * post: preset is loaded
         */
        public void actionPerformed(ActionEvent event){
            String presetName = presetNameInput.getText();
            presets.addItem(presetName);
            
            try {
                outStream = new FileWriter(presetFile, true);
                writeText = new BufferedWriter(outStream);
                
                /*writing preset into file*/
                writeText.newLine();
                writeText.write(presetName);
                
                for(int row = 0; row < board.length; row++){
                    writeText.newLine();
                    for (int col = 0; col < board[0].length; col++){
                        writeText.write(board[row][col].getText());
                    }
                }
                writeText.close();
                outStream.close();
            } catch (FileNotFoundException e){
                System.out.println("File could not be found.");
                System.err.println("FileNotFoundException: " + e.getMessage());
            } catch (IOException e){
                System.out.println("File could not been read.");
                System.err.println("IOException: " + e.getMessage());
            }
        }
    }
}