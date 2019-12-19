package agh.ostatni5.eomc.view;
import agh.ostatni5.eomc.Options;

import javax.swing.*;

public class MainFrame extends JFrame {
    private StartPanel startPanel;
    private JButton clearButton;
    private SimulationPanel [] simulationPanels;
    public MainFrame() {
        super("Evolution of moving creatures");
        setSize(600,600);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        startPanel = new StartPanel(this);
        add(startPanel);
        clearButton = new JButton("CLEAR SIMULATIONS");
        add(clearButton);
        clearButton.setVisible(false);
        clearButton.addActionListener(actionEvent -> {
            clearButton.setVisible(false);
            startPanel.setVisible(true);
            for (SimulationPanel simulationPanel : simulationPanels) {
                simulationPanel.willBeClosed();
                remove(simulationPanel);
            }
            pack();
        });
    }

    public void startSimulations(Options options){
        clearButton.setVisible(true);
        simulationPanels = new SimulationPanel[options.values[8]];
        for (int i = 0; i < simulationPanels.length; i++) {
            simulationPanels[i]= new SimulationPanel(options);
            add(simulationPanels[i]);
        }
        pack();
    }


}