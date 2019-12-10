package agh.ostatni5.eomc;

import javafx.scene.image.Image;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Simulation {
    public static void main(String[] args) throws IOException, FontFormatException {
        WorldMap worldMap = new WorldMap(20,10,8,4);
        String text = worldMap.toString();
        System.out.print(text);

//        File font_file = new File("src/res/fonts/andalemo.ttf");
//        Font font = Font.createFont(Font.TRUETYPE_FONT, font_file);
//
//        EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                new MyFrame(text,font);
//            }
//        });

    }

}

