package agh.ostatni5.eomc;

public class Energy {
    int start;
    int value;
    public Energy(int start, int value) {
        this.start = start;
        this.value = value;
    }

    public void gain(int energyGain) {
        value += energyGain;
    }
    public void loss(int lossEnergy) {
        value -= lossEnergy;
    }

    public float valueToStartRatio()
    {
        return (float)value/(float)(start);
    }
}
