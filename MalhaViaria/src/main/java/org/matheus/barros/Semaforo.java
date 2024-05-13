package org.matheus.barros;

import java.util.concurrent.Semaphore;

public class Semaforo {
    private Semaphore semaforo;

    public Semaforo() {
        semaforo = new Semaphore(1); // Inicia o semáforo com uma permissão
    }

    public void adquirirPermissao() throws InterruptedException {
        semaforo.acquire(); // Adquire uma permissão do semáforo
    }

    public void liberarPermissao() {
        semaforo.release(); // Libera uma permissão do semáforo
    }
}
