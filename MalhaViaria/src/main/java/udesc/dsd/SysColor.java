package udesc.dsd;

import java.awt.*;
import java.util.Random;

public class SysColor {
    private static final Color[] VEHICLE_COLORS = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE};
    private static final Color[] CELL_COLORS = {new Color(230, 230, 250), new Color(173, 216, 230), new Color(240, 128, 128), new Color(144, 238, 144), new Color(255, 215, 0), new Color(255, 218, 185)};

    // Método para obter uma cor aleatória para veículos
    public static Color getVehicleColor() {
        Random rand = new Random();
        return VEHICLE_COLORS[rand.nextInt(VEHICLE_COLORS.length)];
    }

    // Método para obter a cor da célula com base no tipo
    public static Color getCellColor(CellType type) {
        switch (type) {
            case NONE:
                return CELL_COLORS[0]; // LIGHT_GRAY
            case ROAD_UP:
                return CELL_COLORS[1]; // LIGHT_BLUE
            case ROAD_RIGHT:
            case ROAD_DOWN:
                return CELL_COLORS[2]; // LIGHT_CORAL
            case ROAD_LEFT:
                return CELL_COLORS[3]; // LIGHT_GREEN
            default:
                return CELL_COLORS[4]; // LIGHT_YELLOW
        }
    }
}