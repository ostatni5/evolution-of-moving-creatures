package agh.ostatni5.eomc;

public class Options {
    public String[] names ={"MapWidth","MapHeight","JungleRatio","CreaturesCount","GrassCount","StartEnergy","DayEnergyCost","GrassEnergy","SimulationCount","SimulationLength","CanvasSize"};
    public int[] values = new int[11];
    public Options() {
    }
    public Options(int[] values) {
        this.values = values;
    }
}
