package agh.ostatni5.eomc;

import agh.ostatni5.eomc.view.MainFrame;
import agh.ostatni5.eomc.view.StartPanel;

import java.awt.*;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class Main {
    public static void main(String[] args) throws IOException, FontFormatException {
        MainFrame mainFrame = new MainFrame();
        StartPanel startPanel = new StartPanel(mainFrame);
        mainFrame.add(startPanel);
        mainFrame.pack();
    }
}

