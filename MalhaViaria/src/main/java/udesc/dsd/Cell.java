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

    public synchronized boolean isEmpty(){
        return vehicle == null;
    }

    public CellType getType(){
        return type;
    }
    public Position getPosition(){
        return position;
    }

    public boolean isEntrance() {
        return isEntrance;
    }

    public boolean isCross() {
        return isCross;
    }

    public boolean isExit() {
        return isExit;
    }

    public boolean isCrossEnd(){
        return type.getCode() > 4 && type.getCode() < 9;
    }

    public void setVehicle(Vehicle v){
        this.vehicle = v;
    }

    public void setVehicleAndLock(Vehicle v) throws InterruptedException {
        lock();
        setVehicle(v);
    }

    public void removeVehicle(){
        this.vehicle = null;
    }

    public void removeVehicleAndRelease() {
        removeVehicle();
        release();
    }

    public abstract void release();

    public abstract void lock() throws InterruptedException;

    public abstract boolean tryLock() throws InterruptedException;

}