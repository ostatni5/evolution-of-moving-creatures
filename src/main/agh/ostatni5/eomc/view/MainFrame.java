package agh.ostatni5.eomc.view;
import agh.ostatni5.eomc.Options;
import agh.ostatni5.eomc.SimulationPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        super("Evolution of moving creatures");
        setSize(600,600);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void startSimulations(Options options){

        SimulationPanel [] simulationPanels = new SimulationPanel[options.values[8]];
        for (int i = 0; i < simulationPanels.length; i++) {
            simulationPanels[i]= new SimulationPanel(options);
            add(simulationPanels[i].init());
        }

        pack();
    }


}