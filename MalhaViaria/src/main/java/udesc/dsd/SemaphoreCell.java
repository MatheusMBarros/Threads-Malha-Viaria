package udesc.dsd;

import java.util.concurrent.Semaphore;

public class SemaphoreCell extends Cell {
    private Semaphore semaphore;

    public SemaphoreCell(Position position, CellType type, boolean isEntrance, boolean isCross, boolean isExit) {
        super(position, type, isEntrance, isCross, isExit);
        this.semaphore = new Semaphore(1);
    }

    public void lock() throws InterruptedException {
        semaphore.acquire();
    }

    public void release() {
        semaphore.release();
    }




}

