package udesc.dsd;

public class SemaphoreCellFactory extends CellFactory {
    @Override
    public Cell createCell(Position position, CellType type, boolean isEntrance, boolean isCross, boolean isExit) {
        return new SemaphoreCell(position, type, isEntrance, isCross, isExit);
    }
}
