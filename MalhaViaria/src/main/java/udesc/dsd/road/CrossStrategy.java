package udesc.dsd.road;

import udesc.dsd.road.cell.Cell;
import udesc.dsd.road.cell.SemaphoreCell;

import java.util.HashMap;
import java.util.Random;

public class CrossStrategy {

    private Road road;
    private Cell cell;

    public CrossStrategy(Road road, Cell c){
        this.road = road;
        this.cell = c;
    }

    public void cross(){
        Random random = new Random();
        int option = random.nextInt(3);
        CrossAction routine = getPossibilities().get(cell.getType().getCode())[option];
        routine.doRoutine();
    }

    private void fromDownToRight(){
        Cell a = road.cellAtUp(cell.getPosition());
        Cell b = road.cellAtRight(a.getPosition());

        if(a.isCrossEnd()) cross();
        else tryCross(a, b);
    }

    private void fromDownToUp(){
        Cell a = road.cellAtUp(cell.getPosition());
        Cell b = road.cellAtUp(a.getPosition());
        Cell c = road.cellAtUp(b.getPosition());

        if(b.isCrossEnd()) cross();
        else tryCross(a, b, c);
    }

    private void fromDownToLeft(){
        Cell a = road.cellAtUp(cell.getPosition());
        Cell b = road.cellAtUp(a.getPosition());
        Cell c = road.cellAtLeft(b.getPosition());
        Cell d = road.cellAtLeft(c.getPosition());

        if(c.isCrossEnd()) cross();
        else tryCross(a, b, c, d);
    }

    private void fromLeftToDown(){
        Cell a = road.cellAtRight(cell.getPosition());
        Cell b = road.cellAtDown(a.getPosition());

        if(a.isCrossEnd()) cross();
        else tryCross(a, b);
    }

    private void fromLeftToRight(){
        Cell a = road.cellAtRight(cell.getPosition());
        Cell b = road.cellAtRight(a.getPosition());
        Cell c = road.cellAtRight(b.getPosition());

        if(b.isCrossEnd()) cross();
        else tryCross(a, b, c);
    }

    private void fromLeftToUp(){
        Cell a = road.cellAtRight(cell.getPosition());
        Cell b = road.cellAtRight(a.getPosition());
        Cell c = road.cellAtUp(b.getPosition());
        Cell d = road.cellAtUp(c.getPosition());

        if(c.isCrossEnd()) cross();
        else tryCross(a, b, c, d);
    }

    private void fromRightToUp(){
        Cell a = road.cellAtLeft(cell.getPosition());
        Cell b = road.cellAtUp(a.getPosition());
        if(a.isCrossEnd()) cross();
        else tryCross(a, b);
    }

    private void fromRightToLeft(){
        Cell a = road.cellAtLeft(cell.getPosition());
        Cell b = road.cellAtLeft(a.getPosition());
        Cell c = road.cellAtLeft(b.getPosition());

        if(b.isCrossEnd()) cross();
        else tryCross(a, b, c);
    }

    private void fromRightToDown(){
        Cell a = road.cellAtLeft(cell.getPosition());
        Cell b = road.cellAtLeft(a.getPosition());
        Cell c = road.cellAtDown(b.getPosition());
        Cell d = road.cellAtDown(c.getPosition());

        if(c.isCrossEnd()) cross();
        else tryCross(a, b, c, d);
    }

    private void fromUpToLeft(){
        Cell a = road.cellAtDown(cell.getPosition());
        Cell b = road.cellAtLeft(a.getPosition());

        if(a.isCrossEnd()) cross();
        else tryCross(a, b);
    }

    private void fromUpToDown(){
        Cell a = road.cellAtDown(cell.getPosition());
        Cell b = road.cellAtDown(a.getPosition());
        Cell c = road.cellAtDown(b.getPosition());
        
        if(b.isCrossEnd()) cross();
        else tryCross(a, b, c);
    }

    private void fromUpToRight(){
        Cell a = road.cellAtDown(cell.getPosition());
        Cell b = road.cellAtDown(a.getPosition());
        Cell c = road.cellAtRight(b.getPosition());
        Cell d = road.cellAtRight(c.getPosition());

        if(c.isCrossEnd()) cross();
        else tryCross(a, b, c, d);
    }

    private void tryCross(Cell a, Cell b){
        
        Random r = new Random();
        boolean gone = false;
        
        try {
            do {
                boolean blockedA = a.tryBlock();
                boolean blockedB = b.tryBlock();
                if (blockedA && blockedB) {
                    if (this.cell instanceof SemaphoreCell) {
                        this.cell.release();
                        cell.getVehicle().moveToCellInCross(a);
                        a.getVehicle().moveToCellInCross(b);
                    }
                    else {
                        cell.getVehicle().moveToCell(a);
                        a.getVehicle().moveToCell(b);
                    }
                    gone = true;
                    a.release();
                    b.release();
                } else {
                    if (blockedA) a.release();
                    if (blockedB) b.release();
                }
            } while (!gone);

        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private void tryCross(Cell a, Cell b, Cell c){
        
        Random r = new Random();
        boolean gone = false;
        
        try{
            do {
                boolean blockedA = a.tryBlock();
                boolean blockedB = b.tryBlock();
                boolean blockedC = c.tryBlock();

                if(blockedA && blockedB && blockedC){
                    if (this.cell instanceof SemaphoreCell){
                        this.cell.release();
                        cell.getVehicle().moveToCellInCross(a);
                        a.getVehicle().moveToCellInCross(b);
                        b.getVehicle().moveToCellInCross(c);
                    }else {
                        cell.getVehicle().moveToCell(a);
                        a.getVehicle().moveToCell(b);
                        b.getVehicle().moveToCell(c);
                    }
                    gone = true;
                    a.release();
                    b.release();
                    c.release();
                } else {
                    if (blockedA) a.release();
                    if (blockedB) b.release();
                    if (blockedC) c.release();
                }
            } while (!gone);

        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private void tryCross(Cell a, Cell b, Cell c, Cell d){
        
        Random r = new Random();
        boolean gone = false;
        
        try {
            do {
                boolean blockedA = a.tryBlock();
                boolean blockedB = b.tryBlock();
                boolean blockedC = c.tryBlock();
                boolean blockedD = d.tryBlock();

                if (blockedA && blockedB && blockedC && blockedD) {
                    if (this.cell instanceof SemaphoreCell){
                        this.cell.release();
                        cell.getVehicle().moveToCellInCross(a);
                        a.getVehicle().moveToCellInCross(b);
                        b.getVehicle().moveToCellInCross(c);
                        c.getVehicle().moveToCellInCross(d);

                    }else {
                        cell.getVehicle().moveToCell(a);
                        a.getVehicle().moveToCell(b);
                        b.getVehicle().moveToCell(c);
                        c.getVehicle().moveToCell(d);
                    }
                    gone = true;
                    a.release();
                    b.release();
                    c.release();
                    d.release();
                } else {
                    if (blockedA) a.release();
                    if (blockedB) b.release();
                    if (blockedC) c.release();
                    if (blockedD) d.release();
                }
            } while (!gone);

        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private HashMap<Integer, CrossAction[]> getPossibilities(){
        HashMap<Integer, CrossAction[]> possibilities = new HashMap<>();

        possibilities.put(1, new CrossAction[]{
                this::fromDownToRight,
                this::fromDownToUp,
                this::fromDownToLeft
        });

        possibilities.put(2, new CrossAction[]{
                this::fromLeftToDown,
                this::fromLeftToRight,
                this::fromLeftToUp
        });

        possibilities.put(3, new CrossAction[]{
                this::fromUpToLeft,
                this::fromUpToDown,
                this::fromUpToRight
        });

        possibilities.put(4, new CrossAction[]{
                this::fromRightToUp,
                this::fromRightToLeft,
                this::fromRightToDown
        });

        return possibilities;
    }
}
