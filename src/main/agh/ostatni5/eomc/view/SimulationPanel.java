package agh.ostatni5.eomc.view;

import agh.ostatni5.eomc.*;
import agh.ostatni5.eomc.view.GameCanvas;
import agh.ostatni5.eomc.view.MyLabel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;

public class SimulationPanel extends JPanel {
    Options options;
    History history = new History();
    private boolean runThread= true;

    public SimulationPanel(Options options) {
        this.options = options;
        init();
    }

    private void init() {
        AtomicReference<Boolean> simulationRunning = new AtomicReference<>(true);
        WorldMap worldMap = new WorldMap(options);

        GameCanvas gameCanvas = new GameCanvas(worldMap, options.values[10]);
        MyLabel jLabel = new MyLabel("Stats");
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        jLabel.setPreferredSize(new Dimension(200, 200));
        add(gameCanvas);
        add(jLabel);
        setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));

        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));

        JButton markDominantButton = new JButton("MARK DOMINANT GENOTYPE");
        statsPanel.add(markDominantButton);

        JLabel historyLabel = new JLabel("History of selected");
        statsPanel.add(historyLabel);

        MyLabel historyOfCreatureLabel = new MyLabel("");
        statsPanel.add(historyOfCreatureLabel);


        add(statsPanel);

        JPanel buttonActionPanel = new JPanel();
        buttonActionPanel.setLayout(new BoxLayout(buttonActionPanel, BoxLayout.Y_AXIS));
        buttonActionPanel.setPreferredSize(new Dimension(200, 30));

        JButton stopButton = new JButton("PAUSE");
        buttonActionPanel.add(stopButton);

        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel idLabel = new JLabel("Choose id to watch");
        actionPanel.add(idLabel);

        JComboBox<String> idCombo = new JComboBox<>();
        idCombo.setMaximumSize(new Dimension(200, 30));
        actionPanel.add(idCombo);

        JLabel idSpinnerLabel = new JLabel("How long");
        actionPanel.add(idSpinnerLabel);

        JButton watcherButton = new JButton("WATCH");
        actionPanel.add(watcherButton);

        JLabel idInfoLabel = new JLabel("Choose id to show gens");
        actionPanel.add(idInfoLabel);

        JComboBox<Object> idInfoCombo = new JComboBox<>();
        idInfoCombo.setMaximumSize(new Dimension(200, 30));
        actionPanel.add(idInfoCombo);

        JButton showInfoButton = new JButton("SHOW GENS");
        actionPanel.add(showInfoButton);

        MyLabel genomeLabel = new MyLabel("GENOME HERE");
        actionPanel.add(genomeLabel);

        buttonActionPanel.add(actionPanel);
        add(buttonActionPanel);


        actionPanel.setVisible(false);


        AtomicReference<StatisticsCreature> statisticsCreature = new AtomicReference<>();
        AtomicReference<Boolean> showInfo = new AtomicReference<>(false);

        Thread simulationLoop = new Thread(() -> {
            try {
                for (int i = 0; i < options.values[9] && worldMap.stats.creatureCount > 0 && runThread; i++) {
                    if (simulationRunning.get()) {
                        worldMap.nextDay();
                        history.addRecord(worldMap.stats);
                        jLabel.setHtml(worldMap.stats.toHtml());
                        gameCanvas.repaint();
                    } else {
                        i--;
                    }
                    Thread.sleep(1000 / 24);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        simulationLoop.start();

        markDominantButton.addActionListener(actionEvent -> {
            if (markDominantButton.getText().equals("MARK DOMINANT GENOTYPE")) {
                markDominantButton.setText("HIDE DOMINANT GENOTYPE");
                gameCanvas.showDominant();
            } else {
                markDominantButton.setText("MARK DOMINANT GENOTYPE");
                gameCanvas.hideDominant();
            }
            gameCanvas.repaint();
        });


        stopButton.addActionListener(actionEvent -> {
            if (stopButton.getText().equals("PAUSE")) {
                stopButton.setText("RESUME");
                simulationRunning.set(false);

                LinkedList<String> opt = new LinkedList<String>();
                for (Object o : worldMap.getAliveCreatures().values().toArray()) {
                    Creature creature = ((Creature) o);
                    opt.add((creature.getId().own + ":" + creature.getPosition().toString()));
                }
                String[] arrOpt = opt.toArray(new String[0]);
                idCombo.setModel(new DefaultComboBoxModel<>(arrOpt));
                idInfoCombo.setModel(new DefaultComboBoxModel<>(arrOpt));
                if (arrOpt.length > 0)
                    actionPanel.setVisible(true);

                if(showInfo.get())
                {
                    historyOfCreatureLabel.setHtml(statisticsCreature.get().toHtml());
                    showInfo.set(false);
                }

            } else {
                stopButton.setText("PAUSE");
                simulationRunning.set(true);
                actionPanel.setVisible(false);
                if(!showInfo.get())
                {
                    historyOfCreatureLabel.setHtml("");
                    statisticsCreature.set(null);
                    gameCanvas.clearStatisticsCreature();
                }
            }
        });

        watcherButton.addActionListener(actionEvent -> {
            Creature creature = worldMap.getAliveCreatures().get(Integer.valueOf(Objects.requireNonNull(idInfoCombo.getSelectedItem()).toString().split(":")[0]));
            statisticsCreature.set(new StatisticsCreature(creature, worldMap.stats.dayCount));
            gameCanvas.setStatisticsCreature(statisticsCreature.get());
            showInfo.set(true);
            stopButton.doClick();
        });

        showInfoButton.addActionListener(actionEvent -> {
            Creature creature = worldMap.getAliveCreatures().get(Integer.valueOf(Objects.requireNonNull(idInfoCombo.getSelectedItem()).toString().split(":")[0]));
            genomeLabel.setText(creature.getGenotype().toString());

        });

    }

    public void willBeClosed()
    {
        history.toFile();
        runThread = false;
    }
}
