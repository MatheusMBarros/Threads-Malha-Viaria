package udesc.dsd;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Vehicle extends Thread {
    private Road road;
    private Cell cell;
    private Long speed;
    private Color color;


    public Vehicle(Road road, long speed, Color color) {
        this.road = road;
        this.speed = speed;
        this.color = color;
    }

    @Override
    public void run() {
    }
}


