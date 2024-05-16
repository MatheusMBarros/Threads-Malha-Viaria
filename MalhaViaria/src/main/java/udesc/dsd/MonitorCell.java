package udesc.dsd;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorCell extends Cell {
    private final Lock lock = new ReentrantLock();


    public MonitorCell(Position position, CellType type, boolean isEntrance, boolean isCross, boolean isExit) {
        super(position, type, isEntrance, isCross, isExit);
    }

    public void lock() throws InterruptedException {
        lock.lock();
    }

    public void release() {
        lock.unlock();
    }
}
