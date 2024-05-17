package udesc.dsd.factory;

import udesc.dsd.road.cell.Cell;
import udesc.dsd.road.cell.CellType;
import udesc.dsd.road.cell.Position;
import udesc.dsd.road.cell.SemaphoreCell;

public class SemaphoreCellFactory extends CellFactory {
    @Override
    public Cell createCell(Position position, CellType type, boolean isCross) {
        return new SemaphoreCell(position, type, isCross);
    }
}
