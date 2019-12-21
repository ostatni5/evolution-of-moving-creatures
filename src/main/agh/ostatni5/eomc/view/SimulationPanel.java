package agh.ostatni5.eomc.view;

import agh.ostatni5.eomc.core.Creature;
import agh.ostatni5.eomc.core.Options;
import agh.ostatni5.eomc.core.WorldMap;
import agh.ostatni5.eomc.stats.History;
import agh.ostatni5.eomc.stats.StatisticsCreature;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static javax.swing.SwingConstants.TOP;
import static org.junit.Assert.assertEquals;

public class SimulationPanel extends JPanel {
    private Options options;
    private History history = new History();
    private boolean runThread= true;
    private GridBagConstraints c;
    private MyLabel jLabel;
    private GameCanvas gameCanvas;
    private JButton markDominantButton;
    private JButton stopButton;
    private WorldMap worldMap;
    private JButton watcherButton;
    private JComboBox<String> idCombo;
    private JComboBox<Object> idInfoCombo;
    private MyLabel historyOfCreatureLabel;
    private JButton showInfoButton;
    private MyLabel genomeLabel;
    private JPanel moreActionsPanel;
    private AtomicReference<StatisticsCreature> statisticsCreature = new AtomicReference<>();
    private AtomicReference<Boolean> showInfo = new AtomicReference<>(false);
    private AtomicReference<Boolean> simulationRunning = new AtomicReference<>(false);


    public SimulationPanel(Options options) {
        this.options = options;
        setLayout(new GridBagLayout());
        c= new GridBagConstraints();
        init();
    }

    private void init() {
        worldMap = new WorldMap(options);
        initComponents();
        simulationRunning.set(true);
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
    }

    public void willBeClosed()
    {
        history.toFile();
        runThread = false;
    }

    private void initComponents()
    {
        gameCanvas = new GameCanvas(worldMap, options.values[10]);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        add(gameCanvas,c);
        jLabel = new MyLabel("Stats");
        jLabel.setHorizontalAlignment(JLabel.LEFT);
        jLabel.setVerticalAlignment(JLabel.TOP);
        jLabel.setPreferredSize(new Dimension(210, 400));
        c.gridx = 1;
        add(jLabel);
        setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));

        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));

        markDominantButton = new JButton("MARK DOMINANT GENOTYPE");
        markDominantButton.setPreferredSize(new Dimension(200, 40));
        statsPanel.add(markDominantButton);

        JLabel historyLabel = new JLabel("History of selected");
        statsPanel.add(historyLabel);

        historyOfCreatureLabel = new MyLabel("");
        statsPanel.add(historyOfCreatureLabel);

        c.gridx = 2;
        add(statsPanel);

        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
        actionsPanel.setPreferredSize(new Dimension(200, 300));
        actionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionsPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        stopButton = new JButton("PAUSE");
        stopButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        stopButton.setPreferredSize(new Dimension(200, 30));
        actionsPanel.add(stopButton);

        moreActionsPanel = new JPanel();
        moreActionsPanel.setLayout(new BoxLayout(moreActionsPanel, BoxLayout.Y_AXIS));
        moreActionsPanel.setVisible(false);

        JLabel idLabel = new JLabel("Choose id to watch");
        moreActionsPanel.add(idLabel);

        idCombo = new JComboBox<>();
        idCombo.setMaximumSize(new Dimension(200, 30));
        moreActionsPanel.add(idCombo);

        watcherButton = new JButton("WATCH");
        moreActionsPanel.add(watcherButton);

        JLabel idInfoLabel = new JLabel("Choose id to show gens");
        moreActionsPanel.add(idInfoLabel);

        idInfoCombo = new JComboBox<>();
        idInfoCombo.setMaximumSize(new Dimension(200, 30));
        moreActionsPanel.add(idInfoCombo);

        showInfoButton = new JButton("SHOW GENS");
        moreActionsPanel.add(showInfoButton);

        genomeLabel = new MyLabel("GENOME HERE");
        moreActionsPanel.add(genomeLabel);

        actionsPanel.add(moreActionsPanel);
        c.gridx = 2;
        add(actionsPanel);

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
                    moreActionsPanel.setVisible(true);

                if(showInfo.get())
                {
                    historyOfCreatureLabel.setHtml(statisticsCreature.get().toHtml());
                    showInfo.set(false);
                }

            } else {
                stopButton.setText("PAUSE");
                simulationRunning.set(true);
                moreActionsPanel.setVisible(false);
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
            historyOfCreatureLabel.setHtml("On PAUSE will show here");
        });

        showInfoButton.addActionListener(actionEvent -> {
            Creature creature = worldMap.getAliveCreatures().get(Integer.valueOf(Objects.requireNonNull(idInfoCombo.getSelectedItem()).toString().split(":")[0]));
            genomeLabel.setText(creature.getGenotype().toString());
        });
    }

}
