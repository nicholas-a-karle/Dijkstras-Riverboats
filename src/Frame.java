import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Frame extends JFrame {
    
    JButton loadFileButton, pathfindButton, selectFileButton;
    JSpinner numSpinner;
    JSpinner[][] spinners;
    JFileChooser fileChooser;
    JCheckBox upperBox, diagonalBox, lowerBox;

    JTextArea textArea;
    PrintStream printStream;
    String chosenFile;
    File inFile, outFile;

    Graph graph;
    boolean graphDisplayable, upper, diagonal, lower;

    final int DEF_VAL_MAT_SIZE = 4;
    final int DEF_VAL_MAT_SIZE_MIN = 1;
    final int DEF_VAL_MAT_SIZE_MAX = 16;
    final int DEF_VAL_MAT_SIZE_SS = 1;

    public Frame() throws IOException {
        super();
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        initOutputFile();
        initCheckboxes();
        initNumSpinner();
        initSpinners((int) numSpinner.getValue()); //should default to default value of numSpinner
        initLoadFileButton();
        initSelectFileButton();
        initPathfindButton();
        initTextArea();
        setVisible(true);
        setGraph();
        graphDisplayable = true;
        printStream = System.out;
        
    }

    public void initCheckboxes() {
        upper = true;
        diagonal = false;
        lower = false;
        upperBox = new JCheckBox("Upper Matrix", upper);
        diagonalBox = new JCheckBox("Diagonal Matrix", diagonal);
        lowerBox = new JCheckBox("Lower Matrix", lower);
        upperBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {//checkbox has been selected
                    upper = true;
                } else {//checkbox has been deselected
                    upper = false;
                };
            }
        });
        diagonalBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {//checkbox has been selected
                    diagonal = true;
                } else {//checkbox has been deselected
                    diagonal = false;
                };
            }
        });
        lowerBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {//checkbox has been selected
                    lower = true;
                } else {//checkbox has been deselected
                    lower = false;
                };
            }
        });

        upperBox.setBounds(50, 35, 150, 30);
        diagonalBox.setBounds(200, 35, 150, 30);
        lowerBox.setBounds(350, 35, 150, 30);
        add(upperBox);
        add(diagonalBox);
        add(lowerBox);
    }

    public void readInputFile() throws Exception {
        BufferedReader r = new BufferedReader(new FileReader(inFile));
        
        ArrayList<String> linesBuffer = new ArrayList<String>();

        //get all lines in a buffer
        String buffer = "";
        while ((buffer = r.readLine()) != null) {
            linesBuffer.add(buffer);
        }

        //linesBuffer[0] should represent linesBuffer.size() - 2
        //if it does not, error

        int n = Integer.parseInt(linesBuffer.get(0).trim());

        System.out.println("n = " + n + " \tsize = " + linesBuffer.size());
        //if (n < linesBuffer.size() - 2) throw new Exception("Incorrect Input File Format");

        double[][] arr = new double[n][n];

        int m; //number of values in line
        double[] tmp; //parsed line
        int init = (upper || diagonal) ? 0 : 1;
        int end = (diagonal || lower) ? n: n-1;
        for (int i = init; i < end; ++i) {

            //determine how many entries should exist in this array in the input file
            if (lower && diagonal && upper) {
                m = n;
            } else {
                if (diagonal) m = 1;
                else m = 0;
                if (lower) m += i - 1;
                if (upper) m += n - i - 1;
            }

            tmp = parseString(linesBuffer.get(i + 1), m);

            for (int j = 0; j < m; ++j) {
                arr[i][j + n - m] = tmp[j];
            }
        }

        graph = new Graph(arr);

        //porting these values to the visual
        System.out.println( "New N: \t" + ((n < DEF_VAL_MAT_SIZE_MAX) ? n : DEF_VAL_MAT_SIZE_MAX));
        numSpinner.setValue(((n < DEF_VAL_MAT_SIZE_MAX) ? n : DEF_VAL_MAT_SIZE_MAX));
        removeSpinners();
        initSpinners(((n < DEF_VAL_MAT_SIZE_MAX) ? n : DEF_VAL_MAT_SIZE_MAX));

        //porting more values to the visual
        for (int i = 0; i < (int) numSpinner.getValue(); ++i) {
            for (int j = 0; j < (int) numSpinner.getValue(); ++j) {
                spinners[i][j].setValue(arr[i][j]);
            }
        }

        revalidate();
        repaint();
    }

    public double[] parseString(String str, int n) {
        //start with no space, separated by one space, ended with one space
        String[] split = str.trim().split("\s+");
        double[] arr = new double[n];

        System.out.println("Expected Num: " + n);
        for (int i = 0; i < n && i < split.length; ++i) {
            System.out.println("\"" + split[i] + "\"");
            arr[i] = Double.parseDouble(split[i]);
        }

        return arr;
    }

    public void initOutputFile() throws IOException {
        outFile = new File("output.txt");
        outFile.createNewFile();
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
                if (graphDisplayable) {
                    setGraph();
                } else graphDisplayable = true; //next time you click pathfind, the loaded file will default to displayed info
                Pair<int[], double[]> paths = Pathfind.dijkstras(graph);
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
                try {
                    readInputFile();
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            
        });

        loadFileButton.setBounds(250, 10, 100, 20);
        add(loadFileButton);
    }

    public void initFileChooser() {
        System.out.println("Load File Button Clicked");
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("./lib"));
        fileChooser.showOpenDialog(this);
        chosenFile = fileChooser.getSelectedFile().getPath();
        System.out.println("Chosen File: " + chosenFile);
    }

    public void initNumSpinner() {
        numSpinner = new JSpinner(
            new SpinnerNumberModel(DEF_VAL_MAT_SIZE, DEF_VAL_MAT_SIZE_MIN, DEF_VAL_MAT_SIZE_MAX, DEF_VAL_MAT_SIZE_SS)
            );
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
                int def = 0;
                if (spinners != null && (i < spinners.length && j < spinners.length)) {
                    //copy value from spinners[i][j]
                    tempArr[i][j] = new JSpinner(new SpinnerNumberModel((double) spinners[i][j].getValue(), 0, Double.MAX_VALUE, 1.0));
                } else if (i == j) {
                    //initialize on diagonal
                    def = (diagonal) ? 1 : 0;
                    tempArr[i][j] = new JSpinner(new SpinnerNumberModel(def, 0, Double.MAX_VALUE, 1.0));
                } else if (i > j) {
                    //initialize on lower triangle
                    def = (lower) ? 1 : 0;
                    tempArr[i][j] = new JSpinner(new SpinnerNumberModel(def, 0, Double.MAX_VALUE, 1.0));
                } else {
                    //initialize on upper triangle
                    def = (upper) ? 1 : 0;
                    tempArr[i][j] = new JSpinner(new SpinnerNumberModel(def, 0, Double.MAX_VALUE, 1.0));
                }
                tempArr[i][j].setBounds(xloc, yloc, 40, 20);
                add(tempArr[i][j]);
                xloc += 40;
            }
        }

        spinners = tempArr;
    }

    public void setGraph() {
        double[][] temp = new double[spinners.length][spinners.length];

        for (int i = 0; i < spinners.length; ++i) for (int j = 0; j < spinners.length; ++j) {
            temp[i][j] = (double) spinners[i][j].getValue();
        }

        graph = new Graph(temp);
    }

}
