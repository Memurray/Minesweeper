//General Comments
//****************
//Smiley button is just a shortcut for restart the game, it doesn't do anything else
//Bombs are displayed as large red text asterisks
//New game and setup game both discard old game window and create a new one, though setup does wait until you click ok
//so if you change your mind you can just X out of the setup window and nothing gets changed.


import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;

//General UI class
public class Bombs extends JFrame implements ActionListener{
    private JPanel field = new JPanel();  //panel for the minefield
    private JPanel topPanel = new JPanel();  //smilely button and timer panel
    private Minefield MF;  //define the Minefield object that controls game logic
    private JButton resetButton;  //reset button is the smilely button
    private JLabel timerLabel;
    private Container c;
    private ArrayList<JButton> button; // ArrayList of Buttons (bomb tiles)
    private int gridSize,totalSize,bombs;  //potentially redundant in some cases but I don't mind having easy access to this information
    private BombSetup b;  //setupPanel object associated with gamestate
    private int time = 0;  // clock timer time
    private Timer gameTimer;  //timer object
    private boolean funFlag = true;  //dumb feature explained below in facepalm() method
    private ImageIcon smile = new ImageIcon(getClass().getResource("smile.jpg"));  //it's the smiley face image

    //build Game based on input arguments for size and number of bombs
    public Bombs(int fieldSize, int nBombs){
        super("Bombs");  //name window
        bombs = nBombs;
        gridSize = fieldSize;
        totalSize = gridSize * gridSize;  //precalculate 1-D array size

        JMenu gameMenu = new JMenu( "Game" ); // create game options menu

        //Add new option
        JMenuItem newItem = new JMenuItem( "New" );
        newItem.setMnemonic( 'N' );
        gameMenu.add( newItem );

        //Add setup option
        JMenuItem setupItem = new JMenuItem( "Setup" );
        setupItem.setMnemonic( 'S' );
        gameMenu.add( setupItem );
        gameMenu.addSeparator();

        //Add exit option
        JMenuItem exitItem = new JMenuItem( "Exit" ); // create exit item
        exitItem.setMnemonic( 'x' ); // set mnemonic to x
        gameMenu.add( exitItem ); // add exit item to file menu

        //Add help option
        JMenuItem helpItem = new JMenuItem( "Help" );

        //What to do if user clicks new option
        newItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); //kill the old game
                Bombs MSG = new Bombs(gridSize,bombs);  //make a new one with same design specs
                MSG.addWindowListener(  //listen for window close
                        new WindowAdapter(){
                            public void windowClosing(WindowEvent e)
                            {
                                System.exit(0);
                            }
                        }
                );
            }
        });

        //What to do if user clicks setup option
        setupItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               BombSetup b = new BombSetup(gridSize,bombs,Bombs.this);  //makes a new window with options for changing game
            }
        });

        //What to do if user clicks exit option
        exitItem.addActionListener(new ActionListener(){
                    public void actionPerformed( ActionEvent event ) {
                        System.exit( 0 ); // exit application
                    }
            }
        );

        //What to do if user clicks help option
        helpItem.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent event ){
                        JOptionPane.showMessageDialog( Bombs.this,
                                "Game defaults to 8x8 with 15 bombs\nClicking the smilely resets the game using current config\nUse setup to change game setup.",
                                "Help", JOptionPane.PLAIN_MESSAGE );
                    }
                }
        );

        //defines the actual option bar
        JMenuBar bar = new JMenuBar(); // create menu bar
        helpItem.setMaximumSize(new Dimension(40,40));  //I didnt like default behavior of help menu automatically filling the remainder menu bar and Box.glue didnt do what I wanted either so I did this
        setJMenuBar( bar ); // add menu bar to application
        bar.add( gameMenu);
        bar.add( helpItem);

        resetButton = new JButton();
        resetButton.setIcon(smile);

        button = new ArrayList<JButton>();  //

        //What to do when user clicks the smiley
        resetButton.addActionListener(new ActionListener() { //resets to default construction values
            public void actionPerformed(ActionEvent e) {
                time =0;
                funFlag = true;
                gameTimer.stop();
                for (int i = 0; i < totalSize; i++) {
                    (button.get(i)).setText("");
                    (button.get(i)).setBackground(Color.LIGHT_GRAY);
                    (button.get(i)).setForeground(Color.BLACK);
                    (button.get(i)).setMargin(new Insets(1,1,1,1));
                    (button.get(i)).setFont(new Font("Arial", Font.PLAIN, 16));
                }
                MF = new Minefield(fieldSize,nBombs);
                timerLabel.setText("Time: " + Integer.toString(time));
                repaint();
            }
        });

        field.setLayout(new GridLayout(gridSize, gridSize, 2, 2)); //force square layout
        timerLabel = new JLabel("Time: " + Integer.toString(time), SwingConstants.CENTER);
        topPanel.setLayout(new GridLayout(1,3,0,0));
        topPanel.add(timerLabel);
        topPanel.add(resetButton);
        topPanel.add(new JLabel());
        setButtons();
        MF = new Minefield(fieldSize,nBombs);

        //Timer ticks every 1 second while active, updating the timer display
        gameTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                time++;
               timerLabel.setText("Time: " + Integer.toString(time));
               repaint();
            }
        });


        c = getContentPane();
        c.add(field, BorderLayout.CENTER); //add panels to frame
        c.add(topPanel, BorderLayout.NORTH);
        setSize(gridSize*40, gridSize*40+50);  //size based on alloting pixels per grid size
        setResizable(false);  //I dont want the window to be resizable, this does get a little annoying for a 3x3 grid but otherwise I'm happy
        setVisible(true);
    }

    //basic formatting options configured for each button
    public void setButtons(){
        for (int i = 0; i < totalSize; i++) {
            button.add(new JButton()); //card starts face down
            (button.get(i)).setMargin(new Insets(1,1,1,1));
            (button.get(i)).setBackground(Color.LIGHT_GRAY);
            (button.get(i)).setForeground(Color.BLACK);
            (button.get(i)).setFont(new Font("Arial", Font.PLAIN, 16));
            (button.get(i)).addActionListener(this);  //per button listener
            field.add(button.get(i));  //add the button to the board
        }
    }

    //End of game display for losing
    public void lose(){
        gameTimer.stop();
        JOptionPane.showMessageDialog( Bombs.this, "You Lose" );
    }

    //technically losing the game after winning originated as a bug, but I thought it would be amusing to turn it into a feature.
    public void facepalm(){
        gameTimer.stop();
        JOptionPane.showMessageDialog( Bombs.this, "So, you knowingly clicked a bomb after the game was over? \n...\n I take it back, you lose." );
    }

    //End of game display for winning
    public void win(){
        gameTimer.stop();
        funFlag = false;
        JOptionPane.showMessageDialog( Bombs.this, "You Win" );
    }

    //Registering gamespace button clicks
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < totalSize; i++) {
            if (e.getSource() == button.get(i) && (button.get(i)).getText() == ""){//Since all of my clicked buttons have text, logic of isPressed simplified to whether that text wasn't blank
                gameTimer.start();
               (button.get(i)).setBackground(Color.GRAY); //you click it turns grey
                if(MF.clickLogic(i,button)) {  //pass button press off to game logic, doubled up function by giving it a return value when game is lost
                    if (funFlag)  //are we still having fun?
                            lose();
                    else //if the player has already won the game but decides to click on the bomb, they get to lose anyways.
                        facepalm();
                }
                else if(MF.winCheck())  //every turn check if won game
                    win();
            }
        }
        repaint();
    }

    public static void main(String args[])  {
        Bombs MSG = new Bombs(8,15); //Game opens as a 8x8 with 15 bombs
        MSG.addWindowListener(  //listen for window close
                new WindowAdapter(){
                    public void windowClosing(WindowEvent e)
                    {
                        System.exit(0);
                    }
                }
        );
    }
}