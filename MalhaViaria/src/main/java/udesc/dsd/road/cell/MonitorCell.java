package udesc.dsd.road.cell;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MonitorCell extends Cell {

    private final Lock lock = new ReentrantLock();

    public MonitorCell(Position position, CellType type, boolean isCross) {
        super(position, type, isCross);
    }

    @Override
    public void lock(){
        lock.lock();
    }

    @Override
    public void release() {
        lock.unlock();
    }

    @Override
    public boolean tryBlock() throws InterruptedException {
        Random r = new Random();
        return lock.tryLock(r.nextInt(500), TimeUnit.MILLISECONDS);
    }
}
