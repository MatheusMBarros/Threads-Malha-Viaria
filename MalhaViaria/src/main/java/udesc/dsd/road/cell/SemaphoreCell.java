package udesc.dsd.road.cell;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreCell extends Cell {

    private Semaphore semaphore;

    public SemaphoreCell(Position position, CellType type, boolean isCross) {
        super(position, type, isCross);
        this.semaphore = new Semaphore(1);
    }

    @Override
    public void lock() throws InterruptedException {
        semaphore.acquire();
    }

    @Override
    public void release() {
        semaphore.release();
    }

    @Override
    public boolean tryBlock() throws InterruptedException {
        Random rand = new Random();
        return semaphore.tryAcquire(rand.nextInt(500), TimeUnit.MILLISECONDS);
    }
}

