package udesc.dsd;

import javax.swing.*;

public class MainView extends JFrame {

    private JButton btStart;
    private JTextField tfVehicleCount;
    private ButtonGroup rbRoadModel;
    private ButtonGroup rbMutualExclusionGroup;
    private JRadioButton rbModel1;
    private JRadioButton rbModel2;
    private JRadioButton rbModel3;
    private JRadioButton rbSemaphores;
    private JRadioButton rbMonitors;
    private JLabel lbVehicleCount;
    private JLabel lbRoadModel;
    private JLabel lbMutualExclusion;
    private Road road;

    public MainView(){
        setLocationRelativeTo(null);
        setSize(420, 440);
        setVisible(true);
        setTitle("Simulação de trafego");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        setLayout(null);

        // Labels
        lbRoadModel = new JLabel("Malha a ser utilizada");
        lbVehicleCount = new JLabel("Número de Veículos");
        lbMutualExclusion = new JLabel("Algoritmo de exclusão mútua ");

        // Text Field
        tfVehicleCount = new JTextField("10");

        // Radio Buttons for Model
        rbRoadModel = new JRadioButton("1");
        rbRoadModel = new JRadioButton("2");
        rbRoadModel = new JRadioButton("3");

        // Radio Buttons for Mutual Exclusion
        rbSemaphores = new JRadioButton("Semaphore");
        rbMonitors = new JRadioButton("Monitor");

        // Button
        btStart = new JButton("Iniciar");
        btStart.addActionListener(a -> init());

        // Adicionando componentes ao container
        add(lbRoadModel);
        add(lbVehicleCount);
        add(lbMutualExclusion);
        add(new JLabel()); // Espaço vazio

        add(tfVehicleCount);

        add(rbModel1);
        add(rbModel2);
        add(rbModel3);

        add(rbSemaphores);
        add(rbMonitors);

        add(btStart);

        // Posicionando os componentes
        lbRoadModel.setBounds(20, 20, 200, 25);
        lbVehicleCount.setBounds(20, 50, 200, 25);
        lbMutualExclusion.setBounds(20, 80, 200, 25);

        tfVehicleCount.setBounds(220, 50, 150, 25);

        rbModel1.setBounds(20, 110, 150, 25);
        rbModel2.setBounds(20, 140, 150, 25);
        rbModel3.setBounds(20, 170, 150, 25);

        rbSemaphores.setBounds(20, 200, 150, 25);
        rbMonitors.setBounds(20, 230, 150, 25);

        btStart.setBounds(20, 260, 150, 25);
    }

    private void init(){
        String file = null;

        if (rbModel1.isSelected())
            file = "malha-exemplo-1.txt";
        else if(rbModel2.isSelected())
            file = "malha-exemplo-2.txt";
        else if (rbModel3.isSelected())
            file = "malha-exemplo-3.txt";

        if (file != null && cellFactory != null) {
            Road road = new Road(file, cellFactory);
            CarFactory carFactory = new CarFactory(road);
            EntranceMediatorRoutine routine = new EntranceMediatorRoutine(road);

            int carCount = Integer.parseInt(tfCarCount.getText());

            for (int i = 0; i < carCount; i++) {
                Car car = carFactory.buildCar();
                routine.addToQueue(car);
            }

            new RoadView(road);

            routine.start();
        } else {
            // Lida com o caso em que nenhum modelo de malha ou método de exclusão mútua foi selecionado
        }

        String method= rbSemaphores.isSelected()? new SemaphoricCellFactory() : rbMonitors.isSelected()? new MonitorsCellFactory() : null;
        road = new Road(file, cellFactory);

        CarFactory carFactory = new CarFactory(road);

        EntranceMediatorRoutine routine = new EntranceMediatorRoutine(road);

        int carCount = Integer.parseInt(tfCarCount.getText());

        for(int i = 0; i < carCount; i++){
            Car car = carFactory.buildCar();
            routine.addToQueue(car);
        }
        new RoadView(road);

        routine.start();
    }

}