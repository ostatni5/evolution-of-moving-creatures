package agh.ostatni5.eomc.view;

import agh.ostatni5.eomc.Options;
import agh.ostatni5.eomc.ReadOptionsJSON;

import javax.swing.*;
import java.awt.*;

public class StartPanel extends JPanel {
    private int[] startValues = {20, 20, 2, 10, 10, 256, 2, 14, 1,10000,400};
    private MainFrame mainFrame;
    private JTextField[] jTextFields;
    private JLabel[] jLabels;
    private ReadOptionsJSON optionsJSON = new ReadOptionsJSON();
    private Options options = optionsJSON.getParsedOptions();
    public StartPanel(MainFrame mainFrame) {

        this.mainFrame = mainFrame;
        Dimension dimension = new Dimension(400,30*options.names.length);
        setMaximumSize(dimension);
        setLayout(new GridLayout(0, 2));
        jLabels = new JLabel[options.names.length];
        jTextFields = new JTextField[options.names.length];
        for (int i = 0; i < options.names.length; i++) {
            jLabels[i] = new JLabel(options.names[i]);
            jTextFields[i] = new JTextField();
            jTextFields[i].setText(String.valueOf(options.values[i]));
            add(jLabels[i]);
            add(jTextFields[i]);
        }
        JButton jButton = new JButton("Launch");
        add(jButton);
        jButton.addActionListener(actionEvent -> {
            setVisible(false);
            getValues();
            mainFrame.startSimulations(options);
        });
    }

    private void getValues(){
        for (int i = 0; i < jTextFields.length; i++) {
            options.values[i] = Integer.parseInt(jTextFields[i].getText());
        }
    }
}
