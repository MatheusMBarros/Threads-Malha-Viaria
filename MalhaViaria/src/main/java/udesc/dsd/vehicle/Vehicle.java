package udesc.dsd.vehicle;

import udesc.dsd.road.cell.Cell;
import udesc.dsd.road.CrossStrategy;
import udesc.dsd.road.cell.Position;
import udesc.dsd.road.Road;

import java.awt.*;

public class Vehicle extends Thread {
    private Road road;
    private Cell cell;
    private int speed;
    private Color color;


    public Vehicle(Road road, int speed, Color color) {
        this.road = road;
        this.speed = speed;
        this.color = color;
    }

    @Override
    public void run() {
        while (road.isActive() && !this.isInterrupted()) {
            try {
                if(cell == null){
                    moveToCell(road.getEmptyEntrance());
                }

                Cell nextCell = getNextCell();
                if(nextCell != null){
                    if(!nextCell.isCross()){
                        moveToCell(nextCell);
                    }
                    else{
                        CrossStrategy strategy = new CrossStrategy(road, cell);
                        strategy.cross();
                    }
                }
                else{
                    sleep(speed);
                    interrupt();
                    this.removeFromCell();
                    road.removeVehicle(this);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Cell getCell(){
        return cell;
    }

    public Color getColor(){
        return color;
    }

    public void setCell(Cell cell) throws InterruptedException {
        this.cell = cell;
    }

    public void moveToCell(Cell cell){
        try {
            Cell aux = this.cell;
            cell.setVehicleAndLock(this);
            setCell(cell);
            if (aux != null) aux.removeVehicleAndRelease();
            sleep(speed);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void moveToCellInCross(Cell cell){
        try {
            Cell aux = this.cell;
            setCell(cell);
            cell.setVehicle(this);
            if (aux != null) aux.removeVehicle();
            sleep(speed);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void removeFromCell(){
        cell.removeVehicleAndRelease();
        this.cell = null;
    }

    private Cell getNextCell(){
        try {
            Cell cell = this.cell;
            Position pos = cell.getPosition();

            return switch (cell.getType()) {
                case ROAD_UP -> road.cellAtUp(pos);
                case ROAD_RIGHT -> road.cellAtRight(pos);
                case ROAD_DOWN -> road.cellAtDown(pos);
                case ROAD_LEFT -> road.cellAtLeft(pos);
                default -> null;
            };

        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
}


