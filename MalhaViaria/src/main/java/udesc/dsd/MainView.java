package udesc.dsd;

import javax.swing.*;
import java.io.IOException;

public class MainView extends JFrame {

    private JButton btStart;
    private JTextField tfVehicleCount;
    private JRadioButton rbRoadModel1;
    private JRadioButton rbRoadModel2;
    private JRadioButton rbRoadModel3;
    private JRadioButton rbMutualExclusion1;
    private JRadioButton rbMutualExclusion2;
    private ButtonGroup rbRoadModel;
    private ButtonGroup rbMutualExclusion;
    private JLabel lbVehicleCount;
    private JLabel lbRoadModel;
    private JLabel lbMutualExclusion;
    private Road road;

    public MainView(){
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(350, 450);
        setTitle("Simulação de trafego");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        setLayout(null);

        // Labels
        lbRoadModel = new JLabel("Malha a ser utilizada:");
        lbVehicleCount = new JLabel("Número de Veículos:");
        lbMutualExclusion = new JLabel("Algoritmo de exclusão mútua:");

        // Text Field
        tfVehicleCount = new JTextField("10");

        // Radio Buttons for Model
        rbRoadModel1 = new JRadioButton("1");
        rbRoadModel2 = new JRadioButton("2");
        rbRoadModel3 = new JRadioButton("3");

        rbRoadModel = new ButtonGroup();
        rbRoadModel.add(rbRoadModel1);
        rbRoadModel.add(rbRoadModel2);
        rbRoadModel.add(rbRoadModel3);

        // Radio Buttons for Mutual Exclusion
        rbMutualExclusion1 = new JRadioButton("Semaphore");
        rbMutualExclusion2 = new JRadioButton("Monitor");

        rbMutualExclusion = new ButtonGroup();
        rbMutualExclusion.add(rbMutualExclusion1);
        rbMutualExclusion.add(rbMutualExclusion2);

        // Button
        btStart = new JButton("Iniciar");
        btStart.addActionListener(a -> init());

        // Adicionando componentes ao container
        add(lbRoadModel);
        add(rbRoadModel1);
        add(rbRoadModel2);
        add(rbRoadModel3);

        add(lbVehicleCount);
        add(tfVehicleCount);

        add(lbMutualExclusion);
        add(rbMutualExclusion1);
        add(rbMutualExclusion2);

        add(btStart);

        // Posicionando os componentes
        lbRoadModel.setBounds(20, 20, 200, 25);
        rbRoadModel1.setBounds(20, 50, 150, 25);
        rbRoadModel2.setBounds(20, 80, 150, 25);
        rbRoadModel3.setBounds(20, 110, 150, 25);

        lbVehicleCount.setBounds(20, 160, 200, 25);
        tfVehicleCount.setBounds(220, 160, 100, 25);

        lbMutualExclusion.setBounds(20, 210, 200, 25);
        rbMutualExclusion1.setBounds(20, 240, 150, 25);
        rbMutualExclusion2.setBounds(20, 270, 150, 25);

        btStart.setBounds(20, 340, 150, 25);
    }

    private void init(){

        String file = getRoadModel();
        CellFactory cellFactory = getMutualExclusion();
        int vehicleCount = Integer.parseInt(tfVehicleCount.getText());

        try {
            if (file != null && cellFactory != null && vehicleCount > 0) {

                road = new Road(file, cellFactory, vehicleCount);

                new RoadView(road);

                road.start();
                dispose();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getRoadModel(){
        if (rbRoadModel1.isSelected())
            return "malha-exemplo-1.txt";
        else if(rbRoadModel2.isSelected())
            return "malha-exemplo-2.txt";
        else if (rbRoadModel3.isSelected())
            return "malha-exemplo-3.txt";
        return null;
    }

    private CellFactory getMutualExclusion(){
        if (rbMutualExclusion1.isSelected())
            return new SemaphoreCellFactory();
        else if(rbMutualExclusion2.isSelected())
            return new MonitorCellFactory();
        return null;
    }

}