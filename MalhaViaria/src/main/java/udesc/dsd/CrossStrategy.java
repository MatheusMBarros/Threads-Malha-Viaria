package udesc.dsd;

import java.util.HashMap;
import java.util.Random;

import static java.lang.Thread.sleep;

public class CrossStrategy {

    private Road road;
    private Cell cell;

    public CrossStrategy(Road road, Cell c){
        this.road = road;
        this.cell = cell;
    }

    public void cross(){
        Random random = new Random();
        int option = random.nextInt(3);
        CrossAction routine = getPossibilities().get(cell.getType().getCode())[option];
        routine.doRoutine();
    }

    private void fromUpToRight(){
        Cell a = road.cellAtUp(cell.getPosition());
        Cell b = road.cellAtRight(a.getPosition());

        if(a.isCrossEnd()) cross();
        else tryCross(a, b);
    }

    private void fromUpToUp(){
        Cell a = road.cellAtUp(cell.getPosition());
        Cell b = road.cellAtUp(a.getPosition());
        Cell c = road.cellAtUp(b.getPosition());

        if(b.isCrossEnd()) cross();
        else tryCross(a, b, c);
    }

    private void fromUpToLeft(){
        Cell a = road.cellAtUp(cell.getPosition());
        Cell b = road.cellAtUp(a.getPosition());
        Cell c = road.cellAtLeft(b.getPosition());
        Cell d = road.cellAtLeft(c.getPosition());

        if(c.isCrossEnd()) cross();
        else tryCross(a, b, c, d);
    }

    private void fromRightToDown(){
        Cell a = road.cellAtRight(cell.getPosition());
        Cell b = road.cellAtDown(a.getPosition());

        if(a.isCrossEnd()) cross();
        else tryCross(a, b);
    }

    private void fromRightToRight(){
        Cell a = road.cellAtRight(cell.getPosition());
        Cell b = road.cellAtRight(a.getPosition());
        Cell c = road.cellAtRight(b.getPosition());

        if(b.isCrossEnd()) cross();
        else tryCross(a, b, c);
    }

    private void fromRightToUp(){
        Cell a = road.cellAtRight(cell.getPosition());
        Cell b = road.cellAtRight(a.getPosition());
        Cell c = road.cellAtUp(b.getPosition());
        Cell d = road.cellAtUp(c.getPosition());

        if(c.isCrossEnd()) cross();
        else tryCross(a, b, c, d);
    }

    private void fromLeftToUp(){
        Cell a = road.cellAtLeft(cell.getPosition());
        Cell b = road.cellAtUp(a.getPosition());
        if(a.isCrossEnd()) cross();
        else tryCross(a, b);
    }

    private void fromLeftToLeft(){
        Cell a = road.cellAtLeft(cell.getPosition());
        Cell b = road.cellAtLeft(a.getPosition());
        Cell c = road.cellAtLeft(b.getPosition());

        if(b.isCrossEnd()) cross();
        else tryCross(a, b, c);
    }

    private void fromLeftToDown(){
        Cell a = road.cellAtLeft(cell.getPosition());
        Cell b = road.cellAtLeft(a.getPosition());
        Cell c = road.cellAtDown(b.getPosition());
        Cell d = road.cellAtDown(c.getPosition());

        if(c.isCrossEnd()) cross();
        else tryCross(a, b, c, d);
    }

    private void fromDownToLeft(){
        Cell a = road.cellAtDown(cell.getPosition());
        Cell b = road.cellAtLeft(a.getPosition());

        if(a.isCrossEnd()) cross();
        else tryCross(a, b);
    }

    private void fromDownToDown(){
        Cell a = road.cellAtDown(cell.getPosition());
        Cell b = road.cellAtDown(a.getPosition());
        Cell c = road.cellAtDown(b.getPosition());
        
        if(b.isCrossEnd()) cross();
        else tryCross(a, b, c);
    }

    private void fromDownToRight(){
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
                boolean blockedA = a.tryLock();
                boolean blockedB = b.tryLock();
                if (blockedA && blockedB) {
                    if (this.cell instanceof SemaphoreCell) {
                        this.cell.release();
                        cell.vehicle.moveToCellInCross(a);
                        cell.vehicle.moveToCellInCross(b);
                    }
                    else {
                        cell.vehicle.moveToCell(a);
                        cell.vehicle.moveToCell(b);
                    }
                    gone = true;
                    a.release();
                    b.release();
                } else {
                    if (blockedA) a.release();
                    if (blockedB) b.release();
                    sleep(r.nextLong(500));
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
                boolean blockedA = a.tryLock();
                boolean blockedB = b.tryLock();
                boolean blockedC = c.tryLock();

                if(blockedA && blockedB && blockedC){
                    if (this.cell instanceof SemaphoreCell){
                        this.cell.release();
                        cell.vehicle.moveToCellInCross(a);
                        cell.vehicle.moveToCellInCross(b);
                        cell.vehicle.moveToCellInCross(c);
                    }else {
                        cell.vehicle.moveToCell(a);
                        cell.vehicle.moveToCell(b);
                        cell.vehicle.moveToCell(c);
                    }
                    gone = true;
                    a.release();
                    b.release();
                    c.release();
                } else {
                    if (blockedA) a.release();
                    if (blockedB) b.release();
                    if (blockedC) c.release();
                    sleep(r.nextLong(500));
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
                boolean blockedA = a.tryLock();
                boolean blockedB = b.tryLock();
                boolean blockedC = c.tryLock();
                boolean blockedD = d.tryLock();

                if (blockedA && blockedB && blockedC && blockedD) {
                    if (this.cell instanceof SemaphoreCell){
                        this.cell.release();
                        cell.vehicle.moveToCellInCross(a);
                        cell.vehicle.moveToCellInCross(b);
                        cell.vehicle.moveToCellInCross(c);
                        cell.vehicle.moveToCellInCross(d);

                    }else {
                        cell.vehicle.moveToCell(a);
                        cell.vehicle.moveToCell(b);
                        cell.vehicle.moveToCell(c);
                        cell.vehicle.moveToCell(d);
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
                    sleep(r.nextLong(500));
                }
            } while (!gone);

        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private HashMap<Integer, CrossAction[]> getPossibilities(){
        HashMap<Integer, CrossAction[]> possibilities = new HashMap<>();

        possibilities.put(1, new CrossAction[]{
                this::fromUpToRight,
                this::fromUpToUp,
                this::fromUpToLeft
        });

        possibilities.put(2, new CrossAction[]{
                this::fromRightToDown,
                this::fromRightToRight,
                this::fromRightToUp
        });

        possibilities.put(3, new CrossAction[]{
                this::fromDownToLeft,
                this::fromDownToDown,
                this::fromDownToRight
        });

        possibilities.put(4, new CrossAction[]{
                this::fromLeftToUp,
                this::fromLeftToLeft,
                this::fromLeftToDown
        });

        return possibilities;
    }
}
