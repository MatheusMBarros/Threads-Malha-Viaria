package udesc.dsd;

public abstract class Cell {
    private final Position position;
    private final CellType type;
    protected Vehicle vehicle;
    private final boolean isEntrance;
    private final boolean isCross;
    private final boolean isExit;


    public Cell(Position position, CellType type, boolean isEntrance, boolean isCross, boolean isExit) {
        this.position = position;
        this.type = type;
        this.isEntrance = isEntrance;
        this.isCross = isCross;
        this.isExit = isExit;
    }

    public void setVehicle(Vehicle vehicle){
        this.vehicle = vehicle;
    }

    public void removeVehicle(){
        this.vehicle = null;
    }

    public synchronized boolean isEmpty(){
        return vehicle == null;
    }

    public CellType getType(){
        return type;
    }
}