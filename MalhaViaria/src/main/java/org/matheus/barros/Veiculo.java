package org.matheus.barros;

import org.matheus.barros.MalhaGUI;
import org.matheus.barros.Malha;


import javax.swing.*;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Veiculo extends Thread {
    private static final int[][] DIRECOES = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}}; // cima, direita, baixo, esquerda
    private Malha malha;
    private int linhaAtual;
    private int colunaAtual;
    private int direcao;
    private Random random;
    private MalhaGUI gui;
    private static final Semaphore semaforoCruzamento = new Semaphore(1);
    private int velocidade;

    public Veiculo(Malha malha, int linha, int coluna, MalhaGUI gui, int velocidade) {
        this.malha = malha;
        this.linhaAtual = linha;
        this.colunaAtual = coluna;
        this.direcao = escolherDirecaoInicial();
        this.random = new Random();
        this.gui = gui;
        this.velocidade = velocidade;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(velocidade > 0 ? velocidade : 1); // Garante que o tempo de espera seja sempre positivo
                mover();
                SwingUtilities.invokeLater(gui::repaint);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void mover() {
        System.out.println("movendo" + linhaAtual);
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
        if (malha.ehPosicaoValida(proximaLinha, proximaColuna)) {
            // Verifica se a próxima posição é uma via
            int tipoSegmentoProximo = malha.getTipoSegmento(proximaLinha, proximaColuna);
            if (tipoSegmentoProximo >= 1 && tipoSegmentoProximo <= 4) {
                // Move o veículo para a próxima posição
                linhaAtual = proximaLinha;
                colunaAtual = proximaColuna;
            }
        }

        // Atualiza a direção do veículo se necessário
        if (malha.getTipoSegmento(linhaAtual, colunaAtual) >= 5) {
            boolean[] saidasDisponiveis = malha.getSaidasDisponiveis(linhaAtual, colunaAtual);
            for (int i = 0; i < DIRECOES.length; i++) {
                if (saidasDisponiveis[i]) {
                    direcao = i;
                    break;
                }
            }
        }
    }


    private void setColunaAtual(int colunaAtual) {
        this.colunaAtual = colunaAtual;
    }

    private void setLinhaAtual(int linhaAtual) {
        this.linhaAtual = linhaAtual;
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

    public int getColunaAtual() {
        return colunaAtual;
    }

    public int getLinhaAtual() {
        return linhaAtual;
    }

    public int getVelocidade() {
        return this.velocidade;
    }
}
