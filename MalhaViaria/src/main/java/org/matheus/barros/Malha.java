package org.matheus.barros;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

    public class Malha {
        private int[][] segmentos;
        private int linhas;
        private int colunas;

        public Malha(String arquivo) {
            carregarMalha(arquivo);
        }

        private void carregarMalha(String arquivo) {
            try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
                linhas = Integer.parseInt(br.readLine());
                colunas = Integer.parseInt(br.readLine());
                segmentos = new int[linhas][colunas];

                for (int i = 0; i < linhas; i++) {
                    String[] linha = br.readLine().split("\t");
                    for (int j = 0; j < colunas; j++) {
                        segmentos[i][j] = Integer.parseInt(linha[j]);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    public synchronized boolean podeMoverPara(int linhaAtual, int colunaAtual, int linhaDestino, int colunaDestino) {
        // Verificar se a posição de destino está dentro da malha
        if (linhaDestino < 0 || linhaDestino >= linhas || colunaDestino < 0 || colunaDestino >= colunas) {
            return false;
        }

        // Verificar se a posição de destino está livre
        if (segmentos[linhaDestino][colunaDestino] != 0) {
            return false;
        }

        // Verificar se há movimento permitido entre a posição atual e a posição de destino
        int tipoAtual = segmentos[linhaAtual][colunaAtual];
        int tipoDestino = segmentos[linhaDestino][colunaDestino];
        switch (tipoAtual) {
            case 1: // Estrada Cima
                return tipoDestino == 3 || tipoDestino == 5 || tipoDestino == 6 || tipoDestino == 11 || tipoDestino == 12;
            case 2: // Estrada Direita
                return tipoDestino == 4 || tipoDestino == 7 || tipoDestino == 9 || tipoDestino == 10 || tipoDestino == 12;
            case 3: // Estrada Baixo
                return tipoDestino == 1 || tipoDestino == 5 || tipoDestino == 7 || tipoDestino == 9 || tipoDestino == 10;
            case 4: // Estrada Esquerda
                return tipoDestino == 2 || tipoDestino == 6 || tipoDestino == 8 || tipoDestino == 11 || tipoDestino == 12;
            case 5: // Cruzamento Cima
                return tipoDestino != 0;
            case 6: // Cruzamento Direita
                return tipoDestino != 0;
            case 7: // Cruzamento Baixo
                return tipoDestino != 0;
            case 8: // Cruzamento Esquerda
                return tipoDestino != 0;
            case 9: // Cruzamento Cima e Direita
                return tipoDestino != 0;
            case 10: // Cruzamento Cima e Esquerda
                return tipoDestino != 0;
            case 11: // Cruzamento Direita e Baixo
                return tipoDestino != 0;
            case 12: // Cruzamento Baixo e Esquerda
                return tipoDestino != 0;
            default:
                return false;
        }
        // Métodos para obter informações sobre a malha, como tipo de segmento, pontos de entrada e saída, etc.
    }

    public int getColunas() {
        return this.colunas;
    }
    public int getLinhas() {
        return this.linhas;
    }

    public int getTipoSegmento(int i, int j) {
        return segmentos[i][j];
    }
        public boolean ehPontoDeSaida(int linha, int coluna) {
            return (linha == 0 || linha == getLinhas() - 1 || coluna == 0 || coluna == getColunas() - 1);
        }
        public boolean[] getSaidasDisponiveis(int linha, int coluna) {
            boolean[] saidas = new boolean[4]; // Representa as direções: cima, direita, baixo, esquerda

            // Verifica se a posição está em uma borda da malha
            if (linha == 0) // Borda superior
                saidas[0] = true;
            if (linha == getLinhas() - 1) // Borda inferior
                saidas[2] = true;
            if (coluna == 0) // Borda esquerda
                saidas[3] = true;
            if (coluna == getColunas() - 1) // Borda direita
                saidas[1] = true;

            // Verifica se a posição é um cruzamento com saídas disponíveis
            int tipoSegmento = getTipoSegmento(linha, coluna);
            switch (tipoSegmento) {
                case 5: // Cruzamento Cima
                    saidas[0] = true;
                    break;
                case 6: // Cruzamento Direita
                    saidas[1] = true;
                    break;
                case 7: // Cruzamento Baixo
                    saidas[2] = true;
                    break;
                case 8: // Cruzamento Esquerda
                    saidas[3] = true;
                    break;
                case 9: // Cruzamento Cima e Direita
                    saidas[0] = true;
                    saidas[1] = true;
                    break;
                case 10: // Cruzamento Cima e Esquerda
                    saidas[0] = true;
                    saidas[3] = true;
                    break;
                case 11: // Cruzamento Direita e Baixo
                    saidas[1] = true;
                    saidas[2] = true;
                    break;
                case 12: // Cruzamento Baixo e Esquerda
                    saidas[2] = true;
                    saidas[3] = true;
                    break;
            }

            return saidas;
        }
        public boolean ehPosicaoValida(int linha, int coluna) {
            return linha >= 0 && linha < getLinhas() && coluna >= 0 && coluna < getColunas();
        }

        public boolean ehPosicaoLivre(int linha, int coluna) {
            // Adicione aqui a lógica para verificar se a posição está livre (sem veículo)
            // Por exemplo, você pode manter uma matriz de booleanos para controlar a ocupação das posições
            // Neste exemplo fictício, vamos supor que todas as posições estão sempre livres
            return true;
        }
        public void moverVeiculo(Veiculo veiculo, int linhaAtual, int colunaAtual, int linhaDestino, int colunaDestino) {
            // Adicione aqui a lógica para mover o veículo da posição atual para a posição de destino
            // Por exemplo, atualize a matriz que controla a ocupação das posições
        }

        public boolean ehCruzamento(int linha, int coluna) {
            int tipoSegmento = getTipoSegmento(linha, coluna);
            return tipoSegmento >= 5 && tipoSegmento <= 12; // Retorna true se o tipo de segmento estiver entre 5 e 12
        }



    }
