package experimentos;

public class MedidorTempo {

    public long iniciar() {
        return System.nanoTime();
    }

    public long finalizar(long inicio) {
        return System.nanoTime() - inicio;
    }
}

