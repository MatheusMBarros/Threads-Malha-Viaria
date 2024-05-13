package org.matheus.barros;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.Semaphore;


public class Veiculo extends Thread {
    private static final int[][] DIRECOES = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}}; // cima, direita, baixo, esquerda
    private Malha malha;
    private int linhaAtual;
    private int colunaAtual;
    private int direcao;
    private Color cor;
    private Random random;
    private MalhaGUI gui;
    private SemaforoCruzamento semaforoCruzamento;
    private int velocidade;



    public Veiculo(Malha malha, int linha, int coluna, MalhaGUI gui, int velocidade, Color cor, SemaforoCruzamento semaforoCruzamento) {
        this.malha = malha;
        this.linhaAtual = linha;
        this.colunaAtual = coluna;
        this.direcao = escolherDirecaoInicial();
        this.random = new Random();
        this.gui = gui;
        this.velocidade = velocidade;
        this.cor = cor;
        this.semaforoCruzamento = semaforoCruzamento;

    }

    @Override
    public void run() {
        while (!isInterrupted() && gui.isSimulacaoRodando()) {
            try {
                semaforoCruzamento.entrarCruzamento(); // Tenta entrar no cruzamento
                mover();
                semaforoCruzamento.sairCruzamento(); // Sai do cruzamento após a movimentação
                Thread.sleep(velocidade > 0 ? velocidade : 1); // Garante que o tempo de espera seja sempre positivo
                SwingUtilities.invokeLater(gui::repaint);
            } catch (InterruptedException e) {
                e.notifyAll();
                interrupt();
            }
        }
    }

    public Color getCor(){
        return this.cor;
    }

    public void mover() {
        // Tentar adquirir o semáforo do cruzamento
        // Movimento do veículo
        int proximaLinha = linhaAtual;
        int proximaColuna = colunaAtual;

        // Determina a próxima posição com base na direção atual do veículo
        switch (direcao) {
            case 0: // Cima
                proximaLinha--;
                break;
            case 1: // Direita
                proximaColuna++;
                break;
            case 2: // Baixo
                proximaLinha++;
                break;
            case 3: // Esquerda
                proximaColuna--;
                break;
        }

        // Verifica se a próxima posição é válida
        if (gui.posicaoEstaLivre(proximaLinha, proximaColuna)) {
            // Move o veículo para a próxima posição
            linhaAtual = proximaLinha;
            colunaAtual = proximaColuna;
        } else if (malha.getTipoSegmento(proximaLinha, proximaColuna) >= 9 && malha.getTipoSegmento(proximaLinha, proximaColuna) <= 12) {
            // Se a próxima posição for um cruzamento
            if (semaforoCruzamento.availablePermits() == 1) {
                // Verifica se há apenas um veículo no cruzamento
                boolean[] saidasDisponiveis = malha.getDirecoesDisponiveis(proximaLinha, proximaColuna);

                // Escolhe a direção correta de acordo com o tipo de cruzamento
                switch (malha.getTipoSegmento(proximaLinha, proximaColuna)) {
                    case 9: // Cruzamento Cima e Direita
                        if (direcao == 0 && saidasDisponiveis[1]) {
                            direcao = 1;
                            colunaAtual++;
                        } else if (direcao == 1 && saidasDisponiveis[0]) {
                            direcao = 0;
                            linhaAtual--;
                        }
                        break;
                    case 10: // Cruzamento Cima e Esquerda
                        if (direcao == 0 && saidasDisponiveis[3]) {
                            direcao = 3;
                            colunaAtual--;
                        } else if (direcao == 3 && saidasDisponiveis[0]) {
                            direcao = 0;
                            linhaAtual--;
                        }
                        break;
                    case 11: // Cruzamento Direita e Baixo
                        if (direcao == 1 && saidasDisponiveis[2]) {
                            direcao = 2;
                            linhaAtual++;
                        } else if (direcao == 2 && saidasDisponiveis[1]) {
                            direcao = 1;
                            colunaAtual++;
                        }
                        break;
                    case 12: // Cruzamento Baixo e Esquerda
                        if (direcao == 2 && saidasDisponiveis[3]) {
                            direcao = 3;
                            colunaAtual--;
                        } else if (direcao == 3 && saidasDisponiveis[2]) {
                            direcao = 2;
                            linhaAtual++;
                        }
                        break;
                }
            }
        }

        // Redesenha a GUI
        SwingUtilities.invokeLater(gui::repaint);

    }






    private int escolherDirecaoInicial() {
        // Determina a direção inicial com base nas saídas disponíveis na posição atual
        boolean[] saidasDisponiveis = malha.getDirecoesDisponiveis(linhaAtual, colunaAtual);
        for (int i = 0; i < DIRECOES.length; i++) {
            if (saidasDisponiveis[i]) {
                return i;
            }
        }
        return -1; // Não deveria acontecer se a malha estiver bem definida
    }

    public int getColunaAtual() {
        return colunaAtual;
    }

    public int getLinhaAtual() {
        return linhaAtual;
    }

}


