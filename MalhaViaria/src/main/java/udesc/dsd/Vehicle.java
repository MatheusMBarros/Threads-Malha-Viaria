package udesc.dsd;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Vehicle extends Thread {
    private static final int[][] DIRECOES = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}}; // cima, direita, baixo, esquerda
    private Road road;
    private int linhaAtual;
    private int colunaAtual;
    private int direction;
    private Color cor;
    private Random random;
    private Main gui;
    private int sleep;

    private SemaphoreCell semaphore;
    private MonitorCell monitor;

    public Vehicle(Road road, int linha, int coluna, Main gui, int velocidade, Color cor, SemaphoreCell semaphore, MonitorCell monitor) {
        this.road = road;
        this.cell = linha;
        this.colunaAtual = coluna;
        this.direction = chooseDirection();
        this.random = new Random();
        this.gui = gui;
        this.sleep = velocidade;
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
        switch (direction) {
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
            int tipoSegmentoProximo = road.getTipoSegmento(proximaLinha, proximaColuna);
            if (tipoSegmentoProximo >= 1 && tipoSegmentoProximo <= 4) {
                // Move o veículo para a próxima posição
                linhaAtual = proximaLinha;
                colunaAtual = proximaColuna;
            }

            if (tipoSegmentoProximo >= 9 && tipoSegmentoProximo <= 12) {
                if (semaphore.availablePermits() == 1) {
                    // Verifica se há apenas um veículo no cruzamento
                    boolean[] saidasDisponiveis = road.getDirecoesDisponiveis(proximaLinha, proximaColuna);

                    // Escolhe a direção correta de acordo com o tipo de cruzamento
                    switch (tipoSegmentoProximo) {
                        case 9: // Cruzamento Cima e Direita
                            if (direction == 0 && saidasDisponiveis[1]) {
                                direction = 1;
                                colunaAtual++;
                            } else if (direction == 1 && saidasDisponiveis[0]) {
                                direction = 0;
                                linhaAtual--;
                            }
                            break;
                        case 10: // Cruzamento Cima e Esquerda
                            if (direction == 0 && saidasDisponiveis[3]) {
                                direction = 3;
                                colunaAtual--;
                            } else if (direction == 3 && saidasDisponiveis[0]) {
                                direction = 0;
                                linhaAtual--;
                            }
                            break;
                        case 11: // Cruzamento Direita e Baixo
                            if (direction == 1 && saidasDisponiveis[2]) {
                                direction = 2;
                                linhaAtual++;
                            } else if (direction == 2 && saidasDisponiveis[1]) {
                                direction = 1;
                                colunaAtual++;
                            }
                            break;
                        case 12: // Cruzamento Baixo e Esquerda
                            if (direction == 2 && saidasDisponiveis[3]) {
                                direction = 3;
                                colunaAtual--;
                            } else if (direction == 3 && saidasDisponiveis[2]) {
                                direction = 2;
                                linhaAtual++;
                            }
                            break;
                    }
                }
            }
        }

        SwingUtilities.invokeLater(gui::repaint);

        // Atualiza a direção do veículo se necessário
//        if (road.getTipoSegmento(linhaAtual, colunaAtual) >= 5) {
//            boolean[] saidasDisponiveis = road.getDirecoesDisponiveis(linhaAtual, colunaAtual);
//            for (int i = 0; i < DIRECOES.length; i++) {
//                if (saidasDisponiveis[i]) {
//                    direction = i;
//                    break;
//                }
//            }
//        }
    }

    private int chooseDirection() {
        // Determina a direção inicial com base nas saídas disponíveis na posição atual
        boolean[] saidasDisponiveis = road.getDirecoesDisponiveis(linhaAtual, colunaAtual);
        for (int i = 0; i < DIRECOES.length; i++) {
            if (saidasDisponiveis[i]) {
                return i;
            }
        }
        return -1; // Não deveria acontecer se a road estiver bem definida
    }

    public int getColunaAtual() {
        return colunaAtual;
    }

    public int getLinhaAtual() {
        return linhaAtual;
    }

}


