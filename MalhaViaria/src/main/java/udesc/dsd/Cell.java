package udesc.dsd;

public abstract class Cell {
    private final int row;
    private final int col;
    private final int direction;
    protected Vehicle vehicle;
    private Road road;
    private final boolean isEntrance;
    private final boolean isCross;

    public Cell(int row, int col, int direction, boolean isEntrance, boolean isCross) {
        this.row = row;
        this.col = col;
        this.direction = direction;
        this.isEntrance = isEntrance;
        this.isCross = isCross;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isCross() {return isCross;}

    public void setCar(Vehicle car) throws InterruptedException {
        block(); //entao espere
        this.vehicle = car;//eu ocupei
        ui.update(car.getCarIcon(), row, col);
    }

    public void setRoad(Road road){
        this.road = road;
    }

    public void removeCar() {
        this.car = null; //desocupei
        ui.update(direction.getImage(), row, col);
        release(); //pode vir
    }

    private Cell cellAtUp(){
        return road.getCellAtUpFrom(this);
    }

    private Cell cellAtRight(){
        return road.getCellAtRightFrom(this);
    }

    private Cell cellAtDown(){
        return road.getCellAtDownFrom(this);
    }

    private Cell cellAtLeft(){
        return road.getCellAtLeftFrom(this);
    }

    public boolean cellAtUpIsCross(){
        Cell cell = cellAtUp();
        return cell != null && cell.isCross();
    }

    public boolean cellAtDownIsCross(){
        Cell cell = cellAtDown();
        return cell != null && cell.isCross();
    }

    public boolean cellAtLeftIsCross(){
        Cell cell = cellAtLeft();
        return cell != null && cell.isCross();    }

    public boolean cellAtRightIsCross(){
        Cell cell = cellAtRight();
        return cell != null && cell.isCross();
    }

    public abstract void release();

    public abstract void block() throws InterruptedException;

    public abstract boolean tryBlock() throws InterruptedException;

    public boolean isEntrance() {
        return isEntrance;
    }

    public synchronized boolean isFree(){
        return car == null;
    }

}