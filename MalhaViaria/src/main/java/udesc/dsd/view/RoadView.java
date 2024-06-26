package udesc.dsd.view;

import udesc.dsd.road.cell.Cell;
import udesc.dsd.road.cell.CellType;
import udesc.dsd.road.Road;
import udesc.dsd.util.SysColor;
import udesc.dsd.vehicle.Vehicle;

import javax.swing.*;
import java.awt.*;
import java.util.ConcurrentModificationException;

public class RoadView extends JFrame {
    private Road road;

    private final int cellHeight = 30;
    private final int cellWidth = 30;

    private int width;
    private int height;

    private JButton btEnd;

    public RoadView(Road road) {

        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Simulação de tráfego");
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
                drawVehicles(g);
            }

        };

        width = (road.getGrid()[0].length * cellWidth) + (road.getGrid()[0].length);
        height = (road.getGrid().length * cellHeight) + (road.getGrid().length);

        setSize(width, height + 60);

        panel.setPreferredSize(new Dimension(width, height));
        add(panel);

        btEnd = new JButton("Terminar Simulação");
        btEnd.addActionListener(a -> end());
        add(btEnd, BorderLayout.SOUTH);

        // Adicionando um Timer para atualizar a interface gráfica periodicamente
        Timer timer = new Timer(1, e -> repaint());
        timer.start();
    }

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

    private synchronized void drawVehicles(Graphics g) {

        try{
            for (Vehicle vehicle : road.getVehicles()) {
                if (vehicle.getCell() != null && vehicle.isAlive()) {
                    int row = vehicle.getCell().getPosition().y;
                    int col = vehicle.getCell().getPosition().x;
                    int x = col * cellWidth;
                    int y = row * cellHeight;
                    g.setColor(vehicle.getColor());
                    g.fillOval(x, y, cellWidth, cellHeight);
                }
            }
        }
        catch (ConcurrentModificationException e){
            e.printStackTrace();
        }
    }

    private void end(){
        road.end();
        dispose();
        new MainView();
    }

}
