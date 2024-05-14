package org.matheus.barros;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Veiculo extends Thread {
    private static final int[][] DIRECOES = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}}; // cima, direita, baixo, esquerda
    private Malha malha;
    private int linhaAtual;
    private int colunaAtual;
    private int direcao;
    private Color cor;
    private Random random;
    private MalhaGUI gui;
    private int velocidade;

    private SemaphoreCruzamento semaphore;
    private MonitorCruzamento monitor;

    public Veiculo(Malha malha, int linha, int coluna, MalhaGUI gui, int velocidade, Color cor, SemaphoreCruzamento semaphore, MonitorCruzamento monitor) {
        this.malha = malha;
        this.linhaAtual = linha;
        this.colunaAtual = coluna;
        this.direcao = escolherDirecaoInicial();
        this.random = new Random();
        this.gui = gui;
        this.velocidade = velocidade;
        this.cor = cor;
        this.semaphore = semaphore;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while (!isInterrupted() && gui.isSimulacaoRodando()) {
            try {
                Thread.sleep(velocidade > 0 ? velocidade : 1); // Garante que o tempo de espera seja sempre positivo

                if(gui.onMonitor){
                    monitor.entrarCruzamento();
                    mover();
                    monitor.sairCruzamento();
                }
                if(gui.onSemaphore){
                    semaphore.entrarCruzamento(); // Tenta entrar no cruzamento
                    mover();
                    semaphore.sairCruzamento(); // Sai do cruzamento após a movimentação
                }

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
            // Verifica se a próxima posição é uma via
            int tipoSegmentoProximo = malha.getTipoSegmento(proximaLinha, proximaColuna);
            if (tipoSegmentoProximo >= 1 && tipoSegmentoProximo <= 4) {
                // Move o veículo para a próxima posição
                linhaAtual = proximaLinha;
                colunaAtual = proximaColuna;
            }

            if (tipoSegmentoProximo >= 9 && tipoSegmentoProximo <= 12) {
                if (semaphore.availablePermits() == 1) {
                    // Verifica se há apenas um veículo no cruzamento
                    boolean[] saidasDisponiveis = malha.getDirecoesDisponiveis(proximaLinha, proximaColuna);

                    // Escolhe a direção correta de acordo com o tipo de cruzamento
                    switch (tipoSegmentoProximo) {
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
        }

        SwingUtilities.invokeLater(gui::repaint);

        // Atualiza a direção do veículo se necessário
//        if (malha.getTipoSegmento(linhaAtual, colunaAtual) >= 5) {
//            boolean[] saidasDisponiveis = malha.getDirecoesDisponiveis(linhaAtual, colunaAtual);
//            for (int i = 0; i < DIRECOES.length; i++) {
//                if (saidasDisponiveis[i]) {
//                    direcao = i;
//                    break;
//                }
//            }
//        }
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


