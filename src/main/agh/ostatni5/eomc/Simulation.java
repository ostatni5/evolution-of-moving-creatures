package agh.ostatni5.eomc;

import agh.ostatni5.eomc.view.GameCanvas;
import agh.ostatni5.eomc.view.MyFrame;
import agh.ostatni5.eomc.viewFx.MyApp;
import javafx.application.Application;
import javafx.scene.image.Image;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeSet;

public class Simulation {
    public static void main(String[] args) throws IOException, FontFormatException {

        WorldMap worldMap = new WorldMap(20,20,8,4,10,10);
        MyFrame myFrame = new MyFrame();
        GameCanvas gameCanvas = new GameCanvas(worldMap);
        myFrame.add(gameCanvas);

        System.out.print(worldMap);

    }

}

