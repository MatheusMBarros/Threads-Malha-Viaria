package udesc.dsd;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorCell extends Cell {
    private final Lock lock = new ReentrantLock();
    private final Condition canEnter = lock.newCondition();
    private int veiculosNoCruzamento = 0;

    public void lock() throws InterruptedException {
        lock.lock();
        try {
            while (veiculosNoCruzamento > 0) {
                canEnter.await();
            }
            veiculosNoCruzamento++;
        } finally {
            lock.unlock();
        }
    }

    public void release() {
        lock.lock();
        try {
            veiculosNoCruzamento--;
            canEnter.signal();
        } finally {
            lock.unlock();
        }
    }
}
