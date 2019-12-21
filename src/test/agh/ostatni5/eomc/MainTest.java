package agh.ostatni5.eomc;

import agh.ostatni5.eomc.view.GameCanvas;
import agh.ostatni5.eomc.view.MainFrame;
import agh.ostatni5.eomc.view.MyLabel;
import org.junit.Test;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void queueTest()
    {
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(1);
        priorityQueue.add(2);
        priorityQueue.add(5);
        priorityQueue.add(2);
        priorityQueue.add(3);
        System.out.println(priorityQueue);
        System.out.println(priorityQueue.poll());
        System.out.println(priorityQueue.element());
        System.out.println(priorityQueue);



    }



}