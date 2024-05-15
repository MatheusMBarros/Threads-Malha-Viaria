package udesc.dsd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Road {
    private Cell[][] grid;
    private final List<Vehicle> cars = new ArrayList<>();

    public Road(String file) {
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {

            try{
                int cols;
                int lines = 0;
                int columnCounter = 0;
                int lineCounter = 0;
                String[] values;
                String line = reader.readLine();

                while(line != null){
                    values = line.split("\t");

                    if(lineCounter < 2){
                        if (lineCounter == 0) lines = Integer.parseInt(values[0]);
                        else{
                            cols = Integer.parseInt(values[0]);
                            matrix = new Cell[lines][cols];
                        }

                        lineCounter++;
                        line = reader.readLine();
                        continue;
                    }

                    assert matrix != null;

                    for(String value : values){
                        int numValue = Integer.parseInt(value);
                        int cellLine = lineCounter - 2;
                        Direction direction = new Direction(numValue);
                        boolean isEntrance = isEntrance(direction.to(), cellLine, columnCounter);
                        boolean isCross = isCross(direction.to());

                        Cell cell = factory.createCell(cellLine, columnCounter, direction, isEntrance, isCross);

                        matrix[cell.getRow()][cell.getCol()] = cell;
                        columnCounter++;
                    }

                    lineCounter++;
                    columnCounter = 0;
                    line = reader.readLine();
                }

            } catch (IOException e){
                return new Cell[0][0];
            }

            return matrix;

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

        for (Cell[] cells : matrix) {
            for(Cell cell : cells){
                if (cell.isEntrance()) entrances.add(cell);
            }
        }
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

    public boolean isEntrancePoint(int linha, int coluna) {
        return ((linha == 0 && this.getTipoSegmento(linha, coluna) == 3) ||
                (linha == getLinhas() - 1 && this.getTipoSegmento(linha, coluna) == 1) ||
                (coluna == 0 && this.getTipoSegmento(linha, coluna) == 2) ||
                (coluna == getColunas() - 1 && this.getTipoSegmento(linha, coluna) == 4)) ;
    }

    public boolean[] getDirecoesDisponiveis(int linha, int coluna) {
        boolean[] saidas = new boolean[4]; // Representa as direções: cima, direita, baixo, esquerda

        // Verifica se a posição está em uma borda da malha
        if (linha == 0) // Borda superior
            saidas[2] = true;
        if (linha == getLinhas() - 1) // Borda inferior
            saidas[0] = true;
        if (coluna == 0) // Borda esquerda
            saidas[1] = true;
        if (coluna == getColunas() - 1) // Borda direita
            saidas[3] = true;

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

}
