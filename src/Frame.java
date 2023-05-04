import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Frame extends JFrame {
    
    JButton loadFileButton, pathfindButton, selectFileButton;
    JSpinner numSpinner;
    JSpinner[][] spinners;
    JFileChooser fileChooser;
    JTextArea textArea;
    String chosenFile;
    File inFile, outFile;
    PrintStream printStream;

    public Frame() {
        super();
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        initNumSpinner();
        initSpinners((int) numSpinner.getValue()); //should default to default value of numSpinner
        initLoadFileButton();
        initSelectFileButton();
        initPathfindButton();
        initTextArea();
        setVisible(true);
        printStream = System.out;
    }

    public void initSelectFileButton() {
        selectFileButton = new JButton("Select File");

        selectFileButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                initFileChooser();
                outFile = new File(chosenFile);
            }
            
        });

        selectFileButton.setBounds(350, 10, 100, 20);
        add(selectFileButton);
    }

    public void initTextArea() {
        textArea = new JTextArea();
        textArea.setBounds(750, 10, 750, 800);
        add(textArea);
        
    }

    public void initPathfindButton() {
        pathfindButton = new JButton("Pathfind");

        pathfindButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Pair<int[], double[]> paths = Pathfind.dijkstras(getGraph());
                try {
                    Pathfind.output(paths, System.out, textArea, outFile);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            
        });

        pathfindButton.setBounds(100, 10, 100, 20);
        add(pathfindButton);
    }

    public void initLoadFileButton() {
        loadFileButton = new JButton("Load File");

        loadFileButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                initFileChooser();
                inFile = new File(chosenFile);
            }
            
        });

        loadFileButton.setBounds(250, 10, 100, 20);
        add(loadFileButton);
    }

    public void initFileChooser() {
        System.out.println("Load File Button Clicked");
        fileChooser = new JFileChooser();
        fileChooser.showOpenDialog(this);
        chosenFile = fileChooser.getSelectedFile().getPath();
        System.out.println("Chosen File: " + chosenFile);
        loadFile();
    }

    public void loadFile() {

    }

    public void initNumSpinner() {
        numSpinner = new JSpinner(new SpinnerNumberModel(4, 1, 16, 1));
        numSpinner.setBounds(10, 10, 80, 20);

        numSpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {

                System.out.println( "New N: \t" + (int) numSpinner.getValue());
                removeSpinners();
                initSpinners( (int) numSpinner.getValue());
                revalidate();
                repaint();

            }

        });

        add(numSpinner);
    }

    public void removeSpinners() {
        if (spinners == null) {
            System.out.println("Spinners unitialized");
            return;
        }
        for (int i = 0; i < spinners.length; ++i) {
            for (int j = 0; j < spinners[i].length; ++j) {
                remove(spinners[i][j]);
            }
        }
    }

    public void initSpinners(int n) {
        JSpinner[][] tempArr = new JSpinner[n][n];
        int yloc, xloc;

        yloc = 40;
        for (int i = 0; i < n; ++i) {
            yloc += 30;
            xloc = 50;
            for (int j = 0; j < n; ++j) {

                if (spinners != null && (i < spinners.length && j < spinners.length)) {
                    //copy value from spinners[i][j]
                    tempArr[i][j] = new JSpinner(new SpinnerNumberModel((double) spinners[i][j].getValue(), 0, Double.MAX_VALUE, 1.0));
                } else if (i == j) {
                    //initialize to 0 on diagonal
                    tempArr[i][j] = new JSpinner(new SpinnerNumberModel(0.0, 0, Double.MAX_VALUE, 1.0));
                } else if (i > j) {
                    //initialize to 0 on lower triangle
                    tempArr[i][j] = new JSpinner(new SpinnerNumberModel(0.0, 0, Double.MAX_VALUE, 1.0));
                } else {
                    //initialize to 1 on upper triangle
                    tempArr[i][j] = new JSpinner(new SpinnerNumberModel(1.0, 0, Double.MAX_VALUE, 1.0));
                }
                tempArr[i][j].setBounds(xloc, yloc, 40, 20);
                add(tempArr[i][j]);
                xloc += 40;
            }
        }

        spinners = tempArr;
    }

    public Graph getGraph() {
        double[][] temp = new double[spinners.length][spinners.length];

        for (int i = 0; i < spinners.length; ++i) for (int j = 0; j < spinners.length; ++j) {
            temp[i][j] = (double) spinners[i][j].getValue();
        }

        return new Graph(temp);
    }

}
