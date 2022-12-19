import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.ChangeEvent;
import java.text.DecimalFormat;
import javax.swing.table.DefaultTableModel;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class WindPowerCalculator extends JFrame {

    // wind data variables
    private double kappa;
    private double lambda;
    private double meanVelocity;

    // geometrical variables
    private double rho;
    private double diameter;
    private double width;
    private double height;

    // economic variables
    private double eta;
    private double cost;
    private double turbineCost;
    private double rate;
    private double yield;
    private double revenue;

    // radio buttons
    private JRadioButton windInputEquationRB = new JRadioButton("Equation", true);
    private JRadioButton windInputTableRB = new JRadioButton("Table");
    private JRadioButton hTurbineRB = new JRadioButton("Horizontal", true);
    private JRadioButton vTurbineRB = new JRadioButton("Vertical");

    // button groups
    ButtonGroup windInputBG = new ButtonGroup();
    ButtonGroup turbineBG = new ButtonGroup();

    // run button
    private JButton runB = new JButton("Run");
    private JButton clearB = new JButton("Clear");

    // fields
    private JTextField kappaTF = new JTextField(3);
    private JTextField lambdaTF = new JTextField(3);
    private JTextField rhoTF = new JTextField(3);
    private JTextField diameterTF = new JTextField(3);
    private JTextField heightTF = new JTextField(3);
    private JTextField widthTF = new JTextField(3);
    private JTextField etaTF = new JTextField(2);
    private JTextField costTF = new JTextField(4);
    private JTextField turbineCostTF = new JTextField(5);
    private JTextField rateTF = new JTextField(4);
    private JTextField meanTF = new JTextField(4);
    private JTextField yieldTF = new JTextField(4);
    private JTextField revenueTF = new JTextField(4);

    // table
    String[][] data = {{"0-1", ""},
                    {"1-2", ""},
                    {"2-3", ""},
                    {"3-4", ""},
                    {"4-5", ""},
                    {"5-6", ""},
                    {"6-7", ""},
                    {"8-9", ""},
                    {"9-10", ""},
                    {"10-11", ""},
                    {"12-13", ""},
                    {"13-14", ""},
                    {"14-15", ""}};

    JTable dataTable = new JTable(data, new String[] {"m/s", "freq"}) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 1;
        }
    };

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
    // Constructor
    public WindPowerCalculator () {
        this.setTitle("Wind Power Calculator");
        this.setSize(450, 525);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(null);
        this.setResizable(false);

        /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
        // Packing

        // WIND INPUT DATA BUTTON GROUP
        windInputEquationRB.addChangeListener(new DataRadioButtonHandler());
        windInputTableRB.addChangeListener(new DataRadioButtonHandler());
        windInputBG.add(windInputEquationRB);
        windInputBG.add(windInputTableRB);
        JPanel windInputPanel = new JPanel();
        windInputPanel.setLayout(new GridLayout(2, 1));
        windInputPanel.add(windInputEquationRB);
        windInputPanel.add(windInputTableRB);
        windInputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Wind data"));
        windInputPanel.setBounds(30, 30, 90, 60);
        this.getContentPane().add(windInputPanel);

        // KAPPA AND LAMBDA
        JLabel kappaL = new JLabel("kappa", SwingConstants.LEFT);
        kappaL.setBounds(30, 120, 45, 30);
        this.getContentPane().add(kappaL);
        kappaTF.setBounds(30, 150, 45, 20);
        this.getContentPane().add(kappaTF);

        JLabel lambdaL = new JLabel("lambda", SwingConstants.LEFT);
        lambdaL.setBounds(75, 120, 45, 30);
        this.getContentPane().add(lambdaL);
        lambdaTF.setBounds(75, 150, 45, 20);
        this.getContentPane().add(lambdaTF);

        // TURBINE TYPE BUTTON GROUP
        hTurbineRB.addChangeListener(new TurbineRadioButtonHandler());
        vTurbineRB.addChangeListener(new TurbineRadioButtonHandler());
        turbineBG.add(hTurbineRB);
        turbineBG.add(vTurbineRB);
        JPanel turbineTypePanel = new JPanel();
        turbineTypePanel.setLayout(new GridLayout(2, 1));
        turbineTypePanel.add(hTurbineRB);
        turbineTypePanel.add(vTurbineRB);
        turbineTypePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Turbine"));
        turbineTypePanel.setBounds(150, 30, 105, 60);
        this.getContentPane().add(turbineTypePanel);

        // PHYSICAL AND GEOMETRICAL PROPERTIES
        JLabel rhoL = new JLabel("density");
        rhoL.setBounds(150, 150, 60, 20);
        rhoTF.setBounds(210, 150, 45, 20);
        this.getContentPane().add(rhoL);
        this.getContentPane().add(rhoTF);

        JLabel diameterL = new JLabel("diameter");
        diameterL.setBounds(150, 190, 60, 20);
        diameterTF.setBounds(210, 190, 45, 20);
        this.getContentPane().add(diameterL);
        this.getContentPane().add(diameterTF);

        JLabel heightL = new JLabel("height");
        heightL.setBounds(150, 230, 60, 20);
        heightTF.setBounds(210, 230, 45, 20);
        heightTF.setEnabled(false);
        this.getContentPane().add(heightL);
        this.getContentPane().add(heightTF);

        JLabel widthL = new JLabel("width");
        widthL.setBounds(150, 270, 60, 20);
        widthTF.setBounds(210, 270, 45, 20);
        widthTF.setEnabled(false);
        this.getContentPane().add(widthL);
        this.getContentPane().add(widthTF);

        JLabel etaL = new JLabel("efficiency");
        etaL.setBounds(150, 310, 60, 20);
        etaTF.setBounds(210, 310, 45, 20);
        this.getContentPane().add(etaL);
        this.getContentPane().add(etaTF);

        JLabel costL = new JLabel("$/kWh");
        costL.setBounds(150, 350, 60, 20);
        costTF.setBounds(210, 350, 45, 20);
        this.getContentPane().add(costL);
        this.getContentPane().add(costTF);

        JLabel turbineCostL = new JLabel("turbine $");
        turbineCostL.setBounds(150, 390, 60, 20);
        turbineCostTF.setBounds(210, 390, 45, 20);
        this.getContentPane().add(turbineCostL);
        this.getContentPane().add(turbineCostTF);

        JLabel rateL = new JLabel("ROR");
        rateL.setBounds(150, 430, 60, 20);
        rateTF.setBounds(210, 430, 45, 20);
        this.getContentPane().add(rateL);
        this.getContentPane().add(rateTF);

        // WIND DATA TABLE
        dataTable.setRowHeight(20);
        dataTable.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.setBounds(30, 190, 90, 260);
        this.getContentPane().add(scrollPane);

        // BUTTONS
        runB.addActionListener(new RunButtonHandler());
        runB.setBounds(270, 30, 60, 30);
        clearB.addActionListener(new ClearButtonHandler());
        clearB.setBounds(345, 30, 70, 30);
        this.getContentPane().add(runB);
        this.getContentPane().add(clearB);

        // OUTPUT
        JLabel meanL = new JLabel("mean speed");
        meanL.setBounds(270, 150, 75, 20);
        meanTF.setBounds(360, 150, 55, 20);
        this.getContentPane().add(meanL);
        this.getContentPane().add(meanTF);

        JLabel yieldL = new JLabel("kWh yielded");
        yieldL.setBounds(270, 190, 75, 20);
        yieldTF.setBounds(360, 190, 55, 20);
        this.getContentPane().add(yieldL);
        this.getContentPane().add(yieldTF);

        JLabel revenueL = new JLabel("revenue in $");
        revenueL.setBounds(270, 230, 75, 20);
        revenueTF.setBounds(360, 230, 55, 20);
        this.getContentPane().add(revenueL);
        this.getContentPane().add(revenueTF);

        // Packing
        /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
        Toolkit myScreen = Toolkit.getDefaultToolkit();
        Dimension screenSize = myScreen.getScreenSize();
        this.setLocation(screenSize.width/4, screenSize.height/4);
        this.setVisible(true);

    }

    // Run Button Listener
    private class RunButtonHandler implements ActionListener {
        public void actionPerformed (ActionEvent e) {
            // decimal formatting
            DecimalFormat dc = new DecimalFormat("0.00");

            // call the mean velocity function
            setMeanVelocity();

            // set the mean velocity text field
            String formattedText = dc.format(meanVelocity);
            meanTF.setText(formattedText);

            // get input data
            rho = Double.parseDouble(rhoTF.getText());

            // get geometry according to turbine type
            double sweptArea = 0.0;
            if (hTurbineRB.isSelected()) {
                diameter = Double.parseDouble(diameterTF.getText());
                sweptArea = Math.PI * Math.pow(diameter / 2, 2);
            }
            else if (vTurbineRB.isSelected()) {
                height = Double.parseDouble(heightTF.getText());
                width = Double.parseDouble(widthTF.getText());
                sweptArea = height * width;
            }

            // get the rest of the input
            eta = Double.parseDouble(etaTF.getText());
            cost = Double.parseDouble(costTF.getText());
            turbineCost = Double.parseDouble(turbineCostTF.getText());
            rate = Double.parseDouble(rateTF.getText());

            // compute energy yield
            yield = 0.5 * rho * Math.pow(meanVelocity, 3) * sweptArea * eta;
            yield *= 24 * 365 / 1000.0;
            yieldTF.setText(dc.format(yield));

            // compute revenue in $
            revenue = cost * yield;
            revenueTF.setText(dc.format(revenue));

            // show income data
            IncomeData id = new IncomeData();
        }
    }

    // Clear Button Listener
    private class ClearButtonHandler implements ActionListener {
        public void actionPerformed (ActionEvent e) {
            kappaTF.setText("");
            lambdaTF.setText("");
            rhoTF.setText("");
            diameterTF.setText("");
            heightTF.setText("");
            widthTF.setText("");
            etaTF.setText("");
            costTF.setText("");
            turbineCostTF.setText("");
            rateTF.setText("");
            meanTF.setText("");
            yieldTF.setText("");
            revenueTF.setText("");

            int len = dataTable.getModel().getRowCount();

            for (int i = 0; i < len; i++) {
                dataTable.getModel().setValueAt("", i, 1);
            }
        }
    }

    // Radio Button Listeners
    private class DataRadioButtonHandler implements ChangeListener {
        public void stateChanged (ChangeEvent e) {
            if (e.getSource() == windInputEquationRB) {
                AbstractButton aButton = (AbstractButton) e.getSource();
                ButtonModel bModel = aButton.getModel();
                if (bModel.isPressed()) {
                    dataTable.setEnabled(false);
                    kappaTF.setEnabled(true);
                    lambdaTF.setEnabled(true);
                }
            }
            else if (e.getSource() == windInputTableRB) {
                AbstractButton aButton = (AbstractButton) e.getSource();
                ButtonModel bModel = aButton.getModel();
                if (bModel.isPressed()) {
                    kappaTF.setEnabled(false);
                    lambdaTF.setEnabled(false);
                    dataTable.setEnabled(true);
                }
            }
        }
    }

    private class TurbineRadioButtonHandler implements ChangeListener {
        public void stateChanged (ChangeEvent e) {
            if (e.getSource() == hTurbineRB) {
                AbstractButton aButton = (AbstractButton) e.getSource();
                ButtonModel bModel = aButton.getModel();
                if (bModel.isPressed()) {
                    heightTF.setEnabled(false);
                    widthTF.setEnabled(false);
                    diameterTF.setEnabled(true);
                }
            }
            else if (e.getSource() == vTurbineRB) {
                AbstractButton aButton = (AbstractButton) e.getSource();
                ButtonModel bModel = aButton.getModel();
                if (bModel.isPressed()) {
                    diameterTF.setEnabled(false);
                    heightTF.setEnabled(true);
                    widthTF.setEnabled(true);
                }
            }
        }
    }
    // Gamma function
    private double logGamma (double x) {
        double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
        double ser = 1.0
                + 76.18009173 / (x + 0)
                - 86.50532033 / (x + 1)
                + 24.01409822 / (x + 2)
                - 1.231739516 / (x + 3)
                + 0.00120858003 / (x + 4)
                - 0.00000536382 / (x + 5);
        return tmp + Math.log(ser * Math.sqrt(2 * Math.PI));
    }

    private double gamma (double x) {return Math.exp(logGamma(x));}

    // Set the mean velocity
    private void setMeanVelocity () {
        if (windInputEquationRB.isSelected()) {
            double K = Double.parseDouble(kappaTF.getText());
            double L = Double.parseDouble(lambdaTF.getText());
            meanVelocity = L * gamma(1 + 1/K);
        }

        if (windInputTableRB.isSelected()) {
            // get the frequencies column
            int len = dataTable.getModel().getRowCount();

            double[] f = new double[len];
            double[] v = new double[len];

            for (int i = 0; i < len; i++) {
                v[i] = 0.5 + i;
                String s = dataTable.getModel().getValueAt(i, 1).toString();
                f[i] = Double.parseDouble(s);
            }

            // use MMLM method
            double g = 1.0;
            double t1, t2, t3;
            double t4 = 1;
            double n;

            while (true) {
                t1 = 0;
                t2 = 0;
                t3 = 0;
                for (int j = 0; j < len; j++) {
                    t1 += Math.log(v[j]) * Math.pow(v[j], g) * f[j];
                    t2 += f[j] * Math.pow(v[j], g);
                    t3 += f[j] * Math.log(v[j]);
                }

                n = 1.0 / (t1/t2 - t3/t4);

                if (Math.abs((g - n) / n) < 0.01)
                    break;
                else
                    g = n;
            }

            double K = n;
            double L = Math.pow(t2/t4, 1.0/n);

            meanVelocity = L * gamma(1 + 1/K);
        }
    }

    // A window that shows how many years it takes to recover the investment
    class IncomeData extends JFrame {
        //
        private JTable incomeTable;

        // data
        private DefaultTableModel model;

        // scroll pane
        JScrollPane incomeDataScrollPane = new JScrollPane();

        // column names
        String[] columnNames = { "Year", "Income", "Investment", "Change", "NPV", "Acc. NPV" };

        // constructor
        public IncomeData () {
            this.setTitle("Income Data");
            this.setSize(400, 200);
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.getContentPane().setLayout(new GridLayout(2, 1));

            // set the table mode and pass it to the table
            model = new DefaultTableModel(null, columnNames);
            incomeTable = new JTable(model);

            // fill the scroll pane
            incomeDataScrollPane.setViewportView(incomeTable);

            // fill the table
            int year = 0;
            boolean recovered = false;
            double income;
            double investment;
            double change;
            double npv;
            double acc_npv = 0.0;

            while (!recovered) {
                if (year == 0) {
                   income = 0.0;
                   investment = turbineCost;
                }
                else {
                    income = revenue;
                    investment = 0.0;
                }

                // compute net change, npv and acc_npv
                change = income - investment;
                npv =  change / Math.pow((1.0 + rate), year);
                acc_npv = npv + acc_npv;

                // fill row
                DecimalFormat dc = new DecimalFormat("0.00");
                String incomeS = dc.format(income);
                String investmentS = dc.format(investment);
                String changeS = dc.format(change);
                String npvS = dc.format(npv);
                String acc_npvS = dc.format(acc_npv);

                Object[] row = {String.valueOf(year), incomeS, investmentS, changeS, npvS, acc_npvS};
                model.addRow(row);

                // increase year count
                year++;

                // check if the acc_npv is positive
                if ((acc_npv > 0) || (year > 25))
                    recovered = true;
            }

            JLabel resultLabel = new JLabel();
            resultLabel.setPreferredSize(new Dimension(360, 60));

            if (year < 25)
                resultLabel.setText("Return time: " + (year - 1) + " years");
            else
                resultLabel.setText("The return time is more than 25 years. Your project is doomed!");

            resultLabel.setHorizontalAlignment(JLabel.CENTER);
            resultLabel.setVerticalAlignment(JLabel.CENTER);

            this.getContentPane().add(resultLabel);
            this.getContentPane().add(incomeDataScrollPane);

            Toolkit myScreen = Toolkit.getDefaultToolkit();
            Dimension screenSize = myScreen.getScreenSize();
            this.setLocation(screenSize.width/4 + 500, screenSize.height/4);
            this.setVisible(true);
        }
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
    // Main method
    public static void main (String[] args) {
        WindPowerCalculator wpc = new WindPowerCalculator();
    }

}
