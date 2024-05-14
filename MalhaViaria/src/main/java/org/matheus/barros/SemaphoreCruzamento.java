package org.matheus.barros;

import java.util.concurrent.Semaphore;

public class SemaphoreCruzamento {
    private Semaphore semaforo;

    public SemaphoreCruzamento() {
        semaforo = new Semaphore(1); // Inicializa o semáforo com uma permissão (um veículo pode passar)
    }

    public void entrarCruzamento() throws InterruptedException {
        semaforo.acquire(); // Tenta adquirir uma permissão do semáforo (entra no cruzamento)
    }

    public void sairCruzamento() {
        semaforo.release(); // Libera a permissão do semáforo (sai do cruzamento)
    }

    public int availablePermits() {
        return semaforo.availablePermits();
    }




}
