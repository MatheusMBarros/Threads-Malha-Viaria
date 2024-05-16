package udesc.dsd;

import java.awt.*;
import java.util.Random;

public class SysColor {
    private static final Color[] VEHICLE_COLORS = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE, Color.CYAN, Color.MAGENTA};
    private static final Color[] CELL_COLORS = {
            new Color(0, 0, 0),  // WHITE (para NONE)
            new Color(173, 216, 230),  // LightSkyBlue (para ROAD_UP)
            new Color(240, 128, 128),  // LightCoral (para ROAD_RIGHT)
            new Color(144, 238, 144),  // LightGreen (para ROAD_DOWN)
            new Color(255, 218, 185),  // PeachPuff (para ROAD_LEFT)
            new Color(255, 255, 224),  // LightYellow (para CROSSROAD_UP)
            new Color(240, 248, 255),  // AliceBlue (para CROSSROAD_RIGHT)
            new Color(255, 160, 122),  // LightSalmon (para CROSSROAD_DOWN)
            new Color(152, 251, 152),  // PaleGreen (para CROSSROAD_LEFT)
            new Color(255, 182, 193),  // LightPink (para CROSSROAD_UP_RIGHT)
            new Color(135, 206, 250),  // LightSkyBlue (para CROSSROAD_UP_LEFT)
            new Color(224, 255, 255),  // LightCyan (para CROSSROAD_DOWN_RIGHT)
            new Color(230, 230, 250)   // Lavender (para CROSSROAD_DOWN_LEFT)
    };

    // Método para obter uma cor aleatória para veículos
    public static Color getVehicleColor() {
        Random rand = new Random();
        return VEHICLE_COLORS[rand.nextInt(VEHICLE_COLORS.length)];
    }

    // Método para obter a cor da célula com base no tipo
    public static Color getCellColor(CellType type) {
        return CELL_COLORS[type.getCode()];
    }
}