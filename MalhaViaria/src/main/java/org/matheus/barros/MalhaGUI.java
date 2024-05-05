package org.matheus.barros;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class MalhaGUI extends JFrame {
    private Malha malha;
    private int larguraCelula = 50;
    private int alturaCelula = 50;
    private int larguraMalha;
    private int alturaMalha;
    private JPanel panel;
    private Random random;
    private int maxVeiculos;
    private boolean simulacaoRodando;
    private ArrayList<Veiculo> veiculos;

    private JButton iniciarButton;
    private JTextField quantidadeVeiculosField;

    public MalhaGUI(Malha malha) {
        this.malha = malha;
        larguraMalha = malha.getColunas() * larguraCelula;
        alturaMalha = malha.getLinhas() * alturaCelula;

        setTitle("Simulador de Tráfego");
        setSize(larguraMalha, alturaMalha + 50); // Adicionando espaço para os botões
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                desenharMalha(g);
                desenharVeiculos(g); // Desenha os veículos toda vez que a malha é redesenhada
            }
        };

        panel.setPreferredSize(new Dimension(larguraMalha, alturaMalha));
        add(panel);

        random = new Random();
        maxVeiculos = 10;
        simulacaoRodando = false;
        veiculos = new ArrayList<>();

        // Adicionando botões e inputs
        JPanel controlPanel = new JPanel();
        iniciarButton = new JButton("Iniciar");
        iniciarButton.addActionListener(e -> iniciarSimulacao());
        controlPanel.add(iniciarButton);
        quantidadeVeiculosField = new JTextField("10", 5); // Valor padrão de 10 veículos
        controlPanel.add(new JLabel("Quantidade de Veículos:"));
        controlPanel.add(quantidadeVeiculosField);
        add(controlPanel, BorderLayout.SOUTH);

        // Chamando setVisible dentro de SwingUtilities.invokeLater para garantir que seja executado na EDT
        SwingUtilities.invokeLater(() -> setVisible(true));
    }

    private void desenharVeiculos(Graphics g) {
        for (Veiculo veiculo : veiculos) {
            desenharVeiculo(g, veiculo.getLinhaAtual(), veiculo.getColunaAtual());
        }
    }

    private void desenharVeiculo(Graphics g, int linha, int coluna) {
        int x = coluna * larguraCelula;
        int y = linha * alturaCelula;
        g.setColor(Color.RED);
        g.fillOval(x, y, larguraCelula, alturaCelula);
    }

    public void iniciarSimulacao() {
        try {
            maxVeiculos = Integer.parseInt(quantidadeVeiculosField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Digite um número válido para a quantidade de veículos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        simulacaoRodando = true;
        while (simulacaoRodando) {
            if (veiculos.size() < maxVeiculos) {
                inserirVeiculo();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void inserirVeiculo() {
        int linha;
        int coluna;

        int borda = random.nextInt(4);

        switch (borda) {
            case 0:
                linha = 0;
                coluna = random.nextInt(malha.getColunas());
                break;
            case 1:
                linha = random.nextInt(malha.getLinhas());
                coluna = malha.getColunas() - 1;
                break;
            case 2:
                linha = malha.getLinhas() - 1;
                coluna = random.nextInt(malha.getColunas());
                break;
            case 3:
                linha = random.nextInt(malha.getLinhas());
                coluna = 0;
                break;
            default:
                linha = 0;
                coluna = 0;
        }

        if (malha.getTipoSegmento(linha, coluna) >= 1 && malha.getTipoSegmento(linha, coluna) <= 4) {
            Veiculo veiculo = new Veiculo(malha, linha, coluna , this);
            System.out.println(veiculo.getName());
            veiculos.add(veiculo);
            veiculo.start(); // Inicia a thread do veículo
        } else {
            inserirVeiculo();

        }
    }

    private void desenharMalha(Graphics g) {
        for (int i = 0; i < malha.getLinhas(); i++) {
            for (int j = 0; j < malha.getColunas(); j++) {
                int tipo = malha.getTipoSegmento(i, j);
                Color cor;
                switch (tipo) {
                    case 0:
                        cor = Color.black;
                        break;
                    case 1:
                        cor = Color.blue;
                        break;
                    case 2:
                    case 3:
                        cor = Color.green;
                        break;
                    case 4:
                        cor = Color.pink;
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

    public static void main(String[] args) {
        Malha malha = new Malha("src/main/java/org/matheus/barros/malha.txt");
        MalhaGUI gui = new MalhaGUI(malha);
    }
}
