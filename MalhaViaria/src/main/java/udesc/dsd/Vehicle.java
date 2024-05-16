package udesc.dsd;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Vehicle extends Thread {
    private Road road;
    private Cell cell;
    private Long speed;
    private Color color;


    public Vehicle(Road road, long speed, Color color) {
        this.road = road;
        this.speed = speed;
        this.color = color;
    }

    @Override
    public void run() {
        while (road.isActive()) {
            try {
                Cell nextCell = getNextCell();
                if(nextCell != null){
                    if(nextCell.isCross()){
                        CrossStrategy strategy = new CrossStrategy(road, nextCell);
                        strategy.cross();
                    }
                    else{
                        moveToCell(nextCell);
                        sleep(speed);
                    }
                }
                else{
                    removeFromCell();
                    interrupt();
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
        cell.setVehicleAndLock(this);
        this.cell = cell;
    }

    public void moveToCell(Cell cell){
        try {
            Cell aux = this.cell;
            setCell(cell);
            cell.setVehicleAndLock(this);
            if (aux != null) aux.removeVehicleAndRelease();
            Thread.sleep(speed);
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
            Thread.sleep(speed);
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


