package org.matheus.barros;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class MalhaGUI extends JFrame {
    private Malha malha;
    private int larguraCelula = 40;
    private int alturaCelula = 40;
    private int larguraMalha;
    private int alturaMalha;
    private JPanel painel;
    private Random random;
    private int maxVeiculos;
    private boolean simulacaoRodando;

    private ArrayList<Veiculo> veiculos;

    private JButton iniciarSimulacao;
    private JButton terminarSimulacao;
    private JTextField quantidadeVeiculosField;

    public MalhaGUI(Malha malha) {
        this.malha = malha;
        larguraMalha = malha.getColunas() * larguraCelula;
        alturaMalha = malha.getLinhas() * alturaCelula;

        setTitle("Simulador de Tráfego");
        setSize(larguraMalha + 16, alturaMalha + 100); // Adicionando espaço para os botões
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        painel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            desenharMalha(g);
            desenharVeiculos(g);
            }
        };

        painel.setPreferredSize(new Dimension(larguraMalha, alturaMalha));
        add(painel);

        random = new Random();
        simulacaoRodando = false;
        veiculos = new ArrayList<Veiculo>();

        // Adicionando botões e inputs
        JPanel controlPanel = new JPanel(new FlowLayout());

        iniciarSimulacao = new JButton("Iniciar Simulação");
        iniciarSimulacao.addActionListener(e -> iniciarSimulacao());
        controlPanel.add(iniciarSimulacao);

        terminarSimulacao = new JButton("Terminar Simulação");
        terminarSimulacao.addActionListener(e -> terminarSimulacao());
        controlPanel.add(terminarSimulacao);

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

    private void desenharVeiculos(Graphics g) {
        for (Veiculo veiculo : veiculos) {
            int linha = veiculo.getLinhaAtual();
            int coluna = veiculo.getColunaAtual();
            int x = coluna * larguraCelula;
            int y = linha * alturaCelula;
            g.setColor(veiculo.getCor());
            g.fillOval(x, y, larguraCelula, alturaCelula);
        }
    }

    private void desenharMalha(Graphics g) {
        for (int i = 0; i < malha.getLinhas(); i++) {
            for (int j = 0; j < malha.getColunas(); j++) {
                int tipo = malha.getTipoSegmento(i, j);
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

        // Inicie a simulação em uma nova thread para não bloquear a EDT
        new Thread(() -> {
            while (simulacaoRodando) {
                if (veiculos.size() < maxVeiculos) {
                    inserirVeiculo();
                    SwingUtilities.invokeLater(this::repaint); // Redesenha a interface gráfica após adicionar um novo veículo
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.notify();
                }
            }
        }).start();
    }

    public void terminarSimulacao(){
        Iterator<Veiculo> iterator = veiculos.iterator();
        while (iterator.hasNext()) {
            Veiculo veiculo = iterator.next();
            veiculo.interrupt();
            iterator.remove();
        }
        simulacaoRodando = false;
    }

    private void inserirVeiculo() {
        ArrayList<Point> posicoesValidas = new ArrayList<>();
        // Itera sobre todas as posições da malha
        for (int i = 0; i < malha.getLinhas(); i++) {
            for (int j = 0; j < malha.getColunas(); j++) {
                // Verifica se a posição está livre e não é uma área proibida (tipo 0)
                if (posicaoEstaLivre(i, j) && malha.ehPontoDeEntrada(i, j)) {
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
            Veiculo veiculo = new Veiculo(malha, linha, coluna, this, random.nextInt(1000), new Color((int) (Math.random() * 0x1000000))); // Velocidade aleatória
            veiculos.add(veiculo);

            // Inicia a thread do veículo na EDT
            SwingUtilities.invokeLater(veiculo::start);
        }
    }

    public static void main(String[] args) {
        Malha malha = new Malha("C:\\Users\\AVLip\\Documents\\DEV\\Threads-Malha-Viaria\\MalhaViaria\\src\\malhas\\malha-exemplo-1.txt");
        MalhaGUI gui = new MalhaGUI(malha);
    }

    public boolean posicaoEstaLivre(int linha, int coluna) {
        for(Veiculo veiculo : veiculos){
            if(veiculo.getLinhaAtual() == linha && veiculo.getColunaAtual() == coluna) {
                return false;
            }
        }
        return malha.ehPosicaoValida(linha, coluna);
    }

    public boolean isSimulacaoRodando(){
        return simulacaoRodando;
    }
}
