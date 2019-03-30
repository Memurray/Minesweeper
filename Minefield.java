import java.util.ArrayList;
import java.util.Collections;
import java.awt.*;
import javax.swing.*;

public class Minefield  {
    private int[][] fieldArray;  //2-d array for tracking position information
    private int gridSize;
    private int totalSize;
    private int bombs;
    private int revealed;

    //game logic constructor
    //initialises variables and randomizes the board
    public Minefield(int inSize, int nBombs) {
        fieldArray = new int[inSize][inSize];
        gridSize = inSize;
        totalSize = inSize * inSize;
        revealed=0;
        bombs = nBombs;
        randomize();
    }

    private void randomize(){
        ArrayList<Integer> toShuffle = new ArrayList<Integer>();
        for (int i = 0; i < bombs; i++){  //adds to arraylist values of -1 equal to number of bombs
            toShuffle.add(-1);
        }
        for (int i = 0; i < totalSize-bombs; i++){
            toShuffle.add(0);  //adds the rest of the values for the other spaces, they arent evaluated yet so they're just 0
        }
        Collections.shuffle(toShuffle);   //shuffle the arraylist
        for (int i = 0; i < gridSize; i++){ //stick the 1-d array into a 2-d array
            for (int j = 0; j < gridSize; j++){
                fieldArray[i][j] = toShuffle.get(i*gridSize+j);
            }
        }
        evaluate();  //evaluate the number of bombs near each square
    }

    private void evaluate() {  //for all elements of the grid
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (fieldArray[i][j] == -1){  //if the element is a bomb
                    for (int m = -1; m < 2; m++) {  //look at all valid neighbors
                        if(i+m >= 0 && i+m < gridSize){  //check if y coord is valid
                            for (int n = -1; n < 2; n++) {
                                if((j+n >= 0) && (j+n < gridSize) && (fieldArray[i+m][j+n] != -1) ){  //check if x coord is valid and that current item isnt a bomb
                                    fieldArray[i+m][j+n] ++; //increment the current item's number of promixal bombs
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //recursively called function for clearing functionality
    //All elements inspected will be revealed, but only 0 bomb squares will trigger recursive calls, inspired by fill algorithm explained in class during exam prep
    public void chainReveal(int i, ArrayList<JButton> button ){
        int row = i/gridSize;  //translate 1-d array index to 2-d array indices
        int col = i%gridSize;
        (button.get(i)).setBackground(Color.GRAY);  //all buttons shown turn grey
        if(fieldArray[row][col] != 0)  //if the button has proximal bombs, count text is blue
            (button.get(i)).setForeground(Color.BLUE);
        (button.get(i)).setText(Integer.toString(fieldArray[row][col]));
        revealed++;  //track the number of tiles revealed
        if(fieldArray[row][col] == 0) {  //if current button is 0, run chainReveal on all valid neighbors
            for (int m = -1; m < 2; m++) {
                if (row + m >= 0 && row + m < gridSize) {
                    for (int n = -1; n < 2; n++) {
                        int toSet = i + m * gridSize + n;  //translate 2-d indices back to 1-d
                        if ((col + n >= 0) && (col + n < gridSize)  && ((button.get(toSet)).getText() == "")) {  //if x coord is valid and neighbor hasnt been revealed, run new instance of chainReveal
                            chainReveal(toSet, button);
                        }
                    }
                }
            }
        }
    }

    //What to do when bomb class sends off a validated game board button click
    public boolean clickLogic (int i, ArrayList<JButton> button ){
        int row = i/gridSize; //get 2-d array coords from 1-d array index
        int col = i%gridSize;
        if(fieldArray[row][col] == 0) {  //if clicked has no bomb neighbors trigger clearing operation
            chainReveal(i, button);
        }
        else if(fieldArray[row][col] == -1) { //if button click was a bomb
            for (int m = 0; m < gridSize; m++) { //reveal the whole game board
                for (int n = 0; n < gridSize; n++) {
                    int toSet = m*gridSize+n;
                    (button.get(toSet)).setText(Integer.toString(fieldArray[m][n]));
                    (button.get(toSet)).setBackground(Color.GRAY);
                    if(fieldArray[m][n] == 1)  //bomb neighbors are blue
                        (button.get(toSet)).setForeground(Color.BLUE);
                    else if(fieldArray[m][n] == -1) {  //bombs are red and are just large font asterisks
                        (button.get(toSet)).setForeground(Color.RED);
                        (button.get(toSet)).setFont(new Font("Arial", Font.PLAIN, 30));
                        (button.get(toSet)).setMargin(new Insets(15,1,1,1));
                        (button.get(toSet)).setText("*");
                    }
                }
            }
            return true;  //short circuit for game lose return
        }
        else {  //if square is a bomb neighbor
            (button.get(i)).setText(Integer.toString(fieldArray[row][col]));  //show the bomb neighbor count
            (button.get(i)).setForeground(Color.BLUE);  //text is blue
            revealed++;
        }
        return false;  //since i return true on game loss, I need to return true else
    }

    public boolean winCheck(){ //check if the game has been won
        if(revealed + bombs == totalSize)  //if the number of squares revealed + bombs is total game size, you win. Due to ordering of calls, this will be preempted by loss determination
            return true;
        return false;
    }
}