package udesc.dsd.road.cell;

import udesc.dsd.vehicle.Vehicle;

public abstract class Cell {
    private final Position position;
    private final CellType type;
    private Vehicle vehicle;
    private final boolean isCross;


    public Cell(Position position, CellType type, boolean isCross) {
        this.position = position;
        this.type = type;
        this.isCross = isCross;
    }

    public synchronized boolean isEmpty(){
        return vehicle == null;
    }

    public CellType getType(){
        return type;
    }

    public Vehicle getVehicle(){
        return vehicle;
    }
    public Position getPosition(){
        return position;
    }

    public boolean isCross() {
        return isCross;
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

    public abstract boolean tryBlock() throws InterruptedException;

}