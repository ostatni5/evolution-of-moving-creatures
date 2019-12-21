package agh.ostatni5.eomc.core;

public enum Biome {
    JUNGLE,SAVANNA;
    public String toString(){
        switch(this) {
            case JUNGLE: return "*";
            case SAVANNA: return " ";
        }
        return null;
    }
}
