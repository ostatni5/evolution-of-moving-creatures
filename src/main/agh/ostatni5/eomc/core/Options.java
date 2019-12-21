package agh.ostatni5.eomc.core;

public class Options {
    static final public String[] names ={"MapWidth","MapHeight","JungleRatio","CreaturesCount","GrassCount","StartEnergy","DayEnergyCost","GrassEnergy","SimulationCount","SimulationLength","CanvasSize"};
    public int[] values = new int[names.length];
    public Options() {
    }
    public Options(int[] values) {
        this.values = values;
    }
}
