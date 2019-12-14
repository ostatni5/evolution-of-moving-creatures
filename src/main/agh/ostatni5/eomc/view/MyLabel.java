package agh.ostatni5.eomc.view;

import javax.swing.*;

public class MyLabel extends JLabel {

    public MyLabel(String string) {
        super(string);
    }
    public void setText(String string){
        super.setText("<html>"+string.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>")+"<html>");
    }
    public void setHtml(String string){
        super.setText(string);
    }
}
