package udesc.dsd;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class RoadView extends JFrame {
    private Road road;

    private final int cellHeight = 30;
    private final int cellWidth = 30;

    private int width;
    private int height;


    public RoadView(Road road) {

        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Simulação de trafego");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.road = road;
        initComponents();

        setVisible(true);
    }

    private void initComponents(){

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawRoad(g);
//                desenharVeiculos(g);
            }
        };


        width = (road.getGrid()[0].length * cellWidth) + (road.getGrid()[0].length);
        height = (road.getGrid().length * cellHeight) + (road.getGrid().length);

        setSize(width, height + 60);

        panel.setPreferredSize(new Dimension(width, height));
        add(panel);

        // Adicionando um Timer para atualizar a interface gráfica periodicamente
        Timer timer = new Timer(1000, e -> repaint());
        timer.start();
    }

//    private void desenharVeiculos(Graphics g) {
//        for (Vehicle vehicle : road.getVehicles()) {
//            int linha = vehicle.get();
//            int coluna = vehicle.getColunaAtual();
//            int x = coluna * cellWidth;
//            int y = linha * cellHeight;
//            g.setColor(vehicle.getColor());
//            g.fillOval(x, y, cellWidth, cellHeight);
//        }
//    }

    private void drawRoad(Graphics g) {

        Cell[][] grid = road.getGrid();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                CellType type = grid[i][j].getType();

                Color color = SysColor.getCellColor(type);

                g.setColor(color);
                g.fillRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                g.setColor(Color.BLACK);
                g.drawRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
            }
        }
    }

}
