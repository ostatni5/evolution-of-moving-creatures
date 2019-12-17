package agh.ostatni5.eomc;

import agh.ostatni5.eomc.view.GameCanvas;
import agh.ostatni5.eomc.view.MyFrame;
import agh.ostatni5.eomc.view.MyLabel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;

public class Simulation {
    public static void main(String[] args) throws IOException, FontFormatException {
        AtomicReference<Boolean> simulationRunning = new AtomicReference<>(true);
        WorldMap worldMap = new WorldMap(50, 50, 50, 20, 50, 1000);
        MyFrame myFrame = new MyFrame();
        myFrame.setLayout(new BoxLayout(myFrame.getContentPane(), BoxLayout.Y_AXIS));
        JPanel panel = new JPanel();
        GameCanvas gameCanvas = new GameCanvas(worldMap);


        MyLabel jLabel = new MyLabel("Stats");
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        jLabel.setPreferredSize(new Dimension(200,200));
        panel.add(gameCanvas);
        panel.add(jLabel);
        panel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));

        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));

        JLabel strongestGenomeLabel = new JLabel("Creatures with strongest genome");
        statsPanel.add(strongestGenomeLabel);

        MyLabel strongestGenomeIdLabel = new MyLabel("10");
        strongestGenomeIdLabel.setPreferredSize(new Dimension(200,100));
        statsPanel.add(strongestGenomeIdLabel);

        JLabel historyLabel = new JLabel("History of selected");
        statsPanel.add(historyLabel);

        MyLabel historyOfCreatureLabel = new MyLabel("");
        statsPanel.add(historyOfCreatureLabel);


        panel.add(statsPanel);

        JPanel buttonActionPanel = new JPanel();
        buttonActionPanel.setLayout(new BoxLayout(buttonActionPanel, BoxLayout.Y_AXIS));
        buttonActionPanel.setPreferredSize(new Dimension(200,30));

        JButton stopButton = new JButton("PAUSE");
        buttonActionPanel.add(stopButton);

        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel idLabel = new JLabel("Choose id to watch");
        actionPanel.add(idLabel);

        JComboBox<String> idCombo = new JComboBox<>();
        idCombo.setMaximumSize(new Dimension(200,30));
        actionPanel.add(idCombo);

        JLabel idSpinnerLabel = new JLabel("How long");
        actionPanel.add(idSpinnerLabel);

        SpinnerModel model = new SpinnerNumberModel(1, 1, 10000, 1);
        JSpinner spinnerIdInfo = new JSpinner(model);
        spinnerIdInfo.setMaximumSize(new Dimension(200,30));
        actionPanel.add(spinnerIdInfo);

        JButton watcherButton = new JButton("WATCH");
        actionPanel.add(watcherButton);

        JLabel idInfoLabel = new JLabel("Choose id to show gens");
        actionPanel.add(idInfoLabel);

        JComboBox<Object> idInfoCombo = new JComboBox<>();
        idInfoCombo.setMaximumSize(new Dimension(200,30));
        actionPanel.add(idInfoCombo);

        JButton showInfoButton = new JButton("SHOW GENS");
        actionPanel.add(showInfoButton);

        MyLabel genomeLabel = new MyLabel("GENOME HERE");
        actionPanel.add(genomeLabel);

        buttonActionPanel.add(actionPanel);
        panel.add(buttonActionPanel);






        actionPanel.setVisible(false);
        myFrame.add(panel);
        myFrame.pack();

        AtomicReference<StatisticsCreature> statisticsCreature = new AtomicReference<>();
        AtomicReference<Boolean> waitingForInfo = new AtomicReference<>(false);
        Thread simulationLoop = new Thread(() -> {
            try {
                for (int i = 0; i < 100000 && worldMap.creatureCount > 0; i++) {
                    if(simulationRunning.get()) {
                        worldMap.nextDay();
                        jLabel.setHtml(worldMap.statisticsMap.toHtml());
                        assertEquals(worldMap.creatureCount, worldMap.creatureCount2);
                        myFrame.repaint();
                        StringBuilder stringBuilder = new StringBuilder();
                        int gen = MyArrays.getIndexOfMax(worldMap.genCount);
                        worldMap.aliveCreatures.values().forEach(creature -> {
                            if(creature.genotype.getDominantGen().ordinal() == gen) stringBuilder.append(creature.ID).append(":").append(creature.getPosition()).append(" ");
                        });
                        strongestGenomeIdLabel.setText(stringBuilder.toString());

                        if(waitingForInfo.get())
                        {
                            historyOfCreatureLabel.setText("Data will show at "+statisticsCreature.get().showDate);
                            if(statisticsCreature.get().showDate == i)
                            {
                                stopButton.setText("RESUME");
                                simulationRunning.set(false);
                                historyOfCreatureLabel.setHtml(statisticsCreature.get().toHtml());
                                waitingForInfo.set(false);
                            }

                        }
                    }
                    else
                    {
                        i--;

                    }
                    Thread.sleep(1000 / 200);
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        simulationLoop.start();

        stopButton.addActionListener(actionEvent -> {
            if(stopButton.getText().equals("PAUSE"))
            {
                stopButton.setText("RESUME");
                simulationRunning.set(false);
                actionPanel.setVisible(true);
                LinkedList<String> opt = new LinkedList<String>();
                for (Object o : worldMap.aliveCreatures.values().toArray()) {
                    Creature c = ((Creature)o);
                    opt.add((c.ID +":"+c.getPosition().toString()));
                }
                String[] arrOpt= opt.toArray(new String[0]);
                idCombo.setModel(new DefaultComboBoxModel<>(arrOpt));
                idInfoCombo.setModel(new DefaultComboBoxModel<>(arrOpt));

            }else
            {
                stopButton.setText("PAUSE");
                simulationRunning.set(true);
                actionPanel.setVisible(false);
            }
        });

        watcherButton.addActionListener(actionEvent -> {
            Creature creature = worldMap.aliveCreatures.get(Integer.valueOf(idInfoCombo.getSelectedItem().toString().split(":")[0]));
            statisticsCreature.set(new StatisticsCreature(creature, worldMap.dayCount, (Integer) spinnerIdInfo.getValue()));
            waitingForInfo.set(true);
            stopButton.doClick();
        });

        showInfoButton.addActionListener(actionEvent -> {
            Creature creature = worldMap.aliveCreatures.get(Integer.valueOf(idInfoCombo.getSelectedItem().toString().split(":")[0]));
            genomeLabel.setText(creature.genotype.toString());

        });
    }

}

