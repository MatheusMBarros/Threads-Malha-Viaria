package udesc.dsd;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreCell extends Cell {

    private Semaphore semaphore;

    public SemaphoreCell(Position position, CellType type, boolean isEntrance, boolean isCross, boolean isExit) {
        super(position, type, isEntrance, isCross, isExit);
        this.semaphore = new Semaphore(1);
    }

    public void lock() throws InterruptedException {
        semaphore.acquire();
    }
    @Override
    public void release() {
        semaphore.release();
    }

    @Override
    public boolean tryLock() throws InterruptedException {
        Random rand = new Random();
        return semaphore.tryAcquire(rand.nextInt(500), TimeUnit.MILLISECONDS);
    }
}

