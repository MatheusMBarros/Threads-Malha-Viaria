package udesc.dsd;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MonitorCell extends Cell {

    private final Lock lock = new ReentrantLock();

    public MonitorCell(Position position, CellType type, boolean isEntrance, boolean isCross, boolean isExit) {
        super(position, type, isEntrance, isCross, isExit);
    }

    public void lock(){
        lock.lock();
    }

    public void release() {
        lock.unlock();
    }

    @Override
    public boolean tryLock() throws InterruptedException {
        Random r = new Random();
        return lock.tryLock(r.nextInt(500), TimeUnit.MILLISECONDS);
    }
}
