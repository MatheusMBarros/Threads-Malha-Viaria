package udesc.dsd;

public class MonitorCellFactory extends CellFactory {
    @Override
    public Cell createCell(Position position, CellType type, boolean isEntrance, boolean isCross, boolean isExit) {
        return new MonitorCell(position, type, isEntrance, isCross, isExit);
    }
}
