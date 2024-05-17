package udesc.dsd.factory;

import udesc.dsd.road.cell.Cell;
import udesc.dsd.road.cell.CellType;
import udesc.dsd.road.cell.Position;

public abstract class CellFactory {

    public abstract Cell createCell(Position position, CellType type, boolean isCross);
}