package udesc.dsd.factory;

import udesc.dsd.road.cell.Cell;
import udesc.dsd.road.cell.CellType;
import udesc.dsd.road.cell.MonitorCell;
import udesc.dsd.road.cell.Position;

public class MonitorCellFactory extends CellFactory {
    @Override
    public Cell createCell(Position position, CellType type, boolean isCross) {
        return new MonitorCell(position, type, isCross);
    }
}
