// Decompiled by Jad v1.5.7a. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Nomogram.java

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.StringTokenizer;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;

public class Nomogram extends JApplet
{

    public Nomogram()
    {
    }

    public void init()
    {
        data = new ExpressionData();
        try
        {
            javax.swing.UIManager.LookAndFeelInfo temp[] = UIManager.getInstalledLookAndFeels();
            UIManager.setLookAndFeel(temp[2].getClassName());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        expPanel = new ExpresionPanel(this, data);
        graphPanel = new GraphPanel(this, data);
        displayPanel = new DisplayPanel(this, data);
        gui = new JTabbedPane();
        gui.add("Expression Setup", expPanel);
        gui.add("Graph Labels", graphPanel);
        gui.add("Nomogram", displayPanel);
        gui.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e)
            {
                expPanel.update(data);
                graphPanel.update(data);
                displayPanel.update(data);
            }

        });
        menu = setupMenu();
        statusField = new JTextField("Status Field");
        statusField.setEditable(false);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(menu, "North");
        getContentPane().add(gui, "Center");
        getContentPane().add(statusField, "South");
        getContentPane().setSize(width, height);
    }

    public void storeData(ExpressionData d)
    {
        data = d;
        graphPanel.update(data);
        gui.setSelectedComponent(graphPanel);
    }

    public void storeData2(ExpressionData d)
    {
        data = d;
        switch(data.exType)
        {
        case 1: // '\001'
            data.min[2] = data.constants[0] * data.min[0] * data.constants[1] * data.min[1];
            data.max[2] = data.constants[0] * data.max[0] * data.constants[1] * data.max[1];
            break;

        case 2: // '\002'
            data.min[2] = data.constants[0] * data.min[0] + data.constants[1] * data.min[1];
            data.max[2] = data.constants[0] * data.max[0] + data.constants[1] * data.max[1];
            break;

        case 3: // '\003'
            data.min[2] = (data.constants[0] * data.min[0]) / (data.constants[1] * data.max[1]);
            data.max[2] = (data.constants[0] * data.max[0]) / (data.constants[1] * data.min[1]);
            break;

        case 4: // '\004'
            data.min[2] = data.constants[0] * data.min[0] - data.constants[1] * data.max[1];
            data.max[2] = data.constants[0] * data.max[0] - data.constants[1] * data.min[1];
            break;

        case 5: // '\005'
            double alpha = Math.pow(data.constants[0] * Math.pow(data.min[0], data.exponents[0]) * data.constants[1] * Math.pow(data.min[1], data.exponents[1]), 1.0D / data.exponents[2]);
            double beta = Math.pow(data.constants[0] * Math.pow(data.max[0], data.exponents[0]) * data.constants[1] * Math.pow(data.max[1], data.exponents[1]), 1.0D / data.exponents[2]);
            double cappa = Math.pow(data.constants[0] * Math.pow(data.min[0], data.exponents[0]) * data.constants[1] * Math.pow(data.max[1], data.exponents[1]), 1.0D / data.exponents[2]);
            double delta = Math.pow(data.constants[0] * Math.pow(data.max[0], data.exponents[0]) * data.constants[1] * Math.pow(data.min[1], data.exponents[1]), 1.0D / data.exponents[2]);
            data.min[2] = Math.min(alpha, beta);
            data.min[2] = Math.min(data.min[2], cappa);
            data.min[2] = Math.min(data.min[2], delta);
            data.max[2] = Math.max(alpha, beta);
            data.max[2] = Math.max(data.max[2], cappa);
            data.max[2] = Math.max(data.max[2], delta);
            break;

        default:
            data.min[2] = 0.0D;
            data.max[2] = 0.0D;
            displayError("Invalid type");
            break;
        }
        displayPanel.draw(true, data);
        gui.setSelectedComponent(displayPanel);
    }

    public void displayError(String msg)
    {
        JOptionPane.showInternalMessageDialog(this, msg + "\nInvalid Results may Occur", "Alert", 0);
    }

    public static boolean isLog(ExpressionData dat)
    {
        switch(dat.exType)
        {
        case 1: // '\001'
        case 3: // '\003'
        case 5: // '\005'
            return true;

        case 2: // '\002'
        case 4: // '\004'
        default:
            return false;
        }
    }

    protected JMenuBar setupMenu()
    {
        JMenuBar bar = new JMenuBar();
        bar.setBackground(Color.lightGray);
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        fileMenu.setBackground(Color.lightGray);
        start = new JMenuItem("New", 78);
        open = new JMenuItem("Open", 79);
        save = new JMenuItem("Save", 83);
        print = new JMenuItem("Print", 80);
        JMenuItem exit = new JMenuItem("Exit", 120);
        start.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                data = new ExpressionData();
                graphPanel.update(data);
                expPanel.update(data);
                displayPanel.reset(data);
                gui.setSelectedComponent(expPanel);
            }

        });
        open.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                data = open();
                graphPanel.update(data);
                expPanel.update(data);
                displayPanel.reset(data);
                displayPanel.draw(true, data);
                gui.setSelectedComponent(displayPanel);
            }

        });
        save.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                save(data);
            }

        });
        print.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                PrintUtilities.printComponent(displayPanel);
            }

        });
        exit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }

        });
        fileMenu.add(start);
        fileMenu.addSeparator();
        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(print);
        fileMenu.addSeparator();
        fileMenu.add(exit);
        bar.add(fileMenu);
        JMenu points = new JMenu("Remove Lines");
        points.setBackground(Color.lightGray);
        JMenuItem first = new JMenuItem("Delete First Line");
        JMenuItem last = new JMenuItem("Delete Last Line");
        JMenuItem clear = new JMenuItem("Clear All Lines.");
        first.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                displayPanel.dequeue();
            }

        });
        last.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                displayPanel.pop();
            }

        });
        clear.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                displayPanel.clearLines();
            }

        });
        points.add(first);
        points.add(last);
        points.add(clear);
        bar.add(points);
        JMenu help = new JMenu("Help");
        help.setMnemonic('H');
        help.setBackground(Color.lightGray);
        JMenuItem doc = new JMenuItem("Documentation", 68);
        JMenuItem about = new JMenuItem("About...", 65);
        about.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog(Nomogram.this, "Created at the University of Rochester ECE Department\n                                   June 19, 2003", "About....", -1);
            }

        });
        help.add(doc);
        help.add(about);
        bar.add(help);
        return bar;
    }

    private void save(ExpressionData data)
    {
        try
        {
            JFileChooser name = new JFileChooser();
            name.showSaveDialog(null);
            PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(name.getSelectedFile())));
            output.print("Type: ");
            output.println(data.exType);
            output.print("Constants:");
            output.print(" " + data.constants[0]);
            output.print(" " + data.constants[1]);
            output.println(" " + data.constants[2]);
            output.print("Exponenets: ");
            output.print(" " + data.exponents[0]);
            output.print(" " + data.exponents[1]);
            output.println(" " + data.exponents[2]);
            output.print("Names: ");
            output.print("`" + data.name[0]);
            output.print(" `" + data.name[1]);
            output.println(" `" + data.name[2]);
            output.print("Labels: ");
            output.print("`" + data.units[0]);
            output.print(" `" + data.units[1]);
            output.println(" `" + data.units[2]);
            output.print("Min: ");
            output.print(" " + data.min[0]);
            output.print(" " + data.min[1]);
            output.println(" " + data.min[2]);
            output.print("Max: ");
            output.print(" " + data.max[0]);
            output.print(" " + data.max[1]);
            output.println(" " + data.max[2]);
            output.print("Interval: ");
            output.print(" " + data.interval[0]);
            output.print(" " + data.interval[1]);
            output.println(" " + data.interval[2]);
            output.print("Sign: ");
            output.print(" " + data.sign[0]);
            output.print(" " + data.sign[1]);
            output.println(" " + data.sign[2]);
            output.print("Direction: ");
            output.print(" " + data.flipped[0]);
            output.print(" " + data.flipped[1]);
            output.println(" " + data.flipped[2]);
            output.close();
        }
        catch(IOException e)
        {
            statusField.setText("An Error has occured while trying to save.");
            System.exit(-1);
        }
    }

    private ExpressionData open()
    {
        ExpressionData data = new ExpressionData();
        try
        {
            JFileChooser name = new JFileChooser();
            name.showOpenDialog(null);
            BufferedReader input = new BufferedReader(new FileReader(name.getSelectedFile()));
            input = new BufferedReader(new FileReader(name.getSelectedFile()));
            StringTokenizer tokens = new StringTokenizer(input.readLine());
            if(tokens.countTokens() >= 2)
            {
                tokens.nextToken();
                data.exType = Integer.parseInt(tokens.nextToken());
            }
            tokens = new StringTokenizer(input.readLine());
            if(tokens.countTokens() >= 4)
            {
                tokens.nextToken();
                data.constants[0] = Double.parseDouble(tokens.nextToken());
                data.constants[1] = Double.parseDouble(tokens.nextToken());
                data.constants[2] = Double.parseDouble(tokens.nextToken());
            }
            tokens = new StringTokenizer(input.readLine());
            if(tokens.countTokens() >= 4)
            {
                tokens.nextToken();
                data.exponents[0] = Double.parseDouble(tokens.nextToken());
                data.exponents[1] = Double.parseDouble(tokens.nextToken());
                data.exponents[2] = Double.parseDouble(tokens.nextToken());
            }
            tokens = new StringTokenizer(input.readLine(), "`");
            if(tokens.countTokens() >= 4)
            {
                tokens.nextToken();
                data.name[0] = tokens.nextToken();
                data.name[1] = tokens.nextToken();
                data.name[2] = tokens.nextToken();
            }
            tokens = new StringTokenizer(input.readLine(), "`");
            if(tokens.countTokens() >= 4)
            {
                tokens.nextToken();
                data.units[0] = tokens.nextToken();
                data.units[1] = tokens.nextToken();
                data.units[2] = tokens.nextToken();
            }
            tokens = new StringTokenizer(input.readLine());
            if(tokens.countTokens() >= 4)
            {
                tokens.nextToken();
                data.min[0] = Double.parseDouble(tokens.nextToken());
                data.min[1] = Double.parseDouble(tokens.nextToken());
                data.min[2] = Double.parseDouble(tokens.nextToken());
            }
            tokens = new StringTokenizer(input.readLine());
            if(tokens.countTokens() >= 4)
            {
                tokens.nextToken();
                data.max[0] = Double.parseDouble(tokens.nextToken());
                data.max[1] = Double.parseDouble(tokens.nextToken());
                data.max[2] = Double.parseDouble(tokens.nextToken());
            }
            tokens = new StringTokenizer(input.readLine());
            if(tokens.countTokens() >= 4)
            {
                tokens.nextToken();
                data.interval[0] = Double.parseDouble(tokens.nextToken());
                data.interval[1] = Double.parseDouble(tokens.nextToken());
                data.interval[2] = Double.parseDouble(tokens.nextToken());
            }
            tokens = new StringTokenizer(input.readLine());
            if(tokens.countTokens() >= 4)
            {
                tokens.nextToken();
                data.sign[0] = Integer.parseInt(tokens.nextToken());
                data.sign[1] = Integer.parseInt(tokens.nextToken());
                data.sign[2] = Integer.parseInt(tokens.nextToken());
            }
            tokens = new StringTokenizer(input.readLine());
            if(tokens.countTokens() >= 4)
            {
                tokens.nextToken();
                data.flipped[0] = tokens.nextToken().equals("true");
                data.flipped[1] = tokens.nextToken().equals("true");
                data.flipped[2] = tokens.nextToken().equals("true");
            }
        }
        catch(IOException e)
        {
            statusField.setText("An error has occured with the loading of a file.");
            System.exit(-1);
        }
        return data;
    }

    static int height = 650;
    static int width = 750;
    protected ExpressionData data;
    ExpresionPanel expPanel;
    GraphPanel graphPanel;
    DisplayPanel displayPanel;
    JTabbedPane gui;
    public JTextField statusField;
    JMenuBar menu;
    JMenuItem open;
    JMenuItem save;
    JMenuItem print;
    JMenuItem start;
    public static final int A = 0;
    public static final int B = 1;
    public static final int C = 2;
    public static final int ERROR = 0;
    public static final int MULTIPLICATION = 1;
    public static final int ADDITION = 2;
    public static final int DIVISION = 3;
    public static final int SUBTRACTION = 4;
    public static final int EXPONENTIAL = 5;

    static 
    {
        A = 0;
        B = 1;
        C = 2;
        ERROR = 0;
        MULTIPLICATION = 1;
        ADDITION = 2;
        DIVISION = 3;
        SUBTRACTION = 4;
        EXPONENTIAL = 5;
    }


}
