package udesc.dsd;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class RoadView extends JFrame {
    private Road road;
    private int larguraCelula = 40;
    private int alturaCelula = 40;
    private int larguraMalha;
    private int alturaMalha;
    private JPanel painel;
    private Random random;
    private int maxVeiculos;
    private boolean simulacaoRodando;

    private ArrayList<Vehicle> vehicles;

    private JButton iniciarSimulacao;
    private JButton terminarSimulacao;
    private JButton simulaSemaphore;
    private JButton simulaMonitor;


    private JTextField quantidadeVeiculosField;

    public RoadView(Road road) {
        this.road = road;
        larguraMalha = road.getColunas() * larguraCelula;
        alturaMalha = road.getLinhas() * alturaCelula;

        setTitle("Simulador de Tráfego");
        setSize(larguraMalha + 100, alturaMalha + 100); // Adicionando espaço para os botões
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        painel = new JPanel(new FlowLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                desenharMalha(g);
                desenharVeiculos(g);
            }
        };

        painel.setPreferredSize(new Dimension(larguraMalha + 100, alturaMalha));
        add(painel, BorderLayout.CENTER);

        random = new Random();
        simulacaoRodando = false;
        vehicles = new ArrayList<Vehicle>();

        // Adicionando botões e inputs
        JPanel controlPanel = new JPanel(new FlowLayout());

        iniciarSimulacao = new JButton("Iniciar Simulação");
        iniciarSimulacao.addActionListener(e -> iniciarSimulacao());
        controlPanel.add(iniciarSimulacao);
        iniciarSimulacao.setEnabled(false);

        terminarSimulacao = new JButton("Terminar Simulação");
        terminarSimulacao.addActionListener(e -> terminarSimulacao());
        controlPanel.add(terminarSimulacao);
        terminarSimulacao.setEnabled(false);


        simulaSemaphore = new JButton("Semaforo");
        simulaSemaphore.addActionListener(e -> usaSemaforo());
        controlPanel.add(simulaSemaphore);

        simulaMonitor = new JButton("Monitor");
        simulaMonitor.addActionListener(e -> usaMonitor());
        controlPanel.add(simulaMonitor);


        quantidadeVeiculosField = new JTextField("10", 5); // Valor padrão de 10 veículos
        controlPanel.add(new JLabel("Quantidade de Veículos:"));
        controlPanel.add(quantidadeVeiculosField);

        add(controlPanel, BorderLayout.SOUTH);

        // Chamando setVisible dentro de SwingUtilities.invokeLater para garantir que seja executado na EDT
        SwingUtilities.invokeLater(() -> setVisible(true));

        // Adicionando um Timer para atualizar a interface gráfica periodicamente
        Timer timer = new Timer(1000, e -> repaint());
        timer.start();
    }

    private String usaMonitor() {
        simulaSemaphore.setEnabled(false);
        onMonitor = true;
        iniciarSimulacao.setEnabled(true);
        return "Strinf";
    }

    private String usaSemaforo() {
        simulaMonitor.setEnabled(false);
        onSemaphore = true;
        iniciarSimulacao.setEnabled(true);
        return "Strinf";
    }

    private void desenharVeiculos(Graphics g) {
        for (Vehicle vehicle : vehicles) {
            int linha = vehicle.getLinhaAtual();
            int coluna = vehicle.getColunaAtual();
            int x = coluna * larguraCelula;
            int y = linha * alturaCelula;
            g.setColor(vehicle.getCor());
            g.fillOval(x, y, larguraCelula, alturaCelula);
        }
    }

    private void desenharMalha(Graphics g) {
        for (int i = 0; i < road.getLinhas(); i++) {
            for (int j = 0; j < road.getColunas(); j++) {
                int tipo = road.getTipoSegmento(i, j);
                Color cor;
                switch (tipo) {
                    case 0:
                        cor = Color.BLACK;
                        break;
                    case 1:
                        cor = Color.BLUE;
                        break;
                    case 2:
                    case 3:
                        cor = Color.GREEN;
                        break;
                    case 4:
                        cor = Color.PINK;
                        break;
                    default:
                        cor = Color.WHITE;
                }
                g.setColor(cor);
                g.fillRect(j * larguraCelula, i * alturaCelula, larguraCelula, alturaCelula);
                g.setColor(Color.BLACK);
                g.drawRect(j * larguraCelula, i * alturaCelula, larguraCelula, alturaCelula);
            }
        }
    }

    public void iniciarSimulacao() {
        if (simulacaoRodando) {
            return;
        }
        try {
            maxVeiculos = Integer.parseInt(quantidadeVeiculosField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Digite um número válido para a quantidade de veículos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        simulacaoRodando = true;
        terminarSimulacao.setEnabled(true);
        iniciarSimulacao.setEnabled(false);
        simulaSemaphore.setEnabled(false);
        simulaMonitor.setEnabled(false);

        // Inicie a simulação em uma nova thread para não bloquear a EDT
        new Thread(() -> {
            while (simulacaoRodando) {
                if (vehicles.size() < maxVeiculos) {
                    inserirVeiculo();
                    SwingUtilities.invokeLater(this::repaint); // Redesenha a interface gráfica após adicionar um novo veículo
                }
                try {
                    Thread.sleep(1000); // Atraso de 1 segundo antes de adicionar outro veículo
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void terminarSimulacao(){
        Iterator<Vehicle> iterator = vehicles.iterator();
        while (iterator.hasNext()) {
            Vehicle vehicle = iterator.next();
            vehicle.interrupt();
            iterator.remove();
        }
        simulacaoRodando = false;
        simulaMonitor.setEnabled(true);
        onMonitor = false;
        simulaSemaphore.setEnabled(true);
        onSemaphore = false;
        terminarSimulacao.setEnabled(false);

    }

    private void inserirVeiculo() {
        ArrayList<Point> posicoesValidas = new ArrayList<>();
        // Itera sobre todas as posições da road
        for (int i = 0; i < road.getLinhas(); i++) {
            for (int j = 0; j < road.getColunas(); j++) {
                // Verifica se a posição está livre e não é uma área proibida (tipo 0)
                if (posicaoEstaLivre(i, j) && road.ehPontoDeEntrada(i, j)) {
                    posicoesValidas.add(new Point(i, j)); // Adiciona a posição válida à lista
                }
            }
        }
        // Verifica se existem posições válidas disponíveis
        if (!posicoesValidas.isEmpty()) {
            // Escolhe aleatoriamente uma das posições válidas disponíveis
            Point posicaoSelecionada = posicoesValidas.get(random.nextInt(posicoesValidas.size()));
            int linha = posicaoSelecionada.x;
            int coluna = posicaoSelecionada.y;

            // Cria o veículo na posição selecionada
            Vehicle vehicle = new Vehicle(road, linha, coluna, this, random.nextInt(200, 1000), new Color((int) (Math.random() * 0x10000000)), new SemaphoreCell(), new MonitorCell()); // Velocidade aleatória
            vehicles.add(vehicle);

            // Inicia a thread do veículo na EDT
            SwingUtilities.invokeLater(vehicle::start);
        }
    }

    public static void main(String[] args) {
        Road road = new Road("MalhaViaria/src/malhas/road-exemplo-1.txt");
        Main gui = new Main(road);
    }

    public boolean posicaoEstaLivre(int linha, int coluna) {

        if (!road.ehPosicaoValida(linha, coluna)){
            return false;
        }
        for(Vehicle vehicle : vehicles){
            if(vehicle.getLinhaAtual() == linha && vehicle.getColunaAtual() == coluna) {
                return false;
            }
        }

        return true;
    }

    public boolean isSimulacaoRodando(){
        return simulacaoRodando;
    }
}
