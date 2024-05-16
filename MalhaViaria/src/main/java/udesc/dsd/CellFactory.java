package udesc.dsd;

public abstract class CellFactory {

    public abstract Cell createCell(Position position, CellType type, boolean isEntrance, boolean isCross, boolean isExit);
}