import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BombSetup extends JFrame {
    private int nBombs;
    private int nSize;
    private JButton beginnerB, interB, expertB, okExit;
    private JPanel buttonPanel = new JPanel();
    private JPanel sliderPanel = new JPanel();
    private JPanel labels = new JPanel();
    private JPanel titles = new JPanel();
    private JSlider sizeSlider, bombSlider;
    private JLabel bombLabel , sizeLabel;
    private JLabel bombTitle , sizeTitle;

    //Bomb setup pop up window constructor
    public BombSetup(int size, int bombs,Bombs parentBomb){
        super("Bombs Setup");
        nSize = size;
        nBombs = bombs;

        //Define button for quick setting sliders to Beginner difficulty
        beginnerB = new JButton("Beginner");
        beginnerB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sizeSlider.setValue(4);
                bombSlider.setValue(4);
            }
        });

        //Define button for quick setting sliders to Intermediate difficulty
        interB = new JButton("Intermediate");
        interB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sizeSlider.setValue(8);
                bombSlider.setValue(15);
            }
        });

        //Define button for quick setting sliders to Expert difficulty
        expertB = new JButton("Expert");
        expertB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sizeSlider.setValue(12);
                bombSlider.setValue(40);
            }
        });
        //Layout options for difficulty buttons
        buttonPanel.setLayout(new GridLayout(1,3,0,0));
        buttonPanel.add(beginnerB);
        buttonPanel.add(interB);
        buttonPanel.add(expertB);

        //labels for values in slider
        labels.setLayout(new GridLayout(2,1,0,0));
        bombLabel = new JLabel(Integer.toString(nBombs));
        sizeLabel = new JLabel(Integer.toString(nSize));
        labels.add(sizeLabel);
        labels.add(bombLabel);

        //left side titles for sliders
        titles.setLayout(new GridLayout(2,1,0,0));
        bombTitle = new JLabel("Bombs ");
        sizeTitle = new JLabel("Size ");
        titles.add(sizeTitle);
        titles.add(bombTitle);

        //slider settings
        sizeSlider = new JSlider( SwingConstants.HORIZONTAL, 4, 12, size );
        bombSlider = new JSlider( SwingConstants.HORIZONTAL, 2, (int)nSize*nSize/2, nBombs);  //max slider defined as a function of board size

        //What happens if you change the size slider
        sizeSlider.addChangeListener(new ChangeListener(){
            public void stateChanged( ChangeEvent e ){
                nSize = sizeSlider.getValue();
                sizeLabel.setText(Integer.toString(nSize));
                bombSlider.setMaximum((int)nSize*nSize/2);  //updates bomb slider's max size
                repaint();
            }
        }
        );
        //What happens if you change the bomb slider
        bombSlider.addChangeListener(new ChangeListener(){
            public void stateChanged( ChangeEvent e ){
                nBombs = bombSlider.getValue();
                bombLabel.setText(Integer.toString(nBombs));
                repaint();
            }
        }
        );
        sliderPanel.setLayout(new GridLayout(2,1,0,0));
        sliderPanel.add(sizeSlider);
        sliderPanel.add(bombSlider);
        okExit = new JButton("OK");

        //What happens if you press the ok button
        okExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parentBomb.dispose();  //closes the previous game window
                Bombs MSG = new Bombs(nSize,nBombs); //define bomb game object
                MSG.addWindowListener(  //listen for window close
                        new WindowAdapter(){
                            public void windowClosing(WindowEvent e)
                            {
                                System.exit(0);
                            }
                        }
                );
                dispose();
            }
        });

        add( buttonPanel, BorderLayout.NORTH);
        add( sliderPanel, BorderLayout.CENTER);
        add( labels, BorderLayout.EAST);
        add( titles, BorderLayout.WEST);
        add(okExit,BorderLayout.SOUTH);
        setSize(500, 200);
        setResizable(false);
        setVisible(true);
    }
}