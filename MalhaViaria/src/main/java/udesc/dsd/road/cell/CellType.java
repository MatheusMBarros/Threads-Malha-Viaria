package udesc.dsd.road.cell;

public enum CellType {

    NONE(0),
    ROAD_UP(1),
    ROAD_RIGHT(2),
    ROAD_DOWN(3),
    ROAD_LEFT(4),
    CROSSROAD_UP(5),
    CROSSROAD_RIGHT(6),
    CROSSROAD_DOWN(7),
    CROSSROAD_LEFT(8),
    CROSSROAD_UP_RIGHT(9),
    CROSSROAD_UP_LEFT(10),
    CROSSROAD_DOWN_RIGHT(11),
    CROSSROAD_DOWN_LEFT(12);

    private final int code;

    CellType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static CellType valueOf(int code) {
        for (CellType ct : CellType.values()) {
            if (code == ct.getCode()) {
                return ct;
            }
        }
        throw new IllegalArgumentException();
    }

}