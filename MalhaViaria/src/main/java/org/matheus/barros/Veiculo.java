package org.matheus.barros;

import java.util.Random;

public class Veiculo extends Thread {
    private static final int[][] DIRECOES = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}}; // cima, direita, baixo, esquerda
    private Malha malha;
    private int linhaAtual;
    private int colunaAtual;
    private int direcao; // Índice da direção atual no array DIRECOES
    private Random random;
    private MalhaGUI gui; // Referência para a interface gráfica

    public Veiculo(Malha malha, int linha, int coluna, MalhaGUI gui) {
        this.malha = malha;
        this.linhaAtual = linha;
        this.colunaAtual = coluna;
        this.direcao = escolherDirecaoInicial();
        this.random = new Random();
        this.gui = gui;
    }

    @Override
    public void run() {
        while (!malha.ehPontoDeSaida(linhaAtual, colunaAtual)) {
            try {
                Thread.sleep(velocidade()); // Adiciona o tempo de sleep de acordo com a velocidade do veículo
                mover();
                gui.repaint(); // Redesenha a interface gráfica após cada movimento do veículo
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Ao atingir o ponto de saída, encerra a thread
    }

    private int escolherDirecaoInicial() {
        // Determina a direção inicial com base nas saídas disponíveis na posição atual
        boolean[] saidasDisponiveis = malha.getSaidasDisponiveis(linhaAtual, colunaAtual);
        for (int i = 0; i < DIRECOES.length; i++) {
            if (saidasDisponiveis[i]) {
                return i;
            }
        }
        return -1; // Não deveria acontecer se a malha estiver bem definida
    }

    private void mover() {
        // Calcula a próxima posição com base na direção atual
        int proxLinha = linhaAtual + DIRECOES[direcao][0];
        int proxColuna = colunaAtual + DIRECOES[direcao][1];

        // Verifica se a próxima posição está dentro da malha e se está livre
        if (malha.ehPosicaoValida(proxLinha, proxColuna) && malha.ehPosicaoLivre(proxLinha, proxColuna)) {
            malha.moverVeiculo(this, linhaAtual, colunaAtual, proxLinha, proxColuna);
            linhaAtual = proxLinha;
            colunaAtual = proxColuna;

            // Verifica se chegou a um cruzamento
            if (malha.ehCruzamento(linhaAtual, colunaAtual)) {
                // Escolhe aleatoriamente uma das vias de saída disponíveis
                boolean[] saidasDisponiveis = malha.getSaidasDisponiveis(linhaAtual, colunaAtual);
                int novaDirecao = escolherDirecaoAleatoria(saidasDisponiveis);
                if (novaDirecao != -1) {
                    direcao = novaDirecao;
                }
            }
        }
    }

    private int escolherDirecaoAleatoria(boolean[] disponiveis) {
        // Escolhe aleatoriamente uma direção disponível
        int count = 0;
        for (boolean disponivel : disponiveis) {
            if (disponivel) count++;
        }
        if (count == 0) return -1; // Nenhuma direção disponível
        int escolha = random.nextInt(count);
        for (int i = 0; i < disponiveis.length; i++) {
            if (disponiveis[i]) {
                if (escolha == 0) return i;
                escolha--;
            }
        }
        return -1; // Algo deu errado
    }

    public int getColunaAtual() {
        return colunaAtual;
    }

    public int getLinhaAtual() {
        return linhaAtual;
    }

    private int velocidade() {
        // Retorna o tempo de sleep com base na velocidade do veículo
        // (Implemente a lógica de velocidade de acordo com a sua necessidade)
        return 100; // Por exemplo, 100 milissegundos para todos os veículos
    }
}
