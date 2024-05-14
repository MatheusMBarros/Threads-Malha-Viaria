package org.matheus.barros;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorCruzamento {
    private final Lock lock = new ReentrantLock();
    private final Condition canEnter = lock.newCondition();
    private int veiculosNoCruzamento = 0;

    public void entrarCruzamento() throws InterruptedException {
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

    public void sairCruzamento() {
        lock.lock();
        try {
            veiculosNoCruzamento--;
            canEnter.signal();
        } finally {
            lock.unlock();
        }
    }
}
